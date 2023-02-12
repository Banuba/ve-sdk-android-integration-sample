# Video Editor advanced customizations

- [Video recording](#Video-recording)
- [Face AR SDK and AR Cloud](#Face-AR-SDK-and-AR-Cloud)
- [Effects](#Effects)
- [Configure Cover preview screen](#Configure-Cover-preview-screen)
- [Configurations](#Configurations)
- [Configure screens](#Configure-screens)
- [Video Editor SDK size](#Video-Editor-SDK-size)
- [Supported media formats](#Supported-media-formats)
- [Localization](#Localization)

## Video recording
Video editor supports functionality allowing to record video using Android camera. There are many features, configurations and styles 
that will help you to record video easily in an excellent quality.  
Please follow [video recording integration guide](guide_video_recording.md) to know more about available features.

## Face AR SDK and AR Cloud
[Banuba Face AR SDK](https://www.banuba.com/facear-sdk/face-filters) is used in Video Editor for applying AR effects in 2 use cases:
1. Camera screen  
   The user can record video content with various AR effects.
2. Editor screen  
   The user can apply various AR effects on existing video.  

Video Editor SDK has built in integration with Banuba AR Cloud - remote storage for Banuba AR effects.

Please follow [Face AR and AR Cloud integration guide](guide_far_arcloud.md) if you are interested in disabling Face AR, 
integrating AR Cloud, managing AR effects and many more. 


## Configure Cover preview screen
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

## Configurations
There are several classes in the Video Editor SDK that allow you to modify its parameters and behavior:
- [**CameraConfig**](config_camera.md) lets you setup camera specific parameters (min/max recording duration, flashlight, etc.).
- [**EditorConfig**](config_videoeditor.md) lets you modify editor, trimmer, and gallery screens.
- [**MusicEditorConfig**](config_music_editor.md) allows you to change the audio editor screen, e.g. the number of timelines or tracks allowed.
- [**ObjectEditorConfig**](config_object_editor.md) allows you to change text and gif editor screens, e.g. the number of timelines or effects allowed

## Configure screens
You can use the Android themes and styles to change the screens in the mobile Video Editor SDK. You can also change the language and text.

The AI Video Editor SDK includes the following screens:
1. [Editor screen](editor_styles.md)
2. [Gallery screen](guide_gallery.md)
3. [Trimmer screen](trimmer_styles.md)
4. [Aspects screen](aspects_styles.md)
5. [Music Editor screen](guide_audio_content.md#Music-Editor-screen)
6. [Timeline Editor screen](timeline_editor_styles.md)
7. [Cover screen](cover_styles.md)
8. [Alert Dialogs](alert_styles.md)
9. [Drafts screen](drafts_styles.md)
10. [Media progress screen](media_progress_styles.md)
11. [Sharing screen](sharing_screen_styles.md)

## Launch methods
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

## Video Editor SDK size

| Options | Mb      | Note |
| -------- | --------- | ----- |
| :white_check_mark: Face AR SDK  | 37.3 | AR effect sizes are not included. AR effect takes 1-3 MB in average.
| :x: Face AR SDK | 15.5  | no AR effects  |  

You can either include the AR filters in the app or have users download them from the [AR cloud](#Configure-AR-cloud) to dramatically decrease the app size.

## Supported media formats
| Audio                                  | Video      | Images      |
|----------------------------------------| ---------  | ----------- |
| .aac, .mp3, .wav,<br>.ogg, .m4a, .flac |.mp4, .mov | .jpg, .gif, .heic, .png,<br>.nef, .cr2, .jpeg, .raf, .bmp
 

### Localization

To change any particular text in the Video Editor SDK just provide your custom value for string resource provided in String resources section of [every screen](#Configure-screens) (check out an example of [string resources](editor_styles.md#string-resources) on editor screen). Keep ResourceId the same and change only related value.

To localize Video Editor SDK follow an [official guide](https://developer.android.com/guide/topics/resources/localization) and provide string resources for every locale in your app with the same ResourceId and translated values.
