# Video editing guide

- [Configurations](#Configurations)
- [Aspects](#Aspects)

## Configurations
Video editing includes 3 main classes you can use to change configurations implemented in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt)
- ```EditorConfig``` - to change editor, trimmer, and gallery screens.
- ```MusicEditorConfig``` - to change audio editor screen, e.g. the number of timelines, tracks and others.
- ```ObjectEditorConfig``` - to change text and stickers editing screens, e.g. the number of timelines, effects allowed and others.

### EditorConfig
We grouped a number of properties into related sections(editor, trimmer, gallery) for your convince.

#### Editor

| Property                       |               Values               | Description |
|--------------------------------|:----------------------------------:| :------------- |
| minTotalVideoDurationMs        |  Number > 0, Default ```1_000L```  | the minimum total video duration *in milliseconds* that is required to proceed from the Trimmer screen to Editor screen (i.e. 3000 for 3 seconds).
| maxTotalVideoDurationMs        | Number > 0, Default ```120_000L``` | the maximum total video duration *in milliseconds* that is required to proceed from the Trimmer screen to Editor screen (i.e. 60000 for 1 minute).
| maxMediaSources                | 0 < Number <= 50, Default ```50``` | the maximum amount of video sources which can be added to Trimmer screen
| editorSupportsTimeEffects      |   true/false, Default ```true```   | defines if time effects (i.e. Rapid, SlowMo) are enabled
| editorSupportsVisualEffects    |   true/false, Default ```true```   | defines if visual effects (i.e. Glitch, VHS, etc.) are enabled
| editorSupportsMusicMixer       |   true/false, Default ```true```   | defines if the Audio editor is enabled
| supportsTextOnVideo            |   true/false, Default ```true```   | defines if text effects is enabled
| textOnVideoMaxSymbols          | -1 or Number > 0, Default ```-1``` | defines the maximum symbols available for the text effect. Value "-1" means no limit.
| stickersApiKey                 |               String               | Set your GIPHY key here.
| showEditorConfig               |  true/false, Default ```false```   | Enable button "Show config" that shows debug info of `EditorConfig` properties. *Do not forget to setup false for production build*.
| supportsTrimRecordedVideo      |   true/false, Default ```true```   | defines if the trimmer screen will be opened after camera. ```false``` means editor screen will be opened after camera.
| supportsStickersOnVideo      |   true/false, Default ```true```   | defines if adding stickers to video feature is enabled
| supportsGalleryOnTrimmer      |   true/false, Default ```true```   | defines if adding media from gallery is available on trimmer screen
| supportsGalleryOnCover      |   true/false, Default ```true```   | defines if adding media from gallery is available on cover screen

#### Trimmer

| Property |              Values               | Description |
| ------------- |:---------------------------------:| :------------- |
| trimmerMinSourceVideoDurationMs |  Number > 0, Default ```300L```   | the minimum video duration *in milliseconds* that is required to be appended to the list of video sources recorded with camera or seleced from gallery.
| trimmerActionVibrationDurationMs |   Number >= 0, Default ```3L```   | defines vibration duration *in milliseconds* when drag and drop is applied on Trimmer screen. **Note**, this value should be small enougn to provide good user experience. Value "0" means no vibration.
| trimmerTimelineOneScreenDurationMs | Number > 0, Default ```10_000L``` | defines the maximum total video duration *in milliseconds* that takes the whole device's width on the Trimmer screen
| supportsTransitions |  true/false, Default ```true```   | if [transition effects](guide_effect.md#Transitions) are available

#### Slideshow
Slideshow is a small video made from image.

| Property |              Values              | Description |
| ------------- |:--------------------------------:| :------------- |
| slideShowFromGalleryAnimationEnabled |  true/false, Default ```true```  | defines if the slideshow created *from gallery images* is animated
| slideShowFromPhotoAnimationEnabled |  true/false, Default ```true```  | defines if the slideshow created *from captured camera photos* is animated
| slideShowSourceVideoDurationMs | Number > 0, Default ```3_000L``` | defines the duration in of slideshow in *in milliseconds* for 1 image. Total video duration of N images - ```N * slideShowSourceVideoDurationMs```

#### Gallery
| Property |          Available values          | Description |
| ------------- |:----------------------------------:| :------------- |
| gallerySupportsVideo |   true/false, Default ```true```   | defines if the Video tab available on the Gallery screen
| gallerySupportsImage |   true/false, Default ```true```   | defines if the Image tab available on the Gallery screen

### ObjectEditorConfig
The class allows to customize text/gif editor screen behavior.

| Property |              Values              | Description |
| ------------- |:--------------------------------:| :------------- | 
| objectMaxVisibleTimelineCount |   Number > 0, Default ```4```    | the maximum number of rows or timelines for adding effects
| objectInitialTimelineCount |   Number > 0, Default ```2```    | initial number of visible rows or timelines for adding effects
| objectEffectDefaultDuration | Number > 0, Default ```3_000L``` | default duration *in milliseconds* of playing an effect in timeline
| objectEffectMinDurationMs |  Number > 0, Default ```100L```  | the minimum required duration *in milliseconds* of applying the effect
| showObjectEffectsTogether | true/false, Default ```false```  | if stickers and text effects are managed on the single screen
| objectEditorVibrateActionDurationMs |   Number > 0, Default ```2L```   | vibration duration *in milliseconds* that is applied for object effect after drag action

### MusicEditorConfig
The class allows to customize music editor screen behavior.

| Property |        Available values        | Description |
| --------- |:------------------------------:| :------------- |
| minAudioDurationMs | Number > 0, Default ```100L``` | the minimum audio track duration *in milliseconds* that is allowed to add.
| minVoiceRecordingMs | Number > 0, Default ```100L``` | the minimum voice recording duration *in milliseconds* that is allowed to add.
| musicTimelineCount |  Number > 0, Default ```4```   | the number of rows or timelines available for adding audio tracks
| maxTracks |  Number > 0, Default ```10```  | for the maximum allowed audio tracks
| musicEditorVibrateActionDurationMs |  Number > 0, Default ```2L```  | vibration duration *in milliseconds* that is applied for audio track after changes

## Aspects

| ResourceId        |   Description |
| ------------- | :------------- |
| ic_aspect_original | Icon for `EditorAspectSettings.Original` aspect |
| ic_aspect_16_9 | Icon for `EditorAspectSettings.16_9` aspect |
| ic_aspect_9_16 | Icon for `EditorAspectSettings.9_16` aspect |
| ic_aspect_4_3 | Icon for `EditorAspectSettings.4_3` aspect |
| ic_aspect_4_5 | Icon for `EditorAspectSettings.4_5` aspect |


You can turn the feature off by overriding `AspectsProvider` implementation with just single `EditorAspectSettings` type in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt).
```kotlin
single(override = true) {
    object : AspectsProvider {

        override var availableAspects: List<AspectSettings> = listOf(
            EditorAspectSettings.`4_5`
        )

        override fun provide(): AspectsProvider.AspectsData {
            return AspectsProvider.AspectsData(
                allAspects = availableAspects,
                default = availableAspects.first()
            )
        }
    }
}
```

In this case there will not be any aspect icon on trimmer screen. All video sources will be resized to 4x5 aspect ratio by default besides the way they were added (from gallery or recorded on camera).

Use ``AspectRatioProvider`` to provide the default aspect ratio for post processing screens (also if the video editor is started from the editor screen) and for export.
By default ``9:16`` is used but you can configure any aspect you want by implementing it in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt)
```kotlin
        single<AspectRatioProvider>(override = true) {
            object : AspectRatioProvider {
                override fun provide(): AspectRatio {
                    return AspectRatio(4.0/5)
                }
            }
        }
```

