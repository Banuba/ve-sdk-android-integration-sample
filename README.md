
[![](https://www.banuba.com/hubfs/Banuba_November2018/Images/Banuba%20SDK.png)](https://www.banuba.com/video-editor-sdk)


# Banuba AI Video Editor SDK. Integration sample for Android.
Banuba [AI Video Editor SDK](https://www.banuba.com/video-editor-sdk) allows you to quickly add short video functionality and possibly AR filters and effects into your mobile app. On this page, we will explain how to integrate it into an Android app.

<p align="center">
<img src="mddocs/gif/camera_preview.gif" alt="Screenshot" width="24%" height="auto" class="docs-screenshot"/>&nbsp;
<img src="mddocs/gif/camera_pip.gif" alt="Screenshot" width="24%" height="auto" class="docs-screenshot"/>&nbsp;
<img src="mddocs/gif/audio_browser.gif" alt="Screenshot" width="24%" height="auto" class="docs-screenshot"/>&nbsp;
<img src="mddocs/gif/editor_timeline.gif" alt="Screenshot" width="24%" height="auto" class="docs-screenshot"/>&nbsp;
</p>


- [Requirements](#Requirements)
- [Dependencies](#Dependencies)
- [Video Editor SDK size](#Video-Editor-SDK-size)
- [Starting a free trial](#Starting-a-free-trial)
- [Supported media formats](#Supported-media-formats)
- [Camera recording video quality params](#Camera-recording-video-quality-params)
- [Export video quality params](#Export-video-quality-params)
- [Free Trial](#Free-Trial)
- [Token](#Token)
- [Connecting with AR cloud](#Connecting-with-AR-cloud)
- [What can you customize?](#What-can-you-customize)
- [Getting Started](#Getting-Started)  
    + [GitHub packages](#GitHub-packages)
    + [Add dependencies](#Add-dependencies)
    + [Add Activity](#Add-Activity)
    + [Add config files](#Add-config-files)
    + [Configure DI](#Configure-DI)
    + [Configure and start Video Editor SDK in Android Java project](#Configure-and-start-Video-Editor-SDK-in-Android-Java-project)
    + [Check Video Editor SDK availability before opening](#Check-Video-Editor-SDK-availability-before-opening)
     + [Disable Face AR SDK](#Disable-Face-AR-SDK)
    + [Configure export flow](#Configure-export-flow)
    + [Configure watermark](#Configure-watermark)
    + [Configure media content](#Configure-media-content)
    + [Configure audio content](#Configure-audio-content)
    + [Configure audio browser](#Configure-audio-browser)
    + [Configure stickers content](#Configure-stickers-content)
    + [Configure the record button](#Configure-the-record-button)
    + [Configure camera timer](#Configure-camera-timer)
    + [Configure Cover preview screen](#Configure-Cover-preview-screen)
    + [Configure screens](#Configure-screens)
    + [Configure additional Video Editor SDK features](#Configure-additional-Video-Editor-SDK-features)
- [Localization](#Localization)
- [FAQ](mddocs/faq.md)
- [Third party libraries](#Third-party-libraries)

## Requirements  
This is what you need to run the AI Video Editor SDK
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
- [Banuba Face AR SDK](https://www.banuba.com/facear-sdk/face-filters). **Optional**. *Video Editor SDK disables Face AR for devices with CPU armv7l(8 cores) and armv8(working in 32bit mode)*.

[Please see all used dependencies](mddocs/all_dependencies.md)

## Video Editor SDK size

If you want to use the Video Editor SDK for a short video app like TikTok, the [Face AR module](https://www.banuba.com/facear-sdk/face-filters) would be useful for you, as it allows you to add masks and other AR effects. If you just need the video editing-related features, the AI Video Editor SDK can work on its own.

| Options | Mb      | Note |
| -------- | --------- | ----- |
| :white_check_mark: Face AR SDK  | 50.5 | AR effect sizes are not included. AR effect takes 1-3 MB in average.
| :x: Face AR SDK | 21.5  | no AR effects  |  

You can either include the filters in the app or have users download them from the [AR cloud](#Configure-AR-cloud) to decrease the app size.

## Starting a free trial

You should start with getting a trial token. It will grant you **14 days** to freely play around with the AI Video Editor SDK and test its entire functionality the way you see fit.

There is nothing complicated about it - [contact us](https://www.banuba.com/video-editor-sdk#form) or send an email to sales@banuba.com and we will send it to you. We can also send you a sample app so you can see how it works “under the hood”.

## Supported media formats
| Audio      | Video      | Images      |
| ---------- | ---------  | ----------- |
|.aac, .mp3, .wav<br>.ogg, .m4a |.mp4, .mov | .jpg, .gif, .heic, .png,<br>.nef, .cr2, .jpeg, .raf, .bmp

## Camera recording video quality params
| Recording speed | 360p(360 x 640) | 480p(480 x 854) | HD(720 x 1280) | FHD(1080 x 1920) |
| --------------- | --------------- | --------------- | -------------- | ---------------- |
| 1x(Default)     | 1200            | 2000            | 4000           | 6400             |
| 0.5x            | 900             | 1500            | 3000           | 4800             |
| 2x              | 1800            | 3000            | 6000           | 9600             |
| 3x              | 2400            | 4000            | 8000           | 12800            |  

## Export video quality params
Video Editor SDK classifies every device by its performance capabilities and uses the most suitable quality params for the exported video.

Nevertheless it is possible to customize it with `ExportParamsProvider` interface. Just put a required video quality into `ExportManager.Params.Builder` constructor. Check out an [**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/java/com/banuba/example/integrationapp/videoeditor/export/IntegrationAppExportParamsProvider.kt), where multiple video files are exported: the first and the second with the most suitable quality params (defined by `sizeProvider.provideOptimalExportVideoSize()` method) and the third with 360p quality (defined by using an Video Editor SDK constant `VideoResolution.VGA360`).

See the **default bitrate (kb/s)** for exported video (without audio) in the table below:
| 360p(360 x 640) | 480p(480 x 854) | HD(720 x 1280) | FHD(1080 x 1920) |
| --------------- | --------------- | -------------- | ---------------- |
|             1200|             2000|            4000|              6400|

## Free Trial

You should start with getting a trial token. It will grant you **14 days** to freely play around with the AI Video Editor SDK and test its entire functionality the way you see fit.

There is nothing complicated about it - [contact us](https://www.banuba.com/video-editor-sdk#form) or send an email to sales@banuba.com and we will send it to you. We can also send you a sample app so you can see how it works “under the hood”.

## Token 
We offer а free 14-days trial for you could thoroughly test and assess Video Editor SDK functionality in your app. To get access to your trial, please, get in touch with us by [filling a form](https://www.banuba.com/video-editor-sdk) on our website. Our sales managers will send you the trial token.

Video editor token should be put [here](app/src/main/res/values/strings.xml#L6).\
Also you can load token from [Firebase](https://firebase.google.com/docs/database/android/start). [Check](mddocs/token_on_firebase.md) to configure firebase

## Connecting with AR cloud

To decrease the app size, you can connect with our servers and pull AR filters from there. The effects will be downloaded whenever a user needs them. Please check out [step-by-step guide](mddocs/ar_cloud.md) to configure AR Cloud in the Video Editor SDK.


## What can you customize?
We understand that the client should have options to brand video editor to bring its own experience to the market. Therefore we provide list of options to customize:

:white_check_mark: Use your branded icons. [See details](#Configure-screens)  
:white_check_mark: Use you branded colors. [See details](#Configure-screens)  
:white_check_mark: Change text styles i.e. font, color. [See details](#Configure-screens)  
:white_check_mark: Localize and change text resources. Default locale is :us:  
:white_check_mark: Make content you want i.e. a number of video with different resolutions  and durations, an audio file. [See details](#Configure-export-flow)  
:x: Change layout  
:x: Change screen order  

:exclamation: We do custom UX/UI changes as a separate contract. Please contact our [sales@banuba.com](mailto:sales@banuba.com).


## Getting Started
### GitHub packages
GitHub packages are used to download the latest Video Editor SDK modules. You will also need them to receive new AI Video Editor SDK versions.
GitHub packages are set up for trial.


### Add dependencies
Please, specify a list of dependencies as in [app/build.gradle](app/build.gradle#L36) file to integrate AI Video Editor SDK.

### Add Activity  
To manage the main screens - camera, gallery, trimmer, editor, and export - you need to add the VideoCreationActivity to [AndroidManifest.xml](app/src/main/AndroidManifest.xml#L25). Each screen is implemented as a [Fragment](https://developer.android.com/guide/fragments).

``` xml
<activity android:name="com.banuba.sdk.ve.flow.VideoCreationActivity"
    android:screenOrientation="portrait"
    android:theme="@style/CustomIntegrationAppTheme"
    android:windowSoftInputMode="adjustResize"
    tools:replace="android:theme" />
```  

Once it’s done, you’ll be able to launch the video editor.  

Note the [CustomIntegrationAppTheme](app/src/main/res/values/themes.xml#L14) line in the code. Use this theme for changing icons, colors, and other screen elements to customize the app.

### Add config files  
There are several files in the Video Editor SDK that allow you to modify its parameters. All of them go into the Android **assets** folder.
- The [**camera config file**](mddocs/config_camera.md) lets you change the min/max duration of the video, turn the flashlight on and off, etc. 
- [music_editor.json](app/src/main/assets/music_editor.json) allows you to change the audio editor screen, e.g. the number of timelines or tracks allowed.
- [object_editor.json](app/src/main/assets/object_editor.json) contains properties that you can customize on the editor screen.
- [videoeditor.json](app/src/main/assets/videoeditor.json) lets you modify the editor, trimmer, and gallery screens. Note: please keep in mind that *minVideoDuration* and *maxVideoDuration* in this and [camera.json](app/src/main/assets/camera.json) should be the same.

### Configure DI 
You can override the behavior of the video editor in your app with DI libraries and tools (we use [Koin](https://insert-koin.io/), for example).  
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
You will need to override several properties to customize the video editor for your application. Please, take a look at the [full example](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/VideoEditorKoinModule.kt).

Once you’ve overridden the properties that you need, initialize the Koin module in your  [Application.onCreate](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/java/com/banuba/example/integrationapp/IntegrationKotlinApp.kt#L16) method.
``` kotlin
startKoin {
    androidContext(this@IntegrationApp)        
    modules(VideoEditorKoinModule().module)
}
```

### Configure and start Video Editor SDK in Android Java project
You can use Java in your Android project. In this case you can start Koin in this way
``` java
 startKoin(new GlobalContext(), koinApplication -> {
            androidContext(koinApplication, this);
            koinApplication.modules(new VideoeditorKoinModuleKotlin().getModule());
            return null;
        });
```
Please, find the [full example](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/java/com/banuba/example/integrationapp/IntegrationJavaApp.java#L22) of Java Application class.

### Check Video Editor SDK availability before opening

The SDK is protected by the token so its presence is a vital part of Video Editor launch. To check if the SDK is ready to use you may use the following property:
```kotlin
VideoEditorUtils.isAvailable
```

Also you can check token expiration with help of
```kotlin
VideoEditorUtils.isExpired
```
property. See [FAQ page](mddocs/faq.md#10-how-does-video-editor-work-when-token-expires) to get more details about token expiration.

There are a few devices, that does't support Video Editor. To check you may use the following property:
```kotlin
VideoEditorUtils.isSupportsVideoEditor
```

### Disable Face AR SDK
You can use AI Video Editor SDK without Face AR SDK. Please follow these changes to make it.
 
Remove ```BanubaEffectPlayerKoinModule().module``` from the video editor Koin module
```diff
startKoin {
    androidContext(this@IntegrationApp)    
    modules(
        AudioBrowserKoinModule().module,
        VideoEditorKoinModule().module,
-       BanubaEffectPlayerKoinModule().module
    )
}
```
And also remove dependency ```com.banuba.sdk:effect-player-adapter``` from [app/build.gradle](app/build.gradle#L52)
```diff
    implementation "com.banuba.sdk:ve-effects-sdk:${banubaSdkVersion}"
-   implementation "com.banuba.sdk:effect-player-adapter:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ar-cloud-sdk:${banubaSdkVersion}"
```

### Configure export flow  
The Video Editor SDK exports recordings as .mp4 files. There are many ways you can customize this flow to better integrate it into your app.

To change export output, start with the ```ExportParamsProvider``` interface. It contains one method - ```provideExportParams()``` that returns ```List<ExportManager.Params>```. Each item on this list relates to one of the videos in the output and their configuration. Please check out [guide](mddocs/configure_export_params.md) to configure ExportParams. See the example [here](app/src/main/java/com/banuba/example/integrationapp/videoeditor/export/IntegrationAppExportParamsProvider.kt).

The end result would be four files:  

- Optimized video file (resolution will be calculated automatically);
- Same file as above but without a watermark;
- Low-res version of the watermarked file.

By default, they are placed in the "export" directory of external storage. To change the target folder, you should provide a custom Uri instance named **exportDir** through DI.

Should you choose to export files in the background, you’d do well to change ```ExportNotificationManager```. It lets you change the notifications for any export scenario (started, finished successfully, and failed).

### Configure watermark  
To use a watermark, add the ``` WatermarkProvider``` interface to your app. The image goes into the getWatermarkBitmap method. Once you’re done, rearrange the dependency watermarkProvider in [DI](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/VideoEditorKoinModule.kt#L53). See the [example](app/src/main/java/com/banuba/example/integrationapp/videoeditor/impl/IntegrationAppWatermarkProvider.kt) of adding a watermark here.

### Configure media content

AI Video Editor SDK is provided with its own solution for media content (i.e. images and videos) selection - the gallery screen. To use it as a part of SDK just add a dependency into build.gradle:
```kotlin
implementation "com.banuba.sdk:ve-gallery-sdk:1.0.16"
```
and put the new koin module into `startKoin` function:
```diff
startKoin {
    androidContext(this@IntegrationApp)
        modules(
            // other Video Editor modules
+           GalleryKoinModule().module
        )
}
```
The gallery provided by the SDK is fully customizable according to [this guide](mddocs/gallery_styles.md). 

Also there is an option to use **your own implementation of the gallery**. This is available according to this [step-by-step guide](mddocs/configure_external_gallery.md). 

### Configure audio content  

Banuba Video Editor SDK can trim audio tracks, merge them, and apply them to a video. **It doesn’t include music or sounds**. However, it can be integrated with [Mubert](https://mubert.com/) and get music from it (requires additional contract with them). Moreover, the users can add audio files from internal memory (downloaded library) from the phone.

Adding audio content is simple. See this [step-by-step guide](mddocs/audio_content.md) guide for code examples.

### Configure audio browser

Check out [step-by-step guide](mddocs/audio_browser.md) to use audio browser in your app.

### Configure stickers content  

The stickers in the AI Video Editor SDK are GIFs. Adding them is as simple as adding your personal [**Giphy API**](https://developers.giphy.com/docs/api/) into the stickersApiKey parameter in [videoeditor.json](app/src/main/assets/videoeditor.json#L13) file.

If you don't want to use Giphy, check out [guide](mddocs/sticker_loader.md) to set up your sticker loader.

### Configure the record button  

If you want to use the **default record button** provided by the Video Editor SDK with some color, size and animation customization, follow [this guide](mddocs/record_button_styles.md).

### Configure camera timer

This will allow your users to take pictures and videos after a delay. The timer is managed by the ```CameraTimerStateProvider``` interface. Every delay is represented by the TimerEntry object: 

```kotlin
data class TimerEntry(
    val durationMs: Long,
    @DrawableRes val iconResId: Int
)
```
Besides the delay itself, you can customize the icon for it. See the example [here](app/src/main/java/com/banuba/example/integrationapp/videoeditor/impl/IntegrationTimerStateProvider.kt).

More advanced timer settings are available with [**Hands-Free feature**](mddocs/hands_free.md).

### Configure Cover preview screen
If you want to manage Cover preview screen you need to override CoverProvider property in [DI](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/VideoEditorKoinModule.kt#L83).
``` kotlin
override val coverProvider: BeanDefinition<CoverProvider> = single(override = true) {
    CoverProvider.EXTENDED
}
```
There are 3 modes:
``` kotlin
 enum class CoverProvider {
    SIMPLE,     // enable cover screen with simple UI
    EXTENDED,   // enable cover screen with extended UI
    NONE        // disable cover screen
}
```

### Configure screens
You can use the Android themes and styles to change the screens in the mobile Video Editor SDK. You can also change the language and text.

The AI Video Editor SDK incudes the following screens:
1. [Camera screen](mddocs/camera_styles.md)
1. [Editor screen](mddocs/editor_styles.md)
1. [Gallery screen](mddocs/gallery_styles.md)
1. [Trimmer screen](mddocs/trimmer_styles.md)
1. [Music Editor screen](mddocs/music_editor_styles.md)
1. [Timeline Editor screen](mddocs/timeline_editor_styles.md)
1. [Cover screen](mddocs/cover_styles.md)
1. [Alert Dialogs](mddocs/alert_styles.md)  

### Configure additional Video Editor SDK features

1. [Background separation](mddocs/background_mask.md)

### Launch Video Editor

The Video Editor has multiple entry points. Please check out [guide](mddocs/launch_modes.md).

### Localization

To change any particular text in the Video Editor SDK just provide your custom value for string resource provided in String resources section of [every screen](#Configure-screens) (check out an example of [string resources](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/mddocs/editor_styles.md#string-resources) on editor screen). Keep ResourceId the same and change only related value.

To localize Video Editor SDK follow an [official guilde](https://developer.android.com/guide/topics/resources/localization) and provide string resources for every locale in your app with the same ResourceId and translated values. 

## Third party libraries

[View](mddocs/3rd_party_licences.md) information about third party libraries

## Migration guides

[1.0.15.1](mddocs/releases/1.0.15.1.md)\
[1.0.16](mddocs/releases/1.0.16.md)\
[1.0.16.1](mddocs/releases/1.0.16.1.md)\
[1.0.16.2](mddocs/releases/1.0.16.2.md)\
[1.0.16.3](mddocs/releases/1.0.16.3.md)\
[1.0.17](mddocs/releases/1.0.17.md)\
[1.0.17.1](https://vebanuba.notion.site/1-0-17-1-5a1533ef2cfe48ec85d191f3cc7ae3a5)
