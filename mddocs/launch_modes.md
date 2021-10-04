# How to launch Video Editor

## You can launch AI Video Editor via following methods:
1. ### To start Video Editor from camera:
   ```kotlin
     VideoCreationActivity.startFromCamera(
        context: Context,
        pictureInPictureVideo: Uri = Uri.EMPTY,
        additionalExportData: Parcelable? = null,
        audioTrackData: TrackData? = null,
        themResId: Int? = null,
        cameraUIType: CameraUIType = CameraUIType.TYPE_1
     )
   ```

    To Start Video editor in PIP mode pass `pictureInPictureVideo` instead of default value.

2. ### To start Video Editor from Trimmer screen i.e. camera screen is skipped:
   ```kotlin
     VideoCreationActivity.startFromTrimmer(
         context: Context,
         predefinedVideos: Array<Uri>,
         additionalExportData: Parcelable? = null,
         audioTrackData: TrackData? = null
     )
   ```
3. ### To Start Video editor from Drafts screen:
   ```kotlin
     VideoCreationActivity.startFromDrafts(
         context: Context
     )
   ```
## Check out an [**example**](../app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L38)