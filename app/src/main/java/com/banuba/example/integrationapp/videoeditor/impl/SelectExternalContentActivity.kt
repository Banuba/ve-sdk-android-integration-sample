package com.banuba.example.integrationapp.videoeditor.impl

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getStringOrNull
import androidx.core.os.bundleOf
import com.banuba.example.integrationapp.R
import com.banuba.sdk.core.SupportedMediaResourceType
import com.banuba.sdk.core.domain.ProvideMediaContentContract
import com.banuba.sdk.core.domain.ProvideTrackContract
import com.banuba.sdk.core.data.TrackData
import com.banuba.sdk.core.media.MediaType
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class SelectExternalContentActivity : AppCompatActivity(R.layout.activity_select_audio) {

    enum class TYPE {
        AUDIO, MEDIA
    }

    companion object {

        private const val EXTRA_TYPE = "EXTRA_TYPE"

        fun newGetMusicIntent(context: Context) =
            Intent(context, SelectExternalContentActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        EXTRA_TYPE to TYPE.AUDIO
                    )
                )
            }

        fun newGetMediaIntent(context: Context) =
            Intent(context, SelectExternalContentActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        EXTRA_TYPE to TYPE.MEDIA
                    )
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let { extras ->
            val type = extras.getSerializable(EXTRA_TYPE) as? TYPE
            type?.let {
                when (type) {
                    TYPE.AUDIO -> {
                        val contract = ActivityResultContracts.OpenDocument()
                        registerForActivityResult(contract) { uri -> deliverMusicTrackUri(uri) }.launch(
                            arrayOf("audio/*")
                        )
                    }
                    TYPE.MEDIA -> {
                        val contract = ActivityResultContracts.OpenMultipleDocuments()
                        val params = ProvideMediaContentContract.obtainParams(extras)
                        val mediaTypes = mutableListOf<String>().apply {
                            if (params.types.contains(MediaType.Video)) {
                                SupportedMediaResourceType.VIDEO.supportedFormats.onEach {
                                    add("video/$it")
                                }
                            }
                            if (params.types.contains(MediaType.Image)) {
                                SupportedMediaResourceType.PICTURE.supportedFormats.onEach {
                                    add("image/$it")
                                }
                            }
                        }
                        registerForActivityResult(contract) { mediaList ->
                            deliverMediaDataList(mediaList)
                        }.launch(mediaTypes.toTypedArray())
                    }
                }
            }
        }
    }

    private fun deliverMediaDataList(data: List<Uri>) {
        if (data.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED)
            finish()
            return
        }
        val resultIntent = Intent().apply {
            putParcelableArrayListExtra(
                ProvideMediaContentContract.EXTRA_MEDIA_CONTENT_RESULT,
                ArrayList(data)
            )
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun deliverMusicTrackUri(uri: Uri?) {
        if (uri == null) {
            setResult(Activity.RESULT_CANCELED)
            finish()
            return
        }
        val resultIntent = Intent().apply {
            val trackTitle = getFileName(uri) ?: "Track"
            val track = TrackData(
                id = UUID.randomUUID(),
                title = trackTitle,
                localUri = saveAudio(uri)
            )
            putExtra(ProvideTrackContract.EXTRA_RESULT_TRACK_DATA, track)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun saveAudio(source: Uri): Uri {
        val extension = getExtension(source)
        val fileSuffix = ".$extension"
        val targetFile = File(filesDir, "audio_${System.currentTimeMillis()}$fileSuffix")
        if (targetFile.exists()) targetFile.delete()
        targetFile.createNewFile()
        contentResolver.openInputStream(source)?.use { stream ->
            val target = FileOutputStream(targetFile)
            stream.copyTo(target)
        }
        return Uri.fromFile(targetFile)
    }

    private fun getExtension(uri: Uri): String {
        return if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(contentResolver.getType(uri)).orEmpty()
        } else {
            MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        }
    }

    private fun getFileName(uri: Uri): String? = runCatching {
        if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            contentResolver.query(
                uri,
                arrayOf(OpenableColumns.SIZE, OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getStringOrNull(nameIndex)
            }
        } else {
            uri.lastPathSegment
        }
    }.getOrNull()
}
