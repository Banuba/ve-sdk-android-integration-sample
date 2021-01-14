# API for using AR cloud in the SDK

## Implementation

### Step 1

Initialize ArCloudKoinModule before VideoEditorKoinModule.  

```kotlin
startKoin {
    modules(
        ArCloudKoinModule().module,
        VideoEditorKoinModule().module,
    )
}
```

### Step 2

Get AR cloud token.
The token should be put [here](../app/src/main/res/values/strings.xml#L9)

### Step 3

Add following code to your VideoEditorKoinModule.

```kotlin
class VideoEditorKoinModule : FlowEditorModule() {

    ...

    val arEffectsRepositoryProvider: BeanDefinition<ArEffectsRepositoryProvider> =
        single(override = true, createdAtStart = true) {
            ArEffectsRepositoryProvider(
                arEffectsRepository = get(named("backendArEffectsRepository")),
                ioDispatcher = get(named("ioDispatcher"))
            )
        }

    val arEffectsUUIDProvider: BeanDefinition<String> =
        single(named("arEffectsCloudUuid"), override = true) {
            androidContext().getString(R.string.ar_cloud_token)
        }
}
```