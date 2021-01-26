# Banuba VideoEditor SDK
## Camera config

[**camera.json**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/camera.json) file allows to customize the Camera screen behavior and appearance.

To serve this purpose camera.json file has following properties:

 - **minVideoDuration** - for the minimum recording duration *in milliseconds* that is allowed to proceed with Editor screen

 - **maxVideoDuration** - for the maximum video duration *in milliseconds* available to record on the camera

 - **takePhotoOnTap** - defines if it is available to capture photos on the camera (*true* means that by tap on the record button the photo will be created, and to make a video recording you should long press the record button, *false* means that by tap on the record button the video recording starts)

  - **supportsMultiRecords** - defines if it is possible to make a video recording by concatenating several recodings on the camera (*false* means that when the first video recording is stopped the editor screen will be opened)

  - **supportsModeSelection** - defines if there are two options - Normal and Slideshow - available on the camera screen (*true* means that there are two bottom tabs on the camera switching between camera and image gallery screens, *false* means that the camera screen has not these tabs and shows only itself). **Note**, that false here removes the possibility to select pictures for the slideshow (thought it remains to be allowed by capturing photo)

 - **supportsFlashlight** - setups flashlight icon on the camera screen overlay and possibility to make photos with the flashlight

 - **supportsDurationTimeline** - setups [timeline view](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/mddocs/camera_styles.md#L22) on the camera screen (*true* means that this view is shown, *false* means that it is not shown)

  - **supportsSpeedRecording** - setups the speed icon on the camera overlay and possibility to select recording speed

  - **supportsExternalMusic** - setups the music icon on the camera overlay and possibility to add music track playing over the video recording

   - **supportsMuteMic** - setups the mute icon on the camera overlay and possibility to make video recording without capturing sound

  - **textOnMaskColorCountOnPage** - setups how much colors are available while adding text effect on AR mask (**Note**, not all AR masks allow to add text over them). *-1* means that text effects on masks are turned off

 - **switchFacingOnDoubleTap** - *true* allows to switch between front and back camera by double tapping on the camera screen

- **saveLastCameraFacing** - defines if the camera facing (back or front) will be saved for the next camera screen opening

 - **useFixedFPS30** - *true* means that Video Editor SDK may degrade video recording quality to maintain 30 FPS while applying "heavy" AR effects (*This behavior is recommended* and allows to reach seamless flow across wide range of devices). *False* means that FPS may be reduced in order to maintain video quality (not recommended).

  - **showDebugViews** - setups several text views and "camera info" button for the system information (such as current FPS, Iso etc.). It may be useful in debug mode but *do not forget to setup false for production build*

  - **showConfig** - setups the config button on the camera overlay and possibility to see camera config contents in runtime (may be useful in debug mode but *do not forget to setup false for production build*)

  - **banubaColorEffectsAssetsPath (*deprecated*)** - setups the name of the folder within assets/bnb-resources directory to store color filters 

  - **banubaMasksAssetsPath (*deprecated*)** - setups the name of the folder within assets/bnb-resources directory to store AR masks

  - **banubaMaskBeauty (*deprecated*)** - setups the name of the folder within the AR masks directory for the mask that is used for Beautification effect