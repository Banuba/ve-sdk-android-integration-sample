# Advanced integration

This guide is aimed to help you complete advanced integration of Video Editor SDK.

- [Face AR SDK and AR Cloud](#Face-AR-SDK-and-AR-Cloud)
- [Video recording](#Video-recording)
- [Gallery](#Gallery)
- [Video editing](#Video-editing)
- [Add effects](#Add-effects)
- [Add audio content](#Add-audio-content)
- [Cover image](#Cover-image)
- [Drafts](#Drafts)
- [Launch methods](#Launch-methods)

## Face AR SDK and AR Cloud
[Banuba Face AR SDK](https://www.banuba.com/facear-sdk/face-filters) is used in Video Editor for applying AR effects in 2 use cases:
1. Camera screen  
   The user can record video content with various AR effects.
2. Editor screen  
   The user can apply various AR effects on existing video.

Video Editor SDK has built in integration with Banuba AR Cloud - remote storage for Banuba AR effects.

Please follow [Face AR and AR Cloud integration guide](guide_far_arcloud.md) if you are interested in disabling Face AR,
integrating AR Cloud, managing AR effects and many more.

## Video recording
Video editor supports functionality allowing to record video using Android camera. There are many features, configurations and styles 
that will help you to record video easily in an excellent quality.  
Please follow [video recording integration guide](guide_video_recording.md) to know more about available features.

## Gallery
Video editor includes gallery screen where the user is able to pick any video and image stored on the device. 
Any video or image content is validated before using it in playback or export functionality. See [supported media formats](../README.md#Supported-media-formats).  
Visit [Gallery guide](guide_gallery.md) to get more details how to customize or replace with your own version.

## Video editing
Any video recorded or picked in gallery can be edited in video editor. Our SDK allows rich set of functionalities to 
edit video - add various effects, trim video, add transitions and much more.
Visit [Video editing guide](guide_video_editing.md) to know more details.

## Add effects
Video Editor allows to apply a number of various effects to video:
1. Face AR effects
2. Color filters(LUT)
3. Visual
4. Speed
5. Stickers(GIPHY)
6. Text
7. Blur
8. Transitions

Please follow [Video Editor effects integration guide](guide_effects.md) to get more information about applying available effects.

## Add audio content
Video Editor has built in support and API for browsing, playing and applying audio while making video content on various screens.  
Follow [Video Editor audio content integration guide](guide_audio_content.md) to know more details about using audio and API in Video Editor.

## Cover image
Cover image is a frame of a video which the user can easily select on a specific video editor screen i.e cover screen.  
Visit [Cover image guide](guide_cover_image.md) to get more details how to customize or disable it.

## Drafts
The feature that allows your users to save and proceed editing later. Video editor includes built in screen for managing drafts.  
Visit [Drafts guide](guide_drafts.md) to know more details.

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


