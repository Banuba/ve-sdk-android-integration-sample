# Banuba VideoEditor SDK
## Cover screen styles

- [extendedCoverParentStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L234)

    style for the root Constraint layout that represents cover screen
- [extendedCoverNextButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L235)
    
    style for the button that proceeds the user to the export
- [extendedCoverCancelButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L236)

    style for the button that return the user to the editor screen
- [extendedCoverTitleStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L238)

    style for the label in the top middle of the screen
- [extendedCoverDescriptionStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L239)

    style for the hint placed below the preview

- [extendedCoverThumbnailPickerStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L240)

    style for the view that is used to pick a thumbnail as the video cover

- [extendedCoverExternalPhotoChooseStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L243)

    style for the text button allowing the user to choose the cover image from the gallery

    ![img](screenshots/cover1.png)

- [extendedCoverPreviewParentStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L246)

    style for the Constraint layout containing the recent cover preview

- [extendedCoverExternalPhotoStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L248)

    style for the ImageView that shows the cover image

- [extendedCoverExternalPhotoDeleteBackgroundStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L250)

    style for the view at the bottom of the preview that plays a role of the background of "delete" button

- [extendedCoverExternalPhotoDeleteStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L253)

    style for the text button for deletion of the chosen cover image

![img](screenshots/cover2.png)

## String resources

**Pay attention** that some strings on the cover screen are defined in the styles. To localize these strings firstly create string resources and setup them into styles under `android:text` attribute.

| ResourceId        |      Value      |   Description |
| ------------- | :----------- | :------------- |
| cover_image_text | Choose cover | label over the progress bar that allows to choose a cover from video frames. The view is customized in ```coverDescriptionStyle```
| cover_progress_text | Please, wait | text that is shown on the [throbber](alert_styles.md#L25) when the process of extracting cover image was started. The text appearance is customized by the ```waitDialogTextStyle```
| err_cover_image | Failed to create cover image | message shown as a [toast](alert_styles.md#L11) in case of error happened during the process of extracting cover image