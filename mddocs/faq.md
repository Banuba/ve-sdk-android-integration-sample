# FAQ  
This page is aimed to explain the most frequent technical questions asked while integrating our SDK.  

### 1. I want to start and stop video recording by short click  
The user has to keep pressing recording button to record new video by default. Video recording stops when the user releases finger from recording button.  

Please set [takePhotoOnTap](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/camera.json#4) property to **false** to allow the user to start and stop recording new video by short click.  
``` json
 "takePhotoOnTap":false
```

### 2. I want to add AR Mask to the Video Editor (without AR Cloud backend)

Technically AR Mask is a bulk of files within the folder.

You should place AR Mask folder to the **assets/bnb-resources/effects** directory inside the module containing video editor SDK ([**Example**](https://github.com/Banuba/ve-sdk-android-integration-sample/tree/main/app/src/main/assets/bnb-resources/effects)). Be sure that AR mask directory has a **preview.png** file. It is used as an icon of the AR mask in the app.

**Note** that the name of directory will be used as a title of the AR mask within the app.

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

### 5. I want to add color filters

Color filters (luts) are special graphic files placed into **assets/bnb-resources/luts** directory inside the module containing video editor SDK.

To add your own color filter you should place it into aforementioned folder and create a **drawable resource** that will be used as an icon for this particular color effect within the list of effects.

The name of the drawable resource must be **the same** as the graphic file within luts directory.

As an example, check out how the black-and-white color filter is added: 

[**Here**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/bnb-resources/luts/C1.png) is a graphic file named C1.png within assets/bnb-resources/luts directory that allows to achieve black-and-white picture

[**Here**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/drawable/c1.png) is an appropriate drawable resource named c1.png within drawable resource directory that is used as an icon within the list of effects

### 6. I want to change an order of color filters

**By default**, color filters are **ordered alphabetically** in Video editor SDK.

To change an order of color filters within the app, you should use an implementation of the ```ColorFilterOrderProvider``` interface:
```kotlin 
interface ColorFilterOrderProvider {

    fun provide(): List<String>
}
```
Your custom implementation should return a list of color filters names with required order. **Note:** The name of color filter is a name of an appropriate file located in assets/bnb-resources/luts directory. [Example](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/bnb-resources/luts/C1.png). 

Let`s say, you have two color filters in assets/bnb-resources/luts named "c1.png" and "c2.png". By default they appear in alphabetical order (c1, c2). If you want the "c2" filter to be the first, just create following implementation:
```kotlin
class CustomColorFilterOrder: ColorFilterOrderProvider {
    override fun provide(): List<String> = listOf("c2", "c1")
}
```

As the final step pass your custom ```ColorFilterOrderProvider``` implementation inside one of your [Koin modules](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-di) to override the default implementation:

```kotlin
override val colorFilterOrderProvider: BeanDefinition<ColorFilterOrderProvider> = single(override = true) {
        CustomColorFilterOrder()
    }
```

### 7. I want to hide or show debug info on camera and editor screens

On camera screen info visibility is configured within [**camera.json**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/camera.json#L16) config file:
```json
    "showDebugViews": false,
    "showConfig": false
 ```
On editor screen visibility is configured within [**videoeditor.json**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/videoeditor.json#L15) config file:
```json
    "showConfig": false
 ```
Set false to hide info, set true to show.
