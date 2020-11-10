# Banuba VideoEditor SDK
## Camera screen styles  

- [cameraOverlayStyle](app/src/main/res/values/themes.xml#L22) 

    style is intended to setup action button icons, customize relative position and appearance of music, gallery, switch camera icons. [Example](app/src/main/res/values/themes.xml#L199)  
- [cameraActionButtonStyle](app/src/main/res/values/themes.xml#L23)

    style defines action buttons appearance. [Example](app/src/main/res/values/themes.xml#L269)
- [cameraMusicTopIconStyle](app/src/main/res/values/themes.xml#L24)

    special style that defines appearance of music icon in case it is placed on the top of the camera screen (if the music icon is placed on the left side the cameraActionButtonStyle is applied to it, so this attribute can be omitted). [Example](app/src/main/res/values/themes.xml#L266)
- [cameraBackButtonStyle](app/src/main/res/values/themes.xml#L25)

    style defines "back" button appearance. [Example](app/src/main/res/values/themes.xml#L272)
- [cameraNextButtonStyle](app/src/main/res/values/themes.xml#L26)

    style defines "next" button appearance. [Example](app/src/main/res/values/themes.xml#L278)
- [cameraRemoveLastPieceButtonStyle](app/src/main/res/values/themes.xml#L27)

    style defines an appearance for the button that is used to delete the last recorded video chunk from the timeline. [Example](app/src/main/res/values/themes.xml#L312)
- [cameraRecorderTimelineStyle](app/src/main/res/values/themes.xml#L29)

    style is intented to customize the timeline. [Example](app/src/main/res/values/themes.xml#L315)

    ![img](screenshots/camera1.png)
- [cameraEffectsLabelStyle](app/src/main/res/values/themes.xml#L30)

    style for visual effects (color filters and AR masks) list label. By default the label is not visible so this style can be omitted. [Example](app/src/main/res/values/themes.xml#L284)
- [cameraEffectsItemStyle](app/src/main/res/values/themes.xml#L31)

    style applied for every item within visual effects list. [Example](app/src/main/res/values/themes.xml#L287)
- [cameraEffectsRecyclerStyle](app/src/main/res/values/themes.xml#L32)

    style applied for RecyclerView containing visual effects list. [Example](app/src/main/res/values/themes.xml#L290)
- [cameraSpeedPickerLabelStyle](app/src/main/res/values/themes.xml#L33)

    style is applied to the recording speed options label. By default the label is not visible so this style can be omitted. [Example](app/src/main/res/values/themes.xml#L293)
- [cameraSpeedPickerViewStyle](app/src/main/res/values/themes.xml#L34)

    style is applied to custom recording speed options container. [Example](app/src/main/res/values/themes.xml#L296)
- [cameraSpeedPickerItemStyle](app/src/main/res/values/themes.xml#L35)

    style is applied to TextView representing every recording speed option within container. [Example](app/src/main/res/values/themes.xml#L302)

    ![img](screenshots/camera2.png)