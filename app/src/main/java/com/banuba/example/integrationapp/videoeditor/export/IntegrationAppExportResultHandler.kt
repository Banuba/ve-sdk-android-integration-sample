package com.banuba.example.integrationapp.videoeditor.export

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.banuba.sdk.ve.flow.ExportResultHandler
import com.banuba.sdk.veui.ui.EXTRA_EXPORTED_SUCCESS
import com.banuba.sdk.veui.ui.ExportResult

class IntegrationAppExportResultHandler: ExportResultHandler {

    override fun doAction(activity: AppCompatActivity, result: ExportResult.Success?) {
        if (result == null) {
            activity.finish()
            return
        }
        val resultCode = Activity.RESULT_OK
        val data = Intent().apply {
            putExtra(EXTRA_EXPORTED_SUCCESS, result)
        }
        activity.setResult(resultCode, data)
        activity.finish()
    }
}