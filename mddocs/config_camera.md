# Banuba AI Video Editor SDK
## CameraConfig

**CameraConfig** class allows to customize the Camera screen behavior and appearance.

To serve this purpose CameraConfig class has following properties:

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **minRecordedTotalVideoDurationMs** | Number > 0 | for the minimum recording duration *in milliseconds* that is allowed to proceed with Editor screen (i.e. 3000 for 3 seconds)
| **maxRecordedTotalVideoDurationMs** | Number > 0 | for the maximum video duration *in milliseconds* available to record on the camera (i.e. 60000 for 1 minute)
| **minRecordedChunkVideoDurationMs** | Number > 1000 | for the minimum video duration *in milliseconds* that is allowed to record one video on the camera (i.e. 1000 for 1 second)
| **takePhotoOnTap** | true/false | defines if it is available to capture photos on the camera (*true* means that by tap on the record button the photo will be created, and to make a video recording you should long press the record button, *false* means that by tap on the record button the video recording starts)
| **supportsMultiRecords** | true/false | defines if it is possible to make a video recording by concatenating several recodings on the camera (*false* means that when the first video recording is stopped the editor screen will be opened)
| **supportsFlashlight** | true/false | setups flashlight icon on the camera screen overlay and possibility to make photos with the flashlight
| **supportsSpeedRecording** | true/false | setups the speed icon on the camera overlay and possibility to select recording speed
| **supportsExternalMusic** | true/false | setups the music icon on the camera overlay and possibility to add music track playing over the video recording
| **supportsMuteMic** | true/false | setups the mute icon on the camera overlay and possibility to make video recording without capturing sound
| **textOnMaskColorCountOnPage** | -1 or Number > 0 | setups how much colors are available while adding text effect on AR mask (**Note**, not all AR masks allow to add text over them). *-1* means that text effects on masks are turned off
| **switchFacingOnDoubleTap** | true/false | *true* allows to switch between front and back camera by double tapping on the camera screen
| **isStartFrontFacingFirst** | true/false | *true* means that on the first launch of the camera screen the *front* camera is turned on, *false* means that on the first launch of the camera screen the *back* camera is turned on
| **isSaveLastCameraFacing** | true/false | defines if the camera facing (back or front) will be saved for the next camera screen opening
| **cameraFpsMode** | CameraFpsMode enum values | *CameraFpsMode.FIXED* means that Video Editor SDK may degrade video recording quality to maintain 30 FPS while applying "heavy" AR effects (*This behavior is recommended* and allows to reach seamless flow across wide range of devices). *CameraFpsMode.ADAPTIVE* means that FPS may be reduced in order to maintain video quality (not recommended).
| **showCameraInfoAndPerformance** | true/false | setups several text views and "camera info" button for the system information (such as current FPS, Iso etc.). It may be useful in debug mode but *do not forget to setup false for production build*
| **banubaColorEffectsAssetsPath** | String | setups the name of the folder *within assets/bnb-resources* directory to store color filters (i.e. "filters")
| **banubaMasksAssetsPath** | String | setups the name of the folder *within assets/bnb-resources* directory to store AR masks (i.e. "masks")
| **banubaMaskBeauty** | String | setups the name of the folder *within the AR masks directory* for the mask that is used for Beautification effect (i.e. "Beauty")
| **supportsSwitchFacing** | true/false | defines if switching camera facing is available.
| **supportsAudioRateEqualsVideoSpeed** | true/false | determines if the audio playback speed is equal to the video recording speed.
| **supportsGallery** | true/false | defines if there is a gallery icon on the camera screen.
