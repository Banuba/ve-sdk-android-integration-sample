# Audio integration guide

- [Overview](#Overview)
- [Audio Browser](#Audio-Browser)
- [Mubert integration](#Mubert-integration)
- [External API](#External-API)
- [Music Editor screen](#Music-Editor-screen)

## Overview
Audio is a key part of making awesome video content.  

Video Editor SDK can play, trim, merge and apply audio content to a video.  

:exclamation: Important  
1. Banuba does not provide any audio content for Video Editor SDK.
2. Video Editor can apply audio file stored on the device. The SDK is not responsible for downloading audio content except [Mubert](https://mubert.com/)

There are 2 approaches of providing audio content:
1. ```AudioBrowser``` - specific module and a set of screens that includes built in support of browsing and applying audio content within video editor. The user does not leave the sdk while using audio.
2. ```External API``` - the client implements specific API for managing audio content. The user is taken on client's screen when audio is requested.

## Audio Browser
Audio Browser is a specific Android module that allows to browse, play and apply audio content within video editor.  
It supports 2 sources for audio content:
1. ```My Library``` - includes audio content available on the user's device
2. ```Mubert``` - includes built in integration with [Mubert](https://mubert.com/) API.

<p align="center">
<img src="gif/audio_browser.gif" alt="Screenshot" width="30%" height="auto" class="docs-screenshot"/>&nbsp;
</p>

Below is a guide to integrate it into your project.
First, add a dependency to your [gradle](../app/build.gradle#L64) file.

```kotlin
implementation "com.banuba.sdk:ve-audio-browser-sdk:${current sdk version}"
```

Next, add ```AudioBrowserKoinModule``` to Koin modules in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L66)
```diff
startKoin {
    ...    
    modules(
+       AudioBrowserKoinModule().module,
       VideoEditorKoinModule().module
    )
}
```
and add specific implementation for ```musicTrackProvider``` in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L127)
```kotlin
single<ContentFeatureProvider<TrackData, Fragment>>(named("musicTrackProvider")) {
    AudioBrowserMusicProvider()
}
```

Finally, you can customize Audio Browser appearance by implementing the list of available styles and attributes:

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
        - audioBrowserSearchCancelTextStyle
        - audioBrowserConnectionErrorTitle
        - audioBrowserConnectionErrorMessage
        - audioBrowserConnectionErrorBtn
        - audioBrowserConnectionErrorSpinner
        - audioBrowserSubCategoryNameStyle
        - audioBrowserTrackItemDividerDrawable (drawable is required)
        - audioBrowserCategoryItemDividerDrawable (drawable is required)
        - audioBrowserSubCategoryItemDividerDrawable (drawable is required)
```
This sample includes stub implementations of these [styles](../app/src/main/res/values/themes.xml#L295).

## Mubert integration
Audio Browser has built in integration with [Mubert](https://mubert.com/) API.  
Please contact Mubert representatives to request API KEY.

Set Mubert license and token keys in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt).
```kotlin
single {
  MubertApiConfig(
    mubertLicence = "...",
    mubertToken = "..."
  )
}
```

You can use ```MubertApiConfig``` to customize network requests to Mubert as well.

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **generatedTrackDurationSec** | Number > 0 | duration that applied for generated tracks in seconds
| **generateTrackBitrate** | any of the following values: 32, 96, 128, 192, 256, 320 | sound quality measured in kbps
| **generatedTrackIntencity** |  any of the following values: low, medium, high | instrumental saturation (number of stems) for generated tracks
| **generatedTrackFormat** |  any of the following values: mp3, wav, flac | format of generated tracks
| **generatedTracksAmount** | Number > 0 | amount of tracks to generate for selected category

Below is a list string resources you can use or customize

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
| audio_browser_error_license_wrong_key | Mubert key is missing. In order to get it contact Banuba rep. | Error message when Mubert key is not set
| audio_browser_hint_search_categories | Search by categories | Hint for the categories search
| audio_browser_hint_search_sub_categories | Search by groups | Hint for the sub-categroies search
| audio_browser_hint_search_tracks | Search by tracks | Hint for tracks search
| audio_browser_error_dialog_title | Oops, something went wrong… | Title of the dialog message that is shown when something wrong with network request
| audio_browser_error_dialog_description | Please, try again later. | Description of the dialog message that is shown when something wrong with network request
| audio_browser_error_dialog_retry | Retry | Text on the button that allows to retry loading tracks in case of network error
| audio_browser_error_dialog_close | Close | Text on the button that closes dialog message in case of network error
| permission_library_description_message | Allow to access to your storage to select an audio tracks from your device. | Message that is shown to the user before request a permission for loading tracks from the device
| not_supported_audio_browser | Default music browser is not included in your package. You can plugin your own music | Message that is shown to the user when music browser is not included in your token
| audio_browser_connection_error_title | No internet connection | Error message that is shown to the user when there is no internet connection
| audio_browser_connection_error_message | Please, check your connection and try again. | Error message that is shown to the user when there are connection problems
| audio_browser_connection_error_btn | Retry | Text on the button that allows to retry loading tracks in case of network error
| audio_browser_connection_error_toast | No internet connection | Error message that is shown to the user when there is no internet connection


## External API
Video Editor includes special API for integrating your custom audio content provider and applying this content in video editor.   
The user will be taken to your app specific screen when audio is requested on video editor screen i.e. camera or editor.
Next, once the user picks audio content on your app screen you need to follow API and return the user to video editor.  
Any audio file should stored on the device before applying.

Below is a guide of using API to provide your audio to video editor.
First, create new Activity ```CustomAudioContentActivity``` that will handle API and create new method to start it from video editor.
This Activity you can use to implement any API for downloading audio content and showing your beautiful UI to your users.
```kotlin
class CustomAudioContentActivity : AppCompatActivity() {
  ...
  
  companion object {
    fun buildPickMusicResourceIntent(
      context: Context,
      extras: Bundle
    ) =
      Intent(context, AwesomeAudioContentActivity::class.java).apply {
        putExtras(extras)
      }
  }
}
```
where ```extras``` includes a data that can be used in the Activity.
1. ```ProvideTrackContract.EXTRA_LAST_PROVIDED_TRACK``` of TrackData. Can be null. ```null``` is used to dissmiss audio.
2. ```ProvideTrackContract.EXTRA_TRACK_TYPE``` of TrackType.

Next, create ```CustomActivityMusicProvider``` and implement ```ContentFeatureProvider<TrackData>```.
```kotlin
class CustomActivityMusicProvider : ContentFeatureProvider<TrackData, Fragment> {

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    private val activityResultCallback: (TrackData?) -> Unit = {
        activityResultCallbackInternal(it)
    }
    private var activityResultCallbackInternal: (TrackData?) -> Unit = {}

    override fun init(hostFragment: WeakReference<Fragment>) {
        activityResultLauncher = hostFragment.get()?.registerForActivityResult(
            ProvideTrackContract(),
            activityResultCallback
        )
    }

    override fun requestContent(
        context: Context,
        extras: Bundle
    ): ContentFeatureProvider.Result<TrackData> = ContentFeatureProvider.Result.RequestUi(
        intent = CustomAudioContentActivity.buildPickMusicResourceIntent(
            context,
            extras
        )
    )

    override fun handleResult(
        hostFragment: WeakReference<Fragment>,
        intent: Intent,
        block: (TrackData?) -> Unit
    ) {
        activityResultCallbackInternal = block
        activityResultLauncher?.launch(intent)
    }
}
```

And set ```CustomActivityMusicProvider``` to [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt)
```kotlin
    single<ContentFeatureProvider<TrackData>>(named("musicTrackProvider"), override = true) {
        CustomActivityMusicProvider()
    }
```
Please keep in mind that only one instance of ```musicTrackProvider``` can exist either your custom of```External API``` or 
```Audio Browser```.

Finally, pass audio content to apply audio in video editor. Instance of ```TrackData``` is used for passing in ```Intent``` to video editor. 
The audio should be stored on  the device.
```kotlin
val trackData = TrackData(
        UUID.randomUUID(),
        "My awesome track",
        audioTrackUri, // Uri of the audio track on local storage
        // file:///data/user/0/<package>/files/<any folder>/awesome.wav
        "Awesome Artist"
    )
```
To pass ```TrackData``` to video editor your need to use ```setResult``` with ```Intent``` and finish current Activity.
```kotlin
val trackToApply: TrackData = ...

val resultIntent = Intent().apply {
    putExtra(ProvideTrackContract.EXTRA_RESULT_TRACK_DATA,
       trackToApply)
}
setResult(Activity.RESULT_OK, resultIntent)
finish()
```
To dismiss previously selected audio track you can pass ```null``` for ```TrackData```.

## Music Editor screen
Video Editor includes Music Editor screens. These are screens where the user can adjust usage of audio in video editor i.e. trim, add new, delete. 
Music Editor includes voice recording feature as well.  

Below is a list of styles and attributes you can customize to meet your requirements.

- [musicEditorPlaybackControllerParentStyle](../app/src/main/res/values/themes.xml#L177)

  style for the parent of buttons defined in `editorPlaybackControllerCancelStyle`, `editorPlaybackControllerPlayStyle`, `editorPlaybackControllerDoneStyle` attributes. See buttons on the [Editor screen](editor_styles.md#L46) page

- [musicEditorTimelineStyle](../app/src/main/res/values/themes.xml#L180)

  style for the complex view that contains applied music tracks, video timeline, video volume button. It has a lot of custom attributes appliceable to children views

- [musicEditorTimelineSoundWaveStyle](../app/src/main/res/values/themes.xml#L189)

  style for the view that represents added music effect on the timeline view. It configured with a lot of custom attributes

- [musicEditorActionButtonParentStyle](../app/src/main/res/values/themes.xml#L181)

  style for the ConstraintLayout that is used as container for every action button

- [musicEditorActionButtonStyle](../app/src/main/res/values/themes.xml#L184)

  style for the ImageView representing an icon for action button. Drawables for all action buttons in music editor are configured in custom theme attributes:
    - [music_editor_icon_tracks](../app/src/main/res/values/themes.xml#L244) - to open music track selection
    - [music_editor_icon_track_recording](../app/src/main/res/values/themes.xml#L246) - to show voice recording screen
    - [music_editor_icon_track_effects](../app/src/main/res/values/themes.xml#L248) - to open voice recording effects selection
    - [music_editor_icon_track_edit](../app/src/main/res/values/themes.xml#L247) - to open music trimmer for the selected music track
    - [music_editor_icon_track_delete](../app/src/main/res/values/themes.xml#L249) - to delete selected music track

![img](screenshots/musiceditor1_1.png)

- [musicEditorActionButtonTextStyle](../app/src/main/res/values/themes.xml#L185)

  style for action button title

- [timelineEditorBoardStyle](../app/src/main/res/values/themes.xml#L188)

  style for an invisible view that holds applied graphic effects. This view is similar to [editorBoardStyle](editor_styles.md#L86) and has several custom attributes to configure its behavior

![img](screenshots/musiceditor1.png)

- [musicEditorVideoVolumeContainerStyle](../app/src/main/res/values/themes.xml#L206)

  style for the bottom sheet view that is opened by tapping on left bottom icon of the video timeline to change video volume
- [musicEditorVideoVolumeTitleStyle](../app/src/main/res/values/themes.xml#L209)

  style for the title of the volume setting
- [musicEditorVideoVolumeValueStyle](../app/src/main/res/values/themes.xml#L195)

  style for the digit value of the video volume
- [musicEditorVideoVolumeProgressBarStyle](../app/src/main/res/values/themes.xml#L198)

  style for the seek bar that is used to change video volume

![img](screenshots/musiceditor2.png)

- [music_editor_voice_recording_background](../app/src/main/res/values/themes.xml#L222)

  attribute that defines the background of the voice recording button

- [musicEditorRecordingButtonStyle](../app/src/main/res/values/themes.xml#L202)

  style for the voice recording button. There are a lot of custom attributes to configure its appearance. Please check out an [**example**](../app/src/main/res/values/themes.xml#L910)

![img](screenshots/musiceditor3.png)

- [musicEditorVoiceRecordingControllerParentStyle](../app/src/main/res/values/themes.xml#L176)

  style for the view that holds action buttons to manipulate with voice recording on the music editor
- [musicEditorVoiceRecordingControllerCancelStyle](../app/src/main/res/values/themes.xml#L179)

  style for the button that closes voice recording screen returning back to the music editor
- [musicEditorVoiceRecordingControllerResetStyle](../app/src/main/res/values/themes.xml#L182)

  style for the button that removes the last voice recording
- [musicEditorVoiceRecordingControllerDoneStyle](../app/src/main/res/values/themes.xml#L185)

  style for the button that applies voice recordings to the common timeline and returns back to music editor
  ![img](screenshots/musiceditor4.png)

- [musicEditorTrimViewStyle](../app/src/main/res/values/themes.xml#L206)

  style for the view that is used to trim music effects and to apply voice effects. This style is applied to the bottom sheet dialog and all children views configuration are available through custom attributes

- [musicEditorTrimmerStyle](../app/src/main/res/values/themes.xml#L207)

  style for the trim view. It has a bulk of custom attributes to cofigure colors and left/right drawables

- [musicEditorTrimmerSoundWaveStyle](../app/src/main/res/values/themes.xml#L208)

  style for the custom view laying behind the trimmer view. It is similar to [musicEditorTimelineSoundWaveStyle](music_editor_styles.md#L16) and has its own custom attributes as well

  ![img](screenshots/musiceditor5.png)

- [musicEditorEqualizerRecyclerStyle](../app/src/main/res/values/themes.xml#L212)

  style for the RecyclerView containing voice recording effects. This view is shown while applying effects over the voice recording

- [musicEditorEqualizerItemStyle](../app/src/main/res/values/themes.xml#L215)

  style for every item within voice recording effects. This style is applied to custom view and similar to [cameraEffectsItemStyle](camera_styles.md#L36)

- [musicEditorEqualizerThrobberStyle](../app/src/main/res/values/themes.xml#L216)

  style for the circle progress view that is shown over the voice effect item while this effect is being prepared (very short time after click on item and before the item become bigger, i.e. selected)

![img](screenshots/musiceditor6.png)


Music Editor screen also has some theme attributes that define background of some views:
- [music_editor_surface_background](../app/src/main/res/values/themes.xml#L221) - background of Surface view that shows the video
- [music_editor_timeline_background](../app/src/main/res/values/themes.xml#L223) - background of [timeline view](music_editor_styles.md#L8)
- [music_editor_playback_controller_bg](../app/src/main/res/values/themes.xml#L224) - background of control panel on music editor screen
- [music_editor_action_container_bg](../app/src/main/res/values/themes.xml#L225) - background of action buttons container

And you can customize string resources as well.

| ResourceId        |      Value      |   Description |
| ------------- | :----------- | :------------- |
| action_add_music_track | Tracks | title of the button to add tracks on the timeline
| action_add_voice_recording | Record | title of the button to add voice recording to the timeline
| action_effects | Effects | title of the button to add voice effects on the voice recording
| action_edit | Edit | title of the button to edit music effect (or object effect if applies on the timeline screen)
| action_delete | Delete | title of the button to delete selected music effect (or object effect if applies on the timeline screen)
| edit_track_volume_title | Volume | text label shown together with the value when volume of the music effect and video change
| edit_track_volume_percent | %1$d%% | placeholder for the digit volume representation
| edit_track_audio_duration | Audio duration | label on the edit music effect screen
| edit_track_duration_error | Audio should be longer than %1$.1f sec | toast message that is shown when the user tries to cut music track smaller than `minVoiceRecordingMs` parameter of the [**MusicEditorConfig**](config_music_editor.md) class
| error_voice_recording_start | Error on voice recording start | toast message that is shown when the error occurs at the start of the voice recording
| error_voice_recording_stop | Error on voice recording stop | toast message that is shown when the error occurs in the end of the voice recording
| error_invalid_duration_voice_recording | Min voice recording duration - %1$.1f sec | toast message that is shown when the user tries to create voicerecording smaller than `minVoiceRecordingMs` parameter of the [**MusicEditorConfig**](config_music_editor.md) class
| error_invalid_duration_music_track | Min music track duration - %1$.1f sec | toast message that is shown when the user tries to cut the music track smaller than `minVoiceRecordingMs` parameter of the [**MusicEditorConfig**](mddocs/config_music_editor.md) class
| error_voice_recording_delete_file | Internal error when try to delete voice recording file | toast message that is shown when the error occures during voice recording deletion
| error_track_limit | Max available tracks - %1$d | toast message that is shown when the user tries to add more music tracks than defined in `maxTracks` parameter of the [**MusicEditorConfig**](config_music_editor.md) class
| error_no_space | No space | toast message that is shown when the device is out of space before starting voive recording

