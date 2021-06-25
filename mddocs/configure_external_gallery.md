# Configure external gallery

To integrate custom media provider (i.e. gallery screen) into Video Editor SDK flow there are few steps should be comlpeted.

## Step 1

Implement `ContentFeatureProvider<List<Uri>, Fragment>` interface. Example:
```kotlin
class ExternalMediaContentProvider : ContentFeatureProvider<List<Uri>, Fragment> {

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
        intent = CustomMediaProviderActivity.createIntent(context).apply {
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

Method `init` is invoked inside Video Editor SDK to register onActivityResult callback to receive media content. We use `ProvideMediaContentContract` class to manage data bundle that passed between SDK and your external media provider implementation. 

Inside `requestContent` method we create an intent that will be used inside our `CustomMediaProviderActivity` and return it wrapped into `ContentFeatureProvider.Result.RequestUi` object.

**NOTE**: `extras` argument contains meta data for media content selection and should be passed into your external media provider.

`handleResult` method connects onActivityResult callback within Video Editor SDK with the media content provided by `CustomMediaProviderActivity`.

## Step 2

Inside `onCreate()` method of `CustomMediaProviderActivity` obtain media request params:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let { extras ->

        val params = ProvideMediaContentContract.obtainParams(extras)

        }
```

`Params` object contains meta info about request from SDK:
```kotlin
    data class Params(
        val mode: OpenGalleryMode,
        val types: List<MediaType>,
        val maxCount: Int,
        val minCount: Int,
        val supportedFormats: List<String>
    )
```

`mode` - is a type of request

`types` - list of requested media types (Video, Image)

`supportedFormats` - list of media file extensions that are supported by SDK


## Step 3

Inside `CustomMediaProviderActivity` deliver media data into Video Editor SDK:

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

## Step 4

Provide `ExternalMediaContentProvider` created in Step 1 through DI:

```kotlin
    override val mediaContentProvider:
    BeanDefinition<ContentFeatureProvider<List<Uri>>>=
        factory(named("mediaDataProvider"), override = true) {
            ExternalMediaContentProvider()
        }
```