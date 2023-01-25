## Video Editor SDK export media guide
The Video Editor SDK exports recordings as .mp4 files. There are many ways you can customize this flow to better integrate it into your app.

To change export output, start with the ```ExportParamsProvider``` interface. It contains one method - ```provideExportParams()``` that returns ```List<ExportManager.Params>```. Each item on this list relates to one of the videos in the output and their configuration. 

The end result would be four files:

- Optimized video file (resolution will be calculated automatically);
- Same file as above but without a watermark;
- Low-res version of the watermarked file.

By default, they are placed in the "export" directory of external storage. To change the target folder, you should provide a custom Uri instance named **exportDir** through DI.

Should you choose to export files in the background, you’d do well to change ```ExportNotificationManager```. It lets you change the notifications for any export scenario (started, finished successfully, and failed).

:exclamation: If you set ```shouldClearSessionOnFinish``` in ```ExportFlowManager``` to true, you should clear ```VideoCreationActivity``` from backstack. Otherwise crash will be raised.

[Check example](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L208),
where multiple video files are exported: the first and the second with the most suitable quality params
and the third with 360p quality (defined by using Video Editor SDK constant `VideoResolution.VGA360`).

# Configure export params

If you need to change export output you should override `ExportParamsProvider` class which contains only one method `provideExportParams`. This method should return list of `ExportParams`, where every item of this list is configurations for one export video. Therefore, if the list contains 3 items, after exporting you will get 3 videos, where each video will be created according to the set configurations.

## Implement ExportParamsProvider class

For example, let's take the `IntegrationAppExportParamsProvider` class.

```kotlin
class IntegrationAppExportParamsProvider(
    private val exportDir: Uri,
    private val sizeProvider: MediaResolutionProvider,
    private val watermarkBuilder: WatermarkBuilder
) : ExportParamsProvider {

    override fun provideExportParams(
        effects: Effects,
        videoRangeList: VideoRangeList,
        musicEffects: List<MusicEffect>,
        videoVolume: Float
    ): List<ExportParams> {
        val exportSessionDir = exportDir.toFile().apply {
            deleteRecursively()
            mkdirs()
        }
        val extraSoundtrackUri = Uri.parse(exportSessionDir.toString()).buildUpon()
            .appendPath("exported_soundtrack.$DEFAULT_SOUND_FORMAT")
            .build()

        return listOf(
            ExportParams.Builder(sizeProvider.provideOptimalExportVideoSize())
                .effects(effects.withWatermark(watermarkBuilder, WatermarkAlignment.BOTTOM_RIGHT))
                .fileName("export_default_watermark")
                .videoRangeList(videoRangeList)
                .destDir(exportSessionDir)
                .musicEffects(musicEffects)
                .extraAudioFile(extraSoundtrackUri)
                .volumeVideo(videoVolume)
                .build(),
            ExportParams.Builder(sizeProvider.provideOptimalExportVideoSize())
                .effects(effects)
                .fileName("export_default")
                .videoRangeList(videoRangeList)
                .destDir(exportSessionDir)
                .musicEffects(musicEffects)
                .volumeVideo(videoVolume)
                .build(),
            ExportParams.Builder(VideoResolution.Exact.VGA360)
                .effects(effects.withWatermark(watermarkBuilder, WatermarkAlignment.BOTTOM_RIGHT))
                .fileName("export_360_watermark")
                .videoRangeList(videoRangeList)
                .destDir(exportSessionDir)
                .musicEffects(musicEffects)
                .volumeVideo(videoVolume)
                .build()
        )
    }
```

After export based on list on `ExportParams` from `IntegrationAppExportParamsProvider` we get three videos:

