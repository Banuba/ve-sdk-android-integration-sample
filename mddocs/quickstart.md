# Quickstart Guide

This guide walks you through integrating the Banuba Video Editor SDK into your Android application.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Step 1: Add Repositories](#step-1-add-repositories)
- [Step 2: Configure Packaging Options & Dependencies](#step-2-configure-packaging-options--dependencies)
- [Step 3: Update AndroidManifest.xml](#step-3-update-androidmanifestxml)
- [Step 4: Initialize the SDK](#step-4-initialize-the-sdk)
- [Step 5: Launch the Video Editor](#step-5-launch-the-video-editor)

---

## Prerequisites
- **Android Studio**: Arctic Fox or newer.
- **Min SDK**: API 26 (Android 8.0) or higher.
- **Java Version**: Java 17.
- **Architecture**: `arm64-v8a` (recommended for optimal performance).

---

## Step 1: Add Repositories

Add the Banuba Nexus repository to your project-level configuration using **either** Groovy **or** Kotlin DSL:

### Option A: Groovy (`build.gradle`)
```groovy
allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            name = "nexus"
            url = uri("https://nexus.banuba.net/repository/maven-releases")
        }
    }
}
```

### Option B: Kotlin DSL (`settings.gradle.kts`)
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "nexus"
            url = uri("https://nexus.banuba.net/repository/maven-releases")
        }
    }
}
```

---

## Step 2: Configure Packaging Options & Dependencies

### 1. Add Packaging Options
In your app-level module `build.gradle` (e.g., [`app/build.gradle`](../app/build.gradle)), add the required packaging options to prevent library conflicts:

```groovy
android {
    ...
    packagingOptions {
        pickFirst '**/libbanuba-ve-yuv.so'

        jniLibs {
            useLegacyPackaging = true
        }
    }
}
```

### 2. Add Required Plugins
Ensure your app-level [`build.gradle`](../app/build.gradle#L1) includes the necessary plugins:
```groovy
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-parcelize'
}
```

### 3. Add SDK Dependencies
Add the Banuba dependencies to your app-level [`build.gradle`](../app/build.gradle#L66):

```groovy
dependencies {
    def banubaSdkVersion = '1.55.0'

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
}
```

---

## Step 3: Update AndroidManifest.xml

Add the core activity and required permissions to your [`AndroidManifest.xml`](../app/src/main/AndroidManifest.xml#L27):

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <!-- Network permissions (Optional: required for Giphy stickers or downloading AR cloud effects) -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />

    <application>
        <!-- VideoCreationActivity orchestrates all video editor screens -->
        <activity
            android:name="com.banuba.sdk.ve.flow.VideoCreationActivity"
            android:screenOrientation="portrait"
            android:theme="@style/VideoCreationTheme"
            android:windowSoftInputMode="adjustResize"
            tools:replace="android:theme" />
    </application>
</manifest>
```

> [!NOTE]
> You must supply a custom theme (`VideoCreationTheme`) in your [`res/values/themes.xml`](../app/src/main/res/values/themes.xml#L13) to properly style the editor UI.

---

## Step 4: Initialize the SDK

Initialize `VideoEditorSDK` inside your custom [`Application`](../app/src/main/java/com/banuba/example/integrationapp/SampleApp.kt#L39) class:

```kotlin
import android.app.Application
import com.banuba.sdk.arcloud.data.di.ArCloudKoinModule
import com.banuba.sdk.audiobrowser.impl.di.AudioBrowserKoinModule
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule
import com.banuba.sdk.gallery.impl.di.GalleryKoinModule
import com.banuba.sdk.playback.impl.di.PlaybackKoinModule
import com.banuba.sdk.ve.domain.VideoEditorSDK
import com.banuba.sdk.ve.effects.di.VeEffectsKoinModule
import com.banuba.sdk.ve.flow.di.VideoCreationKoinModule
import com.banuba.sdk.ve.impl.di.VeSdkKoinModule
import com.banuba.sdk.ve.ui.di.VeUiSdkKoinModule

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        
        val editorSdk = VideoEditorSDK.Builder(applicationContext).apply {
            // Optional AudioBrowser module for music providers (BanubaMusic, Soundstripe, Local)
            addModule(AudioBrowserKoinModule().module)
            setMusicTrackProvider(AudioBrowserMusicProvider())

            // Optional Gallery module for selecting videos and photos from device
            addModule(GalleryKoinModule().module)

            // Optional module to enable Face AR product features
            addModule(BanubaEffectPlayerKoinModule().module)
        }.initialize("YOUR_LICENSE_TOKEN")
    }
}
```
## Troubleshooting & License Validation

> [!IMPORTANT]
> 1. If your license token is invalid or expired, `editorSdk` will return `null`.
> 2. Always [check license activation status](../app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L104) before launching the editor.
> 3. Expired or revoked licenses will display a fallback "Video content unavailable" screen.

<p align="center">
<img src="screenshots/screen_expired.png" alt="Expired License Screen" width="25%" height="auto">
</p>


---

## Step 5: Launch the Video Editor

Start the video creation flow from your Activity or Fragment (see full implementation in [`MainActivity.kt`](../app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L18)):

```kotlin
class MainActivity : AppCompatActivity() {

    private val createVideoRequest = registerForActivityResult(IntegrationAppExportVideoContract()) { exportResult ->
        exportResult?.let { result ->
            // Handle the exported video result here (e.g., file path, playback)
        }
    }

    private fun startVideoEditor() {
        val intent = VideoCreationActivity.startFromCamera(
            context = this,
            pictureInPictureConfig = null,
            additionalExportData = null,
            audioTrackData = null
        )
        createVideoRequest.launch(intent)
    }
}
```

---

## Additional Customization
Explore advanced [customization options](https://docs.banuba.com/ve-pe-sdk/docs/android/adv-integration-overview) in the official Banuba documentation to customize themes, watermarks, stickers, and export parameters.
