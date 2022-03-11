# Banuba AI Video Editor SDK
## EditorConfig

**EditorConfig** class allows to customize editor, gallery and trimmer screens behavior and appearance.

For convenience we grouped some settings into separate sections related to different screens.
Sections and theirs properties are described below:

## **"editor"**

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **minTotalVideoDurationMs** | Number > 0 | for the minimum total video duration *in milliseconds* that is allowed to proceed from the Trimmer screen to Editor screen (i.e. 3000 for 3 seconds). 
| **maxTotalVideoDurationMs** | Number > 0 | for the maximum total video duration *in milliseconds* available to proceed from the Trimmer screen to Editor screen (i.e. 60000 for 1 minute).
| **maxMediaSources** | 0 < Number <= 50 | for the maximum amount of videos which can be added to Trimmer screen
| **editorSupportsTimeEffects** | true/false | defines if time effects (i.e. Rapid, SlowMo) are available on the Editor screen
| **editorSupportsVisualEffects** | true/false | defines if visual effects (i.e. Glitch, VHS, etc.) are available on the Editor screen
| **editorSupportsMusicMixer** | true/false | defines if the Audio editor is available on the Editor screen
| **editorBanubaColorEffectsAssetsPath** | String | setups the name of the folder *within assets/bnb-resources* directory to store color filters (i.e. "luts"). It also defines if color effects are available on the Editor screen (they are unavailable if this property is absent or empty)
| **supportsTextOnVideo** | true/false | defines if text effects are available on the Editor screen
| **textOnVideoMaxSymbols** | -1 or Number > 0 | setups the maximum symbols available for the text effect. Value "-1" means no limit. By default -1 is applied.
| **stickersApiKey** | String | containes your own Giphy key for downloading stickers from the network. It also defines if stickers are available on the Editor screen (they are unavailable if this property is absent or empty). If you are using a TRIAL license key, to enable the use of a trial Giphy key, enter **"trial"**.
| **showEditorConfig** | true/false | setups the button called "Show config" that opens a helper view with all videoeditor.json properties over the Editor screen. *Do not forget to setup false for production build*.
| **supportsTrimRecordedVideo** | true/false | defines if the camera recordings should go through the Trimmer screen before Editor screen. *False* means that the Editor screen opens right after Camera, *true* means that the Trimmer screen opens after Camera.

There is also "interactions" object in editor section which defines if certain interaction effect is available on the Editor screen. *Please take into account that interaction effects are under development and **are not production ready yet***.

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **questisupportsInteractionQuestionon** | true/false | defines if interactive question is available
| **supportsInteractionQuiz** | true/false | defines if interactive quiz is available
| **supportsInteractionInterest** | true/false | defines if interactive interest is available
| **supportsInteractionPoll** | true/false | defines if interactive poll is available

## **"trimmer"**

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **trimmerMinSourceVideoDurationMs** | Number > 0 | defines the minimum video duration *in milliseconds* allowed to be appended to list of videos recorded with camera or chosen from gallery
| **trimmerActionVibrationDurationMs** | Number >= 0 | defines vibration duration *in milliseconds* when drag and drop applied on Trimmer screen. **Note**, this value should be small enougn to provide good user experience. Value "0" means no vibration.
| **trimmerTimelineOneScreenDurationMs** | Number > 0 | defines the maximum total video duration *in milliseconds* that takes the whole device's width on the Trimmer screen

## **"slideshow"**

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **slideShowFromGalleryAnimationEnabled** | true/false | defines if the slideshow created *from gallery images* are animated
| **slideShowFromPhotoAnimationEnabled** | true/false | defines if the slideshow created *from captured camera photos* are animated
| **slideShowSourceVideoDurationMs** | Number > 0 | defines the duration of created slideshow

## **"gallery"**
| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **gallerySupportsVideo** | true/false | defines if the Video tab available on the Gallery screen
| **gallerySupportsImage** | true/false | defines if the Image tab available on the Gallery screen
