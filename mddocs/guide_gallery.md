# Gallery integration guide

- [Add module](#Add-module)
- [Customize default gallery](#Customize-default-gallery)
- [Implement custom gallery](#Implement-custom-gallery)
- [Progress screen](#Progress-screen)

## Add module 
Video Editor SDK includes built in solution for the gallery where the user can pick any video or image and use it while making video.  
To connect Banuba Gallery screen and its functionality you need to add the dependency in [build.gradle](../app/build.gradle#L60) file.
```kotlin
implementation "com.banuba.sdk:ve-gallery-sdk:1.26.5"
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


Please use the following styles to change appearance of Gallery screen that will meet your requirements.

- [galleryImageViewStyle](../app/src/main/res/values/themes.xml#L116)  
  style defines the overview of gallery icon on the camera screen. The last media resource is used as gallery icon drawable by default.  
  To show the custom icon for gallery set `use_custom_image` attribute to "true" and pass custom drawable to `android:src`. If there is no media resource on the device the icon from `icon_empty_gallery` attribute of `CameraOverlayView` style is used (if `use_custom_image` is false).

  ![img](screenshots/gallery4.png)

- [galleryBackButtonStyle](../app/src/main/res/values/themes.xml#L118)

  back button style which is placed at top left corner of the screen. This icon has two states: if some files are already selected in the gallery and if nothing is selected. Both states have different drawables that are configured into `VideoCreationTheme` [attributes](../app/src/main/res/values/themes.xml#L862):

    - `ic_nav_back_arrow` -  nothing is selected → **get back to the previous screen**
    - `ic_nav_close` - some files are selected → **clear selection**

  ![img](screenshots/gallery5.png)

- [galleryTitleTextStyle](../app/src/main/res/values/themes.xml#L117)

  style for the gallery screen title (screen title equals to the selected media album)

- [galleryItemRadioButtonStyle](../app/src/main/res/values/themes.xml#L123)

  style for the radio button that is used to define selected resource. It has a bulk of custom attributes to customize internal colors
- [galleryItemTextStyle](../app/src/main/res/values/themes.xml#L124)

  style for the TextView that shows duration for video resources

- [galleryTabLayoutStyle](../app/src/main/res/values/themes.xml#L129)

  style for the tab layout used for video and image tabs

- [galleryTabTextColors](../app/src/main/res/values/themes.xml#L130)

  attribute holds the text colors for different states of tabs

- [galleryNextParentStyle](../app/src/main/res/values/themes.xml#L119)

  style for the bottom view that containes selection info and next button

- [galleryNextButtonStyle](../app/src/main/res/values/themes.xml#L120)

  style for the button that appears during selection to proceed with selected resources

- [gallerySelectionDescriptionStyle](../app/src/main/res/values/themes.xml#L121)

  style for the description of selected files

  ![img](screenshots/gallery6.png)

- [galleryEmptyTextStyle](../app/src/main/res/values/themes.xml#L125)

  style for the title and description views that are shown in case of empty gallery
- [galleryAlbumBlurViewStyle](../app/src/main/res/values/themes.xml#L126)

  style for the custom view that is used to apply blur effect as a background for albums list. It can have custom tint via "overlay_color" attribute
- [galleryAlbumTitleTextStyle](../app/src/main/res/values/themes.xml#L127)

  style for the album title
- [galleryAlbumDescTextStyle](../app/src/main/res/values/themes.xml#L128)

  style for the album description

  ![img](screenshots/gallery7.png)

- [galleryColumnsNumber](../app/src/main/res/values/themes.xml#L132) - setup how many columns the gallery screen shows
- [gallery_bg_color](../app/src/main/res/values/themes.xml#L133) - background color for gallery screen
- [gallery_item_corner_radius](../app/src/main/res/values/themes.xml#L134) - setup corner radius for every gallery item
- [gallery_item_margin](../app/src/main/res/values/themes.xml#L135) - setup margins between items in gallery (applied in all directions)
- [gallery_album_divider_color](../app/src/main/res/values/themes.xml#L136) - divider color in the albums list

Below string resources are used and can be customized.

| ResourceId        |      Value      |   Description |
| ------------- | :----------- | :------------- |
| gallery_next | Next | text on the button that opens the editor screen with selected videos or images. Defined in ```galleryNextButtonStyle```
| gallery_tab_title_image | Image | label of image tab
| gallery_tab_title_video | Video | label of video tab
| gallery_selection_count_default | Select Items | description inside ```gallerySelectionDecriptionStyle``` while no selection made
| gallery_selection_count_description_video | Import %1$d Videos | **plurals resource** that is used inside ```gallerySelectionDescriptionStyle``` when user selects video
| gallery_selection_count_description_image | Import %1$d Images | **plurals resource** that is used inside ```gallerySelectionDescriptionStyle``` when user selects image
| gallery_selection_count_description_mix | Import %1$d Files | **plurals resource** that is used inside ```gallerySelectionDescriptionStyle``` when user selects video or image
| gallery_album_untitled | untitled | the title of the album that has no name (it may be created in case of some videos or images has no album name in theirs meta data)
| gallery_album_all_media | All | the title of the album that contains all media resources
| gallery_empty_image_list | No images found | message that is shown in the middle of the gallery screen in case of there are no images found on device. The view is defined in ```galleryEmptyTextStyle```
| gallery_empty_video_list | No videos found | message that is shown in the middle of the gallery screen in case of there are no videos found on device. The view is defined in ```galleryEmptyTextStyle```
| gallery_choose_at_least | Select at least %1$d files | **plurals resource** that is used for the [toast](guide_popus.md) message that is shown when the user want to open the editor screen but the number of selected resources (videos or images) is less than predefined custom value (recently "1" is used). This predefined value is a placeholder in every plural string resource here
| err_gallery_broken_file | Damaged file | message shown as a [toast](guide_popus.md) in case of tapping on the damaged media file
| err_gallery_limit_selected | You can select only %1$d files | **plurals resource** that is used for the toast message if the user tries to select more files than allowed


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


## Progress screen
When import video sources from Gallery takes time progress screen appear.

You can change appearance of this screen by overriding these styles and resources.

- [loadingScreenParentViewStyle](../app/src/main/res/values/themes.xml#L442)  
  style for the root Constraint layout that represents

- [loadingScreenTitleStyle](../app/src/main/res/values/themes.xml#L443)  
  style for the loading dialog title

- [loadingScreenDescStyle](../app/src/main/res/values/themes.xml#L444)  
  style for the loading dialog description

- [loadingScreenProgressStyle](../app/src/main/res/values/themes.xml#L445)  
  style for the loading dialog progress

- [loadingScreenCancelBtnStyle](../app/src/main/res/values/themes.xml#L446)  
  style for the loading dialog cancel button

  ![img](screenshots/media_progress_screen.png)

Below are string you can use or customize.

:exclamation: Important  
Some strings on the screen are defined in the styles. To localize these strings firstly create string resources and setup them into styles under `android:text` attribute.

| ResourceId        |      Value      |   Description |
| ------------- | :----------- | :------------- |
| editor_alert_import_failed | Content uploading failed | title of alert that is shown when the process of gallery resources import was failed
| editor_alert_import_failed_desc | | description of alert that is shown when the process of gallery resources import was failed
| editor_alert_import_failed_positive | Retry | positive button text of alert that is shown when the process of gallery resources import was failed
| editor_alert_import_failed_negative | Cancel | negative button text of alert that is shown when the process of gallery resources import was failed
| editor_alert_export_stopped | Do you want to interrupt the video export? | title of alert that is shown when the export process was stopped
| editor_alert_export_stopped_desc | | description of alert that is shown when the export process was stopped
| editor_alert_export_stopped_positive | Interrupt | positive button text of alert that is shown when the export process was stopped
| editor_alert_export_stopped_negative | Cancel | negative button text of alert that is shown when the export process was stopped
| editor_alert_export_interrupted | Export interrupted | title of alert that is shown when the process of export was interrupted
| editor_alert_export_interrupted_desc | | description of alert that is shown when the process of export was interrupted
| editor_alert_export_interrupted_positive | Retry | positive button text of alert that is shown when the process of export was interrupted
| editor_alert_export_interrupted_negative | Cancel | negative button description of alert that is shown when the process of export was interrupted
| loading_export_title | Exporting video | title of the exporting dialog
| loading_export_description | Please, don\'t lock your screen or switch to other apps | description of the exporting dialog
| loading_import_title | Importing video | title of the importing dialog
| loading_import_description | Please, don\'t lock your screen or switch to other apps | description of the importing dialog
