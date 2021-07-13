# How to launch Video Editor

## You can launch AI Video Editor via following methods:
1. ### To start Video Editor in a normal way:
   ```kotlin
     VideoCreationActivity.buildIntent(
         context: Context,
         additionalExportData: Parcelable?,
         audioTrackData: TrackData?,
         themResId: Int?,
         cameraUIType: CameraUIType
     )
   ```

    As the last parameter `CameraUIType.TYPE_1` is used. If you do not want to change camera layout type just omit this parameter

2. ### To start Video Editor from Trimmer screen i.e. camera screen is skipped:
   ```kotlin
     VideoCreationActivity.buildPredefinedVideosIntent(
         context: Context,
         predefinedVideos: Array<Uri>,
         additionalExportData: Parcelable?,
         audioTrackData: TrackData?
     )
   ```
3. ### To Start Video editor in PIP mode:
   ```kotlin
     VideoCreationActivity.buildPipIntent(
         context: Context,
         pictureInPictureVideo: Uri,
         additionalExportData: Parcelable?,
         audioTrackData: TrackData?
     )
   ```
## Check out an [**example**](../app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L38)