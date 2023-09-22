# Gallery integration guide

- [Add module](#Add-module)
- [Customize default gallery](#Customize-default-gallery)
- [Implement custom gallery](#Implement-custom-gallery)

## Add module 
Video Editor SDK includes built in solution for the gallery where the user can pick any video or image and use it while making video.  
To connect Banuba Gallery screen and its functionality you need to add the dependency in [build.gradle](../app/build.gradle#L60) file.
```kotlin
implementation "com.banuba.sdk:ve-gallery-sdk:1.30.2"
```
and [GalleryKoinModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L71) to the list of modules
```diff
startKoin {
    androidContext(this@IntegrationApp)
        modules(
            ...
+           GalleryKoinModule().module
        )
}
```

## Customize default gallery
Instance of ```EditorConfig``` that you implement in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt)  contains 2 properties that will allow you to filter media content:
- ```gallerySupportsVideo``` - if the ```Video``` tab is shown on the Gallery screen. Values: true/false. Default ```true```
- ```gallerySupportsImage``` - if the ```Image``` tab is shown on the Gallery screen. Values: true/false. Default ```true```


## Implement custom gallery

Video editor allows to replace default gallery with your custom. Please follow implementation guide.  
First, create ```CustomMediaContentProvider``` class that implements ```ContentFeatureProvider<List<Uri>, Fragment>```. 
This class describes a contract between Video Editor and specific Fragment from your project for gallery. 

```kotlin
class CustomMediaContentProvider : ContentFeatureProvider<List<Uri>, Fragment> {

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    private val activityResultCallback: (List<Uri>?) -> Unit = {
        activityResultCallbackInternal(it)
    }
    private var activityResultCallbackInternal: (List<Uri>?) -> Unit = {}

    override fun init(hostComponent: WeakReference<Fragment>) {
        activityResultLauncher = hostComponent.get()?.registerForActivityResult(
            ProvideMediaContentContract(),
            activityResultCallback
        )
    }

    override fun requestContent(
        context: Context,
        extras: Bundle
    ): ContentFeatureProvider.Result<List<Uri>> = ContentFeatureProvider.Result.RequestUi(
        intent = CustomGalleryActivity.createIntent(context).apply {
            putExtras(extras)
        }
    )

    override fun handleResult(
        hostComponent: WeakReference<Fragment>,
        intent: Intent,
        block: (List<Uri>?) -> Unit
    ) {
        activityResultCallbackInternal = block
        activityResultLauncher?.launch(intent)
    }
}
```

Method `init` is invoked in Video Editor to register `onActivityResult` callback and receive media content.
`ProvideMediaContentContract` class is used to manage data bundle that is passed between video editor and your custom media provider implementation.  

Method `requestContent` is used to create `Intent` for starting your custom Activity with gallery.
`extras` argument contains metadata for media content selection and should be passed into your custom media provider.

`handleResult` method connects `onActivityResult` callback within Video Editor with the media content provided by `CustomGalleryActivity`.

Next, obtain media request params in `CustomGalleryActivity.onCreate()` 

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let { extras ->
            val params = ProvideMediaContentContract.obtainParams(extras)
            ...
        }
```

`Params` object contains data about request from video editor
```kotlin
    data class Params(
        val mode: OpenGalleryMode,
        val types: List<MediaType>,
        val maxCount: Int,
        val minCount: Int,
        val supportedFormats: List<String>
    )
```

`mode` - is a type of request (NORMAL, FEATURE_BACKGROUND, ADD_TO_TRIMMER)
`types` - list of requested media types (Video, Image)  
`supportedFormats` - list of media file extensions that are supported by SDK  


Deliver media data from `CustomGalleryActivity` to video editor

```kotlin
val resultIntent = Intent().apply {
            putParcelableArrayListExtra(
                ProvideMediaContentContract.EXTRA_MEDIA_CONTENT_RESULT,
                ArrayList(selectedMedia)
            )
        }
setResult(Activity.RESULT_OK, resultIntent)
```

`selectedMedia` is a list of media Uris with **content://** scheme.

Finally, provide `CustomMediaContentProvider` in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt)

```kotlin
    factory<ContentFeatureProvider<List<Uri>, Fragment>>(named("mediaDataProvider"), override = true) { (external: Boolean?) ->
        CustomMediaContentProvider()
    }
```
and remove module `GalleryKoinModule` from the list of modules.
```diff
   startKoin {
            androidContext(applicationContext)
            allowOverride(true)

            modules(
                ...
-               GalleryKoinModule().module,
                ...
            )
        }
```