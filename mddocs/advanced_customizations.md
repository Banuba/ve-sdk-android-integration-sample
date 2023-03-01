# Advanced customizations

- [Video recording](#Video-recording)
- [Face AR SDK and AR Cloud](#Face-AR-SDK-and-AR-Cloud)
- [Gallery](#Gallery)
- [Cover image](#Cover-image)
- [Launch methods](#Launch-methods)
- [Configurations](#Configurations)
- [Configure screens](#Configure-screens)

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

## Gallery
Video editor includes gallery screen where the user is able to pick any video and image stored on the device. 
Any video or image content is validated before using it in playback or export functionality. See [supported media formats](../README.md#Supported-media-formats).  
Visit [Gallery guide](guide_gallery.md) to get more details how to customize or replace with your own version.

## Cover image
Cover image is a frame of a video which the user can easily select on a specific video editor screen i.e cover screen.  
Visit [Cover image guide](guide_cover_image.md) to get more details how to customize or disable it.

## Launch methods
Video Editor supports multiple launch methods that are in ```VideoCreationActivity``` to meet all your requirements.

1. Launch from Camera screen where the user can record video or take a picture.
```kotlin
     fun startFromCamera(
        context: Context,
        pictureInPictureConfig: PipConfig? = null,
        additionalExportData: Parcelable? = null,
        audioTrackData: TrackData? = null
    )
  ```

Pass instance of ```PipConfig``` to start in Picture-in-Picture(PIP) mode.  
:exclamation: Important  
Video editor will not open in PIP mode if your license token does not support PIP feature.

2. Launch from Trimmer screen where the user can trim video, add transitions and move to editing screens for adding effects.
```kotlin
    fun startFromTrimmer(
            context: Context,
            predefinedVideos: Array<Uri>,
            additionalExportData: Parcelable? = null,
            audioTrackData: TrackData? = null
        )
  ```

3. Launch from Drafts screen where the user can pick any non completed draft and proceed making video.
```kotlin
     fun startFromDrafts(
            context: Context,
            predefinedDraft: Draft? = null
    )
 ```
4. Launch from Editor screen where the user can add effects to video.
```kotlin
    fun startFromEditor(
        context: Context,
        predefinedVideos: Array<Uri>,
        additionalExportData: Parcelable? = null,
        audioTrackData: TrackData? = null
    )
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
3. [Trimmer screen](trimmer_styles.md)
4. [Aspects screen](aspects_styles.md)
5. [Music Editor screen](guide_audio_content.md#Music-Editor-screen)
6. [Timeline Editor screen](timeline_editor_styles.md)
8. [Alert Dialogs](alert_styles.md)
9. [Drafts screen](drafts_styles.md)
10. [Media progress screen](media_progress_styles.md)
11. [Sharing screen](sharing_screen_styles.md)

