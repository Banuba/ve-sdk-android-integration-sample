# Banuba Video Editor SDK Integration sample for Android
[Banuba VE SDK](https://www.banuba.com/video-editor-sdk)
The Most Powerful Augmented Reality Video Editor SDK for Mobile  
In progress

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
- [AndroidX](https://developer.android.com/jetpack/androidx/versions) libraries
- [Banuba Face AR SDK](https://www.banuba.com/facear-sdk/face-filters). *Optional*

## Free Trial
Before purchasing the license cost you have 1-month free trial period.  
1. Sign NDA. [Contact Us](https://www.banuba.com/video-editor-sdk#form)
1. Clone this repository
1. Request [tokens](##Tokens)
1. Put tokens in the app
1. Start the sample
1. Follow [integration guide](##Getting-Started) to bring your customizations

## Tokens  
Banuba uses tokens for [Face AR SDK](https://www.banuba.com/facear-sdk/face-filters) and VE SDK products to differentiate our clients, protects features and technology. SDK requires up to date tokens, otherwise SDK will crash the app.  
Since Banuba VE SDK includes Face AR SDK it is required to specify Face AR token for applying AR effects. Please, put the token [here](app/src/main/res/values/strings.xml#L5)


## Getting Started
### Setup GitHub packages
GitHub packages is used to download the latest SDK modules.
1. Add GitHub properties file to your project  [github.properties](github.properties)

1. Load github.properties in your [build.gradle](build.gradle#L6)  

``` kotlin
buildscript {
    ext.kotlin_version = "1.4.10"

    ext.githubProperties = new Properties()
    ext.githubProperties.load(new FileInputStream(rootProject.file("github.properties"))
    ...
} 
```

1. Add [Maven repository](build.gradle#L23) for GitHub
``` kotlin
allprojects {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Banuba/banuba-ve-sdk")
            credentials {
                username = rootProject.ext.githubProperties['gpr.usr']
                password = rootProject.ext.githubProperties['gpr.key']
            }
        }
        ...
    }
}
```  
### Add dependencies
Please specify list of dependencies as in [app/build.gradle](app/build.gradle#L38) file to integrate Banuba Video Editor SDK.

### Add Activity
Banuba Video Editor contains a specific flow and order of screens i.e. camera, gallery, trimmer, etc. Each screen is a [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment?authuser=1). All Fragmens are handled with [Activity](https://developer.android.com/jetpack/androidx/releases/activity?hl=en&authuser=1) - VideoCreationActivity. Add VideoCreationActivity to [AndroidManifest.xml](app/src/main/AndroidManifest.xml#L21)
``` xml
<activity android:name="com.banuba.sdk.ve.flow.VideoCreationActivity"
    android:screenOrientation="portrait"
    android:theme="@style/CustomIntegrationAppTheme"
    android:windowSoftInputMode="adjustResize"
    tools:replace="android:theme" />
```
It will allow to [launch Video Editor](app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L24).  

[CustomIntegrationAppTheme](app/src/main/res/values/themes.xml#L14) theme overrides icons, colors, etc. for VE SDK screens.

### Add config files  
Banuba VE SDK has several configuration files that allow to customize video editor for your needs. All config files should be placed into Android **assets** folder.  
- [camera.json](app/src/main/assets/camera.json) contains properties that you can customize on camera screen i.e. the minimun and maximum video durations, if your camera screen should support flashlight, etc.
Usually, *minVideoDuration* and *maxVideoDuration* are most used properties.
- [music_editor.json](app/src/main/assets/music_editor.json) contains properties that you can customize on audio editor screen i.e. how many timelines or tracks are allowed, etc.
- [object_editor.json](app/src/main/assets/object_editor.json) contains properties that you can customize on editor screen while editing text or GIF on video effects.
- [videoeditor.json](app/src/main/assets/videoeditor.json) contains properties that you can customize on editor, trimmer, gallery screens.  *Note*: please keep in mind that *minVideoDuration* and *maxVideoDuration* in this and [camera.json](app/src/main/assets/camera.json) should be the same.

### Configure DI  
VideoEditor behavior can be overriden. We use [Koin](https://insert-koin.io/) for this purpose.  
Firstly, you need to create your own implementation of FlowEditorModule.  
``` kotlin
class VideoeditorKoinModule : FlowEditorModule() {

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
You will need to override several properties to customize video editor for your application.
Full example you can take a look [here](app/src/main/java/com/banuba/example/integrationapp/videoeditor/di/VideoeditorKoinModule.kt).  

Secondly, you need to initialize Koin module in your [Application.onCreate](app/main/src/java/com/banuba/example/integrationapp/IntegrationApp.kt#L12) method.  
``` kotlin
startKoin {
    androidContext(this@IntegrationApp)        
    modules(VideoeditorKoinModule().module)
}
```


### Configure export flow  


### How to integrate to Android Java project  
In progress...

### Configure screens  
The SDK allows to override icons, colors, typefaces and many more using Android theme and styles. Every SDK screen has its own set of styles.  
Below you can find how to customize VE SDK to bring your experience.
1. [Camera screen](camera_styles.md)
1. [Editor screen](editor_styles.md)
1. [Gallery screen](gallery_styles.md)
1. [Trimmer screen](trimmer_styles.md)
1. [Music Editor screen](music_editor_styles.md)
1. [Cover screen](cover_styles.md)



