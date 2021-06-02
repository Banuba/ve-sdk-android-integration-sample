# How to launch Video Editor

## You can launch Banuba video editor via following methods:
1. ### To start video editor in a normal way:
   ```kotlin
     VideoCreationActivity.buildIntent(
         context: Context,
         additionalExportData: Parcelable?,
         audioTrackData: TrackData?
     )
   ```
2. ### To start video editor from Trimmer screen i.e. camera screen is skipped:
   ```kotlin
     VideoCreationActivity.buildPredefinedVideosIntent(
         context: Context,
         predefinedVideos: Array<Uri>,
         additionalExportData: Parcelable?,
         audioTrackData: TrackData?
     )
   ```
3. ### To start video editor in PIP mode:
   ```kotlin
     VideoCreationActivity.buildPipIntent(
         context: Context,
         pictureInPictureVideo: Uri,
         additionalExportData: Parcelable?,
         audioTrackData: TrackData?
     )
   ```
## Check out an [**example**](../app/src/main/java/com/banuba/example/integrationapp/MainActivity.kt#L38)