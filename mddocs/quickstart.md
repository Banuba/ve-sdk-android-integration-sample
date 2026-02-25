# Quickstart Guide

- [Installation](#installation)
- [Resources](#resources)
- [AndroidManifest.xml Updates](#AndroidManifest-updates)
- [Koin Module Setup](#Koin-Module-Setup)
- [Launch](#Launch)
- [Advanced integration](#Advanced-integration)

## Installation
Add the repository to [gradle](../build.gradle#L21) in ```allprojects``` section.
```groovy
...

allprojects {
    repositories {
       ...
       maven {
          name = "nexus"
          url = uri("https://nexus.banuba.net/repository/maven-releases")
       }
    }
}
```

Add ```packagingOptions``` to your app's [gradle](../app/build.gradle#L46-L53)
```groovy
android {
...
   packagingOptions {
      pickFirst '**/libbanuba-ve-yuv.so'

      jniLibs {
         useLegacyPackaging = true
      }
   }
...
}
```

Add dependencies to your app's [gradle](../app/build.gradle#L66)

```groovy
    def banubaSdkVersion = '1.49.5'

    implementation "com.banuba.sdk:ffmpeg:5.3.0"
    implementation "com.banuba.sdk:camera-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:camera-ui-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:core-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:core-ui-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-flow-sdk:${banubaSdkVersion}"
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

Ensure these plugins are in your app's [gradle](../app/build.gradle#L1).
```groovy
    apply plugin: 'com.android.application'
    apply plugin: 'kotlin-android'
    apply plugin: 'kotlin-parcelize'
```

## Resources
Video Editor SDK uses a lot of resources required for running in the app.  
Ensure these resources are in your project.

1. [drawable-xhdpi](../app/src/main/res/drawable-xhdpi),
   [drawable-xxhdpi](../app/src/main/res/drawable-xxhdpi),
   [drawable-xxxhdpi](../app/src/main/res/drawable-xxxhdpi) are previews for color filters.

2. [themes.xml](../app/src/main/res/values/themes.xml) includes implementation of ```VideoCreationTheme``` of Video Editor SDK.

## AndroidManifest Updates
Add the following to your [AndroidManifest.xml](../app/src/main/AndroidManifest.xml#L27):

1. ```VideoCreationActivity``` – orchestrates the video editor screens
``` xml
<activity android:name="com.banuba.sdk.ve.flow.VideoCreationActivity"
    android:screenOrientation="portrait"
    android:theme="@style/CustomIntegrationAppTheme"
    android:windowSoftInputMode="adjustResize"
    tools:replace="android:theme" />
```  
2. **Network permissions** (optional)– only required if using [Giphy](https://giphy.com/) stickers or downloading AR effects from the cloud. 
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
```

**Note:** You'll also need a custom VideoCreationTheme [example](../app/src/main/res/values/themes.xml#L13) to style the editor UI.


## Koin Module Setup
1. Create [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt) to initialize and customize the Video Editor SDK.
2. Inside it, add [SampleIntegrationKoinModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L51) with your customizations:
 
``` kotlin
class VideoEditorModule {
   ...
   private class SampleIntegrationKoinModule {
      val module = module {
         single<ArEffectsRepositoryProvider>(createdAtStart = true) {
            ArEffectsRepositoryProvider(
                arEffectsRepository = get(named("backendArEffectsRepository"))
            )
        }

        single<ContentFeatureProvider<TrackData, Fragment>>(
            named("musicTrackProvider")
        ) {
            AudioBrowserMusicProvider()
        }
        
        ...
      }
   } 
}
```
3. Include this module during SDK initialization:
``` diff
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
                
+                SampleIntegrationKoinModule().module,
            )
        }
    }
```

## Launch

Initialize ```VideoEditorModule``` in your [Application](../app/src/main/java/com/banuba/example/integrationapp/SampleApp.kt#L42) class.
``` kotlin
override fun onCreate() {
        super.onCreate()
        VideoEditorModule().initialize(this)
        
    }
```

Create SDK instance of ```BanubaVideoEditor``` with your license token.

``` kotlin
val videoEditorSDK = BanubaVideoEditor.initialize(LICENSE_TOKEN)
```

:exclamation: Important
1. Returns ```nul```l if the license token is invalid – verify your token
2. [Check license activation](../app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L104) before starting the editor.
3. Expired/revoked licenses show a "Video content unavailable" screen

<p align="center">
<img src="screenshots/screen_expired.png"  width="25%" height="auto">
</p>

This example launches from camera ([full implementation]((../app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L18))):
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

## Concepts
- Export - The process of rendering a video file from the editor
- Slideshow - Creates short videos from one or more images
- PIP (Picture-in-Picture)  - Overlays one video segment on top of another
- Trimmer - Screen for trimming, merging, and adjusting video aspect ratios
- Editor - Main editing screen (after trimmer) for applying effects and managing audio

## Advanced integration
Explore [advanced setup and customization](https://docs.banuba.com/ve-pe-sdk/docs/android/adv-integration-overview) in our documentation.



