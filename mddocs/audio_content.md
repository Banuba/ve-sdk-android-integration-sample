# API for using client's audio content in the AI Video Editor SDK
[comment]: <> (This file was created by exporting notion page from here: https://www.notion.so/vebanuba/API-for-using-client-s-audio-content-in-the-SDK-9a0e03dd3ad04962a2cbadebe5c73c19)
## Overview

The user can apply audio tracks on camera and audio editor screens.

The Video Editor SDK can apply an audio track to a video, trim an audio track before applying, create new audio track as a composition of several applied audio tracks on video.

**NOTE: the Video Editor SDK is not responsible for providing audio content. The client has to implement an integration with audio content provider.**

## Implementation

The Video Editor SDK allows to open client's audio content screen in order to user could pick audio tracks and apply it in the SDK. The screen should be implemented as Android [Activity](https://developer.android.com/reference/android/app/Activity).

### Step 1

Please add your Activity for audio content.  

```kotlin
class AwesomeAudioContentActivity : AppCompatActivity() {
...
}
```

### Step 2

Add a method to create Intent of this Activity.

```kotlin
class AwesomeAudioContentActivity : AppCompatActivity() {
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

Argument **extras** contains a data that can be used in the Activity.

1. ProvideTrackContract.EXTRA_LAST_PROVIDED_TRACK of TrackData. Can be null
2. ProvideTrackContract.EXTRA_TRACK_TYPE of TrackType.

### Step 3

Implement ContentFeatureProvider<TrackData>.

```kotlin
class AwesomeActivityMusicProvider : ContentFeatureProvider<TrackData> {

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
        intent = AwesomeAudioContentActivity.buildPickMusicResourceIntent(
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

### Step 4

To use this implementation you need to create Koin module in the app project.

```kotlin
val musicProviderModule = module {

    single<ContentFeatureProvider<TrackData>>(named("musicTrackProvider"), override = true) {
        AwesomeActivityMusicProvider()
    }
}
```

and add this module to Koin in Application class

```kotlin

class AwesomeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ...

        startKoin {
           androidContext(application)
           modules(listOf(VideoEditorMudule + musicProviderModule)
        }
    }
}
```

### Step 5

Now when the user clicks on audio icon your Activity will open.

Please display audio content to the user.

The Video Editor SDK requires audio track to be downloaded on locale storage.

Create an instance of TrackData when the user is ready to apply this audio track in the Video Editor SDK.

```kotlin
val trackData = TrackData(
        UUID.randomUUID(),
        "My awesome track",
        audioTrackUri // Uri of the audio track on local storage
        // file:///data/user/0/<package>/files/<any folder>/awesome.wav
    )
```

```kotlin
@Parcelize
data class TrackData(
    val id: UUID,
    val title: String,
    val localUri: Uri
) : Parcelable
```

### Step 6

Finish Activity with the result.

```kotlin
val trackToApply: TrackData = ...

val resultIntent = Intent().apply {
    putExtra(ProvideTrackContract.EXTRA_RESULT_TRACK_DATA,
       trackToApply)
}
setResult(Activity.RESULT_OK, resultIntent)
finish()
```

Now the selected track will be passed and applied in the Video Editor SDK.