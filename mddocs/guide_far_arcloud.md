## Face AR and AR Cloud integration guide

[Banuba Face AR SDK](https://www.banuba.com/facear-sdk/face-filters) product is used on camera and editor screens for applying various AR effects while making video content.  

Any Face AR effect is a normal folder that includes a number of files required for Face AR SDK to play this effect. 

:exclamation: Important    
Make sure you included ```preview.png``` file in effect folder. This file is used as a preview for AR effect.

### Manage effects
There are 2 options for managing AR effects:
1. Android ```assets```  
   Use [assets/bnb-resources/effects](../app/src/main/assets/bnb-resources/effects) folder 
2. ```AR Cloud```  
   Effects are stored on the remote server. 

:exclamation: Recommendation  
You can use both options i.e. store just a few AR effects in ```assets``` and 100 or more AR effects  on ```AR Cloud```.

### AR Cloud integration
```AR Cloud``` is a cloud solution for storing Banuba Face AR effect on the server and used by Face AR and Video Editor products.  
Any AR effect downloaded from ```AR Cloud``` is cached on the user's device.

Follow next steps to integrate ```AR Cloud``` into your project.  
First, add Gradle ```com.banuba.sdk:ar-cloud``` dependency in [app gradle file](/app/build.gradle).  

```groovy
    def banubaSdkVersion = '1.26.3'
   ...
  + implementation "com.banuba.sdk:ar-cloud:${banubaSdkVersion}"
    ...
```

Next, add ```ArCloudKoinModule``` module to [Koin modules](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L72).
```kotlin
startKoin {
   ...
   
   modules(
      VeSdkKoinModule().module,
      ...
+     ArCloudKoinModule().module,
      ...
   )
}
```

Next, override ```ArEffectsRepositoryProvider``` in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L132).

```kotlin
   single<ArEffectsRepositoryProvider>(createdAtStart = true) {
      ArEffectsRepositoryProvider(
         arEffectsRepository = get(named("backendArEffectsRepository")),
         ioDispatcher = get(named("ioDispatcher"))
      )
    }
```

### Change effects order
By default, all AR effects are listed in alphabetical order. AR effects from ``assets``` are listed in the begining.

Create class [CustomMaskOrderProvider](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L289) and implement ```OrderProvider```.

```kotlin
class CustomMaskOrderProvider : OrderProvider {
    override fun provide(): List<String> = listOf("Background", "HeadphoneMusic")
}
```
:exclamation: Important  
These are names of specific directories located in ```assets/bnb-resources/effects``` or on ```AR Cloud```.  

Next, use ```CustomMaskOrderProvider``` in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L157)
```kotlin
single<OrderProvider>(named("maskOrderProvider")) {
   CustomMaskOrderProvider()
}
```


### Disable Face AR SDK
Video Editor SDK can work without Face AR SDK. 

Remove ```BanubaEffectPlayerKoinModule().module``` from [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt)
```diff
startKoin {
    androidContext(this@IntegrationApp)    
    modules(
        ...
-       BanubaEffectPlayerKoinModule().module
    )
}
```
and remove Gradle dependency ```com.banuba.sdk:effect-player-adapter```
```diff
    ...
-   implementation "com.banuba.sdk:effect-player-adapter:${banubaSdkVersion}"
    ...
```

### Beauty effect integration
Video Editor SDK has built in integration with beautification effect - [Beauty](../app/src/main/assets/bnb-resources/effects/Beauty).
The user interacts with ```Beauty``` effect by clicking on specific button on camera screen.  

:exclamation: Important  
```Beauty``` is not available in the list of all AR effects. It is required to store the effect in ```assets``` and keep name ```Beauty``` with no changes.    
Please move this effect while integrating Video Editor SDK into your project.

### Background effect integration

[Background](../app/src/main/assets/bnb-resources/effects/Background) effect allows to apply various images or videos as a background while recording video content on the camera screen.  
The AR effect requires Face AR and can be added to your license.  
Please request this feature from Banuba business representatives.

First, add ```Background``` effect either to ```assets``` or  ```AR Cloud```.

Next, customize [Android styles](../app/src/main/res/values/themes.xml#L1306).

- **backgroundEffectContainerStyle** - style for the background of the main UI container
- **backgroundEffectHintStyle** - style for the hint on the top of the main UI container
- **backgroundEffectEmptyViewStyle** - style for the view that is shown in case of no media files found
- **backgroundEffectPermissionsBtnStyle** - style for the button that requests premission to device storage
- **backgroundEffectGalleryListStyle** - style for the media files container
- **backgroundEffectGalleryBtnStyle** - style for the button to the right of the media files list that opens the gallery screen to choose the file out there
- **backgroundEffectGalleryThumbStyle** - style for the media file item presented in the files list
- **backgroundEffectGalleryThumbTextStyle** - style for the text over the media file item (duration of the video files is shown here)
- **backgroundEffectGalleryThumbProgressStyle** - style for the small circle progress bar shown while media file is loading into files list
- **backgroundEffectGalleryBorderedImageViewStyle** - style for the media item containig some custom attributes that holds selection animation logic (i.e. scaling of selected item)

![img](screenshots/15_1_Background1.png)

![img](screenshots/15_1_Background2.png)

Finally, customize String resources

| ResourceId        |      Value      |   Description |
| ------------- | :----------- | :------------- |
| background_effect_empty_view | No media found | text shown in case no media found on the device for background
| background_effect_permission_btn | Allow Access | label on the button that request access to device storage for the media files
| background_effect_list_hint | Select media to change the background: | hint over the list of media files
| background_effect_list_permission | Allow access to Gallery to change the background | hint shown in case of not granted premission to access device storage
| background_effect_invalid_file | Damaged file | toast message that is shown if the user selects not allowed media file for background