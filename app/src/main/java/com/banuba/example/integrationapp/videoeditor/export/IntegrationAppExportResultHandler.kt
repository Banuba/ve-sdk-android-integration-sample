package com.banuba.example.integrationapp.videoeditor.export

import androidx.appcompat.app.AppCompatActivity
import com.banuba.sdk.ve.flow.ExportResultHandler
import com.banuba.sdk.veui.ui.ExportResult

class IntegrationAppExportResultHandler: ExportResultHandler {

    override fun doAction(activity: AppCompatActivity, result: ExportResult.Success?) {}
}