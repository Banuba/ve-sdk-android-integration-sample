package com.banuba.example.integrationapp.videoeditor.impl

import com.banuba.sdk.veui.domain.TrimmerAction
import com.banuba.sdk.veui.ui.trimmer.TrimmerActionsProvider

class IntegrationTrimmerActionsProvider : TrimmerActionsProvider {

    override fun provide(): List<TrimmerAction> = listOf(
        TrimmerAction.TRIM, TrimmerAction.DELETE
    )
}