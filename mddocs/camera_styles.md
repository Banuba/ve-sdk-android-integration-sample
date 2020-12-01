# Banuba VideoEditor SDK
## Camera screen styles  

- [cameraOverlayStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L24)

    style is intended to setup action button icons, customize relative position and appearance of music, gallery, switch camera icons  
- [cameraActionButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L25)

    style defines action buttons appearance
- [cameraMusicTopIconStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L26)

    special style that defines appearance of music icon in case it is placed on the top of the camera screen (if the music icon is placed on the left side the cameraActionButtonStyle is applied to it, so this attribute can be omitted)
- [cameraBackButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L27)

    style defines "back" button appearance
- [cameraNextButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L28)

    style defines "next" button appearance
- [cameraRemoveLastPieceButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L29)

    style defines an appearance of the button that is used to delete the last recorded video chunk from the timeline
- [cameraRecorderTimelineStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L31)

    style is intended to customize the timeline

- [videoTimelineCheckedStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L18)

    style for the selected bottom tab of video creation mode

- [videoTimelineUncheckedStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L19)

    style for the unselected bottom tab of video creation mode

    ![img](screenshots/camera1.png)
- [cameraEffectsLabelStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L32)

    style of the label, which appears at the top of the visual effects menu (color filters and AR masks). By default the label is not visible so this style can be omitted
- [cameraEffectsItemStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L33)

    style applied to every item within visual effects list
- [cameraEffectsRecyclerStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L34)

    style applied to RecyclerView containing visual effects list
- [cameraSpeedPickerLabelStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L35)

    style is applied to the recording speed options label. By default the label is not visible so this style can be omitted
- [cameraSpeedPickerViewStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L36)

    style is applied to custom recording speed options container
- [cameraSpeedPickerItemStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L37)

    style is applied to TextView representing every recording speed option within container

- [checkableEffectTitleStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L39)

    style is used to set text appearance of "checkable" effects (applied/removed by selection) title. This type of effects includes:
    - color filters on camera screen
    - AR masks on camera screen
    - color filters on editor screen
    
    The other effects type - "actionable" - defined in [editor styles](editor_styles.md#L57)

    ![img](screenshots/camera2.png)