# FAQ  
These are the answers to the most popular questions we are asked about the Banuba AI Video Editor SDK  

### 1. How do I start/stop recording with a tap? 
By default, the user must hold the “record” button to film and release it to stop filming.   

To change that, simply set the [takePhotoOnTap](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/camera.json#4) property to **false**.  
``` json
 "takePhotoOnTap":false
```

### 2. How do I add an AR mask to the app without using the AR cloud?  
If you don’t want to pull the masks from the backend, you can include them in the app itself. 

To do so, name the folder with the mask files the way you want to call the mask, and place it into **assets/bnb-resources/effects** directory in the module containing the SDK (Example).

Make sure that you include the **preview.png** file in the mask folder. It serves as an icon for the mask within the app. 

### 3. I want to turn off animations from slideshow

Slideshow is created either by selecting pictures from gallery or by making a photo on Video Editor camera screen.

Every slide within slideshow can appeare with or without animations. This behavior is configured within [**videoeditor.json**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/videoeditor.json#L29) config file under ```slideshow``` section:
```kotlin
"slideshow": {
    /* other slideshow related settings */
    "animate": true,
    "animateTakenPhotos": true
  }
```
Here ```animate``` parameter is applicable to the slideshow created by selecting pictures from the gallery and ```animateTakenPhotos``` is for making photo use case.

To turn off animations just **setup false for both fields**.

### 4. I want to start VideoEditor with a preselected audio track

You should create an intent using the method **VideoCreationActivity.buildIntent()** where **audioTrackData** is preselected audio track.

```kotlin
startActivity(
    VideoCreationActivity.buildIntent(
       context,
       cameraMode,
       audioTrackData,
       predefinedVideos
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

### 5. How do I add LUTs to the app?

Color filters are located in the **assets/bnb-resources/luts** directory in the module with the SDK. To add your own, place the files in this folder and create a drawable resource that will be used as an icon for this particular LUT. The name of the drawable resource must be the same as the graphic file in the filter’s directory.

For example, this is the [LUT](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/bnb-resources/luts/C1.png) file, and this is its [drawable resource]((https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/drawable/c1.png)).

### 6. How do I change the order of LUTs?

By default, the filters are listed in alphabetical order. 

To change it, use the implementation of the ```ColorFilterOrderProvider``` interface. 
```kotlin
class CustomColorFilterOrder: ColorFilterOrderProvider {
    override fun provide(): List<String> = listOf("c2", "c1")
}
``` 
This will return the list of color filters with the required order. 
Note: The name of color filter is a name of an appropriate file located in **assets/bnb-resources/luts** directory. [Example](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/bnb-resources/luts/C1.png).

The final step is to pass your custom ```ColorFilterOrderProvider``` implementation in the [DI](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-di) to override the default implementation:

```kotlin
override val colorFilterOrderProvider: BeanDefinition<ColorFilterOrderProvider> = single(override = true) {
        CustomColorFilterOrder()
    }
```

### 7. I want to control visibility of debug info on camera and editor screens

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

### 8. I want to customize gallery icon

Gallery icon is represented by AppCompatImageView. Its style placed into `galleryImageViewStyle` attribute of the main theme ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L94)) 

**Drawable resource** of the gallery icon may vary depending on the use case:
 - in case of the very first launch, if the user **did not grant permission** to [read external storage](https://developer.android.com/reference/android/Manifest.permission#READ_EXTERNAL_STORAGE), or if the gallery on the device **is empty**, the drawable resource defined in `icon_empty_gallery` attribute of the `CameraOverlayStyle` ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L372)) will be used
 - in other cases you can select what to show as a gallery icon: 
    -  **the last media file** from the device
    - **custom drawable** resource

![img](screenshots/faq1.png)

**By default the last media file is used as a drawable**. You have an option to **put background** to the gallery icon by changing `icon_gallery_background` attribute of `CameraOverlayStyle` and you can **add rounded corners** to the gallery icon by changing `icon_gallery_radius` attribute of this style ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L375)).

 **To setup custom drawable resource** instead of the last media file you have to put custom style into `galleryImageViewStyle` attribute of the main theme ([**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L94)) and set there `use_custom_image` to `true` and put your drawable as `android:src` attribute value


