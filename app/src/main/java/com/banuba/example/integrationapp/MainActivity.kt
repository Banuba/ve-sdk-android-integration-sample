package com.banuba.example.integrationapp

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.util.Size
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.banuba.example.integrationapp.videoeditor.IntegrationAppExportVideoContract
import com.banuba.sdk.cameraui.data.PipConfig
import com.banuba.sdk.core.ext.obtainReadFileDescriptor
import com.banuba.sdk.core.ui.ext.replaceFragment
import com.banuba.sdk.core.ui.ext.visible
import com.banuba.sdk.core.ui.ext.visibleOrGone
import com.banuba.sdk.token.storage.provider.TokenProvider
import com.banuba.sdk.ve.flow.VideoCreationActivity
import com.banuba.sdk.ve.processing.SlideShowManager
import com.banuba.sdk.ve.processing.SlideShowTask
import com.banuba.sdk.veui.ext.popFragmentByTag
import com.banuba.sdk.veui.ui.sharing.VideoSharingFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject
import java.io.File

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TOKEN_URL =
            "https://github.com/Banuba/ve-sdk-android-integration-sample#token"

        /**
         * Place here Facebook app id which is used to share exported video
         * across the facebook apps (fb reels, fb stories, instagram stories)
         */
        private const val FB_APP_ID = ""
    }

    private val tokenProvider: TokenProvider by inject(
        TokenProvider::class.java,
        named("banubaTokenProvider")
    )

    private val createVideoRequest =
        registerForActivityResult(IntegrationAppExportVideoContract()) { exportResult ->
            exportResult?.let {
                //handle ExportResult object
                if (FB_APP_ID.isNotEmpty()) {
                    val sharingScreen = VideoSharingFragment.newInstance(it, FB_APP_ID)
                    toggleSharingScreen(visible = true)
                    supportFragmentManager.replaceFragment(
                        fragment = sharingScreen,
                        tag = VideoSharingFragment.TAG,
                        containerId = R.id.shareScreenContainer,
                        addToBackStack = true
                    )
                }
            }
        }

    private val createVideoWithPiPRequest = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        openVideoEditor(pipVideo = it ?: Uri.EMPTY)
    }

    private val createVideoFromImageRequest = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            if (it != Uri.EMPTY) {
                createSlideShowVideo(it)
            }
        }
    }

    /**
     * Opens Video Editor from the trimmer screen with predefined
     * slideshow videos created by the SlideShowTask api.
     * To use this api in your application you should follow the next steps:
     * 1. Create a temporary storage for slideshow videos (it may be any kind of storage:
     *    external sdcard or cache folder)
     * 2. Prepare SlideShowManager.Params object with required configurations: at least one kind of
     *    sources - slide (ParcelFileDescriptor) or picture (Bitmap) together with resulting video duration
     *    should be passed, also the destination file is mandatory.
     *    Animations, debugging and scale mode has their own default values so it is optional to setup them.
     * 3. Invoke SlideShowTask.makeVideo() function with Params object.
     *    It is recommended to invoke it from the background thread because this task is
     *    a time consuming and thread blocking.
     * 4. Use resulting videos as predefined videos for any kind of Video Editor flow -
     *    PipMode, Trimmer, Editor screens.
     */
    private fun createSlideShowVideo(sourceUri: Uri) {
        val externalSlideshowDir = File(getExternalFilesDir(null), "slideshow").apply { mkdirs() }
        val slideshowFromFile = buildSlideshowFromFile(sourceUri, externalSlideshowDir)
        val slideshowFromBitmap = buildSlideshowFromBitmap(externalSlideshowDir)
        openVideoEditorWithSlideShow(listOf(slideshowFromFile, slideshowFromBitmap))
    }

    /**
     * Helper function which represents the way you can create a slideshow
     * from image file Uri. It is just an example.
     * You should create video from within background thread!
     */
    private fun buildSlideshowFromFile(source: Uri, folder: File): Uri {
        val slideshowFromFile = File(folder, "slideshowFromFile.mp4").apply { createNewFile() }
        return obtainReadFileDescriptor(source)?.let {
            val params = SlideShowManager.Params.Builder(
                size = Size(1280, 768),
                slides = listOf(Pair(it, 3000))
            )
                .destFile(slideshowFromFile)
                .animationEnabled(true)
                .debugEnabled(true)
                .scaleMode(SlideShowManager.SlideShowScaleMode.SCALE_BALANCED)
                .build()
            SlideShowTask.makeVideo(params)
            Uri.fromFile(slideshowFromFile)
        } ?: Uri.EMPTY
    }

    /**
     * Helper function which represents the way you can create a slideshow
     * from a Bitmap. It is just an example.
     * You should create video from within background thread!
     */
    private fun buildSlideshowFromBitmap(folder: File): Uri {
        val slideshowFromBitmap = File(folder, "slideshowFromBitmap.mp4").apply { createNewFile() }
        val bitmap = assets.open("image_sample.jpeg").run {
            BitmapFactory.decodeStream(this)
        }
        val params = SlideShowManager.Params.Builder(
            size = Size(1280, 768),
            picture = Pair(bitmap, 3000)
        )
            .destFile(slideshowFromBitmap)
            .animationEnabled(true)
            .debugEnabled(false)
            .build()
        SlideShowTask.makeVideo(params)
        return Uri.fromFile(slideshowFromBitmap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        com.banuba.sdk.ve.processing.FFmpeg(context = this).execute(emptyArray()).run {
            waitFor()
            Log.d("FFmpeg", errorStream.reader().readText())
        }

        btnVideoEditor.setOnClickListener {
            openVideoEditor()
        }
        btnPiPVideoEditor.setOnClickListener {
            createVideoWithPiPRequest.launch("video/*")
        }
        btnDraftsVideoEditor.setOnClickListener {
            openVideoEditorWithDrafts()
        }
        btnSlideShowVideoEditor.setOnClickListener {
            createVideoFromImageRequest.launch("image/*")
        }

        if (tokenProvider.getToken().isEmpty()) {
            infoTextView.text =
                "Video editor requires a token. \nPlease, follow the steps described in our Github: $TOKEN_URL "
            infoTextView.visible()
            btnVideoEditor.isEnabled = false
            btnPiPVideoEditor.isEnabled = false
        }
    }

    fun hideSharingScreen() {
        if (supportFragmentManager.popFragmentByTag(VideoSharingFragment.TAG)) {
            toggleSharingScreen(visible = false)
        }
    }

    private fun openVideoEditor(pipVideo: Uri = Uri.EMPTY) {
        val videoCreationIntent = VideoCreationActivity.startFromCamera(
            context = this,
            // set PiP video configuration
            pictureInPictureConfig = PipConfig(
                video = pipVideo,
                openPipSettings = false
            ),
            // setup what kind of action you want to do with VideoCreationActivity
            // setup data that will be acceptable during export flow
            additionalExportData = null,
            // set TrackData object if you open VideoCreationActivity with preselected music track
            audioTrackData = null
        )
        createVideoRequest.launch(videoCreationIntent)
    }

    private fun openVideoEditorWithDrafts() {
        createVideoRequest.launch(VideoCreationActivity.startFromDrafts(this))
    }

    private fun openVideoEditorWithSlideShow(videos: List<Uri>) {
        createVideoRequest.launch(
            VideoCreationActivity.startFromTrimmer(
                this,
                videos.toTypedArray()
            )
        )
    }

    private fun toggleSharingScreen(visible: Boolean) {
        shareScreenContainer.visibleOrGone(visible)
        btnVideoEditor.visibleOrGone(!visible)
        btnPiPVideoEditor.visibleOrGone(!visible)
        btnDraftsVideoEditor.visibleOrGone(!visible)
        btnSlideShowVideoEditor.visibleOrGone(!visible)
    }
}
