package com.banuba.example.integrationapp.videoeditor.impl

import com.banuba.sdk.core.data.ColorFilterOrderProvider

class IntegrationAppColorFilterOrderProvider : ColorFilterOrderProvider {

    override fun provide() = listOf(
        "egypt",
        "byers",
        "chile",
        "hyla",
        "new_zeland",
        "korben",
        "canada",
        "remy",
        "england",
        "retro",
        "norway",
        "neon",
        "japan",
        "instant",
        "lux",
        "sunset",
        "bubblegum",
        "chroma",
        "lilac",
        "pinkvine",
        "spark",
        "sunny",
        "vinyl",
        "glitch",
        "grunge"
    )
}
