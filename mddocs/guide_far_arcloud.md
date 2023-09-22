# Face AR and AR Cloud integration guide

- [Overview](#Overview)
- [Manage effects](#Manage-effects)
- [Integrate AR Cloud](#Integrate-AR-Cloud)
- [Change effects order](#Change-effects-order)
- [Disable Face AR SDK](#Disable-Face-AR-SDK)
- [Integrate Beauty effect](#Integrate-Beauty-effect)
- [Integrate Background effect](#Integrate-Background-effect)

[Banuba Face AR SDK](https://www.banuba.com/facear-sdk/face-filters) product is used on camera and editor screens for applying various AR effects while making video content.  

## Overview
Any Face AR effect is a normal folder that includes a number of files required for Face AR SDK to play this effect. 

:exclamation: Important    
Make sure you included ```preview.png``` file in effect folder. This file is used as a preview for AR effect.

## Manage effects
There are 2 options for managing AR effects:
1. Android ```assets```  
   Use [assets/bnb-resources/effects](../app/src/main/assets/bnb-resources/effects) folder 
2. ```AR Cloud```  
   Effects are stored on the remote server. 

:exclamation: Recommendation  
You can use both options i.e. store just a few AR effects in ```assets``` and 100 or more AR effects  on ```AR Cloud```.

## Integrate AR Cloud
```AR Cloud``` is a cloud solution for storing Banuba Face AR effect on the server and used by Face AR and Video Editor products.  
Any AR effect downloaded from ```AR Cloud``` is cached on the user's device.

Follow next steps to integrate ```AR Cloud``` into your project.  
First, add Gradle ```com.banuba.sdk:ar-cloud``` dependency in [app gradle file](/app/build.gradle).  

```diff
    def banubaSdkVersion = '1.30.2'
    ...
+   implementation "com.banuba.sdk:ar-cloud:${banubaSdkVersion}"
    ...
```

Next, add ```ArCloudKoinModule``` module to [Koin modules](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L67).
```diff
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

Next, override ```ArEffectsRepositoryProvider``` in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt#L120).

```kotlin
   single<ArEffectsRepositoryProvider>(createdAtStart = true) {
      ArEffectsRepositoryProvider(
         arEffectsRepository = get(named("backendArEffectsRepository")),
         ioDispatcher = get(named("ioDispatcher"))
      )
    }
```

## Change effects order
By default, all AR effects are listed in alphabetical order. AR effects from ``assets``` are listed in the begining.

Create new class ```CustomMaskOrderProvider``` and implement ```OrderProvider``` to provide custom order.

```kotlin
class CustomMaskOrderProvider : OrderProvider {
    override fun provide(): List<String> = listOf("Background", "HeadphoneMusic")
}
```
:exclamation: Important  
These are names of specific directories located in ```assets/bnb-resources/effects``` or on ```AR Cloud```.  

Next, use ```CustomMaskOrderProvider``` in [VideoEditorModule](../app/src/main/java/com/banuba/example/integrationapp/VideoEditorModule.kt)
```kotlin
single<OrderProvider>(named("maskOrderProvider")) {
   CustomMaskOrderProvider()
}
```

## Disable Face AR SDK
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

## Integrate Beauty effect
Video Editor SDK has built in integration with beautification effect - [Beauty](../app/src/main/assets/bnb-resources/effects/Beauty).
The user interacts with ```Beauty``` effect by clicking on specific button on camera screen.  

:exclamation: Important  
```Beauty``` is not available in the list of all AR effects. It is required to store the effect in ```assets``` and keep name ```Beauty``` with no changes.    
Please move this effect while integrating Video Editor SDK into your project.

## Integrate Background effect

[Background](../app/src/main/assets/bnb-resources/effects/Background) effect allows to apply various images or videos as a background while recording video content on the camera screen.  
The AR effect requires Face AR and can be added to your license.  
Please request this feature from Banuba business representatives.

First, add ```Background``` effect either to ```assets``` or  ```AR Cloud```.
