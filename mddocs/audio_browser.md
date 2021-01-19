# Audio Browser integration

## Overview

**Audio Browser** is an audio content provider module developed by the Banuba Video Editor SDK team for Android and iOS. 

It allows users to apply audio tracks from audio content service and the device storage to video within SDK.

The module supports integration with [Mubert](https://mubert.com/) API.

## Implementation

### Step 1

Android

Add a dependency into your gradle file containing other VE SDK dependencies and setup its version (the latest is 1.0.14):  

```kotlin
implementation "com.banuba.sdk:ve-audio-browser-sdk:1.0.14"
```

### Step 2

Android

Add the Audio Browser Koin module to the video editor module. The video editor Koin module should be configured according to **[Configuring DI](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-di)** guide.

```kotlin
startKoin {
    androidContext(this@IntegrationApp)        
    modules(VideoEditorKoinModule().module,
    AudioBrowserKoinModule().module)
}
```

**NOTE: the AudioBrowserKoinModule always should be placed after other SDK modules to be able to override default audio content provider implementation according to the internal Koin logic.**



## [Mubert](https://mubert.com/) integration (Optional)

Audio Browser supports integration with [Mubert](https://mubert.com/) API.

Banuba team will provide you a specific Mubert API key that you need to put in your project. 

Please follow below steps to configure it in your application.

### Step 1

Android

Put provided Mubert API Key in `strings.xml`

```kotlin
<string name="mubert_api_key">YOUR_PERSONAL_MUBERT_KEY</string> 
```

NOTE:  name "mubert_api_key"  is mandatory.

### Step 2

Android

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
