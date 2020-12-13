# Banuba AI Video Editor SDK. Integration sample for Android.
Banuba [Video Editor SDK](https://www.banuba.com/video-editor-sdk) allows you to add a fully-functional video editor with Tiktok-like features, AR filters and effects in your app. The following guide explains how you can integrate our SDK into your Android project. 

## Requirements
- Java 1.8+
- Kotlin 1.4+
- Android Studio 4+
- Android OS 6.0 or higher with Camera 2 API
- OpenGL ES 3.0 (3.1 for Neural networks on GPU)  

## Dependencies
- [Koin](https://insert-koin.io/)
- [ExoPlayer](https://github.com/google/ExoPlayer)
- [Glide](https://github.com/bumptech/glide)
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [ffmpeg](https://github.com/FFmpeg/FFmpeg/tree/n3.4.1)
- [AndroidX](https://developer.android.com/jetpack/androidx) libraries
- [Banuba Face AR SDK](https://www.banuba.com/facear-sdk/face-filters). *Optional*  

## SDK size  

If you utilize the AR technology with masks (like Tiktok or Snapchat) you would need to have [Face AR module](https://www.banuba.com/facear-sdk/face-filters), produced by Banuba. Alternatively, you may just have the app that shoots the video/pics and edit it with no AR feature. Depending on your choice, the SDK size will vary:
| Options | Mb      | Note |
| -------- | --------- | ----- |
| :white_check_mark: Face AR SDK  | 55 | AR effect sizes are not included. AR effect takes 1-3 MB in average.
| :x: Face AR SDK | 20  | no AR effects  |  

## Supported media formats  
| Audio      | Video      | Images      |
| ---------- | ---------  | ----------- |
|.aac, .mp3, .wav<br>.ogg, .ac3 |.mp4, .mov, .m4a| .jpg, .gif, .heic, .png,<br>.nef, .cr2, .jpeg, .raf, .bmp
 

## Free Trial  
We offer а free 14-days trial, so you have the opportunity to thoroughly test and assess Video Editor SDK functionality in your app. To get access to your trial, please, get in touch with us via [sales@banuba.com](mailto:sales@banuba.com?subject=Banuba%20AI%20VE%20Free%20Trial). They will send you the trial token. Put it into the app, as described below, to run the SDK.  

## Token 
We offer а free 14-days trial for you could thoroughly test and assess Video Editor SDK functionality in your app. To get access to your trial, please, get in touch with us by [filling a form](https://www.banuba.com/video-editor-sdk) on our website. Our sales managers will send you the trial token. The token should be put [here](app/src/main/res/values/strings.xml#L5). 

## What you can customize?  
We undersand that the client should have options to brand video editor to bring its own experience to the market. Therefore we provide list of options to customize:  

:white_check_mark: Use your branded icons. [See details](###-Configure-screens )  
:white_check_mark: Use you branded colors. [See details](###-Configure-screens)  
:white_check_mark: Change text styles i.e. font, color. [See details](###-Configure-screens)  
:white_check_mark: Localize and change text resources. Default locale is :us:  
:white_check_mark: Make content you want i.e. a number of video with different resolutions and durations, an audio file. [See details](###-Configure-export-flow)  
:x: Change layout  
:x: Change screen order  

:exclamation: We do custom UX/UI changes as a separate contract. Please contact our [sales@banuba.com](mailto:sales@banuba.com).


## Getting Started
### GitHub packages
GitHub packages are used to download the latest SDK modules. You will also need them to receive new SDK versions.
GitHub packages are set up for trial.  


### Add dependencies
Please, specify a list of dependencies as in [app/build.gradle](app/build.gradle#L38) file to integrate Banuba Video Editor SDK.

### Add Activity
Banuba Video Editor contains a specific flow and order of screens: camera, gallery, trimmer, editor and export. Each screen is implemented as a [Fragment](https://developer.android.com/guide/fragments). All Fragmens are handled with [Activity](https://developer.android.com/guide/components/activities/intro-activities) - VideoCreationActivity. Add VideoCreationActivity to [AndroidManifest.xml](app/src/main/AndroidManifest.xml#L21)
``` xml
<activity android:name="com.banuba.sdk.ve.flow.VideoCreationActivity"
    android:screenOrientation="portrait"
    android:theme="@style/CustomIntegrationAppTheme"
    android:windowSoftInputMode="adjustResize"
    tools:replace="android:theme" />
```
It will allow to [launch Video Editor](app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L24).  

[CustomIntegrationAppTheme](app/src/main/res/values/themes.xml#L14) theme overrides icons, colors, etc. for SDK screens. Use it to brand your app.

### Add config files  
The SDK has several configuration files which allow you to customize the video editor for your needs. All config files should be placed into Android **assets** folder:  
- [camera.json](app/src/main/assets/camera.json) contains properties that you can customize on the camera screen, i.e. the minimum and maximum video durations or turn on/off the flashlight feature.
Usually, *minVideoDuration* and *maxVideoDuration* are the most used properties.
- [music_editor.json](app/src/main/assets/music_editor.json) contains properties that you can customize on the audio editor screen, i.e. the number of timelines or tracks allowed.
- [object_editor.json](app/src/main/assets/object_editor.json) contains properties that you can customize on the editor screen.
- [videoeditor.json](app/src/main/assets/videoeditor.json) contains properties that you can customize on the editor, trimmer and gallery screens.  *Note*: please keep in mind that *minVideoDuration* and *maxVideoDuration* in this and [camera.json](app/src/main/assets/camera.json) should be the same.

### Configure DI  
The Video Editor behavior can be overridden. We use [Koin](https://insert-koin.io/) for this purpose.
First, you need to create your own implementation of FlowEditorModule.  
``` kotlin
class VideoEditorKoinModule : FlowEditorModule() {

    override val effectPlayerManager: BeanDefinition<AREffectPlayerProvider> =
        single(override = true) {
            BanubaAREffectPlayerProvider(
                mediaSizeProvider = get(),
                token = androidContext().getString(R.string.video_editor_token)
            )
        }

    ...
}
```  
You will need to override several properties to customize the video editor for your application.
Please, take a look at the [full example](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/VideoEditorKoinModule.kt).

Next, you need to initialize Koin module in your [Application.onCreate](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/java/com/banuba/example/integrationapp/IntegrationKotlinApp.kt#L12) method.  
``` kotlin
startKoin {
    androidContext(this@IntegrationApp)        
    modules(VideoEditorKoinModule().module)
}
```

### Configure and start SDK in Android Java project
You can use Java in your Android project. In this case you can start Koin in this way
``` java
 startKoin(new GlobalContext(), koinApplication -> {
            androidContext(koinApplication, this);
            koinApplication.modules(new VideoeditorKoinModuleKotlin().getModule());
            return null;
        });
```
Please, find the [full example](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/java/com/banuba/example/integrationapp/IntegrationJavaApp.java#17) of Java Application class.


### Configure export flow  
Export is the main process within video editor SDK. Its result is a compiled video file (or files) with "mp4" extension. The export flow can be customized in many directions to make it as seamless for client app as it could be.

### Configure watermark
One of the SDK features is a watermark. You can add your branded image on top of the video, which user exports.

To utilize the watermark, add ``` WatermarkProvider``` interface to your app. 
Add watermark image in the method ```getWatermarkBitmap```. Finally, re-arrange the dependency ```watermarkProvider``` in [DI](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/VideoEditorKoinModule.kt#70). Check out [this example](app/src/main/java/com/banuba/example/integrationapp/videoeditor/impl/IntegrationAppWatermarkProvider.kt) if you have any troubles.

### Configure audio content

The video editor can work with audio files to create even more attractive video recordings. The SDK does not provide audio files on its own, but it has a convenient way to set up your internal or external audio file provider for users would apply audio content.

Check out [step-by-step guide](mddocs/audio_content.md) to add your audio content into the SDK.

### Configure stickers content

Stickers are interactive objects (gif images) that can be added to the video recording to add more fun for users. 

By default [**Giphy API**](https://developers.giphy.com/docs/api/) is used to load stickers. All you need is just to pass your personal Giphy Api Key into **stickersApiKey** parameter in [videoeditor.json](app/src/main/assets/videoeditor.json) file.

### Add post-processing effects
There are several effects in Banuba Video Editor SDK: visual, time and mask. To add a visual effect you need to add a class followed by type, name and the icon of the effect. [Example](app/src/main/java/com/banuba/example/integrationapp/videoeditor/data/VisualEffects.kt).

Same for [Time effects](app/src/main/java/com/banuba/example/integrationapp/videoeditor/data/TimeEffects.kt) and [Masks](app/src/main/java/com/banuba/example/integrationapp/videoeditor/data/MaskEffects.kt).

Finally, override the dependency [editorEffects](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/VideoEditorKoinModule.kt#74) and choose the effects you wannt to use.

### Configure the record button

The record button is a main control on the camera screen which you can fully customize along with animations playing on tap. There are 3 steps to create it:

1. Create custom view for the record button. [Example](app/src/main/java/com/banuba/example/integrationapp/videoeditor/widget/recordbutton/RecordButtonView.kt).

2. Implement ```CameraRecordingAnimationProvider``` interface. Here the view created in step 1 should be provided through method ```provideView()``` within this interface. [Example](app/src/main/java/com/banuba/example/integrationapp/videoeditor/impl/IntegrationAppRecordingAnimationProvider.kt). 

3. Provide ```CameraRecordingAnimationProvider``` implementation in [DI](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/VideoEditorKoinModule.kt#140).


### Configure screens  
The SDK allows overriding icons, colors, typefaces and other things using Android theme and styles. Every SDK screen has its own set of styles.  
Below you can find how to customize each video editor step to bring your branded experience:
1. [Camera screen](mddocs/camera_styles.md)
1. [Editor screen](mddocs/editor_styles.md)
1. [Gallery screen](mddocs/gallery_styles.md)
1. [Trimmer screen](mddocs/trimmer_styles.md)
1. [Music Editor screen](mddocs/music_editor_styles.md)
1. [Timeline Editor screen](mddocs/timeline_editor_styles.md)
1. [Cover screen](mddocs/cover_styles.md)
1. [Alert Dialogs](mddocs/alert_styles.md)

### Localization

The SDK allows to add new language or customize current texts for your app. For your convenience we grouped all string resources used in the SDK according to the screen where they are utilized. To localize the SDK just override these resources in locales you need.  
Default language is :us:.


1. [Camera screen](mddocs/camera_strings.md)

