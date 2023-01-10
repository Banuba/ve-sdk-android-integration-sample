## Customizations

- [Customization](#Customization)
    + [Disable Face AR SDK](#Disable-Face-AR-SDK)
    + [Configure export flow](#Configure-export-flow)
    + [Configure masks and filters order](#Configure-masks-and-filters-order)
    + [Configure watermark](#Configure-watermark)
    + [Configure media content](#Configure-media-content)
    + [Configure audio content](#Configure-audio-content)
    + [Configure audio browser](#Configure-audio-browser)
    + [Configure stickers content](#Configure-stickers-content)
    + [Configure the record button](#Configure-the-record-button)
    + [Configure camera timer](#Configure-camera-timer)
    + [Configure Cover preview screen](#Configure-Cover-preview-screen)
    + [Configure screens](#Configure-screens)
    + [Configure additional Video Editor SDK features](#Configure-additional-Video-Editor-SDK-features)
    + [Check Video Editor SDK availability before opening](#Check-Video-Editor-SDK-availability-before-opening)
    + [Localization](#Localization)
    + [Analytics](#Analytics)

### Disable Face AR SDK
You can use AI Video Editor SDK without Face AR SDK. Please follow these changes to make it.

Remove ```BanubaEffectPlayerKoinModule().module``` from the video editor Koin module
```diff
startKoin {
    androidContext(this@IntegrationApp)    
    modules(
        AudioBrowserKoinModule().module,
        VideoEditorKoinModule().module,
-       BanubaEffectPlayerKoinModule().module
    )
}
```
And also remove dependency ```com.banuba.sdk:effect-player-adapter``` from [app/build.gradle](app/build.gradle#L52)
```diff
    implementation "com.banuba.sdk:ve-effects-sdk:${banubaSdkVersion}"
-   implementation "com.banuba.sdk:effect-player-adapter:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ar-cloud-sdk:${banubaSdkVersion}"
```

### Configure export flow
The Video Editor SDK exports recordings as .mp4 files. There are many ways you can customize this flow to better integrate it into your app.

To change export output, start with the ```ExportParamsProvider``` interface. It contains one method - ```provideExportParams()``` that returns ```List<ExportManager.Params>```. Each item on this list relates to one of the videos in the output and their configuration. Please check out [guide](configure_export_params.md) to configure ExportParams. See the example [here](app/src/main/java/com/banuba/example/integrationapp/videoeditor/export/IntegrationAppExportParamsProvider.kt).

The end result would be four files:

- Optimized video file (resolution will be calculated automatically);
- Same file as above but without a watermark;
- Low-res version of the watermarked file.

By default, they are placed in the "export" directory of external storage. To change the target folder, you should provide a custom Uri instance named **exportDir** through DI.

Should you choose to export files in the background, you’d do well to change ```ExportNotificationManager```. It lets you change the notifications for any export scenario (started, finished successfully, and failed).

:exclamation: If you set ```shouldClearSessionOnFinish``` in ```ExportFlowManager``` to true, you should clear ```VideoCreationActivity``` from backstack. Otherwise crash will be raised.

### Configure masks and filters order
By default, the masks and filters are listed in alphabetical order.

To change it, use the implementation of the ```OrderProvider``` interface.

```kotlin
class CustomMaskOrderProvider : OrderProvider {
    override fun provide(): List<String> = listOf("Background", "HeadphoneMusic", "AsaiLines")
}
```
This will return the list of masks with the required order.
Note: The name of mask is a name of an appropriate directory located in **assets/bnb-resources/effects** directory or received from AR cloud. [Example](https://github.com/Banuba/ve-sdk-android-integration-sample/tree/main/app/src/main/assets/bnb-resources/effects/Background).

```kotlin
class CustomColorFilterOrderProvider : OrderProvider {
    override fun provide(): List<String> = listOf("egypt", "byers")
}
```
This will return the list of color filters with the required order.
Note: The name of color filter is a name of an appropriate file located in **assets/bnb-resources/luts** directory. [Example](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/bnb-resources/luts/egypt.png).

The final step is to pass your custom ```CustomMaskOrderProvider``` and ```CustomColorFilterOrderProvider``` implementation in the [DI](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-di) to override the default implementations:

```kotlin
override val maskOrderProvider: BeanDefinition<OrderProvider> =
    single(named("maskOrderProvider"), override = true) {
        CustomMaskOrderProvider()
    }

override val colorFilterOrderProvider: BeanDefinition<OrderProvider> =
    single(named("colorFilterOrderProvider"), override = true) {
        CustomColorFilterOrderProvider()
    }
```

Note: pay attention that ```OrderProvider``` should be named "maskOrderProvider" and "colorFilterOrderProvider" for masks and filters, respectively.

### Configure watermark
To use a watermark, add the ``` WatermarkProvider``` interface to your app. The image goes into the getWatermarkBitmap method. Once you’re done, rearrange the dependency watermarkProvider in [DI](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/IntegrationKoinModule.kt#L64). See the [example](app/src/main/java/com/banuba/example/integrationapp/videoeditor/impl/IntegrationAppWatermarkProvider.kt) of adding a watermark here.

### Configure media content

AI Video Editor SDK is provided with its own solution for media content (i.e. images and videos) selection - the gallery screen. To use it as a part of SDK just add a dependency into build.gradle:
```kotlin
implementation "com.banuba.sdk:ve-gallery-sdk:1.0.16"
```
and put the new koin module into `startKoin` function:
```diff
startKoin {
    androidContext(this@IntegrationApp)
        modules(
            // other Video Editor modules
+           GalleryKoinModule().module
        )
}
```
The gallery provided by the SDK is fully customizable according to [this guide](gallery_styles.md).

Also there is an option to use **your own implementation of the gallery**. This is available according to this [step-by-step guide](configure_external_gallery.md).

### Configure audio content

Banuba Video Editor SDK can trim audio tracks, merge them, and apply them to a video. **It doesn’t include music or sounds**. However, it can be integrated with [Mubert](https://mubert.com/) and get music from it (requires additional contract with them). Moreover, the users can add audio files from internal memory (downloaded library) from the phone.

Adding audio content is simple. See this [step-by-step guide](audio_content.md) guide for code examples.

### Configure audio browser

Check out [step-by-step guide](audio_browser.md) to use audio browser in your app.

### Configure stickers content

The stickers in the AI Video Editor SDK are GIFs. Adding them is as simple as adding your personal [**Giphy API**](https://developers.giphy.com/docs/api/) into the **stickersApiKey** parameter in [DI](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/IntegrationKoinModule.kt#L112).

``` kotlin
single(override = true) {
    EditorConfig(
        ...
        stickersApiKey = "<-- Paste Giphy key here to load gif images -->"
    )
}
```

GIPHY doesn't charge for their content. The one thing they do require is attribution. Also, there is no commercial aspect to the current version of the product (no advertisements, etc.) To use it, please, add **"Search GIPHY"** text attribution to the search bar.

### Configure the record button

If you want to use the **default record button** provided by the Video Editor SDK with some color, size and animation customization, follow [this guide](record_button_styles.md).

If you want to fully change the look of the button and the animation on tap, you should provide your **custom record button** implementation. This is how it’s done:

1. Create a [custom view](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/d5f0b5de02a55866a4dbd35ebb4243a36ca87585/app/src/main/java/com/banuba/example/integrationapp/videoeditor/widget/recordbutton/RecordButtonView.kt).

2. Implement ```CameraRecordingAnimationProvider``` interface. Here the view created in step 1 should be provided through method ```provideView()``` within this interface. [Example](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/d5f0b5de02a55866a4dbd35ebb4243a36ca87585/app/src/main/java/com/banuba/example/integrationapp/videoeditor/impl/IntegrationAppRecordingAnimationProvider.kt).

3. Implement ```CameraRecordingAnimationProvider``` in the [DI](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/d5f0b5de02a55866a4dbd35ebb4243a36ca87585/app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/VideoEditorKoinModule.kt#L81).

### Configure camera timer

This will allow your users to take pictures and videos after a delay. The timer is managed by the ```CameraTimerStateProvider``` interface. Every delay is represented by the TimerEntry object:

```kotlin
data class TimerEntry(
    val durationMs: Long,
    @DrawableRes val iconResId: Int
)
```
Besides the delay itself, you can customize the icon for it. See the example [here](app/src/main/java/com/banuba/example/integrationapp/videoeditor/impl/IntegrationTimerStateProvider.kt).

More advanced timer settings are available with [**Hands-Free feature**](hands_free.md).

### Configure Cover preview screen
If you want to manage Cover preview screen you need to override CoverProvider property in [DI](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/IntegrationKoinModule.kt#L86).
``` kotlin
single<CoverProvider>(override = true) {
    CoverProvider.EXTENDED
}
```
There are 3 modes:
``` kotlin
 enum class CoverProvider {
    EXTENDED,   // enable cover screen
    NONE        // disable cover screen
}
```

### Configure screens
You can use the Android themes and styles to change the screens in the mobile Video Editor SDK. You can also change the language and text.

The AI Video Editor SDK includes the following screens:
1. [Camera screen](camera_styles.md)
1. [Editor screen](editor_styles.md)
1. [Gallery screen](gallery_styles.md)
1. [Trimmer screen](trimmer_styles.md)
1. [Aspects screen](aspects_styles.md)
1. [Music Editor screen](music_editor_styles.md)
1. [Timeline Editor screen](timeline_editor_styles.md)
1. [Cover screen](cover_styles.md)
1. [Alert Dialogs](alert_styles.md)
1. [Picture in picture](pip_configuration.md)
1. [Drafts screen](drafts_styles.md)
1. [Media progress screen](media_progress_styles.md)

### Configure additional Video Editor SDK features

1. [Background separation](background_mask.md)
1. [Transition effects](transitions_styles.md)
1. [Sharing screen](sharing_screen_styles.md)

### Launch Video Editor

The Video Editor has multiple entry points. Please check out [guide](launch_modes.md).

### Check Video Editor SDK availability before opening

The SDK is protected by the token so its presence is a vital part of Video Editor launch. To check if the SDK is ready to use you may use the following property:
```kotlin
VideoEditorLicenceUtils.isSupportsVeSdk
```

Also you can check token expiration with help of:
```kotlin
EditorLicenseManager.isTokenExpired()
```
property. See [FAQ page](faq.md#how-does-video-editor-work-when-token-expires) to get more details about token expiration.

There are a few devices, that doesn't support Video Editor. To check you may use the following property:
```kotlin
VideoEditorUtils.isSupportsVideoEditor
```

### Localization

To change any particular text in the Video Editor SDK just provide your custom value for string resource provided in String resources section of [every screen](#Configure-screens) (check out an example of [string resources](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/mddocs/editor_styles.md#string-resources) on editor screen). Keep ResourceId the same and change only related value.

To localize Video Editor SDK follow an [official guilde](https://developer.android.com/guide/topics/resources/localization) and provide string resources for every locale in your app with the same ResourceId and translated values.

### Analytics

The SDK generates simple metadata analytics in JSON file that you can use in your application.
You need to make sure that analytics collection is enabled in your token.

Analytics data is provided as a JSON string and can be extracted from ```ExportResult.Success``` object (which is a normal result of successfully exported video) in the following way:
```kotlin
//"exportResult" is an instance of ExportResult.Success object
val outputBundle = exportResult.additionalExportData.getBundle(ExportBundleProvider.Keys.EXTRA_EXPORT_OUTPUT_INFO)
val analytics = outputBundle?.getString(ExportBundleProvider.Keys.EXTRA_EXPORT_ANALYTICS_DATA)
```
Use ```ExportBundleProvider.Keys``` constants to parse analytics data string.

Output example:
```JSON
{
   "video_duration":16.384,
   "video_resolutions":[
      "1280x720"
   ],
   "camera_effects":[
   ],
   "post_processing_effects":[
      "mask:2_5D_HeadphoneMusic",
      "fx:dv_cam",
      "fx:cathode",
      "fx:acid_whip",
      "time:rapid"
   ],
   "video_count":2,
   "video_sources":[
      {
         "title":"2022-04-10T12-56-11.362",
         "type":"camera",
         "duration":9795,
         "startTime":0,
         "endTime":9.795
      },
      {
         "title":"video",
         "type":"gallery",
         "duration":8366,
         "startTime":9.795,
         "endTime":18.161
      }
   ]
}
```
