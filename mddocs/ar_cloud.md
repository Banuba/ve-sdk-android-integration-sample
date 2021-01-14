# API for using AR cloud in the SDK

AR cloud is a product, which saves space for your app. In a nutshell, it stores AR masks on a server, instead of the SDK code. When the user open the app they download the masks, which are then saved in his phone’s memory.  

With AR cloud you also can easily rotate masks for some special events like Christmas, Halloween, or Diwali.
AR cloud is fully compatible with both Video Editor and Face AR products.

### Step 1

Initialize ArCloudKoinModule module in Koin.  
*ArCloudKoinModule should be placed before VideoEditorKoinModule.*

```kotlin
startKoin {
    modules(
        ArCloudKoinModule().module,
        VideoEditorKoinModule().module,
    )
}
```

### Step 2

Configure AR Cloud dependencies in DI layer.

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

### Step 3

Paste AR Cloud client id value [here](../app/src/main/res/values/strings.xml#L14).  
*Please ask Banuba to provide **client id** for trial period.*