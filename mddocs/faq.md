# FAQ  
These are the answers to the most popular questions we are asked about the Banuba AI Video Editor SDK

- [How do I start/stop recording with a tap?](#how-do-i-startstop-recording-with-a-tap)
- [How do I add an AR mask to the app without using the AR cloud?](#how-do-i-add-an-ar-mask-to-the-app-without-using-the-ar-cloud)
- [I want to turn off animations from slideshow](#i-want-to-turn-off-animations-from-slideshow)
- [I want to start VideoEditor with a preselected audio track](#i-want-to-start-videoEditor-with-a-preselected-audio-track)
- [How do I add LUTs to the app?](#how-do-i-add-luts-to-the-app)
- [How do I change the order of LUTs](#how-do-i-change-the-order-of-luts)
- [I want to control visibility of debug info on camera and editor screens](#i-want-to-control-visibility-of-debug-info-on-camera-and-editor-screens)
- [I want to customize gallery icon](#i-want-to-customize-gallery-icon)
- [How does video editor work when token expires?](#how-does-video-editor-work-when-token-expires)
- [I want to change the font in Video Editor](#i-want-to-change-the-font-in-video-editor)
- [How to obtain GIF preview image of exported video](#how-to-obtain-gif-preview-image-of-exported-video)
- [How to export in Background](#how-to-export-in-background)
- [How to change drafts configuration](#how-to-change-drafts-configuration)
- [How to add other text fonts that are used in the editor screen](#how-to-add-other-text-fonts-that-are-used-in-the-editor-screen)
- [How to change exported video codec configuration](#how-to-change-exported-video-codec-configuration)
- [How can I get a track data of the audio used in my video after export?](#how-can-i-get-a-track-data-of-the-audio-used-in-my-video-after-export)
- [Optimizing app size](#optimizing-app-size)
- [How do I specify the video file saving directory?](#how-do-I-specify-the-video-file-saving-directory)
- [How do I change the duration of the image display in a slideshow?](#how-do-I-change-the-duration-of-the-image-display-in-a-slideshow)
- [How do I change the launguage (how do I add new locale support)](#how-do-I-change-the-launguage-how-do-I-add-new-locale-support)
- [How do I disable the video (FX) effects?](#how-do-i-disable-the-video-fx-effects)
- [How to use different FFmpeg build](#how-to-use-different-ffmpeg-build)

### How do I start/stop recording with a tap?
By default, the user must hold the “record” button to film and release it to stop filming.   

To change that, simply set the `takePhotoOnTap` property inside [**CameraConfig**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/mddocs/config_camera.md) class to **false**.

### How do I add an AR mask to the app without using the AR cloud?
If you don’t want to pull the masks from the backend, you can include them in the app itself. 

To do so, name the folder with the mask files the way you want to call the mask, and place it into **assets/bnb-resources/effects** directory in the module containing the Video Editor SDK (Example).

Make sure that you include the **preview.png** file in the mask folder. It serves as an icon for the mask within the app. 

### I want to turn off animations from slideshow

Slideshow is created either by selecting pictures from gallery or by making a photo on Video Editor camera screen.

Every slide within slideshow can appear with or without animations. This behavior is configured within [**EditorConfig**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/mddocs/config_videoeditor.md#slideshow) class under ```slideshow``` section:

To turn off animations just **setup false for both fields**.

### I want to start VideoEditor with a preselected audio track

You should create an intent using the method **VideoCreationActivity.buildIntent()** where **audioTrackData** is preselected audio track.

```kotlin
startActivity(
    VideoCreationActivity.buildIntent(
       context,
       cameraMode,
       audioTrackData
    )
)
```
**audioTrackData** is an object of TrackData class

```kotlin
data class TrackData(
    val id: UUID,
    val title: String,
    val localUri: Uri
)
```

### How do I add LUTs to the app?

Color filters are located in the **assets/bnb-resources/luts** directory in the module with the AI Video Editor SDK. To add your own, place the files in this folder and create a drawable resource that will be used as an icon for this particular LUT. The name of the drawable resource must be the same as the graphic file in the filter’s directory.

For example, this is the [lux LUT](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/bnb-resources/luts/lux.png) file, and this is its [drawable resource](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/drawable-hdpi/lux.png).

### How do I change the order of LUTs?

By default, the filters are listed in alphabetical order. 

To change it, use the implementation of the ```OrderProvider``` interface.
```kotlin
class CustomColorFilterOrderProvider : OrderProvider {
    override fun provide(): List<String> = listOf("egypt", "byers")
}
``` 
This will return the list of color filters with the required order.
Note: The name of color filter is a name of an appropriate file located in **assets/bnb-resources/luts** directory. [Example](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/bnb-resources/luts/egypt.png).

The final step is to pass your custom ```CustomColorFilterOrderProvider``` implementation in the [DI](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-di) to override the default implementation:

```kotlin
override val colorFilterOrderProvider: BeanDefinition<OrderProvider> =
    single(named("colorFilterOrderProvider"), override = true) {
        CustomColorFilterOrderProvider()
    }
```

### I want to control visibility of debug info on camera and editor screens

You can control visibility of camera config information and camera preview params(FPS, ISO). Change `showCameraInfoAndPerformance` property in [**CameraConfig**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/mddocs/config_camera.md) class.

You can control visibility of editor config. Change `showEditorConfig` property in  [**EditorConfig**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/mddocs/config_videoeditor.md) class.

### I want to customize gallery icon

Gallery icon is represented by AppCompatImageView. Its style placed into `galleryImageViewStyle` attribute of the main theme ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/1e37324dea76304e8e9205d463844ac5c8c199f7/app/src/main/res/values/themes.xml#L104))

**Drawable resource** of the gallery icon may vary depending on the use case:
 - in case of the very first launch, if the user **did not grant permission** to [read external storage](https://developer.android.com/reference/android/Manifest.permission#READ_EXTERNAL_STORAGE), or if the gallery on the device **is empty**, the drawable resource defined in `icon_empty_gallery` attribute of the `CameraOverlayStyle` ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/1e37324dea76304e8e9205d463844ac5c8c199f7/app/src/main/res/values/themes.xml#L469)) will be used
 - in other cases you can select what to show as a gallery icon: 
    -  **the last media file** from the device
    - **custom drawable** resource

![img](screenshots/faq1.png)

**By default the last media file is used as a drawable**. You have an option to **put background** to the gallery icon by changing `icon_gallery_background` attribute of `CameraOverlayStyle` and you can **add rounded corners** to the gallery icon by changing `icon_gallery_radius` attribute of this style ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/1e37324dea76304e8e9205d463844ac5c8c199f7/app/src/main/res/values/themes.xml#L420)).

 **To setup custom drawable resource** instead of the last media file you have to put custom style into `galleryImageViewStyle` attribute of the main theme ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/1e37324dea76304e8e9205d463844ac5c8c199f7/app/src/main/res/values/themes.xml#L105)) and set there `use_custom_image` to `true` and put your drawable as `android:src` attribute value

### How does video editor work when token expires?

[Token](https://github.com/Banuba/ve-sdk-android-integration-sample#token) provided by sales managers has an expiration term to protect Video Editor SDK from malicious access. When the token expires the following happens:
 - video resolution will be lowered to 360p on camera, after trimmer and after export
 - Banuba watermark is applied to every exported video

 Also [FaceAR SDK](https://docs.banuba.com/face-ar-sdk/overview/token_management#how-does-it-work) you may expect the following actions if the token expires:
 - on the first expired month a watermark with "Powered by Banuba" label will be added on the top of both recorded and exported videos
 - after the first month the camera screen will be blurred and a full-screen watermark will be displayed

 Please keep your licence up to date to avoid unwanted behavior.

 ### I want to change the font in Video Editor

All text view styles in Video Editor SDK are inherited from the `Text` style, thus the font set in this style will be applied to all text views in Video Editor.

To apply `customFont` to Video Editor just override this style:

 ```
<style name="Text">
    <item name="android:fontFamily">@font/customFont</item>
</style>
 ```

Using this approach you don't have to repeatedly set the font to any other styles while customizing the Video Editor.


 ### How to obtain GIF preview image of exported video

 Video Editor SDK allows to obtain exported video preview as a gif image.

 Inside your `ExportParamsProvider` implementation create a `GifMaker.Params`:

 ```kotlin
     data class Params(
        val destFile: File,
        val sourceVideoRangeMs: LongRange = 0..1000L,
        val fps: Int = 15,
        val width: Int = 240,
        val useDithering: Boolean = true,
        val reverse: Boolean = true
    )
 ```

Where

 - `destFile` - is a file where gif will be stored after export
- `sourceVideoRangeMs` - is a range of exported video that will be used to create gif image
-  `fps` - frames per second within gif image
- `width` - is a width of gif image in pixels
- `useDithering` - is a flag that apply or remove dithering effect (in simple words make an image of better quality)
- `reverse` - is a flag to reverse playback inside gif

 This object should be passed into `interactivePreview()` method of `ExportParams` along with the rest export data:

 ```diff
ExportParams.Builder(sizeProvider.videoResolution)
                .effects(effects)
                .fileName("export_video")
                .videoRangeList(videoRangeList)
                .destDir(exportSessionDir)
                .musicEffects(musicEffects)
                .extraAudioFile(extraSoundtrackUri)
                .volumeVideo(videoVolume)
 +              .interactivePreview(gifPreviewParams)
                .build()
 ```


### How to export in Background
1. Provide `BackgroundExportFlowManager` through DI: 

    ```kotlin
    override val exportFlowManager: BeanDefinition<ExportFlowManager> = single(override = true) {
            BackgroundExportFlowManager(
                exportDataProvider = get(),
                editorSessionHelper = get(),
                draftManager = get(),
                exportNotificationManager = get(),
                exportDir = get(named("exportDir")),
                shouldClearSessionOnFinish = true,
                publishManager = get()
            )
        }
    ```

2. Add android activity that you want to open after export into `AndroidManifest.xml` file with special intent action filter. We suggest to use **applicationId** as a part of intent action name to make it unique among other possible intent actions:

    ```kotlin
    <activity android:name=".CustomActivity">
        <intent-filter>
          <action android:name="${applicationId}.ShowExportResult" />
          <category android:name="android.intent.category.DEFAULT" />
        </intent-filter>
    </activity>
    ```

3. Provide `ExportResultHandler` implementation. `doAction` method should start activity mentioned in step 2 with the same intent action name as mentioned in AndroidManifest:

    ```kotlin
    class CustomExportResultHandler : ExportResultHandler {

        override fun doAction(activity: AppCompatActivity, result: ExportResult.Success?) {
            val intent = Intent("${activity.packageName}.ShowExportResult").apply {
                result?.let { putExtra(EXTRA_EXPORTED_SUCCESS, it) }
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            activity.startActivity(intent)
        }
    }
    ```

    **Note**: action that is passed into `Intent` constructor **must be the same** as  the action name from activity intent filter from step 2.

4. Inside activity from step 2 add `ExportFlowManager` **koin injection**:

    ```kotlin
    class CustomActivity: AppCompatActivity() {

    	private val exportFlowManager: ExportFlowManager by inject()

    }
    ```

5. Inside `onCreate()` method start to observe `resultData` `LiveData` from `ExportFlowManager` to receive export states inside your activity:

    ```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            
            exportFlowManager.resultData.nonNull().observe(this) { exportResult ->
                //todo: Do your stuff with different ExportResult objects
            }
    }
    ```

    **Note**: `ExportResult` is a sealed class:

    ```kotlin
    sealed class ExportResult {

        object Inactive : ExportResult()

        object Stopped : ExportResult()

        data class Progress(
            val preview: Uri
        ) : ExportResult()

        @Parcelize
        data class Success(
            val videoList: List<ExportedVideo>,
            val preview: Uri,
            val metaUri: Uri,
            val additionalExportData: Parcelable? = null
        ) : ExportResult(), Parcelable

        @Parcelize
        data class Error(val type: ExportError) : ExportResult(), Parcelable
    }
    ```

6. *Optional*. Provide `ExportNotificationManager` to manage notifications about exporting process state.

    Here you have 3 options:

    - Remove notifications by providing implementation with stubbed methods:

        ```kotlin
        class EmptyExportNotificationManger() : ExportNotificationManager {

        fun showExportStartedNotification(){}
        fun showSuccessfulExportNotification(result: ExportResult.Success){}
        fun showFailedExportExportNotification(){}

        }
        ```

        Do not forget to provide this implementation through DI:

        ```kotlin
            val exportNotificationManager: BeanDefinition<ExportNotificationManager> = single(override = true) {
            EmptyExportNotificationManager()
        }
        ```

    - Configure provided default implementation. 
        
        In this case you just need to override or translate following android resources:
         
         - R.string.export.notification_started - for message about started export
         - R.string.export_notification_success - for message about succeeded export
         - R.string.export_notification_fail - for message about failed export
         - R.drawable.ic_export_notification - for notification icon in system bar

    - Provide custom implementation with your own logic through DI:

        ```kotlin
        val exportNotificationManager: BeanDefinition<ExportNotificationManager> = single(override = true) {
            CustomExportNotificationManger()
        }
        ```

 ### How to change drafts configuration

 By default drafts enabled, asks the user to save a draft before leave any VideoEditor screen. If you need to change drafts configuration you should add the code below in the `VideoEditorKoinModule`:

 ```kotlin
     override val draftConfig: BeanDefinition<DraftConfig> = factory(override = true) {
         DraftConfig.ENABLED_ASK_TO_SAVE
     }
 ```

You can choose one of these options:
1. `ENABLED_ASK_TO_SAVE` - drafts enabled, asks the user to save a draft
2. `ENABLED_ASK_IF_SAVE_NOT_EXPORT` - drafts enabled, asks the user to save a draft without export
3. `ENABLED_SAVE_BY_DEFAULT` - drafts enabled, saved by default without asking the user
4. `DISABLED` - disabled drafts

 ### How to add other text fonts that are used in the editor screen

To add other text fonts that are used in the editor screen follow the next steps:

1. Add font files to the `app/src/main/res/font/` directory;

2. Add fonts names to the [**strings.xml**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/strings.xml) resource file:
    ```xml
    <string name="font_1_title" translatable="false">Font 1 Title</string>
    <string name="font_N_title" translatable="false">Font N Title</string>
    ```

3. Add `font_resources.xml` with fonts array declaration to the `app/src/main/res/values/` directory. The format of `font_resources.xml` should be the next one:
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <resources>
        <array name="font_resources">
            <item>@array/font_1_resource</item>    <!-- link to the font description array -->
            <item>@array/font_N_resource</item>
        </array>

        <array name="font_1_resource">
            <item>@string/font_1_title</item>      <!-- font name -->
            <item>@font/font_1</item>              <!-- link to the font file -->
        </array>

        <array name="font_N_resource">
            <item>@string/font_N_title</item>
            <item>@font/font_N</item>
        </array>
    </resources>
    ```

4. The final step is to pass your custom `font_resources` id to the `ResourcesTextOnVideoTypefaceProvider` in the [DI](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-di) to override the default implementation:

    ```kotlin
    override val textOnVideoTypefaceProvider: BeanDefinition<TextOnVideoTypefaceProvider> =
        single(override = true) {
            ResourcesTextOnVideoTypefaceProvider(
                fontsArrayResId = R.array.font_resources,
                context = get()
            )
        }
    ```

### How to change exported video codec configuration

By default for exported video the `HEVC` codec is enabled. If you need to change codec configuration you should add the code below in the `VideoEditorKoinModule`:

```kotlin
    val codecConfiguration: BeanDefinition<CodecConfiguration> = single(override = true) {
        CodecConfiguration.AVC_PROFILES
    }
```

You can choose one of these options:
1. `HEVC` - H265 codec enabled
2. `AVC_PROFILES` - H264 codec with profiles enabled
3. `BASELINE` - H264 codec without profiles enabled

### How can I get a track data of the audio used in my video after export?

You can get track data after export using `getExportedMusicEffect` method of `ExportBundleHelper` object. Just pass into the `getExportedMusicEffect` the `additionalExportData` paramete of `ExportResult.Success` object. In the result you get `List<MusicEffectExportData>` where `MusicEffectExportData` is:

```kotlin
    @Parcelize
    data class MusicEffectExportData(
        val title: String,
        val type: MusicEffectType,
        val uri: Uri
    ) : Parcelable
```

`MusicEffectType` contains next values:
1. `TRACK` - audio tracks that were added on  the `Editor` screen
2. `VOICE` - voice record track that was added on the `Editor` screen
3. `CAMERA_TRACK` - an audio track that was added on the `Camera` screen

### Optimizing app size

The easiest way to gain immediate app size savings when publishing to Google Play is by uploading your app as an [**Android App Bundle**](https://developer.android.com/guide/app-bundle), which is a new upload format that includes all your app’s compiled code and resources. Google Play’s new app serving model then uses your app bundle to generate and serve optimized APKs for each user’s device configuration, so they download only the code and resources they need to run your app.

As a result, the final size of our library for one of the platform types (`armeabi-v7a`,` arm64-v8a`, `x86`,` x86_64`) will be **24-26 MB** less than indicated in the documentation

### How do I specify the video file saving directory?

By default, they are placed in the "export" directory of external storage. To change the target folder, you should provide a custom Uri instance named exportDir through DI. For example, 
to change the title of destination directory to "my_awesome_directory", provide the Uri instance below:

```kotlin
single(named("exportDir"), override = true) {
            get<Context>().getExternalFilesDir("")?.toUri()
                ?.buildUpon()
                ?.appendPath("my_awesome_directory")
                ?.build() ?: throw NullPointerException("exportDir should not be null!")
        }
```

### How do I change the duration of the image display in a slideshow?

Use the ```slideShowSourceVideoDurationMs``` parameter in [EditorConfig](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/mddocs/config_videoeditor.md) class:

```kotlin
{
   single<EditorConfig>(override = true) {
      EditorConfig(
         slideShowSourceVideoDurationMs = 3000
      )
   }
}
```

### How do I change the launguage (how do I add new locale support)?

There is no special language switching mechanism in the Video Editor SDK (VE SDK).

Out of the box, the VE SDK includes support for two locales: English (default) and Russian. If you need to support any other locales, you can do it according to the standard Android way. See how [Create locale directories and resource files](https://developer.android.com/training/basics/supporting-devices/languages#CreateDirs) for more details. After adding a new locale resource file into your application with integrated VE SDK, you need to re-define the VE SDK strings keys with new locale string values.
To do that you need to add all needed string keys in the new locale `strings.xml` file. You can find the main VE SDK string keys you need in the [Configure screens](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-screens) doc page. E.g. string keys of the Editor screen you can find [here](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/mddocs/editor_styles.md#string-resources).
The newly added locale will be applied after the device language is changed by system settings.

If you need to change language programmatically in your application, see the next links how it can be done:
[one](https://www.geeksforgeeks.org/how-to-change-the-whole-app-language-in-android-programmatically/),
[two](https://medium.com/swlh/android-app-specific-language-change-programmatically-using-kotlin-d650a5392220)

### How do I disable the video (FX) effects?
The allowed video effects are included into the token, but they can be disabled manually by customer.

To do it, pass your custom implementation of the ```EnabledEffectsProvider``` interface in the [DI](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-di) to override the default implementation:
```kotlin
single(named("fxEffectsProvider"), override = true) {
    EnabledEffectsProvider {
        listOf("acid_whip", "soul", "zoom")
    }
}
```
This will return the list of enabled video effects.

**Note**: To get all video effects names list use ```DefaultEnabledFxEffectsProvider().provide()``` method.

### How to use different FFmpeg build
Check out [step-by-step guide](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/mddocs/ffmpeg_dependency.md) to use your FFmpeg build.
