# FAQ  
This page is aimed to explain the most frequent technical questions asked while integrating our SDK.  

### 1. I want to start and stop video recording by short click.  
The user has to keep pressing recording button to record new video by default. Video recording stops when the user releases finger from recording button.  

Please set [takePhotoOnTap](https://github.com/Banuba/ve-sdk-android-integration-sample/blob/main/app/src/main/assets/camera.json#4) property to **false** to allow the user to start and stop recording new video by short click.  
``` json
 "takePhotoOnTap":false
```

### 2. I want to add AR Mask to the Video Editor (without AR Cloud backend)

Technically AR Mask is a bulk of files within the folder.

You should place AR Mask folder to the **assets/bnb-resources/effects** directory inside the module containing video editor SDK ([**Example**](https://github.com/Banuba/ve-sdk-android-integration-sample/tree/main/app/src/main/assets/bnb-resources/effects)). Be sure that AR mask directory has a **preview.png** file. It is used as an icon of the AR mask in the app.

**Note** that the name of directory will be used as a title of the AR mask within the app.


