# Banuba VideoEditor SDK
## Alert Dialog styles

### In progress...

There are following types of user notification used in Banuba Video Editor SDK:
- toast
- wait dialog (represents long running tasks)
- alert with control buttons

All of them are customized by theme attributes described in this file.

- [toastStyle](app/src/main/res/values/themes.xml#L198)

    style to customize toast messages

    ![img](screenshots/alert1.png)

- [throbberViewStyle](app/src/main/res/values/themes.xml#L42)

    style for the circle progress bar. It is allowed to customize gradient colors using custom attribute

- [waitDialogTextStyle](app/src/main/res/values/themes.xml#L43)

    style for the TextView that shows a description

- [wait_dialog_bg](app/src/main/res/values/themes.xml#L199)

    theme attribute that configures the background of progress dialog

- [wait_dialog_throbber_bg](app/src/main/res/values/themes.xml#L200)

    theme attribut that configures the background of circle progress bar within progress dialog

    ![img](screenshots/alert2.png)

Recently to customize alert dialogs with control buttons you should override fragment_dialog_alert.xml file in layout resource folder. For correct implemetation all views' ids should be the same as in the original file:

- alertTitleText - for dialog description
- alertPositiveButton - for the button meaning positive answer from the user (right button)
- alertNegativeButton - for the button meaning negative answer from the user (left button)

After overriding layout file you can use your any of your own styles to customize these views.

![img](screenshots/alert3.png)

Every alert dialog has its own type within SDK. It allows to customize descriptions and text over the positive and negative buttons. Also there is a possibility to add custom icon for every alert dialog type. To setup drawables for this reason they should be passed into custom theme attributes:

- [alert_camera_delete_video](app/src/main/res/values/themes.xml#L50)
- [alert_camera_return_to_editor](app/src/main/res/values/themes.xml#L51)
- [alert_camera_return_to_trimmer](app/src/main/res/values/themes.xml#L52)
- [alert_camera_save](app/src/main/res/values/themes.xml#L53)
- [alert_camera_reset_all](app/src/main/res/values/themes.xml#L54)
- [alert_editor_delete_voice_record](app/src/main/res/values/themes.xml#L55)
- [alert_editor_reset_all](app/src/main/res/values/themes.xml#L56)
- [alert_editor_reset_effects](app/src/main/res/values/themes.xml#L57)
