## Cover image guide

Cover image screen allows users to pick any frame of video as image or choose an image from gallery.  

```CoverProvider``` supports 2 modes for managing cover image screen in video editor. Default ```EXTENDED```
``` kotlin
 enum class CoverProvider {
    EXTENDED,   // enable cover screen
    NONE        // disable cover screen
}
```
You can change the mode in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L130)
``` kotlin
single<CoverProvider>(override = true) {
    CoverProvider.EXTENDED
}
```
 
### Used string resources
:exclamation: Important  
Some string resources are defined in the styles. To localize these strings firstly create string resources and setup them into styles under `android:text` attribute.

| ResourceId        |      Value      |
| ------------- | :----------- |
| cover_image_text | Choose cover | 
| cover_progress_text | Please, wait |
| err_cover_image | Failed to create cover image | 
