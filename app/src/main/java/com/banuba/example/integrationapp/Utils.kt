package com.banuba.example.integrationapp

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleCoroutineScope
import com.banuba.sdk.core.ext.obtainReadFileDescriptor
import com.banuba.sdk.ve.slideshow.SlideShowScaleMode
import com.banuba.sdk.ve.slideshow.SlideShowSource
import com.banuba.sdk.ve.slideshow.SlideShowTask
import kotlinx.coroutines.launch
import java.io.File

// Helper methods not required for the SDK integration
object Utils {
    // Creates the list with 2 video sources - the first based on image taken from gallery and
    // the second on image taken from assets
    fun createSlideShowList(
        context: Context,
        lifecycleCoroutineScope: LifecycleCoroutineScope,
        selectedImage: Uri
    ): List<Uri> {
        val list = mutableListOf<Uri>()
        lifecycleCoroutineScope.launch {
            val externalSlideshowDir =
                File(context.getExternalFilesDir(null), "slideshow").apply { mkdirs() }

            list.add(buildSlideshowFromFile(context, selectedImage, externalSlideshowDir))
            list.add(buildSlideshowFromBitmap(context, externalSlideshowDir))
        }
        return list
    }

    // Plays exported video to demonstrate the result
    fun playExportedVideo(
        activity: AppCompatActivity,
        videoUri: Uri
    ) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val uri = FileProvider.getUriForFile(
                activity,
                "${activity.packageName}.provider",
                File(videoUri.encodedPath ?: "")
            )
            setDataAndType(uri, "video/mp4")
        }
        activity.startActivity(intent)
    }

    // Previews exported image to demonstrate the result
    fun previewExportedImage(
        activity: AppCompatActivity,
        uri: Uri
    ) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(uri, "image/*")

        try {
            activity.startActivity(intent)
        } catch (e: Exception) {
            Log.e(JavaSampleApp.TAG, "Can't handle intent")
        }
    }

    /**
     * Helper function to create slideshow video from image taken from the gallery.
     * It is highly recommended to execute in the background thread
     */
    private suspend fun buildSlideshowFromFile(
        context: Context,
        source: Uri,
        folder: File
    ): Uri {
        val slideshowFromFile = File(folder, "slideshowFromFile.mp4").apply { createNewFile() }
        return context.obtainReadFileDescriptor(source)?.let {
            val params = SlideShowTask.Params.create(
                context = context,
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
     * Helper function to create slideshow video from image taken from assets.
     * It is highly recommended to execute in the background thread
     */
    private suspend fun buildSlideshowFromBitmap(
        context: Context,
        folder: File
    ): Uri {
        val slideshowFromBitmap = File(folder, "slideshowFromBitmap.mp4").apply { createNewFile() }
        val bitmap = context.assets.open("image_sample.jpeg").run {
            BitmapFactory.decodeStream(this)
        }
        val params = SlideShowTask.Params.create(
            context = context,
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
}