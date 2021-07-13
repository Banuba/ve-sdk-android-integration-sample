# Audio Browser integration

## Overview

**Audio Browser** is an audio content provider module developed by the Banuba AI Video Editor SDK team for Android and iOS.

It allows users to apply audio tracks from audio content service and the device storage to video within Video Editor SDK.

The module supports integration with [Mubert](https://mubert.com/) API.

## Implementation

### Step 0

Before starting integration make sure that [**camera.json**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/camera.json) file has a parameter:

```kotlin
"supportsExternalMusic": true
```

And [**videoeditor.json**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/videoeditor.json) file has a parameter:

```kotlin
"supportsMusicMixer": true
```

### Step 1

Add a dependency into your gradle file containing other Video Editor SDK dependencies and setup its version (the latest is 1.0.16):

```kotlin
implementation "com.banuba.sdk:ve-audio-browser-sdk:${current sdk version}"
```

### Step 2

Add the Audio Browser Koin module to the video editor module. The video editor Koin module should be configured according to **[Configuring DI](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-di)** guide.

```kotlin
startKoin {
    androidContext(this@IntegrationApp)        
    modules(
       AudioBrowserKoinModule().module,
       VideoEditorKoinModule().module
    )
}
```

Within the main Video Editor SDK module (`VideoEditorKoinModule`) override `musicTrackProvider` to use audio browser as an audio content provider:

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
- audioBrowserTrackLoadMoreStyle
- audioBrowserTrackLoadMoreItemStyle
- audioBrowserThrobberViewStyle
```

**Almost all** **attributes have an appropriate style** (with the name similar to an attribute) that can be used as a default style implementation. For example, the `AudioBrowserBottomSheetStyle` can be placed under `audioBrowserBottomSheetStyle` theme attribute. 

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

Additional attributes that do not have appropriate styles are:

```
- audioBrowserSubCategoryNameStyle
- audioBrowserTrackItemDividerDrawable (drawable is required)
- audioBrowserCategoryItemDividerDrawable (drawable is required)
- audioBrowserSubCategoryItemDividerDrawable (drawable is required)
```

**Check out an example of audio browser styles customization in our [sample project](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L262).**

### Step 4 (Optional)

Audio browser allows to use **external audio sources** that provide network API. In this case the `TrackLoader` interface named `"remoteTrackLoader"` should be overridden:

```kotlin
val customRemoteTrackLoader: BeanDefinition<TrackLoader> = single(named("remoteTrackLoader"), override = true) {
            CustomRemoteTrackLoader()
        }
```

To map networking results into audio browser entities a convenient parameterized interface `com.banuba.sdk.audiobrowser.domain.Mapper` can be used.

**NOTE: By using custom implementation you should create and provide through DI all mappers and other utility classes (eg. API services) on your own.**

## [Mubert](https://mubert.com/) integration (Optional)

Audio Browser supports integration with [Mubert](https://mubert.com/) API.

If you decide to go with Mubert  just let our sale rep know. He will help you to get with Mubert team and generate the PAT key. This key should be put into the video editor, to get Mubert work.

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

## Localization
There are a lot of string resources that are used withing audio browser:
| ResourceId        |      Value      |   Description |
| ------------- | :----------- | :------------- |
| apply_track | Use | Text of the button that applies music track
| remove_track| Stop\nusing | Text on the button that cancels selected music track
| track_loading_failed | Sorry, audio content is temporarily unavailable | Message for the uknown error within audiobrowser
| track_search_cancel | Cancel | Text of the button that cancels recent search
| audio_browser_title_library | My library | label of the music track list from the device
| audio_browser_title_category | Music | label for the music track list from external API
| audio_browser_title_empty_category | Music | label for the music track list that has no category
| audio_browser_load_more | Show more | Text on the button that allows to load more tracks
| audio_browser_error_tracks_not_found | No tracks found | Error message in case of failed tracks search
| audio_browser_error_categories_not_found | No categories found | Error message in case of failed categories search
| audio_browser_error_empty_library | No tracks yet | Message that is shown if there are no tracks on the device
| audio_browser_error_license_not_active | The license is not active | Error message from the Mubert API
| audio_browser_error_license_expired | The license expired | Error message from the Mubert API
| audio_browser_error_license_access | Access denied, check license access type | Error message from the Mubert API
| audio_browser_error_license_api_version | Access denied, check license API version | Error message from the Mubert API
| audio_browser_hint_search_categories | Search by categories | Hint for the categories search
| audio_browser_hint_search_sub_categories | Search by groups | Hint for the sub-categroies search
| audio_browser_hint_search_tracks | Search by tracks | Hint for tracks search
| audio_browser_error_dialog_title | Oops, something went wrong… | Title of the dialog message that is shown when something wrong with network request
| audio_browser_error_dialog_description | Please, try again later. | Description of the dialog message that is shown when something wrong with network request
| audio_browser_error_dialog_retry | Retry | Text on the button that allows to retry loading tracks in case of network error
| audio_browser_error_dialog_close | Close | Text on the button that closes dialog message in case of network error
| permission_library_description_message | Allow to access to your storage to select an audio tracks from your device. | Message that is shown to the user before request a permission for loading tracks from the device
| audio_browser_load_more | Load more | Text on the button that allows to request more tracks from the Mubert service