package com.banuba.example.integrationapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.banuba.sdk.cameraui.data.PipConfig
import com.banuba.sdk.core.ext.obtainReadFileDescriptor
import com.banuba.sdk.core.ui.ext.replaceFragment
import com.banuba.sdk.core.ui.ext.visible
import com.banuba.sdk.core.ui.ext.visibleOrGone
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.ve.flow.VideoCreationActivity
import com.banuba.sdk.ve.slideshow.SlideShowScaleMode
import com.banuba.sdk.ve.slideshow.SlideShowSource
import com.banuba.sdk.ve.slideshow.SlideShowTask
import com.banuba.sdk.veui.ext.popFragmentByTag
import com.banuba.sdk.veui.ui.sharing.VideoSharingFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {

    companion object {
        /**
         * Place here Facebook app id which is used to share exported video
         * across the facebook apps (fb reels, fb stories, instagram stories)
         */
        private const val FB_APP_ID = ""
    }

    private val createVideoRequest =
        registerForActivityResult(CustomExportResultVideoContract()) { exportResult ->
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
                } else {
                    if (exportResult is ExportResult.Success) {
                        val videoUri = exportResult.videoList.getOrNull(0)

                        videoUri?.let {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                val uri = FileProvider.getUriForFile(
                                    applicationContext,
                                    "$packageName.provider",
                                    File(videoUri.sourceUri.encodedPath ?: "")
                                )
                                setDataAndType(uri, "video/mp4")
                            }
                            startActivity(intent)
                        }
                    }
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
        lifecycleScope.launch {
            val externalSlideshowDir =
                File(getExternalFilesDir(null), "slideshow").apply { mkdirs() }
            val slideshowFromFile = buildSlideshowFromFile(sourceUri, externalSlideshowDir)
            val slideshowFromBitmap = buildSlideshowFromBitmap(externalSlideshowDir)
            openVideoEditorWithSlideShow(listOf(slideshowFromFile, slideshowFromBitmap))
        }
    }

    /**
     * Helper function which represents the way you can create a slideshow
     * from image file Uri. It is just an example.
     * You should create video from within background thread!
     */
    private suspend fun buildSlideshowFromFile(source: Uri, folder: File): Uri {
        val slideshowFromFile = File(folder, "slideshowFromFile.mp4").apply { createNewFile() }
        return obtainReadFileDescriptor(source)?.let {
            val params = SlideShowTask.Params.create(
                context = this,
                size = Size(1280, 768),
                destFile = slideshowFromFile,
                sources = listOf(
                    SlideShowSource.File(
                        source = source,
                        durationMs = 3000
                    )
                ),
                animationEnabled = true,
                debugEnabled = true,
                scaleMode = SlideShowScaleMode.SCALE_BALANCED
            )
            SlideShowTask.makeVideo(params)
            Uri.fromFile(slideshowFromFile)
        } ?: Uri.EMPTY
    }

    /**
     * Helper function which represents the way you can create a slideshow
     * from a Bitmap. It is just an example.
     * You should create video from within background thread!
     */
    private suspend fun buildSlideshowFromBitmap(folder: File): Uri {
        val slideshowFromBitmap = File(folder, "slideshowFromBitmap.mp4").apply { createNewFile() }
        val bitmap = assets.open("image_sample.jpeg").run {
            BitmapFactory.decodeStream(this)
        }
        val params = SlideShowTask.Params.create(
            context = this,
            size = Size(1280, 768),
            destFile = slideshowFromBitmap,
            sources = listOf(
                SlideShowSource.Picture(
                    source = bitmap,
                    durationMs = 3000
                )
            ),
            animationEnabled = true,
            debugEnabled = false,
            scaleMode = SlideShowScaleMode.SCALE_BALANCED
        )
        SlideShowTask.makeVideo(params)
        return Uri.fromFile(slideshowFromBitmap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val videoEditor = (application as SampleApp).videoEditor
        if (videoEditor == null) {
            licenseStateView.visible()
            licenseStateView.text = SampleApp.ERR_SDK_NOT_INITIALIZED
            return
        }

        // Might take up to 1 sec in the worst case.
        // Please optimize use of this function in your project to bring the best user experience
        videoEditor.getLicenseState { isValid ->
            if(isValid) {
                // ✅ License is active, all good
                // You can show button that opens Video Editor or
                // Start Video Editor right away
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
            } else {
                // ❌ Use of Video Editor is restricted. License is revoked or expired.
                licenseStateView.text = SampleApp.ERR_LICENSE_REVOKED
                Log.w(SampleApp.TAG, SampleApp.LICENSE_TOKEN)

                licenseStateView.visible()
                btnVideoEditor.isEnabled = false
                btnPiPVideoEditor.isEnabled = false
                btnDraftsVideoEditor.isEnabled = false
                btnSlideShowVideoEditor.isEnabled = false
            }
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
