# Banuba AI Video Editor SDK
## VideoEditor config

[**videoeditor.json**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/videoeditor.json) сonfig file allows to customize editor, gallery, trimmer screens behavior and appearance.

There are separate sections related to different screens in videoeditor.json file.
Sections and theirs properties are described below:

## **"editor"**

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **minVideoDuration** | Number > 0 | for the minimum total video duration *in milliseconds* that is allowed to proceed from the Trimmer screen to Editor screen (i.e. 3000 for 3 seconds). 
| **maxVideoDuration** | Number > 0 | for the maximum total video duration *in milliseconds* available to proceed from the Trimmer screen to Editor screen (i.e. 60000 for 1 minute).
| **maxMediaSources** | 0 < Number <= 50 | for the maximum amount of videos which can be added to Trimmer screen
| **supportsTimeEffects** | true/false | defines if time effects (i.e. Rapid, SlowMo) are available on the Editor screen
| **supportsVisualEffects** | true/false | defines if visual effects (i.e. Glitch, VHS, etc.) are available on the Editor screen
| **supportsMusicMixer** | true/false | defines if the Audio editor is available on the Editor screen
| **banubaColorEffectsAssetsPath** | String | setups the name of the folder *within assets/bnb-resources* directory to store color filters (i.e. "luts"). It also defines if color effects are available on the Editor screen (they are unavailable if this property is absent or empty)
| **supportsTextOnVideo** | true/false | defines if text effects are available on the Editor screen
| **textOnVideoMaxSymbols** | -1 or Number > 0 | setups the maximum symbols available for the text effect. Value "-1" means no limit. By default -1 is applied.
| **supportsEditorLinkOnVideo** | true/false | defines if the Link on video effect is available on the Editor screen. **Note**, This config is mandatory, however Link on video is not supported effect and this config will soon become deprecated.
| **stickersApiKey** | String | containes your own Giphy key for downloading stickers from the network. It also defines if stickers are available on the Editor screen (they are unavailable if this property is absent or empty)
| **showConfig** | true/false | setups the button called "Show config" that opens a helper view with all videoeditor.json properties over the Editor screen. *Do not forget to setup false for production build*.
| **supportsTrimRecordedVideo** | true/false | defines if the camera recordings should go through the Trimmer screen before Editor screen. *False* means that the Editor screen opens right after Camera, *true* means that the Trimmer screen opens after Camera.

There is also "interactions" object in editor section which defines if certain interaction effect is available on the Editor screen. *Please take into account that interaction effects are under development and are not production ready yet*.

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **question** | true/false | defines if interactive question is available
| **quiz** | true/false | defines if interactive quiz is available
| **interest** | true/false | defines if interactive interest is available
| **poll** | true/false | defines if interactive poll is available
| **linkOnVideo** | true/false | defines if interactive link is available

## **"trimmer"**

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **minSourceVideoDuration** | Number > 0 | defines the minimum video duration *in milliseconds* allowed to be appended to list of videos recorded with camera or chosen from gallery
| **actionVibrationDurationMs** | Number >= 0 | defines vibration duration *in milliseconds* when drag and drop applied on Trimmer screen. **Note**, this value should be small enougn to provide good user experience. Value "0" means no vibration.
| **oneScreenDurationMs** | Number > 0 | defines the maximum total video duration *in milliseconds* that takes the whole device's width on the Trimmer screen

## **"slideshow"**

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **animate** | true/false | defines if the slideshow created *from gallery images* are animated
| **animateTakenPhotos** | true/false | defines if the slideshow created *from captured camera photos* are animated
| **slideShowSourceDurationMs** | Number > 0 | defines the duration of created slideshow

## **"gallery"**
| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **supportsVideo** | true/false | defines if the Video tab available on the Gallery screen
| **supportsImage** | true/false | defines if the Image tab available on the Gallery screen