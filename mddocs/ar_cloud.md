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

Get AR cloud UUID. Add following code to your VideoEditorKoinModule.

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
           Paste your UUID here
        }
}
```