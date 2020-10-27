package com.banuba.example.integrationapp.videoeditor.export

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.banuba.sdk.ve.flow.ExportFlowManager
import com.banuba.sdk.veui.ui.ExportResult
import com.banuba.sdk.veui.ui.ExportTaskParams

class IntegrationAppExportFlowManager: ExportFlowManager {

    override val provideExportInBackground: Boolean = false

    override val resultData: LiveData<ExportResult> = MutableLiveData()

    override fun releaseRawSessionData() {}

    override fun startExport(exportTaskParams: ExportTaskParams) {}

    override fun stopExport() {}
}