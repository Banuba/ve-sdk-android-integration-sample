# Audio Browser integration

## Overview

**Audio Browser** is an audio content provider module developed by the Banuba Video Editor SDK team for Android and iOS. 

It allows users to apply audio tracks from audio content service and the device storage to video within SDK.

The module supports integration with [Mubert](https://mubert.com/) API.

## Implementation

### Step 0

Before starting integration make sure that `camera.json` file has a parameter:

```kotlin
"supportsExternalMusic": true
```

And `videoeditor.json` file has a parameter:

```kotlin
"supportsMusicMixer": true
```

### Step 1

Add a dependency into your gradle file containing other VE SDK dependencies and setup its version (the latest is 1.0.15):

```kotlin
implementation "com.banuba.sdk:ve-audio-browser-sdk:1.0.15"
```

### Step 2

Add the Audio Browser Koin module to the video editor module. The video editor Koin module should be configured according to **[Configuring DI](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-di)** guide.

```kotlin
startKoin {
    androidContext(this@IntegrationApp)        
    modules(VideoEditorKoinModule().module,
						AudioBrowserKoinModule().module)
}
```

Within the main SDK module (`VideoEditorKoinModule`) override `musicTrackProvider` to use audio browser as an audio content provider:

```kotlin
override val musicTrackProvider: BeanDefinition<ContentFeatureProvider<TrackData>> =
        single(named("musicTrackProvider"), override = true) {
            AudioBrowserMusicProvider()
        }
```

### Step 3

There are bulk of **theme attributes** that should be added to the main **Video Editor SDK theme** to setup Audio Browser appearance:

```xml
- audioBrowserBottomSheetStyle
- audioBrowserTrackItemStyle
- audioBrowserTrackNameStyle
- audioBrowserTrackDurationStyle
- audioBrowserTrackApplyBtnStyle
- audioBrowserTrackErrorImageStyle
- audioBrowserTrackPlaybackBtnStyle
- audioBrowserTrackSeekBarStyle
- audioBrowserDownloadAnimationViewStyle
- audioBrowserDownloadCancelViewStyle
- audioBrowserCategoryItemStyle
- audioBrowserCategoryImageStyle
- audioBrowserCategoryNameStyle
- audioBrowserSubCategoryItemStyle
- audioBrowserSubCategoryImageStyle
- audioBrowserErrorViewStyle
- audioBrowserRecyclerViewStyle
- audioBrowserSearchViewStyle
- audioBrowserCloseViewStyle
- audioBrowserTitleViewStyle
- audioBrowserLibraryViewStyle
- audioBrowserCameraTrackContainerStyle
- audioBrowserCameraTrackIconViewStyle
- audioBrowserCameraTrackTitleViewStyle
- audioBrowserCameraTrackResetBtnStyle
```

**Almost all** **attributes have an appropriate style** (with the name similar to an attribute) that can be used as a default (exceptions are shown below). For example, the `AudioBrowserBottomSheetStyle` can be placed under `audioBrowserBottomSheetStyle` theme attribute. 

```xml
<style name="YourCustomVideoCreationTheme" parent="VideoCreationTheme">
	... here comes all Video Editor SDK attributes
	<item name="audioBrowserBottomSheetStyle">@style/AudioBrowserBottomSheetStyle</item>
	... here comes all audio browser attributes
</style>
```

All these styles **are recommended** to be used as parents in case of UI customization:

```xml
<style name="YourCustomAudioBrowserBottomSheetStyle" parent="AudioBrowserBottomSheetStyle" />
```

**Exceptions** of this approach are following styles:

```kotlin
- audioBrowserSubCategoryNameStyle (there is no an appropriate default style)
- audioBrowserTrackItemDividerDrawable (here drawable resource is required)
- audioBrowserCategoryItemDividerDrawable (here drawable resource is required)
- audioBrowserSubCategoryItemDividerDrawable (here drawable resource is required)
```

**Check out an example of audio browser styles customization in our [sample project](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L262).**

### Step 4 (Optional)

Audio browser allows to use **external audio sources** that provide network API. In this case the `TrackLoader` interface named `"remoteTrackLoader"` should be overridden:

```kotlin
val customRemoteTrackLoader: BeanDefinition<TrackLoader> = single(named("remoteTrackLoader")) {
            CustomRemoteTrackLoader()
        }
```

To map networking results into audio browser entities a convenient parameterized interface `com.banuba.sdk.audiobrowser.domain.Mapper` can be used.

**NOTE: By using custom implementation you should create and provide through DI all mappers and other utility classes (eg. API services) on your own.**

## [Mubert](https://mubert.com/) integration (Optional)

Audio Browser supports integration with [Mubert](https://mubert.com/) API.

Banuba team will provide you a specific Mubert API key that you need to put in your project. 

Please follow below steps to configure it in your application.

### Step 1

Put provided Mubert API Key in `strings.xml`

```kotlin
<string name="mubert_api_key">YOUR_PERSONAL_MUBERT_KEY</string> 
```

NOTE:  name "mubert_api_key"  is mandatory.

### Step 2

Create a **mubert_api.json** file with your specific requirements for Mubert service. Place  **mubert_api.json** file into `assets` folder of your app.

```kotlin
{
  "generatedTrackDurationSec": 30,
  "generatedTrackBitrate": 128,
  "generatedTrackIntencity": "high",
  "generatedTrackFormat": "mp3",
  "generatedTracksAmount": 5
}
```

Config parameters are follows:

- **generatedTrackDurationSec** - generated audio track duration in seconds
- **generatedTrackBitrate** - possible bitrates are: 32, 96, 128, 192, 256, 320
- **generatedTrackIntencity** - can be low, medium, high. high - is recommended.
- **generatedTrackFormat** - mp3
- **generatedTracksAmount** - how much audio tracks should be generated at a time (**NOTE: the more tracks you want the more time it takes to generate all of them**)