# Banuba Video Editor SDK Integration sample for Android
[Banuba VE SDK](https://www.banuba.com/video-editor-sdk)
The Most Powerful Augmented Reality Video Editor SDK for Mobile  
In progress

## Dependencies
In progress

## Usage
In progres

## Trial
In progress

## Tokens  
Banuba uses tokens for [Face AR SDK](https://www.banuba.com/facear-sdk/face-filters) and VE SDK products to differentiate our clients, protects features and technology. SDK requires up to date tokens, otherwise SDK will crash the app.  
Since Banuba VE SDK includes Face AR SDK it is required to specify 2 tokens.  
1. Face AR token for applying AR effects. Please, put the token [here](app/src/main/res/values/strings.xml#L5)
1. Video effects token for applying visual and time effects. Please, put the token [here](app/src/main/res/values/strings.xml#L7)


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
<activity
            android:name="com.banuba.sdk.ve.flow.VideoCreationActivity"
            android:screenOrientation="portrait"
            android:theme="@style/CustomIntegrationAppTheme"
            android:windowSoftInputMode="adjustResize"
            tools:replace="android:theme" />
```
It will allow to [launch Video Editor](app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L24). 

### Add config files

### Configure DI

### Configure your export flow



