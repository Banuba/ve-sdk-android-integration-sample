## Video Editor SDK customizations guide

- [Configurations](#Configurations)
- [Face AR SDK and AR Cloud](#Face-AR-SDK-and-AR-Cloud)
- [Effects](#Effects)
- [Configure media content](#Configure-media-content)
- [Configure audio content](#Configure-audio-content)
- [Configure audio browser](#Configure-audio-browser)
- [Configure the record button](#Configure-the-record-button)
- [Configure camera timer](#Configure-camera-timer)
- [Configure Cover preview screen](#Configure-Cover-preview-screen)
- [Configure screens](#Configure-screens)
- [Configure additional Video Editor SDK features](#Configure-additional-Video-Editor-SDK-features)
- [Localization](#Localization)

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

### Effects
Video Editor allows to apply a number of various effects to video:
1. Face AR effects
2. Color filters(LUT)
3. Visual
4. Speed
5. Stickers
6. Text
7. Blur
8. Transitions

Please follow [Video Editor effects integration guide](guide_effects.md) to get more information about applying available effects.

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

1. [Sharing screen](sharing_screen_styles.md)

### Launch Video Editor

The Video Editor has multiple entry points. Please check out [guide](launch_modes.md).

### Localization

To change any particular text in the Video Editor SDK just provide your custom value for string resource provided in String resources section of [every screen](#Configure-screens) (check out an example of [string resources](editor_styles.md#string-resources) on editor screen). Keep ResourceId the same and change only related value.

To localize Video Editor SDK follow an [official guilde](https://developer.android.com/guide/topics/resources/localization) and provide string resources for every locale in your app with the same ResourceId and translated values.
