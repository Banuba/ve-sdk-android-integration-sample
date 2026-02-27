
[![](https://www.banuba.com/hubfs/Banuba_November2018/Images/Banuba%20SDK.png)](https://www.banuba.com/video-editor-sdk)
# Banuba Video Editor & Photo Editor SDKs - Android integration sample
<p align="center">
<img src="mddocs/gif/camera_preview.gif" alt="Screenshot" width="19%" height="auto" class="docs-screenshot"/>&nbsp;
<img src="mddocs/gif/camera_pip.gif" alt="Screenshot" width="19%" height="auto" class="docs-screenshot"/>&nbsp;
<img src="mddocs/gif/audio_browser.gif" alt="Screenshot" width="19%" height="auto" class="docs-screenshot"/>&nbsp;
<img src="mddocs/gif/background_separation.gif" alt="Screenshot" width="19%" height="auto" class="docs-screenshot"/>&nbsp;
</p>
<p align="center">
<img src="mddocs/gif/PE_UI_retouch.gif" alt="Screenshot" width="19%" height="auto" class="docs-screenshot"/>&nbsp;
<img src="mddocs/gif/PE_UI_make.gif" alt="Screenshot" width="19%" height="auto" class="docs-screenshot"/>&nbsp;
<img src="mddocs/gif/PE_UI_overlays.gif" alt="Screenshot" width="19%" height="auto" class="docs-screenshot"/>&nbsp;
<img src="mddocs/gif/PE_UI_backdrops.gif" alt="Screenshot" width="19%" height="auto" class="docs-screenshot"/>&nbsp;
</p>

## Overview
[Banuba Video Editor SDK](https://www.banuba.com/video-editor-sdk) allows you to quickly add short video functionality and possibly AR filters and effects into your mobile app.  

[Banuba AR Photo Editor SDK](https://www.banuba.com/photo-editor-sdk) allows you to quickly add the photo editing capabilities to your app.

## Usage
### License
Before you commit to a license, you are free to test all the features of the SDK for free.  
**The trial period lasts 14 days**.

Send us a message to start the [Video Editor SDK trial](https://www.banuba.com/video-editor-sdk#form) | [Photo Editor SDK trial](https://www.banuba.com/photo-editor-sdk#form). We will get back to you with the trial token.

### Quickstart Integration Guide
See the [Quickstart Guide](mddocs/quickstart.md) for complete integration steps, including:

1. Repository setup

2. License token [configuration](app/src/main/java/com/banuba/example/integrationapp/SampleApp.kt#L13)

3. Running the sample app


### Documentation
Explore the full capabilities of our SDKs:
- [Video Editor SDK](https://docs.banuba.com/ve-pe-sdk/docs/android/requirements-ve)
- [Photo Editor SDK](https://docs.banuba.com/ve-pe-sdk/docs/android/requirements-pe)

### Support
For questions about Video Editor or Photo Editor SDK, reach out to Banuba support service
- [Video Editor SDK](https://www.banuba.com/faq/kb-tickets/new) 
- [Photo Editor SDK](https://www.banuba.com/support)

### Requirements
- Kotlin 2.2+ or Java 17
- Android OS 8.0 or higher with Camera 2 API
- OpenGL ES 3.0 (3.1 for Neural networks on GPU)
- :white_check_mark: arm64-v8a , :white_check_mark: armv7, :exclamation: x86-64 limited support, :x: x86 - no support

### Supported media formats
| Audio                                  | Video      | Images      |
|----------------------------------------| ---------  | ----------- |
| .aac, .mp3, .wav,<br>.ogg, .m4a, .flac |.mp4, .mov | .jpg, .gif, .heic, .png,<br>.nef, .cr2, .jpeg, .raf, .bmp|