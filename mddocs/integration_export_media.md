## Video Editor SDK export media guide
The Video Editor SDK exports recordings as .mp4 files. There are many ways you can customize this flow to better integrate it into your app.

To change export output, start with the ```ExportParamsProvider``` interface. It contains one method - ```provideExportParams()``` that returns ```List<ExportManager.Params>```. Each item on this list relates to one of the videos in the output and their configuration. Please check out [guide](configure_export_params.md) to configure ExportParams. See the example [here](app/src/main/java/com/banuba/example/integrationapp/videoeditor/export/IntegrationAppExportParamsProvider.kt).

The end result would be four files:

- Optimized video file (resolution will be calculated automatically);
- Same file as above but without a watermark;
- Low-res version of the watermarked file.

By default, they are placed in the "export" directory of external storage. To change the target folder, you should provide a custom Uri instance named **exportDir** through DI.

Should you choose to export files in the background, you’d do well to change ```ExportNotificationManager```. It lets you change the notifications for any export scenario (started, finished successfully, and failed).

:exclamation: If you set ```shouldClearSessionOnFinish``` in ```ExportFlowManager``` to true, you should clear ```VideoCreationActivity``` from backstack. Otherwise crash will be raised.

[Check example](../app/src/main/java/com/banuba/example/integrationapp/videoeditor/export/IntegrationAppExportParamsProvider.kt),
where multiple video files are exported: the first and the second with the most suitable quality params (defined by `sizeProvider.provideOptimalExportVideoSize()` method)
and the third with 360p quality (defined by using Video Editor SDK constant `VideoResolution.VGA360`).
