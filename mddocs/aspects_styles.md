# Banuba AI Video Editor SDK
## Aspects screen styles

- [trimmerAspectsButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L147)

    style for the button that opens aspects list

    ![img](screenshots/aspects1.png)

- [trimmerAspectsDoneButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L148)

    style for the button that applies selected aspect

- [trimmerAspectsCancelButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L149)

    style for the button that returns the user to the previous screen

- [trimmerAspectsRecyclerViewStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L150)

    style for the view containing all aspects

- [trimmerAspectsItemParentStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L151)

    style for the view containing single aspect

- [trimmerAspectsItemImageStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L152)

    style for the image for single aspect

- [trimmerAspectsItemDescriptionStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L153)

    style for the description text for single aspect

    ![img](screenshots/aspects2.png)


### Icon resources

| ResourceId        |   Description |
| ------------- | :------------- |
| ic_aspect_original | Icon for `EditorAspectSettings.Original` aspect |
| ic_aspect_16_9 | Icon for `EditorAspectSettings.16_9` aspect |
| ic_aspect_9_16 | Icon for `EditorAspectSettings.9_16` aspect |
| ic_aspect_4_3 | Icon for `EditorAspectSettings.4_3` aspect |
| ic_aspect_4_5 | Icon for `EditorAspectSettings.4_5` aspect |

**NOTE: Aspects feature may be turned off.** To remove it you should override `AspectsProvider` implementation with just one `EditorAspectSettings` type. For example:

```kotlin
override val aspectsProvider: BeanDefinition<AspectsProvider> = single(override = true) {
    object : AspectsProvider {

        override var availableAspects: List<AspectSettings> = listOf(
            EditorAspectSettings.`4_5`
        )

        override fun provide(): AspectsProvider.AspectsData {
            return AspectsProvider.AspectsData(
                allAspects = availableAspects,
                default = availableAspects.first()
            )
        }
    }
}
```

In this example there is **no aspects icon** on trimmer screen and **all videos will be resized to 4x5 aspect ratio** by default besides the way they were added (from gallery or recorded on camera).

Besides AspectsProvider that provides aspects for selection on trimmer screen there is ``AspectRatioProvider`` interface which provides the default aspect ratio for post processing screens (also if the video editor is started from the editor screen) and for export. By default 9:16 is used but you can configure any aspect you want by providing it through DI, for example:
```kotlin
        single<AspectRatioProvider>(override = true) {
            object : AspectRatioProvider {
                override fun provide(): AspectRatio {
                    return AspectRatio(4.0/5)
                }
            }
        }
```

:exclamation: If you want to display video without black lines provide the following ```VideoDrawParams``` through DI:
```kotlin
val videoDrawParams: BeanDefinition<VideoDrawParams> = factory(override = true) {
    VideoDrawParams(
        scaleType = VideoScaleType.CenterCrop,
        normalizedCropRect = RectF(0F, 0F, 1F, 1F)
    )
}
```
Default value is:
```kotlin
val videoDrawParams: BeanDefinition<VideoDrawParams> = factory(override = true) {
    VideoDrawParams(
        scaleType = VideoScaleType.CenterInside(VideoBackgroundType.Black),
        normalizedCropRect = RectF(0F, 0F, 1F, 1F)
    )
}
```

:exclamation:To configure video scaling on the editor screen during playback provide ```PlayerScaleType``` through DI. To ensure that the video will be fully shown - use ```CENTER_INSIDE``` (keep in mind that if device and video resolutions are different black lines will appear), to fill the screen - use ```FIT_SCREEN_HEIGHT``` (it fills the screen only if video has aspect ratio 9:16)
```kotlin
    factory<PlayerScaleType>(named("editorVideoScaleType"), override = true) {
         PlayerScaleType.CENTER_INSIDE
}
```
<p align="center">
    <img src="screenshots/aspects3.png" alt="Screenshot" width="20%" height="auto" class="docs-screenshot"/>&nbsp;
</p>

```kotlin
    factory<PlayerScaleType>(named("editorVideoScaleType"), override = true){
         PlayerScaleType.FIT_SCREEN_HEIGHT
}
```
<p align="center">
    <img src="screenshots/aspects4.png" alt="Screenshot" width="20%" height="auto" class="docs-screenshot"/>&nbsp;
</p>