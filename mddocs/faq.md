# FAQ  
These are the answers to the most popular questions we are asked about the Banuba AI Video Editor SDK  

### **1. How do I start/stop recording with a tap?** 
By default, the user must hold the “record” button to film and release it to stop filming.   

To change that, simply set the [takePhotoOnTap](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/camera.json#L5) property to **false**.
``` json
 "takePhotoOnTap":false
```

### **2. How do I add an AR mask to the app without using the AR cloud?**  
If you don’t want to pull the masks from the backend, you can include them in the app itself. 

To do so, name the folder with the mask files the way you want to call the mask, and place it into **assets/bnb-resources/effects** directory in the module containing the Video Editor SDK (Example).

Make sure that you include the **preview.png** file in the mask folder. It serves as an icon for the mask within the app. 

### **3. I want to turn off animations from slideshow**

Slideshow is created either by selecting pictures from gallery or by making a photo on Video Editor camera screen.

Every slide within slideshow can appeare with or without animations. This behavior is configured within [**videoeditor.json**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/1e37324dea76304e8e9205d463844ac5c8c199f7/app/src/main/assets/videoeditor.json#L31) config file under ```slideshow``` section:
```kotlin
"slideshow": {
    /* other slideshow related settings */
    "animate": true,
    "animateTakenPhotos": true
  }
```
Here ```animate``` parameter is applicable to the slideshow created by selecting pictures from the gallery and ```animateTakenPhotos``` is for making photo use case.

To turn off animations just **setup false for both fields**.

### **4. I want to start VideoEditor with a preselected audio track**

You should create an intent using the method **VideoCreationActivity.buildIntent()** where **audioTrackData** is preselected audio track.

```kotlin
startActivity(
    VideoCreationActivity.buildIntent(
       context,
       cameraMode,
       audioTrackData
    )
)
```
**audioTrackData** is an object of TrackData class

```kotlin
data class TrackData(
    val id: UUID,
    val title: String,
    val localUri: Uri
)
```

### **5. How do I add LUTs to the app?**

Color filters are located in the **assets/bnb-resources/luts** directory in the module with the AI Video Editor SDK. To add your own, place the files in this folder and create a drawable resource that will be used as an icon for this particular LUT. The name of the drawable resource must be the same as the graphic file in the filter’s directory.

For example, this is the [bubblegum LUT](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/bnb-resources/luts/bubblegum.png) file, and this is its [drawable resource](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/drawable-hdpi/bubblegum.png).

### **6. How do I change the order of LUTs?**

By default, the filters are listed in alphabetical order. 

To change it, use the implementation of the ```ColorFilterOrderProvider``` interface. 
```kotlin
class CustomColorFilterOrder: ColorFilterOrderProvider {
    override fun provide(): List<String> = listOf("egypt", "byers")
}
``` 
This will return the list of color filters with the required order. 
Note: The name of color filter is a name of an appropriate file located in **assets/bnb-resources/luts** directory. [Example](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/bnb-resources/luts/egypt.png).

The final step is to pass your custom ```ColorFilterOrderProvider``` implementation in the [DI](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-di) to override the default implementation:

```kotlin
override val colorFilterOrderProvider: BeanDefinition<ColorFilterOrderProvider> = single(override = true) {
        CustomColorFilterOrder()
    }
```

### **7. I want to control visibility of debug info on camera and editor screens**

You can control visibility of camera config information and camera preview params(FPS, ISO). Change the following properties in [**camera.json**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/camera.json#L16) config file to control:
```json
    "showDebugViews": false,
    "showConfig": false
 ```
You can control visibility of editor config. Change the following properties in  [**videoeditor.json**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/videoeditor.json#L15) config file to control:
```json
    "showConfig": false
 ```
Set **false** to hide info, set **true** to show.

### **8. I want to customize gallery icon**

Gallery icon is represented by AppCompatImageView. Its style placed into `galleryImageViewStyle` attribute of the main theme ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/1e37324dea76304e8e9205d463844ac5c8c199f7/app/src/main/res/values/themes.xml#L104))

**Drawable resource** of the gallery icon may vary depending on the use case:
 - in case of the very first launch, if the user **did not grant permission** to [read external storage](https://developer.android.com/reference/android/Manifest.permission#READ_EXTERNAL_STORAGE), or if the gallery on the device **is empty**, the drawable resource defined in `icon_empty_gallery` attribute of the `CameraOverlayStyle` ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/1e37324dea76304e8e9205d463844ac5c8c199f7/app/src/main/res/values/themes.xml#L469)) will be used
 - in other cases you can select what to show as a gallery icon: 
    -  **the last media file** from the device
    - **custom drawable** resource

![img](screenshots/faq1.png)

**By default the last media file is used as a drawable**. You have an option to **put background** to the gallery icon by changing `icon_gallery_background` attribute of `CameraOverlayStyle` and you can **add rounded corners** to the gallery icon by changing `icon_gallery_radius` attribute of this style ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/1e37324dea76304e8e9205d463844ac5c8c199f7/app/src/main/res/values/themes.xml#L420)).

 **To setup custom drawable resource** instead of the last media file you have to put custom style into `galleryImageViewStyle` attribute of the main theme ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/1e37324dea76304e8e9205d463844ac5c8c199f7/app/src/main/res/values/themes.xml#L105)) and set there `use_custom_image` to `true` and put your drawable as `android:src` attribute value

 ### **9. How to enable video rotation on trimmer**

 To allow video rotation on trimmer you should add a following line into [**videoeditor.json**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/videoeditor.json) file under **"trimmer"** section:
 ```json
 "trimmer": {
    ...
    "supportsRotation": true
  }
 ```
The rotation button appearance can be customized by `trimmerRotateButtonStyle` theme [**attribute**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/1e37324dea76304e8e9205d463844ac5c8c199f7/app/src/main/res/values/themes.xml#L145) (more on trimmer screen customization [**here**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/mddocs/trimmer_styles.md)).

### **10. How does video editor work when token expires?**

[Token](https://github.com/Banuba/ve-sdk-android-integration-sample#token) provided by sales managers has an expiration term to protect Video Editor SDK from malicious access. When the token expires the following happens:
 - video resolution will be lowered to 360p on camera, after trimmer and after export
 - Banuba watermark is applied to every exported video

 Also [FaceAR SDK](https://docs.banuba.com/face-ar-sdk/overview/token_management#how-does-it-work) you may expect the following actions if the token expires:
 - on the first expired month a watermark with "Powered by Banuba" label will be added on the top of both recorded and exported videos
 - after the first month the camera screen will be blurred and a full-screen watermark will be displayed

 Please keep your licence up to date to avoid unwanted behavior.

 ### **11. I want to change the font in Video Editor**

All text view styles in Video Editor SDK are inherited from the `Text` style, thus the font set in this style will be applied to all text views in Video Editor.

To apply `customFont` to Video Editor just override this style:

 ```
<style name="Text">
    <item name="android:fontFamily">@font/customFont</item>
</style>
 ```

Using this approach you don't have to repeatedly set the font to any other styles while customizing the Video Editor.


 ### **12. How to obtain GIF preview image of exported video**

 Video Editor SDK allows to obtain exported video preview as a gif image.

 Inside your `ExportParamsProvider` implementation create a `GifMaker.Params`:

 ```kotlin
     data class Params(
        val destFile: File,
        val sourceVideoRangeMs: LongRange = 0..1000L,
        val fps: Int = 15,
        val width: Int = 240,
        val useDithering: Boolean = true,
        val reverse: Boolean = true
    )
 ```

Where

 - `destFile` - is a file where gif will be stored after export
- `sourceVideoRangeMs` - is a range of exported video that will be used to create gif image
-  `fps` - frames per second within gif image
- `width` - is a width of gif image in pixels
- `useDithering` - is a flag that apply or remove dithering effect (in simple words make an image of better quality)
- `reverse` - is a flag to reverse playback inside gif

 This object should be passed into `interactivePreview()` method of `ExportManager.Params.Builder` along with the rest export data:

 ```diff
ExportManager.Params.Builder(
    mediaResolutionProvider.provideOptimalExportVideoSize())
                .effects(effects)
                .fileName("export_video")
                .videoList(videoList)
                .destDir(exportSessionDir)
                .musicEffects(musicEffects)
                .extraAudioFile(extraSoundtrackUri)
                .volumeVideo(videoVolume)
 +              .interactivePreview(gifPreviewParams)
                .build()
 ```


### **13. How to export in Background**
1. Provide `BackgroundExportFlowManager` through DI: 

    ```kotlin
    override val exportFlowManager: BeanDefinition<ExportFlowManager> = single(override = true) {
            BackgroundExportFlowManager(
                exportDataProvider = get(),
                editorSessionHelper = get(),
                draftManager = get(),
                exportNotificationManager = get(),
                exportDir = get(named("exportDir")),
                shouldClearSessionOnFinish = true,
                publishManager = get()
            )
        }
    ```

2. Add android activity that you want to open after export into `AndroidManifest.xml` file with special intent action filter. We suggest to use "`com.banuba.sdk.ve.flow.ShowExportResult`" string that is stored inside `ExportNotificationManager.ACTION_SHOW_EXPORT_RESULT` constant:

    ```kotlin
    <activity android:name=".CustomActivity">
        <intent-filter>
          <action android:name="com.banuba.sdk.ve.flow.ShowExportResult" />
          <category android:name="android.intent.category.DEFAULT" />
        </intent-filter>
    </activity>
    ```

3. Provide `ExportResultHandler` implementation. `doAction` method should start activity mentioned in step 2:

    ```kotlin
    class CustomExportResultHandler : ExportResultHandler {

        override fun doAction(activity: AppCompatActivity, result: ExportResult.Success?) {
            val intent = Intent(ExportNotificationManager.ACTION_SHOW_EXPORT_RESULT).apply {
                result?.let { putExtra(EXTRA_EXPORTED_SUCCESS, it) }
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            activity.startActivity(intent)
        }
    }
    ```

    **Note**: action that is passed into `Intent` constructor **must be the same** as  the action name from activity intent filter from step 2 (that's why we suggest to use our constant - `ExportNotificationManager.ACTION_SHOW_EXPORT_RESULT`.

4. Inside activity from step 2 add `ExportFlowManager` **koin injection**:

    ```kotlin
    class CustomActivity: AppCompatActivity() {

    	private val exportFlowManager: ExportFlowManager by inject()

    }
    ```

5. Inside `onCreate()` method start to observe `resultData` `LiveData` from `ExportFlowManager` to receive export states inside your activity:

    ```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            
            exportFlowManager.resultData.nonNull().observe(this) { exportResult ->
                //todo: Do your stuff with different ExportResult objects
            }
    }
    ```

    **Note**: `ExportResult` is a sealed class:

    ```kotlin
    sealed class ExportResult {

        object Inactive : ExportResult()

        object Stopped : ExportResult()

        data class Progress(
            val preview: Uri
        ) : ExportResult()

        @Parcelize
        data class Success(
            val message: String,
            val videoList: List<ExportedVideo>,
            val preview: Uri,
            val metaUri: Uri,
            val additionalExportData: Parcelable? = null
        ) : ExportResult(), Parcelable

        @Parcelize
        data class Error(val message: String) : ExportResult(), Parcelable
    }
    ```

6. *Optional*. Provide your own `ExportNotificationManager` :

    ```kotlin
    val exportNotificationManager: BeanDefinition<ExportNotificationManager> = single(override = true) {
            CustomExportNotificationManger(
                context = get(),
                notificationManager = get()
            )
        }
    ```

    **Note**: We provide `DefaultNotificationManager` implementation as a default, so please check out if it fits your goals. If it does you do not need to override it. 

    If you do not want to show notifications at all just stub all methods in your implementation:

    ```kotlin
    class EmptyExportNotificationManger(
        private val context: Context,
        private val notificationManager: NotificationManager
    ) : ExportNotificationManager {

        fun showExportStartedNotification(){}
        fun showSuccessfulExportNotification(result: ExportResult.Success){}
        fun showFailedExportExportNotification(){}

    }
    ```

### **14. How to change drafts configuration**

By default drafts enabled, asks the user to save a draft before leave any VideoEditor screen. If you need to change drafts configuration you should add the code below in the `VideoEditorKoinModule`:

    ```kotlin
    override val draftConfig: BeanDefinition<DraftConfig> = factory(override = true) {
        DraftConfig.ENABLED_ASK_TO_SAVE
    }
    ```

You can choose one of these options:
1. `ENABLED_ASK_TO_SAVE` - drafts enabled, asks the user to save a draft
2. `ENABLED_ASK_IF_SAVE_NOT_EXPORT` - drafts enabled, asks the user to save a draft without export
3. `ENABLED_SAVE_BY_DEFAULT` - drafts enabled, saved by default without asking the user
4. `DISABLED` - disabled drafts