:exclamation: Important  
If you want to display video without black lines provide the following ```VideoDrawParams``` in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt)
```kotlin
single(override = true) {
    VideoDrawParams(
        scaleType = VideoScaleType.CenterCrop,
        normalizedCropRect = RectF(0F, 0F, 1F, 1F)
    )
}
```
Default value is
```kotlin
single {
    VideoDrawParams(
        scaleType = VideoScaleType.CenterInside(VideoBackgroundType.Black),
        normalizedCropRect = RectF(0F, 0F, 1F, 1F)
    )
}
```

Yon can change video scaling on the editor screen while playback by providing ```PlayerScaleType``` [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt).
To ensure that the video will be fully shown - use ```CENTER_INSIDE``` (keep in mind that if device and video resolutions are different black lines will appear),
to fill the screen - use ```FIT_SCREEN_HEIGHT``` (it fills the screen only if video has aspect ratio 9:16)
```kotlin
    factory<PlayerScaleType>(named("editorVideoScaleType"), override = true) {
         PlayerScaleType.CENTER_INSIDE
}
```
<p align="center">
    <img src="screenshots/aspects3.png" alt="Screenshot" width="20%" height="auto" class="docs-screenshot"/>&nbsp;
</p>

```kotlin
    factory<PlayerScaleType>(named("editorVideoScaleType"), override = true){
         PlayerScaleType.FIT_SCREEN_HEIGHT
}
```
<p align="center">
    <img src="screenshots/aspects4.png" alt="Screenshot" width="20%" height="auto" class="docs-screenshot"/>&nbsp;
</p>
