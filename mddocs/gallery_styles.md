# Banuba VideoEditor SDK
## Gallery screen styles

- [galleryImageViewStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L90)

    style defines the overview of gallery icon on the camera screen. By default the last media resource is used as gallery icon drawable. It can be changed by switching "use_custom_image" attribute to "true" and setup custom drawable as a background resource in this style
- [galleryTitleTextStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L91)

    style for the gallery screen title. For pictures gallery the recent album's title is used
- [galleryChoiceButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L92)

    style for the button that switches multi selection
- [galleryBackButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L93)

    style for the button placed on the top left corner of the screen. This button has different drawables depends on the current gallery state and has different behaviour:
    - ["back"](gallery_styles.md#L50) to return to the previous screen
    - ["cross"](gallery_styles.md#L51) to cancel multi selection mode
- [galleryNextButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L94)

    style for the button that appears in case of multi selection mode to proceed with selected resources
- [galleryItemRadioButtonStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L95)

    style for the radio button that is used to define selected resource. It has a bulk of custom attributes to customize internal colors 
- [galleryItemTextStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L96)

    style for the TextView that shows duration for video resources

    ![img](screenshots/gallery1.png)

- [galleryEmptyTextStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L97)

    style for the title and description views that are shown in case of empty gallery
- [galleryAlbumBlurViewStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L98)

    style for the custom view that is used to apply blur effect as a background for albums list. It can have custom tint via "overlay_color" attribute
- [galleryAlbumTitleTextStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L99)

    style for the album title
- [galleryAlbumDescTextStyle](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L100)

    style for the album description

    ![img](screenshots/gallery2.png)

Besides concrete styles there are a lot of theme attributes that allows to configure gallery screen:
- [galleryColumnsNumber](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L102) - setup how much columns the gallery screen shows
- [gallery_bg_color](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L104) - background color for gallery screen
- [gallery_item_corner_radius](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L105) - setup corner radius for every gallery item
- [gallery_item_margin](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L106) - setup margins between items in gallery (applied in all directions)
- [gallery_icon_back](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L107) - for drawable in case of "back" button returns to the previous screen
- [gallery_icon_cross](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L108) - for drawable in case of "back" button cancels selected resources
- [gallery_album_divider_color](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/res/values/themes.xml#L109) - divider color in the albums list

## Gallery string resources

| ResourceId        |      Value      |   Description |
| ------------- | :----------- | :------------- |
| gallery_next | Choose | text on the button that opens the editor screen with selected videos or images. Defined in ```galleryNextButtonStyle```
| gallery_album_untitled | untitled | the title of the album that has no name (it may be created in case of some videos or images has no album name in theirs meta data)
| gallery_album_all_photos | All photos | the title of the album that contains all image resources
| gallery_album_all_video | All videos | the title of the album that contains all video resources
| gallery_empty_image_list | No images found | message that is shown in the middle of the gallery screen in case of there are no images found on device. The view is defined in ```galleryEmptyTextStyle```
| gallery_empty_video_list | No videos found | message that is shown in the middle of the gallery screen in case of there are no videos found on device. The view is defined in ```galleryEmptyTextStyle```
| gallery_choose_at_least | Select at least %1$d files | **plurals resource** that is used for the [toast](alert_styles.md#L11) message that is shown when the user want to open the editor screen but the number of selected resources (videos or images) is less than predefined custom value (recently "1" is used). This predefined value is a placeholder in every plural string resource here
| err_gallery_video_creation | Failed to create video | message shown as a [toast](alert_styles.md#L11) in case of error that apperas during slideshow video creation from selected image resources