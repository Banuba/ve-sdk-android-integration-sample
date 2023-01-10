## Advanced topics

- [Video Editor SDK size](#Video-Editor-SDK-size)
- [Supported media formats](#Supported-media-formats)
- [Camera recording video quality params](#Camera-recording-video-quality-params)
- [Connecting with AR cloud](#Connecting-with-AR-cloud)
- [Export video quality params](#Export-video-quality-params)

## Video Editor SDK size

If you want to use the Video Editor SDK for a short video app like TikTok, the [Face AR module](https://www.banuba.com/facear-sdk/face-filters) would be useful for you, as it allows you to add masks and other AR effects. If you just need the video editing-related features, the AI Video Editor SDK can work on its own.

| Options | Mb      | Note |
| -------- | --------- | ----- |
| :white_check_mark: Face AR SDK  | 37.3 | AR effect sizes are not included. AR effect takes 1-3 MB in average.
| :x: Face AR SDK | 15.5  | no AR effects  |  

You can either include the filters in the app or have users download them from the [AR cloud](#Configure-AR-cloud) to decrease the app size.

## Supported media formats
| Audio      | Video      | Images      |
| ---------- | ---------  | ----------- |
|.aac, .mp3, .wav<br>.ogg, .m4a, .flac |.mp4, .mov | .jpg, .gif, .heic, .png,<br>.nef, .cr2, .jpeg, .raf, .bmp

## Camera recording video quality params
| Recording speed | 360p(360 x 640) | 480p(480 x 854) | HD(720 x 1280) | FHD(1080 x 1920) |
| --------------- | --------------- | --------------- | -------------- | ---------------- |
| 1x(Default)     | 1200            | 2000            | 4000           | 6400             |
| 0.5x            | 900             | 1500            | 3000           | 4800             |
| 2x              | 1800            | 3000            | 6000           | 9600             |
| 3x              | 2400            | 4000            | 8000           | 12800            |  

## Connecting with AR cloud

To decrease the app size, you can connect with our servers and pull AR filters from there. The effects will be downloaded whenever a user needs them. Please check out [step-by-step guide](ar_cloud.md) to configure AR Cloud in the Video Editor SDK.

## Export video quality params
Video Editor SDK classifies every device by its performance capabilities and uses the most suitable quality params for the exported video.

Nevertheless it is possible to customize it with `ExportParamsProvider` interface. Just put a required video quality into `ExportManager.Params.Builder` constructor. Check out an [**example**](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/java/com/banuba/example/integrationapp/videoeditor/export/IntegrationAppExportParamsProvider.kt), where multiple video files are exported: the first and the second with the most suitable quality params (defined by `sizeProvider.provideOptimalExportVideoSize()` method) and the third with 360p quality (defined by using an Video Editor SDK constant `VideoResolution.VGA360`).

See the **default bitrate (kb/s)** for exported video (without audio) in the table below:
| 360p(360 x 640) | 480p(480 x 854) | HD(720 x 1280) | FHD(1080 x 1920) |
| --------------- | --------------- | -------------- | ---------------- |
|             1200|             2000|            4000|              6400|
