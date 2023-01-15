## Banuba Video Editor SDK on Android

## Integration 
- [Getting started](#Getting-started)
    + [Step 1: GitHub packages](#Step-1-GitHub-packages)
    + [Step 2: Add dependencies](#Step-2-Add-dependencies)
    + [Step 3: Add Activity](#Step-3-Add-Activity)
    + [Step 4: Configure DI](#Step-4-Configure-DI)
    + [Step 5: Override config classes (Optional)](#Step-5-Override-config-classes-Optional)
    + [Step 6: Launch Video Editor](#Step-6-Launch-Video-Editor)
    + [FFmpeg build issue (**Error compressed Native Libs**)](#FFmpeg-build-issue-error-compressed-native-libs)
- [Dependencies](#Dependencies)
- [Customizations](#Customizations)
- [FAQ](faq.md)
- [Releases](#Releases)
- [Third party libraries](#Third-party-libraries)

## Dependencies
- [Koin](https://insert-koin.io/)
- [ExoPlayer](https://github.com/google/ExoPlayer)
- [Glide](https://github.com/bumptech/glide)
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [ffmpeg](https://github.com/FFmpeg/FFmpeg/tree/n3.4.1)
- [AndroidX](https://developer.android.com/jetpack/androidx) libraries
- [Banuba Face AR SDK](https://www.banuba.com/facear-sdk/face-filters). **Optional**. *Video Editor SDK disables Face AR for devices with CPU armv7l(8 cores) and armv8(working in 32bit mode)*.

[Please see all used dependencies](all_dependencies.md)

## Getting started 
:exclamation: __The token **IS REQUIRED** to run sample and an integration in your app.__

### Step 1: GitHub packages
GitHub packages are used to download the latest Video Editor SDK modules. You will also need them to receive new AI Video Editor SDK versions.
GitHub packages are set up for trial.

**Note**: pay attention that for getting access and downloading the Video Editor SDK modules you need to use the credentials(**banubaRepoUser** and **banubaRepoPassword**), see the [build.gradle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/build.gradle#L20) for more details.

```groovy
...

allprojects {
    repositories {
        ...

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Banuba/banuba-ve-sdk")
            credentials {
                username = banubaRepoUser
                password = banubaRepoPassword
            }
        }
        maven {
            name = "ARCloudPackages"
            url = uri("https://maven.pkg.github.com/Banuba/banuba-ar")
            credentials {
                username = banubaRepoUser
                password = banubaRepoPassword
            }
        }

        ...
    }
}
```

### Step 2: Add dependencies
Please, specify a list of dependencies as in [app/build.gradle](app/build.gradle#L36) file to integrate AI Video Editor SDK.

```groovy
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

### Step 3: Add Activity
To manage the main screens - camera, gallery, trimmer, editor, and export - you need to add the VideoCreationActivity to [AndroidManifest.xml](app/src/main/AndroidManifest.xml#L25). Each screen is implemented as a [Fragment](https://developer.android.com/guide/fragments).

``` xml
<activity android:name="com.banuba.sdk.ve.flow.VideoCreationActivity"
    android:screenOrientation="portrait"
    android:theme="@style/CustomIntegrationAppTheme"
    android:windowSoftInputMode="adjustResize"
    tools:replace="android:theme" />
```  

Once it’s done, you’ll be able to launch the video editor.

Note the [CustomIntegrationAppTheme](app/src/main/res/values/themes.xml#L21) line in the code. Use this theme for changing icons, colors, and other screen elements to customize the app.

### Step 4: Configure DI
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
You will need to override several properties to customize the video editor for your application. Please, take a look at the [full example](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/IntegrationKoinModule.kt).

Once you’ve overridden the properties that you need, initialize the Koin module in your [Application.onCreate](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/java/com/banuba/example/integrationapp/IntegrationKotlinApp.kt#L16) method.
``` kotlin
startKoin {
    androidContext(this@IntegrationApp)        
    modules(VideoEditorKoinModule().module)
}
```

You can use Java in your Android project. In this case you can start Koin in this way:
``` java
 startKoin(new GlobalContext(), koinApplication -> {
            androidContext(koinApplication, this);
            koinApplication.modules(new VideoeditorKoinModuleKotlin().getModule());
            return null;
        });
```
Please, find the [full example](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/java/com/banuba/example/integrationapp/IntegrationJavaApp.java#L22) of Java Application class.

### Step 5: Override config classes (Optional)
There are several classes in the Video Editor SDK that allow you to modify its parameters and behavior:
- [**CameraConfig**](config_camera.md) lets you setup camera specific parameters (min/max recording duration, flashlight, etc.).
- [**EditorConfig**](config_videoeditor.md) lets you modify editor, trimmer, and gallery screens.
- [**MusicEditorConfig**](config_music_editor.md) allows you to change the audio editor screen, e.g. the number of timelines or tracks allowed.
- [**ObjectEditorConfig**](config_object_editor.md) allows you to change text and gif editor screens, e.g. the number of timelines or effects allowed
- [**MubertApiConfig**](config_mubert_api.md) - optional config class available only in case you plugged in **audio-browser-sdk** module allows to configure music tracks network requests

If you want to customize some of these classes, provide them with just those properties you need to change. For example, to change only max recording duration on the camera screen, provide the following instance:
```kotlin
single(override = true) {
            CameraConfig(
                maxRecordedTotalVideoDurationMs = 40_000
            )
        }
```

### Step 6: Launch Video Editor
To start Video Editor from camera:
```kotlin
val videoCreationIntent = VideoCreationActivity.startFromCamera(
    context = context
)
startActivity(videoCreationIntent)
```
You can use Java in your Android project. In this case you can create intent to start Video Editor from camera in this way:
``` java
Intent videoCreationIntent = VideoCreationActivity.startFromCamera(
    context,
    Uri.EMPTY,
    null,
    null,
    null,
    CameraUIType.TYPE_1
);
startActivity(videoCreationIntent)
```

More information about how to launch Video Editor you can find [here](launch_modes.md)

## FFmpeg build issue (**Error compressed Native Libs**)
:exclamation: If in the Video Editor process work you see the message **"Error compressed Native Libs. Look documentation"**, then do next:

1. Add the ```android.bundle.enableUncompressedNativeLibs=false``` in the ```gradle.properties```:

``` gradle
android.bundle.enableUncompressedNativeLibs=false
```

2. Add ```android:extractNativeLibs="true"``` in the ```<application>``` path of ```AndroidManifest.xml```:

``` xml
<application
    ...
    android:extractNativeLibs="true"
    ...>
```

## Customizations
## What can you customize?
We understand that the client should have options to brand video editor to bring its own experience to the market. Therefore we provide list of options to customize:

:white_check_mark: Use your branded icons. [See details](#Configure-screens)  
:white_check_mark: Use you branded colors. [See details](#Configure-screens)  
:white_check_mark: Change text styles i.e. font, color. [See details](#Configure-screens)  
:white_check_mark: Localize and change text resources. Default locale is :us:  
:white_check_mark: Make content you want i.e. a number of video with different resolutions  and durations, an audio file. [See details](#Configure-export-flow)  
:white_check_mark: Masks and filters order. [See details](#Configure-masks-and-filters-order)  
:x: Change layout  
:x: Change screen order

:exclamation: We do custom UX/UI changes as a separate contract. Please contact our [sales@banuba.com](mailto:sales@banuba.com).


## Third party libraries

[View](3rd_party_licences.md) information about third party libraries

## Releases
[1.0.15.1](releases/1.0.15.1.md)\
[1.0.16](releases/1.0.16.md)\
[1.0.16.1](releases/1.0.16.1.md)\
[1.0.16.2](releases/1.0.16.2.md)\
[1.0.16.3](releases/1.0.16.3.md)\
[1.0.17](releases/1.0.17.md)\
[1.0.17.1](https://vebanuba.notion.site/1-0-17-1-5a1533ef2cfe48ec85d191f3cc7ae3a5)\
[1.0.18](https://vebanuba.notion.site/1-0-18-a08aad0fac724c358b78823abbaadc8e)\
[1.0.18.1](https://www.notion.so/vebanuba/1-0-18-1-ce0a7e6011e74e0488fc8b74d3713965)\
[1.19.0](https://vebanuba.notion.site/1-19-3c52973c27e54faa874c6e0f6ce8eceb)\
[1.20.0](https://vebanuba.notion.site/1-20-0e78026eed384b1f98cf556f037ef777)\
[1.21.0](https://vebanuba.notion.site/1-21-e615d464c62f4f01bbf76953eb0da642)\
[1.22.0](https://vebanuba.notion.site/1-22-1a07b21041e74d32a3582ca311ecbd48)\
[1.22.2](https://vebanuba.notion.site/1-22-2-71790ea2448c4292a869627d94c17969)\
[1.23.0](https://vebanuba.notion.site/1-23-e81b53452ac840e5a25b4e5b3c96839f)\
[1.23.1](https://vebanuba.notion.site/1-23-1-5c2b5918389c40d1b7d868011443cfe5)\
[1.24.0](https://vebanuba.notion.site/1-24-5b97696e0eae4fbca36b71ac6d8a05be)\
[1.24.1](https://vebanuba.notion.site/1-24-1-c6a58469dc5140bc95ad4cc78061a332)\
[1.24.2](https://vebanuba.notion.site/1-24-2-fffb57ad78b246af9a0903be8626967a)\
[1.25.0](https://www.notion.so/vebanuba/1-25-0-9240af0c9b694bc596d8326dd38b7c17)\
[1.25.1](https://www.notion.so/vebanuba/1-25-1-56105d73bcfb4d468a6ce6ea960ab13a)\
[1.26.0](https://www.notion.so/vebanuba/1-26-0-a13cea95a22940b7bf7ec14ab80cfbcb)\
[1.26.0.1](https://www.notion.so/vebanuba/1-26-0-1-97c4631d568e4111b2ca3982f141bbcf)\
[1.26.1](https://www.notion.so/vebanuba/1-26-1-10615b7836ff422bae7ba629894fd300)\
[1.26.2](https://www.notion.so/vebanuba/1-26-2-49f2bc0c102e498fa0e25e6d067aec67)