1. Optimized video file with watermark located in the lower right corner of the screen

    ```kotlin
    ExportParams.Builder(sizeProvider.provideOptimalExportVideoSize())
                    .effects(effects.withWatermark(watermarkBuilder, WatermarkAlignment.BOTTOM_RIGHT))
                    .fileName("export_default_watermark")
                    .videoRangeList(videoRangeList)
                    .destDir(exportSessionDir)
                    .musicEffects(musicEffects)
                    .extraAudioFile(extraSoundtrackUri)
                    .volumeVideo(videoVolume)
                    .build()
    ```

2. Same file as above but without a watermark

    ```kotlin
    ExportParams.Builder(sizeProvider.provideOptimalExportVideoSize())
                    .effects(effects)
                    .fileName("export_default")
                    .videoRangeList(videoRangeList)
                    .destDir(exportSessionDir)
                    .musicEffects(musicEffects)
                    .volumeVideo(videoVolume)
                    .build()
    ```

3. Low-res exported video with watermark located in the lower right corner of the screen

    ```kotlin
    ExportParams.Builder(VideoResolution.Exact.VGA360)
                    .effects(effects.withWatermark(watermarkBuilder, WatermarkAlignment.BOTTOM_RIGHT))
                    .fileName("export_360_watermark")
                    .videoRangeList(videoRangeList)
                    .destDir(exportSessionDir)
                    .musicEffects(musicEffects)
                    .volumeVideo(videoVolume)
                    .build()
    ```

You can customize each video separately. For this you should use `ExportParams.Builder`. The builder contains only one required parameter this is `resolution`, the rest of the parameters are optional. `ExportParams` 's class includes:

- `fileName(fileName: String)` - it is necessary to transfer name of exported video.
- `effects(effects: Effects)` - it is necessary to transfer the effects that are used on the video. If you want add watermark (there is no watermark on the video by default) on your video you should call method `withWatermark` for effects like this:

    ```kotlin
    .effects(effects.withWatermark(watermarkBuilder, WatermarkAlignment.BOTTOM_RIGHT))
    ```

  where `withWatermark` looks like:

    ```kotlin
    fun Effects.withWatermark(
        watermarkBuilder: WatermarkBuilder,
        alignment: WatermarkAlignment
    )
    ```

  where `watermarkBuilder` provide drawable of Watermark and `alignment` indicates where the watermark will be located:

    ```kotlin
    WatermarkAlignment {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
    ```

- `videoRangeList(videoRangeList: VideoRangeList)` - to transfer the videos that are used for creating export video
- `musicEffects(musicEffects: List<MusicEffect>)` - to transfer the music effects that are used on the video
- `destDir(destDir: File)` -  to transfer the file in which the exported video should be saved
- `extraAudioFile(extraAudioTrack: Uri)` - to transfer the uri in which the exported extra audio file should be saved
- `volumeVideo(volume: Float)` - to transfer the audio volume of exported video

## Override ExportParamsProvider

Also you should override you `IntegrationAppExportParamsProvider` in your own `VideoEditorKoinModule`.

```kotlin
class VideoEditorKoinModule : FlowEditorModule() {
		...
    /**
     * Provides params for export
     * */
    override val exportParamsProvider: BeanDefinition<ExportParamsProvider> =
        factory(override = true) {
            IntegrationAppExportParamsProvider(
                exportDir = get(named("exportDir")),
                sizeProvider = get(),
                watermarkBuilder = get()
            )
        }
}
```

## Save videos in gallery

`ForegroundExportFlowManager` and `BackgroundExportFlowManager` requires `PublishManager` dependency. Pass `publishManager = get()` with your `PublishManager` interface implementation to provide your own custom realization. With `PublishManager` you can save your videos in native gallery. The `PublishManager` interface contains only one method `publish` which takes `ExportedVideo` as input. The `publish` should implement saving videos in any place where you want.

```kotlin
class VideoEditorKoinModule : FlowEditorModule() {
        ...
    /**
     * Publishes exported videos
     */
    val publishManager: BeanDefinition<PublishManager> = single(override = true) {
        CustomPublishManager(/* any necessary parameters */)
    }
}
```

