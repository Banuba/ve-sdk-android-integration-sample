# Banuba AI Video Editor SDK
## Timeline Editor screen styles

This screen is used to edit gif (sticker) and text effects. 

Almost all views are **the same** as in [Music Editor screen](music_editor_styles.md) and configured using the same theme attributes. The special attributes related to this screen directly:

- [timelineTextViewStyle](../app/src/main/res/values/themes.xml#L236)

    style for the TextView that represents the text effect on the timeline

- [object_editor_icon_text](../app/src/main/res/values/themes.xml#L237)

    theme attribute for the text effect drawable

- [object_editor_icon_sticker](../app/src/main/res/values/themes.xml#L238)

    theme attribute for the sticker effect drawable

- [object_editor_icon_edit](../app/src/main/res/values/themes.xml#L239)

    theme attribute for the button that opens text editor screen (it is disabled if you try to edit sticker, cause the sticker is only available to delete)
- [object_editor_icon_delete](../app/src/main/res/values/themes.xml#L240)

    theme attribute for the button that deletes selected effect

    ![img](screenshots/timeline1.png)

- [stickersSearchViewStyle](../app/src/main/res/values/themes.xml#L244)

    style for the SearchView while adding sticker effect. A lot of custom attributes are intended to customize search icon, hint color, cursor and other items. 
    <br />GIPHY doesn't charge for their content. The one thing they do require is attribution. Also, there is no commercial aspect to the current version of the product (no advertisements, etc.) We suggest to use **"Search GIPHY"** text as a search hint inside editor_search_hint attribute of this style. 
- [stickersRetryTitleTextStyle](../app/src/main/res/values/themes.xml#L245)

    style for the TextView that is shown in case of absent internet connection appearing during stickers searching
- [stickersRetryMessageTextStyle](../app/src/main/res/values/themes.xml#L246)

    style for the TextView that shows additional information for the user in case of lost network connection
- [stickersRetryBtnStyle](../app/src/main/res/values/themes.xml#L247)

    style for the button that is used to retry stickers loading

![img](screenshots/timeline2.png)


## String resources

Some string resources (i.e. button titles) are common with the music editor screen, so they can be found on the music editor screen configuration [**page**](music_editor_styles.md#L115).

| ResourceId        |      Value      |   Description |
| ------------- | :----------- | :------------- |
| stickers_empty_list | No stickers found | message that is shown while the user is searching [stickers](integration_customizations.md#configure-stickers-content) and has no result
| stickers_search_cancel | Cancel | text on the button on the right of the search view on [stickers](integration_customizations.md#configure-stickers-content) screen
| connection_view_title | Connection failed | message that is shown in case of network error while [stickers](integration_customizations.md#configure-stickers-content) are being loaded. The text appearance is defined in ```stickersRetryTitleTextStyle```
| connection_view_message | Please check connection\nand retry | hint that is shown in case of network error while [stickers](integration_customizations.md#configure-stickers-content) are being loaded. The text appearance is defined in ```stickersRetryMessageTextStyle```
| connection_view_retry | Retry | text on the button that is shown in case of network error while [stickers](integration_customizations.md#configure-stickers-content) are being loaded. It allows to resend the request for stickers. The text appearence is defined in ```stickersRetryBtnStyle```
| error_invalid_duration_object_effect | Min effect duration - %1$.1f sec | toast message that is shown when the user tries to make the object effect shorter than defined in `objectEffectMinDurationMs` parameter of the [**ObjectEditor**](config_object_editor.md) class
