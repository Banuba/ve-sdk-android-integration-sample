# API for using token from Firebase in the AI Video Editor SDK

Banuba token can be stored in Firebase

### Step 1

Please add snapshot `banubaToken` in your [Firebase Realtime Database](https://firebase.google.com/docs/database) with token provided by Banuba representatives.

### Step 2

Add following dependency to top-level build.gradle
```groovy
 classpath "com.google.gms:google-services:${googleServicesVersion}"
```

### Step 3

Add `com.google.gms.google-services` plugin to app module

```groovy
plugins {
    id 'com.android.application'
    id 'com.google.gms.google-services'
    ...
}
```

### Step 4

Configure dependencies in DI layer.

```kotlin
class VideoEditorKoinModule {

    val module = module {
    ...
        single {
            TokenType.FIREBASE
        }

        single(named("firebaseVeSdkTargetUrl")) {
            "Your firebase database URL"
        }
    ...
    }
}
```

### Note:
To verify that token is ready, you can use `VideoEditorLicenceUtils.isSupportsVeSdk` before launch `VideoCreationActivity`. If method returns true - you can safely launch video editor, otherwise exception is raised.
We recommend to follow [this guide](../README.md#integration) to finish the setup.
