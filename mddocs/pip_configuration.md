# Picture in picture

Picture in picture mode is optional for the AI Video Editor SDK and would be disabled if it is not included in your token.
To Start Video editor in PIP mode pass `pictureInPictureConfig` parameter instead of default value in ```startFromCamera``` method (check out an [**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L65)):

```kotlin
VideoCreationActivity.startFromCamera(
    context = this,
    // set PiP video configuration
    pictureInPictureConfig = PipConfig(
        video = Uri.EMPTY, // PiP video Uri
        openPipSettings = false
    )
)
```

## PIP Configuration

Picture in picture supports four modes: ```Floating```, ```TopBottom```, ```React```, ```LeftRight```. You can customize the order of these modes and which of them will be available. Override ```PipLayoutProvider``` in the [DI](https://github.com/Banuba/ve-sdk-android-integration-sample#Step-4-Configure-DI) layer to achieve this:

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
Add the following to the ```IntegrationKoinModule``` class:
```kotlin
factory {
    CameraMuteMicConfig(
        muteInPipMode = true
    )
}
```
```muteInPipMode = true```  microphone will be disabled by default
```muteInPipMode = false```  microphone will be enabled by default
## Example

If you need only ```React``` and ```LeftRight``` modes and ```React``` needs to launch first, then the overridden ```PipLayoutProvider``` will look like this:

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
                EditorPipLayoutSettings.LeftRight(
                        isCameraAlignLeft = true,
                        excludeActions = listOf(EditorPipLayoutAction.SwitchHorizontal)
                )
            )
        }
    }
}
```


Also you can choose default camera align for every mode and exclude actions for every mode: 

```kotlin
...
    return listOf(
        EditorPipLayoutSettings.Floating(
            ...,
            excludeActions = listOf(
                EditorPipLayoutAction.SwitchVertical,
                EditorPipLayoutAction.Square,
                EditorPipLayoutAction.Round
            ),
            isCameraAlignTop = false
        ),
        EditorPipLayoutSettings.TopBottom(
            excludeActions = listOf(
                EditorPipLayoutAction.SwitchVertical,
                EditorPipLayoutAction.Original,
                EditorPipLayoutAction.Centered
            ),
            isCameraAlignTop = false
        ),
        EditorPipLayoutSettings.React(
            ...,
            excludeActions = listOf(
                EditorPipLayoutAction.SwitchVertical,
                EditorPipLayoutAction.Square,
                EditorPipLayoutAction.Round,
                EditorPipLayoutAction.Centered,
                EditorPipLayoutAction.Original
            ),
            isCameraMain = false
        ),
        EditorPipLayoutSettings.LeftRight(
            excludeActions = listOf(
                EditorPipLayoutAction.SwitchHorizontal,
                EditorPipLayoutAction.Original,
                EditorPipLayoutAction.Centered
            ),
            isCameraAlignLeft = false
        )
    )
```
