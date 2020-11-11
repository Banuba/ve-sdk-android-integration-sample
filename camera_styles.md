# Banuba VideoEditor SDK
## Camera screen styles  

- [cameraOverlayStyle](app/src/main/res/values/themes.xml#L24)

    style is intended to setup action button icons, customize relative position and appearance of music, gallery, switch camera icons  
- [cameraActionButtonStyle](app/src/main/res/values/themes.xml#L25)

    style defines action buttons appearance
- [cameraMusicTopIconStyle](app/src/main/res/values/themes.xml#L26)

    special style that defines appearance of music icon in case it is placed on the top of the camera screen (if the music icon is placed on the left side the cameraActionButtonStyle is applied to it, so this attribute can be omitted)
- [cameraBackButtonStyle](app/src/main/res/values/themes.xml#L27)

    style defines "back" button appearance
- [cameraNextButtonStyle](app/src/main/res/values/themes.xml#L28)

    style defines "next" button appearance
- [cameraRemoveLastPieceButtonStyle](app/src/main/res/values/themes.xml#L29)

    style defines an appearance for the button that is used to delete the last recorded video chunk from the timeline
- [cameraRecorderTimelineStyle](app/src/main/res/values/themes.xml#L31)

    style is intented to customize the timeline

- [videoTimelineCheckedStyle](app/src/main/res/values/themes.xml#L18)

    style for the selected bottom tab of video creation mode

- [videoTimelineUncheckedStyle](app/src/main/res/values/themes.xml#L19)

    style for the unselected bottom tab of video creation mode

    ![img](screenshots/camera1.png)
- [cameraEffectsLabelStyle](app/src/main/res/values/themes.xml#L32)

    style for visual effects (color filters and AR masks) list label. By default the label is not visible so this style can be omitted
- [cameraEffectsItemStyle](app/src/main/res/values/themes.xml#L33)

    style applied for every item within visual effects list
- [cameraEffectsRecyclerStyle](app/src/main/res/values/themes.xml#L34)

    style applied for RecyclerView containing visual effects list
- [cameraSpeedPickerLabelStyle](app/src/main/res/values/themes.xml#L35)

    style is applied to the recording speed options label. By default the label is not visible so this style can be omitted
- [cameraSpeedPickerViewStyle](app/src/main/res/values/themes.xml#L36)

    style is applied to custom recording speed options container
- [cameraSpeedPickerItemStyle](app/src/main/res/values/themes.xml#L37)

    style is applied to TextView representing every recording speed option within container

- [checkableEffectTitleStyle](app/src/main/res/values/themes.xml#L39)

    style is used to set text appearance for "checkable" effects (applied/removed by selection) title. This type of effects includes: 
    - color filters on camera screen
    - AR masks on camera screen
    - color filters on editor screen
    
    The other effects type - "actionable" - defined in [editor styles](editor_styles.md#L57)

    ![img](screenshots/camera2.png)