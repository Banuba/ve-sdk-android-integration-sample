# Banuba AI Video Editor SDK
## Trimmer screen styles

- [trimmerStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L116)

    style to customize trimmer view with custom attributes. There are a lot of properties can be changed from its default color to left and right pointers drawables
- [trimmerBackButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L117)

    style for the button that returns the user to the previous screen
- [trimmerNextButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L118)

    style for the button that starts trimming process and proceeds to the editor screen. In case of single video it called "Done" and starts trimming, in case of multi videos it called "Done" or "Next" depending on the current action:
    - applies new time borders for the selected video and return to multi videos screen
    - proceeds with all changes to the editor screen
- [trimmerDurationSumTextStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L119)

    style for the top label that shows the final duration of selected videos after trimming process. Visibility of this view is defined in custom attribute - [trimmer_time_visible](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L138)

- [trimmerContentViewStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L120)

    style for the view that plays a role of a container for the trimmer view or the view containing list of selected videos

- [trimmerContentLabelStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L121)

    style for the label of trimmer view (or list of videos) container

- [trimmerRotateButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L130)

    style for the rotate button

- [trimmer_icon_play](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L134) and [trimmer_icon_pause](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L135)

    attributes for different playback icon states. Visibility of the playback icon itself is defined in [trimmer_play_control_visible](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L133) attribute

    ![img](screenshots/trimmer1.png)
- [trimmerRecyclerStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L122)

    style for the RecyclerView containing the list of selected videos

- [trimmerThumbImageStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L123)

    style for the video item image within trimmer RecyclerView

- [trimmerThumbHintIconStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L131)

    style for the top right icon on the video thumbnail

- [trimmerThumbTextStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L124)

    style for the video item duration within trimmer RecyclerView

- [trimmerDeleteLabelStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L125)

    style for the text view that appears when the user dragging video from the videos list. The video should be moved over the "delete" image to be deleted

- [trimmerDeleteCrossStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L126)

    style for the image view that appears when the user dragging video from the videos list. The video should be moved over the "delete" image to be deleted 

- [trimmerContentHintStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L129)

    style for the TextView describing available action on the screen

- [trimmer_icon_add_video](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L136)

    attribute for the drawable resource representing a button that allows to select additional videos from the gallery. Also there is a [trimmer_bg_color_add_video](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L137) attribute to define the background color of this icon

    ![img](screenshots/trimmer2.png)

- [trimmerCancelButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L125)

    style for the button that cancels recent changes

- [trimmerDurationTimelineStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L126)

    style for the view that represents the whole video after trimming process. This view is shown only when user is changing time borders for the video from videos list. There are a lot of custom attributes allowing to setup colors for different parts of the timeline

    ![img](screenshots/trimmer3.png)


## String resources

| ResourceId        |      Value      |   Description |
| ------------- | :----------- | :------------- |
| trimmer_drag_hint | Drag to delete | hint that is shown to the user when he is long pressing on the video thumbnail in multitrimmer mode. Related TextView is customized in ```trimmerDeleteLabelStyle```
| trimmer_duration_format_normal | %1$s sec | label that is shown on the top of the trimmer screen, represents the duration of the single video (if in trimmer mode) or of all selected videos (if in multitrimmer mode). Realated TextView is customized in ```trimmerDurationSumTextStyle```
| trimmer_duration_format_exceed | Max length %1$s sec | label that is shown on the top of the trimmer screen in case of the sum of all selected videos exceeds the ```maxVideoDuration``` parameter in [videoeditor.json](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/videoeditor.json#L4) file. Realated TextView is customized in ```trimmerDurationSumTextStyle```
| trimmer_content_label_single | Trimmer | label of the trimmer when selected one video. Related TextView is customized in ```trimmerContentLabelStyle```
| trimmer_content_label_multi | Multitrimmer | label of the trimmer when selected several videos. Related TextView is customized in ```trimmerContentLabelStyle```
| trimmer_content_hint | Use long press to change the order or remove | message shown on the multitrim inside the view customized by the `trimmerContentHintStyle`
| err_trimmer_invalid_duration | Video duration should be between %1$d and %2$d seconds | error message shown as a [toast](alert_styles.md#L11). Here placeholders are values from ```minVideoDuration``` and ```maxVideoDuration``` parameters in [videoeditor.json](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/videoeditor.json#L3) file
| err_trimmer_internal | Error while making video | error message shown as a [toast](alert_styles.md#L11) if an undefined exception appeared during trimming video