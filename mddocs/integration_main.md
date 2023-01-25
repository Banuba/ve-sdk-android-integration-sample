## Video Editor UI SDK integration guide into Android project

- [Prerequisite](#Prerequisite)
- [Add dependencies](#Add-dependencies)
- [Update AndroidManifest](#Update-AndroidManifest)
- [Add resources](#Add-resources)
- [Add module](#Add-module)
- [Start sdk](#Start-sdk)
- [Customizations](#Customizations)
- [Troubleshooting](#Troubleshooting)
- [Dependencies and licenses](#Ddependencies-and-licenses)
- [FAQ](faq.md)
- [Releases](#Releases)

### Prerequisite
:exclamation: The license token **IS REQUIRED** to run Video Editor SDK in your app.  
Check [Installation](../README.md#Installation) guide if the license token is not set.

To initialize Banuba Video Editor SDK with the license token use 
``` kotlin
val videoEditorSDK = BanubaVideoEditor.initialize(LICENSE_TOKEN)
```
instance ```videoEditorSDK``` is ```null``` in case if license is incorrect i.e. empty, truncated.
If ```videoEditorSDK``` is not ```null``` you can proceed.

### Add dependencies
GitHub packages is used for getting Android Video Editor SDK modules.

First, add repositories to your [project gradle](../build.gradle#L21) file.
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
                password = ""
            }
        }
        maven {
            name = "ARCloudPackages"
            url = uri("https://maven.pkg.github.com/Banuba/banuba-ar")
            credentials {
                username = "Banuba"
                password = ""
            }
        }

        ...
    }
}
```

Next, specify a list of dependencies in [app gradle](../app/build.gradle#L50) file.

```groovy
    def banubaSdkVersion = '1.26.3'

    implementation "com.banuba.sdk:ffmpeg:4.4"
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
    implementation "com.banuba.sdk:banuba-token-storage-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-export-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-playback-sdk:${banubaSdkVersion}"
```

### Update AndroidManifest
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
[See example](../app/src/main/res/values/themes.xml#L21).

### Add resources
Video Editor SDK uses a lot of resources required for running.  
Please make sure all these resources are provided in your project.

1. [bnb-resources](../app/src/main/assets/bnb-resources) to use build-in Banuba AR and Lut effects. Using Banuba AR ```assets/bnb-resources/effects``` requires [Face AR product](https://docs.banuba.com/face-ar-sdk-v1). Please contact Banuba Sales managers to get more AR effects.

2. [color](../app/src/main/res/color),
   [drawable](../app/src/main/res/drawable),
   [drawable-hdpi](../app/src/main/res/drawable-hdpi),
   [drawable-ldpi](../app/src/main/res/drawable-ldpi),
   [drawable-mdpi](../app/src/main/res/drawable-mdpi),
   [drawable-xhdpi](../app/src/main/res/drawable-xhdpi),
   [drawable-xxhdpi](../app/src/main/res/drawable-xxhdpi),
   [drawable-xxxhdpi](../app/src/main/res/drawable-xxxhdpi) are visual assets used in views and added in the sample for simplicity. You can use your specific assets.

3. [values](../app/src/main/res/values) to use colors and themes. Theme ```VideoCreationTheme``` and its styles use resources in **drawable** and **color** directories.

### Add module
Custom behavior of Video Editor SDK in your app is implemented by using dependency injection framework [Koin](https://insert-koin.io/).  

First, create new class for implementing integration of Video Editor SDK. 
``` kotlin
class VideoEditorModule {
  
}
```
Next, add new class ```SampleIntegrationKoinModule``` for initializing Video Editor SDK modules and add Koin ```module``` declaration 
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
                TokenStorageKoinModule().module,
                VeUiSdkKoinModule().module,
                VeFlowKoinModule().module,
                GalleryKoinModule().module,
                BanubaEffectPlayerKoinModule().module,
                
                SampleIntegrationKoinModule().module,
            )
        }
    }
```

[VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L90) 
demonstrates a list of dependencies and configurations required for the launch.  

Finally, initialize ```VideoEditorModule```  in [Application class](../app/src/main/java/com/banuba/example/integrationapp/SampleApp.kt#L31).
``` kotlin
VideoEditorModule().initialize(this@SampleApp)
```

### Configure media export
Video Editor SDK allows to export a number of media files to meet your requirements.
Implement ```ExportParamsProvider``` and provide ```List<ExportParams>``` where every ```ExportParams``` is a media file i.e. video or audio -
[See example](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L208).

Use ```ExportParams.Builder.fileName()``` method to provide custom media file name.  

Follow [export media guide](integration_export_media.md) to know more about exporting media content.

### Start SDK
Before start Video Editor SDK we strongly recommend checking your license state.

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
:exclamation: Certain screen will appear if you start Video Editor SDK with revoked or expired license.  

   
Video Editor SDK supports multiple entry points:
1. Camera screen
2. Drafts screen
3. Trimmer screen
4. Video editing screen

For example, the following [implementation](../app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#201) starts Video Editor from camera screen. 
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
### Customizations
Video Editor SDK has built in UI/UX experience and provides list of changes you can apply to meet your requirements.

**AVAILABLE**  
:white_check_mark: Use your branded icons. [See details](integration_customizations.md#Configure-screens)  
:white_check_mark: Use you branded colors. [See details](integration_customizations.md#Configure-screens)  
:white_check_mark: Change text styles i.e. font, color. [See details](integration_customizations.md#Configure-screens)  
:white_check_mark: Localize and change text resources. Default locale is :us:  
:white_check_mark: Make content you want i.e. a number of video with different resolutions  and durations, an audio file. [See details](integration_customizations.md#Configure-export-flow)  
:white_check_mark: Masks and filters order. [See details](integration_customizations.md#Configure-masks-and-filters-order)  

NOT AVAILABLE  
:x: Change layout  
:x: Change order of screens after entry point

Please follow [customizations guide](integration_customizations.md) and [advanced guide](integration_advanced.md) to know more about features and configurations.

### Troubleshooting
[This guide](integration_troubleshooting.md) might help you in case if you are experiencing any issues with an integration so far.

### Dependencies and licenses
- [Koin](https://insert-koin.io/)
- [ExoPlayer](https://github.com/google/ExoPlayer)
- [Glide](https://github.com/bumptech/glide)
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [ffmpeg](https://github.com/FFmpeg/FFmpeg/tree/n4.4)
- [AndroidX](https://developer.android.com/jetpack/androidx) libraries
- [Banuba Face AR SDK](https://www.banuba.com/facear-sdk/face-filters). **Optional**. *Video Editor SDK disables Face AR for devices with CPU armv7l(8 cores) and armv8(working in 32bit mode)*.

[See all dependencies and licenses](dependencies_licenses.md)

## Releases
[1.24.0](https://vebanuba.notion.site/1-24-5b97696e0eae4fbca36b71ac6d8a05be)\
[1.24.1](https://vebanuba.notion.site/1-24-1-c6a58469dc5140bc95ad4cc78061a332)\
[1.24.2](https://vebanuba.notion.site/1-24-2-fffb57ad78b246af9a0903be8626967a)\
[1.25.0](https://www.notion.so/vebanuba/1-25-0-9240af0c9b694bc596d8326dd38b7c17)\
[1.25.1](https://www.notion.so/vebanuba/1-25-1-56105d73bcfb4d468a6ce6ea960ab13a)\
[1.26.0](https://www.notion.so/vebanuba/1-26-0-a13cea95a22940b7bf7ec14ab80cfbcb)\
[1.26.0.1](https://www.notion.so/vebanuba/1-26-0-1-97c4631d568e4111b2ca3982f141bbcf)\
[1.26.1](https://www.notion.so/vebanuba/1-26-1-10615b7836ff422bae7ba629894fd300)\
[1.26.2](https://www.notion.so/vebanuba/1-26-2-49f2bc0c102e498fa0e25e6d067aec67)\
[1.26.3](https://www.notion.so/vebanuba/1-26-3-113feef808d14d39abf8ccdd3181b36a)
