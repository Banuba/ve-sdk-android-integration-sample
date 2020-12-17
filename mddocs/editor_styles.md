# Banuba VideoEditor SDK
## Editor screen styles

- [editorOverlayStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L60)

    style defines back icon drawable, playback icon visibility, several editor screen appearance parameters, also setups icons used for voice recording (they can be omitted if music editor feature is enabled)
- [editorActionsStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L61)

    style **setups drawables** for all action buttons **on the editor screen** (for instance, [editor_act_icon_masks_off](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L421) and [editor_act_icon_masks_on](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L422) setup drawables for an icon that related to AR masks applied to video)
- [editorActionButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L62)

    style defines how action buttons look like (size, description, margins, etc.). It is applied to every action button on the editor screen
- [editorBackButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L63)

    style for the button that is used to return the user on the previous screen
- [editorNextButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L64)

    style for the button that is used to proceed with current video

    ![img](screenshots/editor1.png)
- [editorTextActionsStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L65)

    style setups drawables for text editor options (alignment, background)
- [editorTextColorLabelStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L66)

    style for appearance of text label for colors applicable in text editor
- [editorTextEditorInputStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L68)

    style for the view used in text editor to create text effect. It should be mentioned that background color, typeface and text color are customized by the user while creating text effect, so you should avoid to setup these attributes in this style. Also input view allows autosizing
- [editorTypefaceSwitcherStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L69)

    style for the view that is used to switch typeface in text editor 
- [editorTextDoneStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L72)

    style for the button on the text editor that applies created text effect to the video. It can be the same as editorDoneStyle if you do not need to make them different

    ![img](screenshots/editor2.png)
- [editorCancelStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L70)

    style for the button that is used in case of editing visual, time and color effects to cancel the last added effect or return from editing effect to editor screen
- [editorDoneStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L71)

    style for the button that is used in case of editing visual, time and color effects to apply effects to the video. It should be mentioned that the same button for the text effects is defined by editorTextDoneStyle
- [editorUndoButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L74)

    style for the button that is used to remove the last added effect on visual or time effects editor
- [editorVisualAndTimeEffectsRecyclerStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L75)

    style for the RecyclerView containing visual or time effects
- [editorApplyEffectHintStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L78)

    style for the TextView that shows a hint on visual/time effects editor
- [editorTimelineStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L86)

    style for the timeline that represents effects applied on the video in case of editing visual or time effects

- [actionableEffectTitleStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L38)

    style is used to set text appearance for "actionable" effects (applied by long pressing) title. This type of effects includes: 
    - visual effects on editor screen
    - time effects on editor screen
    - AR masks on editor screen
    
    The other effects type - "checkable" - defined in [camera styles](camera_styles.md#L54)

- [editorEffectVisualStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L79)

    style for the every item in the visual effects list

    ![img](screenshots/editor3.png)
- [editorEffectTimeStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L80)

    style for the every item in the time effects list
- [editorEffectMaskStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L81)

    style for the every item in the AR mask effects list
- [editorEffectLutItemStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L82)

    style for the every item in the color filters list
- [editorColorEffectsLabelStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L83)

    style for the color filter list label. By default the label is not visible so this style can be omitted. This style is similar to [cameraEffectsLabelStyle](camera_styles.md#L35) for camera screen
- [editorColorEffectsRecyclerStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L84)

    style applied to RecyclerView containing color filters. This style is similar to [cameraEffectsRecyclerStyle](camera_styles.md#L41) for camera screen
- [editorBoardStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L73)

    style for the custom view that holds all visual effects on the editor screen. This view handles different touch actions (drag, zoom in or out) on effects. It has a bulk of custom attributes that setup its appearance depends on user action


## Editor string resources

| ResourceId        |      Value      |   Description |
| ------------- | :----------- | :------------- |
| control_description_stickers | Stickers | title of the action button that opens stickers
| control_description_text_effects | Text | title of the action button that opens text effects editor
| control_description_visual_effects | VHS | title of the action button that opens visual effects editor
| control_description_mask_effects | Mask | title of the action button that opens AR masks
| control_description_music_effects | Music | title of the action button that opens an audio content
| control_description_time_effects | Time | title of the action button that opens time effects editor
| control_description_color_effects | Luts | title of the action button that opens color filters
| control_description_interactions | Interactions | title of the action button that opens interaction effects editor (feature recently under development)
| control_description_link | Link | title of the action button that applies link to the video (feature recently under development)
| editor_next | Next | text on the button defined in ```editorNextButtonStyle```. Also used on the [trimmer screen](trimmer_styles.md#L10) for the view defined in ```trimmerNextButtonStyle```
| editor_done | Done | text on the button defined in ```editorTextDoneStyle```. Also used on the [cover screen](cover_styles.md#L4) for the view defined in ```coverNextButtonStyle```
| editor_cancel | Cancel | text that is used on the [trimmer screen](trimmer_styles.md#L50) for the view defined in ```trimmerCancelButtonStyle``` and on the [cover screen](cover_styles.md#L7) for the view defined in ```coverCancelButtonStyle```
| editor_exporting | Exporting | text that is shown on the [throbber](alert_styles.md#L25) when export process was started. The text appearance is customized by the ```waitDialogTextStyle```
| editor_effect_hint | Press and hold | hint that is shown on visual effects editor telling the user how the effect can be applied. The view itself is customized in ```editorApplyEffectHintStyle```
| editor_effect_undo | Undo | text on the button that allows to remove recently added effect. View is defined in ```editorUndoButtonStyle```
| stickers_empty_list | No stickers found | message that is shown while the user is searching [stickers](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-stickers-content) and has no result
| stickers_search_hint | Search giphy.com | hint shown on the search view on the screen that provides [stickers](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-stickers-content)
| stickers_search_cancel | Cancel | text on the button on the right of the search view on [stickers](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-stickers-content) screen
| font_regular_title | Regular | title of the normal text appearance for the text effects. The view is defined in ```editorTypefaceSwitcherStyle```
| font_bold_title | Bold | title of the bold text appearance for the text effects. The view is defined in ```editorTypefaceSwitcherStyle```
| font_italic_title | Italic | title of the italic text appearance for the text effects. The view is defined in ```editorTypefaceSwitcherStyle```
| connection_view_title | Connection failed | message that is shown in case of network error while [stickers](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-stickers-content) are being loaded
| connection_view_message | Please check connection\nand retry | hint that is shown in case of network error while [stickers](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-stickers-content) are being loaded
| connection_view_retry | Retry | text on the button that is shown in case of network error while [stickers](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-stickers-content) are being loaded. It allows to resend the request for stickers
| err_file_not_supported | File is not supported | message shown as a [toast](alert_styles.md#L11) when the user is trying to use any media file (select from gallery or select as a cover) that is not supported by the SDK
| err_editor_network_connection_failure | Connection failed | message shown as a [toast](alert_styles.md#L11) in case of the user is trying to load more [stickers](https://github.com/Banuba/ve-sdk-android-integration-sample#configure-stickers-content) but the request is failed
| err_editor_player_initializer | Error on player launch | message shown as a [toast](alert_styles.md#L11) in case of internal error within the SDK