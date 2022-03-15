# Banuba AI Video Editor SDK
## MubertApiConfig

**MubertApiConfig** class allows to customize audio browser network requests.

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **generatedTrackDurationSec** | Number > 0 | duration that applied for generated tracks in seconds
| **generateTrackBitrate** | any of the following values: 32, 96, 128, 192, 256, 320 | sound quality measured in kbps
| **generatedTrackIntencity** |  any of the following values: low, medium, high | instrumental saturation (number of stems) for generated tracks
| **generatedTrackFormat** |  any of the following values: mp3, wav, flac | format of generated tracks
| **generatedTracksAmount** | Number > 0 | amount of tracks to generate for selected category