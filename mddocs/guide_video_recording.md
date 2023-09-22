# Video recording integration guide

- [Quality details](#Quality-details)
- [Customize configurations](#Customize-configurations)
- [Configure microphone state](#Configure-microphone-state)
- [Configure recording modes](#Configure-recording-modes)
- [Configure timer](#Configure-timer)
- [Configure hands free](#Configure-hands-free)
- [Picture in picture](#Picture-in-picture)

## Quality details
Subsequent table describes video quality details used for video recording in various resolutions.  

| Recording speed | 360p(360 x 640) | 480p(480 x 854) | HD(720 x 1280) | FHD(1080 x 1920) |
| --------------- | --------------- | --------------- | -------------- | ---------------- |
| 1x(Default)     | 1200            | 2000            | 4000           | 6400             |
| 0.5x            | 900             | 1500            | 3000           | 4800             |
| 2x              | 1800            | 3000            | 6000           | 9600             |
| 3x              | 2400            | 4000            | 8000           | 12800            |

## Customize configurations
```CameraConfig``` is a main class used to customize features, behavior and user experience for video recording on camera screen i.e. set min/max recording duration, flashlight, etc.  
Video editor includes default implementation but you can provide your own implementation to meet your requirements in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt).  
For example, to sets max video recording duration to 30 seconds.
```kotlin
single(override = true) {
  CameraConfig(maxRecordedTotalVideoDurationMs = 30_000)
}
```

| Property |                                            Values                                            | Description |
| ------------- |:--------------------------------------------------------------------------------------------:| :------------- |
| minRecordedTotalVideoDurationMs |                                Number > 0; Default ```3000```                                | minimum video recording duration *in milliseconds* required to proceed and open video editing screen (i.e. 3000 for 3 seconds)
| maxRecordedTotalVideoDurationMs |                               Number > 0; Default ```120000```                               | maximum video recording duration *in milliseconds* available to record
| minRecordedChunkVideoDurationMs |                              Number > 1000; Default ```1000```                               | minimum video recording duration *in milliseconds* that is allowed to record on camera
| takePhotoOnTap |                               true/false; Default ```false```                                | defines if it is available to take a photo on the camera screen by tap. ```true``` photo is taken by tap and video is recording by long press.
| supportsMultiRecords |                                true/false; Default ```true```                                | defines if the use can record multiple video subsequently. ```false```  when the first video recording is done the editing screen will be opened
| supportsFlashlight |                                true/false; Default ```true```                                | enables flashlight icon on the camera screen and possibility to take a photo with flashlight
| supportsSpeedRecording |                                true/false; Default ```true```                                | enables speed recording icon on the camera screen and possibility to select recording speed
| supportsExternalMusic |                                true/false; Default ```true```                                | enables the music icon on the camera screen and possibility to add music track playing over the video recording
| supportsMuteMic |                                true/false; Default ```true```                                | enables mute microphone icon on the camera screen and possibility to record video without capturing sound
| switchFacingOnDoubleTap |                                true/false; Default ```true```                                | ```true``` allows to switch between front and back camera by double tap
| isStartFrontFacingFirst |                                true/false; Default ```true```                                | ```true``` means that ```front``` camera facing is used on the first launch of the camera screen, ```false``` means that ```back``` camera facing is used on the first launch of the camera screen
| isSaveLastCameraFacing |                                true/false; Default ```true```                                | defines if the camera facing (```back``` or ```front```) is saved and restored
| cameraFpsMode |                 CameraFpsMode enum values; Default ```CameraFpsMode.FIXED```                 | ```CameraFpsMode.FIXED``` means that video recording quality can be degraded to maintain 30 FPS while applying "heavy" Face AR effects (*This behavior is recommended* and allows to reach seamless usage on wide range of devices). ```CameraFpsMode.ADAPTIVE``` means that FPS can be reduced in order to maintain video quality(not recommended).
| showCameraInfoAndPerformance |                               true/false; Default ```false```                                | enables debug views for showing camera system details such as current FPS, Iso etc.
| supportsSwitchFacing |                                true/false; Default ```true```                                | defines if camera facing switching is available.
| supportsAudioRateEqualsVideoSpeed |                               true/false; Default ```false```                                | determines if the audio playback speed is equal to the video recording speed.
| supportsGallery |                                true/false; Default ```true```                                | defines if there is an icon on the camera screen at the bottom-right to pick a content from gallery.
| videoDurations | List<Long>; Default ```listOf(maxRecordedTotalVideoDurationMs, 60_000L, 30_000L, 15_000L)``` | defines the list of durations available to record. The user can see the option on the camera screen and pick new option. For example,  ```60000L``` means that the user can record a number of video with total duration no more than 60 seconds.
| supportsVideoDurationSwitcher | true/false; Default ```true```  | defines if video recording time interval swithced is enabled


## Configure microphone state
Use ```CameraMuteMicConfig``` if you want to customize default state of microphone on the camera screen.
Here is default implementation in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt)
```kotlin
factory {
    CameraMuteMicConfig(
        // If mic should be muted when open camera screen in normal mode
        muteInNormalMode = false,
        // If mic should be muted when open camera screen in picture in picture mode
        muteInPipMode = true,
        // If mic should be muted when open camera screen with passed audio track
        muteWithAudioTrack = true 
    )
}
```

## Configure recording modes
Camera screen includes 3 modes for recording content implemented as ```RecordMode```
- ```Photo```
- ```Video```
- ```Photo``` and ```Video```;**Default**

Implement ```CameraRecordingModesProvider``` in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt) to customize mode that meets your requirements.
Default implementation is
```kotlin
single<CameraRecordingModesProvider> {
    object : CameraRecordingModesProvider {
        override var availableModes = setOf(RecordMode.Video, RecordMode.Photo)
    }
}
```
:exclamation: Important     
```availableModes``` must not be empty, otherwise a crash will happen.

## Configure timer
This feature allows to take a picture or record a video after some delay.  
Implement ```CameraTimerStateProvider``` to customize timer feature in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L291) where every delay is a ```TimerEntry```.
Subsequent example shows how to add timer with 2 options - 0 seconds and 3 seconds.
```kotlin
class CustomCameraTimerStateProvider : CameraTimerStateProvider {

  override val timerStates = listOf(
    TimerEntry(
      durationMs = 0
    ),
    TimerEntry(
      durationMs = 3000
    )
  )
}
```

Video Editor includes more advanced timer feature which is called [Hands Free](#Configure-hands-free).

## Configure hands free
Hands Free is an advanced timer feature that allows to set up delay before starting of video recording and desired duration for video recording. The feature is enabled by default.  
The feature uses ```CameraTimerStateProvider``` implementation to provide list of delays, and ```CameraTimerAnimationProvider``` for countdown animation.
Every ```TimerEntry``` instance of ```CameraTimerStateProvider``` is passed as an argument into ```animate()``` method of ```CameraTimerAnimationProvider```.  

Video Editor includes ```DefaultCameraTimerAnimationProvider``` with 10 seconds timer animation. If you want either more than 10 seconds timer or another animation you can implement ```CameraTimerAnimationProvider```.  

To disable hands free feature you should remove `HandsFreeTimerActionProvider` implementation in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L137).
```diff
-    single<CameraTimerActionProvider> {
-       HandsFreeTimerActionProvider()
-    }
```
```B2BTimerStateProvider``` implementation with of 3, 5, and 10 seconds will be used once you disable hands free feature.

Available string resources to customize.

| ResourceId        |      Value      |   Description |
| ------------- | :----------- | :------------- |
| hands_free_timeline_hint | Drag to set video duration: | text of the timeline hint
| hands_free_record_button_text | start recording | label of the recording button


## Picture in picture
Picture in Picture or ```PIP``` is video editing technique that lets you overlay two videos in the same video.
The multi-layer editing effect is perfect for reaction videos, slideshows, product demos, and more. This feature is similar to TikTok duet feature.  

<p align="center">
<img src="gif/camera_pip.gif"  width="33%" height="auto">
</p>

:exclamation: Important
The feature is disabled by default and can be enabled if the license supports it. Please ask Banuba business representatives to include the feature in your license.  

The subsequent guide explains how to start and customize ```PIP```.  

First, pass ```pictureInPictureConfig``` in [VideoCreationActivity.startFromCamera](../app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L31) method

```kotlin
val localVideoUri = ...
VideoCreationActivity.startFromCamera(
    context = this,
    // set PiP video configuration
    pictureInPictureConfig = PipConfig(
        video = localVideoUri,
        openPipSettings = false // if you want to open pip settings at startup
    )
)
```

```PIP``` includes 4 modes that you can use.
- ```Floating```
- ```TopBottom```
- ```React```
- ```LeftRight```

Implement ```PipLayoutProvider``` in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt) to customize the order of modes and other capabilities. 
```kotlin
single<PipLayoutProvider> {
    object : PipLayoutProvider {
        override fun provide(
            insetsOffset: Int,
            screenSize: Size
        ): List<EditorPipLayoutSettings> {
            val context = androidContext()
            return listOf(
                EditorPipLayoutSettings.Floating(
                    context = context,
                    physicalScreenSize = screenSize,
                    topOffsetPx = context.dimen(R.dimen.pip_floating_top_offset) + insetsOffset
                ),
                EditorPipLayoutSettings.TopBottom(),
                EditorPipLayoutSettings.React(
                    context = context,
                    physicalScreenSize = screenSize,
                    topOffsetPx = context.dimen(R.dimen.pip_react_top_offset) + insetsOffset
                ),
                EditorPipLayoutSettings.LeftRight()
            )
        }
    }
}
```
Please do not forget to update [CameraMuteMicConfig](#Configure-microphone-state) implementation if you want to change use of microphone in ```PIP```.  

You can even customize camera align for each mode and exclude actions for some modes:
```diff
...
    return listOf(
        EditorPipLayoutSettings.Floating(
            ...,
            excludeActions = listOf(
                EditorPipLayoutAction.SwitchVertical,
                EditorPipLayoutAction.Square,
                EditorPipLayoutAction.Round
            ),
+            isCameraAlignTop = false
        ),
        EditorPipLayoutSettings.TopBottom(
            excludeActions = listOf(
                EditorPipLayoutAction.SwitchVertical,
                EditorPipLayoutAction.Original,
                EditorPipLayoutAction.Centered
            ),
+            isCameraAlignTop = false
        ),
        EditorPipLayoutSettings.React(
            ...,
            excludeActions = listOf(
                EditorPipLayoutAction.SwitchVertical,
                EditorPipLayoutAction.Square,
                EditorPipLayoutAction.Round,
                EditorPipLayoutAction.Centered,
                EditorPipLayoutAction.Original
            ),
            isCameraMain = false
        ),
        EditorPipLayoutSettings.LeftRight(
            excludeActions = listOf(
                EditorPipLayoutAction.SwitchHorizontal,
                EditorPipLayoutAction.Original,
                EditorPipLayoutAction.Centered
            ),
+            isCameraAlignLeft = false
        )
    )
```
