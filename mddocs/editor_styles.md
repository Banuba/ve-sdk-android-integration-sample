# Banuba VideoEditor SDK
## Editor screen styles

- [editorOverlayStyle](app/src/main/res/values/themes.xml#L60)

    style defines back icon drawable, playback icon visibility, several editor screen appearance parameters, also setups icons used for voice recording (they can be omitted if music editor feature is enabled)
- [editorActionsStyle](app/src/main/res/values/themes.xml#L61)

    style setups drawables for all action buttons on the editor screen
- [editorActionButtonStyle](app/src/main/res/values/themes.xml#L62)

    style defines how action buttons look like (size, description, margins, etc.). It is applied for every action button on the editor screen
- [editorBackButtonStyle](app/src/main/res/values/themes.xml#L63)

    style for the button that is used to return the user on the previous screen
- [editorNextButtonStyle](app/src/main/res/values/themes.xml#L64)

    style for the button that is used to proceed with current video

    ![img](screenshots/editor1.png)
- [editorTextActionsStyle](app/src/main/res/values/themes.xml#L65)

    style setups drawables for text editor options (alignment, background)
- [editorTextColorLabelStyle](app/src/main/res/values/themes.xml#L66)

    style for appearance of text label for colors applicable in text editor
- [editorTextEditorInputStyle](app/src/main/res/values/themes.xml#L68)

    style for the view used in text editor to create text effect. It should be mentioned that background color, typeface and text color are customized by the user while creating text effect, so you should avoid to setup these attributes in this style. Also input view allows autosizing
- [editorTypefaceSwitcherStyle](app/src/main/res/values/themes.xml#L69)

    style for the view that is used to switch typeface in text editor 
- [editorTextDoneStyle](app/src/main/res/values/themes.xml#L72)

    style for the button on the text editor that applies created text effect to the video. It can be the same as editorDoneStyle if you do not need to make them different

    ![img](screenshots/editor2.png)
- [editorCancelStyle](app/src/main/res/values/themes.xml#L70)

    style for the button that is used in case of editing visual, time and color effects to cancel the last added effect or return from editing effect to editor screen
- [editorDoneStyle](app/src/main/res/values/themes.xml#L71)

    style for the button that is used in case of editing visual, time and color effects to apply effects to the video. It should be mentioned that the same button for the text effects is defined by editorTextDoneStyle
- [editorUndoButtonStyle](app/src/main/res/values/themes.xml#L74)

    style for the button that is used to remove the last added effect on visual or time effects editor
- [editorVisualAndTimeEffectsRecyclerStyle](app/src/main/res/values/themes.xml#L75)

    style for the RecyclerView containing visual or time effects
- [editorApplyEffectHintStyle](app/src/main/res/values/themes.xml#L78)

    style for the TextView that shows a hint on visual/time effects editor
- [editorTimelineStyle](app/src/main/res/values/themes.xml#L86)

    style for the timeline that represents effects applied on the video in case of editing visual or time effects

- [actionableEffectTitleStyle](app/src/main/res/values/themes.xml#L38)

    style is used to set text appearance for "actionable" effects (applied by long pressing) title. This type of effects includes: 
    - visual effects on editor screen
    - time effects on editor screen
    - AR masks on editor screen
    
    The other effects type - "checkable" - defined in [camera styles](camera_styles.md#L54)

- [editorEffectVisualStyle](app/src/main/res/values/themes.xml#L79)

    style for the every item in the visual effects list

    ![img](screenshots/editor3.png)
- [editorEffectTimeStyle](app/src/main/res/values/themes.xml#L80)

    style for the every item in the time effects list
- [editorEffectMaskStyle](app/src/main/res/values/themes.xml#L81)

    style for the every item in the AR mask effects list
- [editorEffectLutItemStyle](app/src/main/res/values/themes.xml#L82)

    style for the every item in the color filters list
- [editorColorEffectsLabelStyle](app/src/main/res/values/themes.xml#L83)

    style for the color filter list label. By default the label is not visible so this style can be omitted. This style is similar to [cameraEffectsLabelStyle](camera_styles.md#L35) for camera screen
- [editorColorEffectsRecyclerStyle](app/src/main/res/values/themes.xml#L84)

    style applied for RecyclerView containing color filters. This style is similar to [cameraEffectsRecyclerStyle](camera_styles.md#L41) for camera screen
- [editorBoardStyle](app/src/main/res/values/themes.xml#L73)

    style for the custom view that holds all visual effects on the editor screen. This view handles different touch actions (drag, zoom in or out) on effects. It has a bulk of custom attributes that setup its appearance depends on user action
