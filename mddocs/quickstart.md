# Quickstart Guide

- [Prerequisites](#Prerequisites)
- [Concepts](#Concepts)
- [Add dependencies](#Add-dependencies)
- [Update AndroidManifest](#Update-AndroidManifest)
- [Add resources](#Add-resources)
- [Add module](#Add-module)
- [Implement export](#Implement-export)
- [Launch](#Launch)
- [Advanced integration](#Advanced-integration)
- [FAQ](#FAQ)
- [Dependencies and licenses](#Dependencies-and-licenses)
- [Releases](#Releases)

## Prerequisites
:exclamation: The license token **IS REQUIRED** to use Video Editor SDK in your app.  
Please check [Installation](../README.md#Installation) out guide if the license token is not set.  
Use the license token to [start Video Editor](#Start-sdk) 

## Concepts
- Export - the process of making video file in video editor.
- Slideshow - the feature that allows to create short video from single or multiple images.
- PIP - short Picture-in-Picture feature.
- Trimmer - a screen where the user can trim, merge, change aspects of videos
- Editor -  a screen where the user can manage effects and audio. The next screen after trimmer.

## Add dependencies
GitHub Packages is used for getting Android Video Editor SDK modules.

First, add repositories to your [project gradle](../build.gradle#L21) file in ```allprojects``` section.
```groovy
...

allprojects {
    repositories {
        ...

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Banuba/banuba-ve-sdk")
            credentials {
                username = "Banuba"
                password = "\u0038\u0036\u0032\u0037\u0063\u0035\u0031\u0030\u0033\u0034\u0032\u0063\u0061\u0033\u0065\u0061\u0031\u0032\u0034\u0064\u0065\u0066\u0039\u0062\u0034\u0030\u0063\u0063\u0037\u0039\u0038\u0063\u0038\u0038\u0066\u0034\u0031\u0032\u0061\u0038"
            }
        }
        maven {
            name = "ARCloudPackages"
            url = uri("https://maven.pkg.github.com/Banuba/banuba-ar")
            credentials {
                username = "Banuba"
                password = "\u0038\u0036\u0032\u0037\u0063\u0035\u0031\u0030\u0033\u0034\u0032\u0063\u0061\u0033\u0065\u0061\u0031\u0032\u0034\u0064\u0065\u0066\u0039\u0062\u0034\u0030\u0063\u0063\u0037\u0039\u0038\u0063\u0038\u0038\u0066\u0034\u0031\u0032\u0061\u0038"
            }
        }

        ...
    }
}
```

Next, specify a list of dependencies in [app gradle](../app/build.gradle#L61) file.

```groovy
    def banubaSdkVersion = '1.33.2'

    implementation "com.banuba.sdk:ffmpeg:5.1.3"
    implementation "com.banuba.sdk:camera-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:camera-ui-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:core-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:core-ui-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-flow-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-timeline-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-ui-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-gallery-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-effects-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:effect-player-adapter:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ar-cloud:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-audio-browser-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-export-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-playback-sdk:${banubaSdkVersion}"
```

Also you need to add "kotlin-parcelize" plugin into plugin section of [app gradle](../app/build.gradle#L4) file.
```groovy
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-parcelize'
}
```

## Update AndroidManifest
Add ```VideoCreationActivity``` in [AndroidManifest.xml](../app/src/main/AndroidManifest.xml#L27) files.  
``` xml
<activity android:name="com.banuba.sdk.ve.flow.VideoCreationActivity"
    android:screenOrientation="portrait"
    android:theme="@style/CustomIntegrationAppTheme"
    android:windowSoftInputMode="adjustResize"
    tools:replace="android:theme" />
```  

```VideoCreationActivity``` is used for brining together and managing a number of Video Editor screens in a certain flow.   
Each screen is implemented as a Android [Fragment](https://developer.android.com/guide/fragments). 

Next, allow Network by adding permissions
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
```
Network is used for downloading AR effects from AR Cloud and stickers from [Giphy](https://giphy.com/).

Custom implementation of ```VideoCreationTheme``` is required for running ```VideoCreationActivity``` for customizing visual appearance of Video Editor SDK i.e. colors, icons and more.  
[See example](../app/src/main/res/values/themes.xml#L13).

## Add resources
Video Editor SDK uses a lot of resources required for running in the app.  
Please make sure all these resources exist in your project.

1. [bnb-resources](../app/src/main/assets/bnb-resources)  Banuba AR and color filters. AR effects ```assets/bnb-resources/effects``` requires [Face AR](https://docs.banuba.com/face-ar-sdk-v1) product.

2. [drawable-hdpi](../app/src/main/res/drawable-hdpi),
   [drawable-xhdpi](../app/src/main/res/drawable-xhdpi),
   [drawable-xxhdpi](../app/src/main/res/drawable-xxhdpi),
   [drawable-xxxhdpi](../app/src/main/res/drawable-xxxhdpi) are visual assets for color filter previews.

3. [themes.xml](../app/src/main/res/values/themes.xml) includes implementation of ```VideoCreationTheme``` of Video Editor SDK.

## Add module
Custom behavior of Video Editor SDK in your app is implemented by using dependency injection framework [Koin](https://insert-koin.io/).  

First, create new class ```VideoEditorModule``` for implementing Video Editor SDK features.  
Next, add new class ```SampleIntegrationKoinModule``` for initializing and customizing Video Editor SDK dependencies. 
``` kotlin
class VideoEditorModule {
   ...
   private class SampleIntegrationKoinModule {
      val module = module {
         ...
      }
   } 
}
```

Next, add method to initialize Video Editor SDK modules and add ```SampleIntegrationKoinModule``` to the list of modules.
``` kotlin
fun initialize(applicationContext: Context) {
        startKoin {
            androidContext(applicationContext)
            allowOverride(true)

            // pass the customized Koin module that implements required dependencies. Keep order of modules
            modules(
                VeSdkKoinModule().module,
                VeExportKoinModule().module,
                VePlaybackSdkKoinModule().module,
                AudioBrowserKoinModule().module, // use this module only if you bought it
                ArCloudKoinModule().module,
                VeUiSdkKoinModule().module,
                VeFlowKoinModule().module,
                GalleryKoinModule().module,
                BanubaEffectPlayerKoinModule().module,
                
                SampleIntegrationKoinModule().module,
            )
        }
    }
```

[VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L65) 
demonstrates a list of dependencies and configurations required for the launch.  

Finally, initialize ```VideoEditorModule```  in [Application class](../app/src/main/java/com/banuba/example/integrationapp/SampleApp.kt) onCreate() method.
``` kotlin
override fun onCreate() {
        super.onCreate()
        VideoEditorModule().initialize(this)
        
        ...
    }
```

## Implement export
Video Editor can export a number of media files to meet your requirements.
Implement ```ExportParamsProvider``` and provide ```List<ExportParams>``` where every ```ExportParams``` is a media file i.e. video or audio.  
Check [CustomExportParamsProvider](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L105) implementation and follow [Export integration guide](guide_export.md) to know more about exporting media content.

## Launch
Create instance of ```BanubaVideoEditor```  by using the license token
``` kotlin
val videoEditorSDK = BanubaVideoEditor.initialize(LICENSE_TOKEN)
```
```videoEditorSDK``` is ```null``` when the license token is incorrect i.e. empty, truncated.
If ```videoEditorSDK``` is not ```null``` you can proceed and start video editor.

Next, we strongly recommend checking your license state before staring video editor
```kotlin
videoEditorSDK.getLicenseState { isValid ->
   if (isValid) {
      // ✅ License is active, all good
      // Start Video Editor SDK
   } else {
      // ❌ Use of Video Editor is restricted. License is revoked or expired.
   }
}
```
:exclamation: Video content unavailable screen will appear if you start Video Editor SDK with revoked or expired license.  
<p align="center">
<img src="screenshots/screen_expired.png"  width="25%" height="auto">
</p>
   
Video Editor supports multiple launch methods described in [this guide](advanced_integration.md#Launch-methods).

The following [implementation](../app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L18) starts Video Editor from camera screen.
```kotlin
 val createVideoRequest =
    registerForActivityResult(IntegrationAppExportVideoContract()) { exportResult ->
        exportResult?.let {
            //handle ExportResult object
        }
    }

val intent = VideoCreationActivity.startFromCamera(
    context = this,
    // set PiP video configuration
    pictureInPictureConfig = null,
    // setup what kind of action you want to do with VideoCreationActivity
    // setup data that will be acceptable during export flow
    additionalExportData = null,
    // set TrackData object if you open VideoCreationActivity with preselected music track
    audioTrackData = null
)
createVideoRequest.launch(intent)
```

## Advanced integration
Video editor has built in UI/UX experience and provides a number of customizations you can use to meet your requirements.

**AVAILABLE**  
:white_check_mark: Use your branded icons  
:white_check_mark: Use you branded colors  
:white_check_mark: Change text styles i.e. font, color  
:white_check_mark: Localize and change text resources. Default locale is :us:  
:white_check_mark: Make content you want i.e. a number of video with different resolutions  and durations, an audio file. [See details](advanced_integration.md#Configure-export-flow)  
:white_check_mark: Masks and filters order. [See details](guide_far_arcloud.md#Change-effects-order)  

NOT AVAILABLE  
:x: Change layout  
:x: Change order of screens after entry point

Visit [Advanced integration guide](advanced_integration.md) to know more about features and customizations.

## FAQ
Visit [FAQ](faq.md) if you are experiencing any issues with an integration.

## Dependencies and licenses
- [Koin](https://insert-koin.io/)
- [ExoPlayer](https://github.com/google/ExoPlayer)
- [Glide](https://github.com/bumptech/glide)
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [ffmpeg](https://github.com/FFmpeg/FFmpeg/tree/n5.1.3)
- [AndroidX](https://developer.android.com/jetpack/androidx) libraries
- [Banuba Face AR SDK](https://www.banuba.com/facear-sdk/face-filters). **Optional**. *Video Editor SDK disables Face AR for devices with CPU armv7l(8 cores) and armv8(working in 32bit mode)*.

[See all dependencies and licenses](dependencies_licenses.md)

## Releases
[1.30.2](https://www.notion.so/vebanuba/1-30-2-d2e8a131674c43e8b3579d56e85bbc86)\
[1.30.1](https://www.notion.so/vebanuba/1-30-1-5a08b6f2e9dc43be8bff62667daa4b1d)\
[1.30.0](https://www.notion.so/vebanuba/1-30-0-e8a3f0f7235946d6b0a42039da8ed148)\
[1.29.3](https://www.notion.so/vebanuba/1-29-3-3902233840384206ae54b5255ea048b4)\
[1.29.2](https://www.notion.so/vebanuba/1-29-2-a1a86dda6ae94527a4922ce482dd85d6)\
[1.29.1](https://www.notion.so/vebanuba/1-29-1-78170ead1c7a4853b39d0d7a4c068b0e)\
[1.29.0](https://www.notion.so/vebanuba/1-29-0-99aed44d533b4670ab053055e0e02d01)\
[1.28.4](https://www.notion.so/vebanuba/1-28-4-0938f3a5b33d434b9346ab31198929e9)\
[1.28.0](https://vebanuba.notion.site/1-28-0-0a848e6d2f5a41cbbf4263d8347e94cc)\
[1.27.0](https://vebanuba.notion.site/1-27-0-03033a59052248e098fd60921376754b)\
[1.26.9](https://vebanuba.notion.site/1-26-9-31040cb35a454b8c8874d2df9984e659)\
[1.26.8](https://vebanuba.notion.site/1-26-8-6d339f3b737c4faab0e1bc86a001616e)\
[1.26.7](https://vebanuba.notion.site/1-26-7-e4badafaf5144200a3a19ae6dfd98a7f)\
[1.26.6](https://vebanuba.notion.site/1-26-6-2093b77cabc54ec0b28100c723ca90fe)\
[1.26.5](https://vebanuba.notion.site/1-26-5-c621ce3ef6c84d67a721ec1e9b829dde)\
[1.26.4](https://vebanuba.notion.site/1-26-4-41c28eeb378043039fa28ae42d2bbf74)\
[1.26.3](https://vebanuba.notion.site/1-26-3-113feef808d14d39abf8ccdd3181b36a)\
[1.26.2](https://vebanuba.notion.site/1-26-2-49f2bc0c102e498fa0e25e6d067aec67)\
[1.26.1](https://vebanuba.notion.site/1-26-1-10615b7836ff422bae7ba629894fd300)\
[1.26.0](https://vebanuba.notion.site/1-26-0-a13cea95a22940b7bf7ec14ab80cfbcb)

