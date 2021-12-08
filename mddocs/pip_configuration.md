# Picture in picture

Picture in picture mode is optional for the AI Video Editor SDK and would be disabled if it is not included in your token.
To Start Video editor in PIP mode pass `pictureInPictureVideo` instead of default value in ```startFromCamera``` method (check out an [**example**](../app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L38)):

```kotlin
VideoCreationActivity.startFromCamera(
    context: Context,
    pictureInPictureVideo: Uri = Uri.EMPTY,
    additionalExportData: Parcelable? = null,
    audioTrackData: TrackData? = null,
    themResId: Int? = null,
    cameraUIType: CameraUIType = CameraUIType.TYPE_1
)
```

## PIP Configuration

Picture in picture supports four modes: ```Floating```, ```TopBottom```, ```React```, ```LeftRight```. You can customize the order of these modes and which of them will be available. Override ```PipLayoutProvider``` in the [DI](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-di) layer to achieve this:

```kotlin
override val pipLayoutProvider: BeanDefinition<PipLayoutProvider> = single(override = true) {
    object : PipLayoutProvider {
        override fun provide(
            insetsOffset: Int,
            screenSize: Size
        ): List<EditorPipLayoutSettings> {
            val context = androidContext()
            return listOf(
                EditorPipLayoutSettings.Floating(
                    context = context,
                    physicalScreenSize = screenSize,
                    topOffsetPx = context.dimen(R.dimen.pip_floating_top_offset) + insetsOffset
                ),
                EditorPipLayoutSettings.TopBottom(),
                EditorPipLayoutSettings.React(
                    context = context,
                    physicalScreenSize = screenSize,
                    topOffsetPx = context.dimen(R.dimen.pip_react_top_offset) + insetsOffset
                ),
                EditorPipLayoutSettings.LeftRight()
            )
        }
    }
}
```

## Example

If you need only ```React``` and ```Floating``` modes and ```React``` needs to launch first, then the overridden ```PipLayoutProvider``` will look like this:

```kotlin
override val pipLayoutProvider: BeanDefinition<PipLayoutProvider> = single(override = true) {
    object : PipLayoutProvider {
        override fun provide(
            insetsOffset: Int,
            screenSize: Size
        ): List<EditorPipLayoutSettings> {
            val context = androidContext()
            return listOf(
                EditorPipLayoutSettings.React(
                    context = context,
                    physicalScreenSize = screenSize,
                    topOffsetPx = context.dimen(R.dimen.pip_react_top_offset) + insetsOffset
                ),
                EditorPipLayoutSettings.Floating(
                    context = context,
                    physicalScreenSize = screenSize,
                    topOffsetPx = context.dimen(R.dimen.pip_floating_top_offset) + insetsOffset
                )
            )
        }
    }
}
```
