## Video Editor UI SDK advanced integration guide

- [Video Editor SDK size](#Video-Editor-SDK-size)
- [Supported media formats](#Supported-media-formats)
- [Video recording quality details](#Video-recording-quality-details)

## Video Editor SDK size

| Options | Mb      | Note |
| -------- | --------- | ----- |
| :white_check_mark: Face AR SDK  | 37.3 | AR effect sizes are not included. AR effect takes 1-3 MB in average.
| :x: Face AR SDK | 15.5  | no AR effects  |  

You can either include the AR filters in the app or have users download them from the [AR cloud](#Configure-AR-cloud) to dramatically decrease the app size.

## Supported media formats
| Audio                                  | Video      | Images      |
|----------------------------------------| ---------  | ----------- |
| .aac, .mp3, .wav,<br>.ogg, .m4a, .flac |.mp4, .mov | .jpg, .gif, .heic, .png,<br>.nef, .cr2, .jpeg, .raf, .bmp

## Video recording quality details
| Recording speed | 360p(360 x 640) | 480p(480 x 854) | HD(720 x 1280) | FHD(1080 x 1920) |
| --------------- | --------------- | --------------- | -------------- | ---------------- |
| 1x(Default)     | 1200            | 2000            | 4000           | 6400             |
| 0.5x            | 900             | 1500            | 3000           | 4800             |
| 2x              | 1800            | 3000            | 6000           | 9600             |
| 3x              | 2400            | 4000            | 8000           | 12800            |  


