# API for using AR cloud in the SDK

Banuba token can be stored in Firebase

### Step 1

Add following dependency to top-level build.gradle
```groovy
 classpath "com.google.gms:google-services:${googleServicesVersion}"
```

### Step 2

Add `com.google.gms.google-services` plugin to app module

```groovy
plugins {
    id 'com.android.application'
    id 'com.google.gms.google-services'
    ...
}
```

### Step 3

Configure dependencies in DI layer.

```kotlin
class VideoEditorKoinModule : FlowEditorModule() {

    ...

   val firebaseTargetUrl: BeanDefinition<String> = single(named("firebaseVeSdkTargetUrl"), override = true) {
        "Your firebase URL"
    }
}
```

### Note:
In firebase, field should be named as `banubaToken`.
To verify that token is ready, you can use `VideoEditorSDK.isAvailable()` before launch `VideoCreationActivity`. If method returns true - you can safely launch video editor, otherwise exception is raised.
