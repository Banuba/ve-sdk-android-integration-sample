## Video Editor SDK customizations guide

- [Configurations](#Configurations)
- [Face AR SDK and AR Cloud](#Face-AR-SDK-and-AR-Cloud)
- [Disable Face AR SDK](#Disable-Face-AR-SDK)
- [Configure export media](integration_export_media.md)
- [Configure masks and filters order](#Configure-masks-and-filters-order)
- [Configure watermark](#Configure-watermark)
- [Configure media content](#Configure-media-content)
- [Configure audio content](#Configure-audio-content)
- [Configure audio browser](#Configure-audio-browser)
- [Configure stickers content](#Configure-stickers-content)
- [Configure the record button](#Configure-the-record-button)
- [Configure camera timer](#Configure-camera-timer)
- [Configure Cover preview screen](#Configure-Cover-preview-screen)
- [Configure screens](#Configure-screens)
- [Configure additional Video Editor SDK features](#Configure-additional-Video-Editor-SDK-features)
- [Check Video Editor SDK availability before opening](#Check-Video-Editor-SDK-availability-before-opening)
- [Localization](#Localization)
- [Analytics](#Analytics)

### Configurations
There are several classes in the Video Editor SDK that allow you to modify its parameters and behavior:
- [**CameraConfig**](config_camera.md) lets you setup camera specific parameters (min/max recording duration, flashlight, etc.).
- [**EditorConfig**](config_videoeditor.md) lets you modify editor, trimmer, and gallery screens.
- [**MusicEditorConfig**](config_music_editor.md) allows you to change the audio editor screen, e.g. the number of timelines or tracks allowed.
- [**ObjectEditorConfig**](config_object_editor.md) allows you to change text and gif editor screens, e.g. the number of timelines or effects allowed
- [**MubertApiConfig**](config_mubert_api.md) - optional config class available only in case you plugged in **audio-browser-sdk** module allows to configure music tracks network requests

If you want to customize some of these classes, provide them with just those properties you need to change. For example, to change only max recording duration on the camera screen, provide the following instance:
```kotlin
single(override = true) {
            CameraConfig(
                maxRecordedTotalVideoDurationMs = 40_000
            )
        }
```

### Face AR SDK and AR Cloud
[Banuba Face AR SDK](https://www.banuba.com/facear-sdk/face-filters) is used in Video Editor for applying AR effects in 2 use cases:
1. Camera screen  
   The user can record video content with various AR effects.
2. Editor screen  
   The user can apply various AR effects on existing video.  

Video Editor SDK has built in integration with Banuba AR Cloud - remote storage for Banuba AR effects.

Please follow [Face AR and AR Cloud integration guide](guide_far_arcloud.md) if you are interested in disabling Face AR, 
integrating AR Cloud, managing AR effects and many more. 



### Configure masks and filters order
By default, the masks and filters are listed in alphabetical order.

To change it, use the implementation of the ```OrderProvider``` interface.

```kotlin
class CustomMaskOrderProvider : OrderProvider {
    override fun provide(): List<String> = listOf("Background", "HeadphoneMusic", "AsaiLines")
}
```
This will return the list of masks with the required order.
Note: The name of mask is a name of an appropriate directory located in **assets/bnb-resources/effects** directory or received from AR cloud. [Example](../app/src/main/assets/bnb-resources/effects/Background).

```kotlin
class CustomColorFilterOrderProvider : OrderProvider {
    override fun provide(): List<String> = listOf("egypt", "byers")
}
```
This will return the list of color filters with the required order.
Note: The name of color filter is a name of an appropriate file located in **assets/bnb-resources/luts** directory. [Example](../app/src/main/assets/bnb-resources/luts/egypt.png).

The final step is to pass your custom ```CustomMaskOrderProvider``` and ```CustomColorFilterOrderProvider``` implementation in the [DI](#configure-di) to override the default implementations:

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
To use a watermark, add the ``` WatermarkProvider``` interface to your app. The image goes into the getWatermarkBitmap method. Once you’re done, rearrange the dependency watermarkProvider in [DI](app/src/main/java/com/banuba/example/integrationapp//IntegrationKoinModule.kt#L64). See the [example](app/src/main/java/com/banuba/example/integrationapp/videoeditor/impl/IntegrationAppWatermarkProvider.kt) of adding a watermark here.

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

1. [Transition effects](transitions_styles.md)
2. [Sharing screen](sharing_screen_styles.md)

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

To change any particular text in the Video Editor SDK just provide your custom value for string resource provided in String resources section of [every screen](#Configure-screens) (check out an example of [string resources](editor_styles.md#string-resources) on editor screen). Keep ResourceId the same and change only related value.

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
