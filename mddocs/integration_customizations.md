## Video Editor SDK customizations guide

- [Configurations](#Configurations)
- [Face AR SDK and AR Cloud](#Face-AR-SDK-and-AR-Cloud)
- [Effects](#Effects)
- [Configure the record button](#Configure-the-record-button)
- [Configure camera timer](#Configure-camera-timer)
- [Configure Cover preview screen](#Configure-Cover-preview-screen)
- [Configure screens](#Configure-screens)
- [Localization](#Localization)

### Configurations
There are several classes in the Video Editor SDK that allow you to modify its parameters and behavior:
- [**CameraConfig**](config_camera.md) lets you setup camera specific parameters (min/max recording duration, flashlight, etc.).
- [**EditorConfig**](config_videoeditor.md) lets you modify editor, trimmer, and gallery screens.
- [**MusicEditorConfig**](config_music_editor.md) allows you to change the audio editor screen, e.g. the number of timelines or tracks allowed.
- [**ObjectEditorConfig**](config_object_editor.md) allows you to change text and gif editor screens, e.g. the number of timelines or effects allowed

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
2. [Editor screen](editor_styles.md)
3. [Gallery screen](guide_gallery.md)
4. [Trimmer screen](trimmer_styles.md)
5. [Aspects screen](aspects_styles.md)
6. [Music Editor screen](guide_audio_content.md#Music-Editor-screen)
7. [Timeline Editor screen](timeline_editor_styles.md)
8. [Cover screen](cover_styles.md)
9. [Alert Dialogs](alert_styles.md)
10. [Picture in picture](pip_configuration.md)
11. [Drafts screen](drafts_styles.md)
12. [Media progress screen](media_progress_styles.md)
13. [Sharing screen](sharing_screen_styles.md)

### Launch methods
Video Editor has multiple launch methods implemented in ```VideoCreationActivity```.

1. Start from Camera screen.
```kotlin
     fun startFromCamera(
        context: Context,
        pictureInPictureConfig: PipConfig? = null,
        additionalExportData: Parcelable? = null,
        audioTrackData: TrackData? = null
    )
  ```

Pass instance of ```PipConfig``` to  ```pictureInPictureVideo``` to start in Picture-in-Picture mode.

2. Start from Trimmer screen
```kotlin
    fun startFromTrimmer(
            context: Context,
            predefinedVideos: Array<Uri>,
            additionalExportData: Parcelable? = null,
            audioTrackData: TrackData? = null
        )
  ```

3. Start from Drafts screen
```kotlin
     fun startFromDrafts(
            context: Context,
            predefinedDraft: Draft? = null
    )
 ```
4. Start from Editor screen
```kotlin
    fun startFromEditor(
        context: Context,
        predefinedVideos: Array<Uri>,
        additionalExportData: Parcelable? = null,
        audioTrackData: TrackData? = null
    )
```

### Localization

To change any particular text in the Video Editor SDK just provide your custom value for string resource provided in String resources section of [every screen](#Configure-screens) (check out an example of [string resources](editor_styles.md#string-resources) on editor screen). Keep ResourceId the same and change only related value.

To localize Video Editor SDK follow an [official guilde](https://developer.android.com/guide/topics/resources/localization) and provide string resources for every locale in your app with the same ResourceId and translated values.
