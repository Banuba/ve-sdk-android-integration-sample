package com.banuba.example.integrationapp

import android.app.Activity
import com.banuba.sdk.export.data.ExportFlowManager
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.export.data.ExportStopReason
import com.banuba.sdk.veui.ui.sharing.SharingActionHandler

class CustomSharingActionHandler(
    private val exportFlowManager: ExportFlowManager
) : SharingActionHandler {
    override fun onClose(activity: Activity, result: ExportResult.Success?) {
        if (exportFlowManager.resultData.value is ExportResult.Progress) {
            exportFlowManager.stopExport(ExportStopReason.CANCEL)
        }
        activity.finish()
    }

    override fun onBack(activity: Activity) {
        if (exportFlowManager.resultData.value is ExportResult.Progress) {
            exportFlowManager.stopExport(ExportStopReason.CANCEL)
        }
        activity.finish()
    }
}
