# Banuba AI Video Editor SDK
## ObjectEditorConfig

**ObjectEditorConfig** class allows to customize text/gif editor screen behavior.

| Property | Available values | Description |
| ------------- | :------------: | :------------- |
| **objectMaxVisibleTimelineCount** | Number > 0 | for the count of *visible* rows to add object effects to
| **objectInitialTimelineCount** | Number > 0 | for the maximum count of rows to add object effects to
| **objectEffectDefaultDuration** | Number > 0 | default duration of the object effect which is added to the timeline
| **objectEffectMinDurationMs** | Number > 0 | for the minimum available object effect duration
| **showObjectEffectsTogether** | true/false | if gif and text object effects are managed on the single screen
| **objectEditorVibrateActionDurationMs** | Number > 0 | configures vibration pattern for object effect drag action