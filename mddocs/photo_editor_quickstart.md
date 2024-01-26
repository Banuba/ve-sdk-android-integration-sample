# Banuba AR Photo Editor Quickstart Guide
## Add dependencies
GitHub Packages is used for getting Android Video Editor SDK modules.

First, add repositories to your [project gradle](../build.gradle#L21) file in ```allprojects``` section.
```groovy
...

allprojects {
    repositories {
        ...

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Banuba/banuba-ve-sdk")
            credentials {
                username = "Banuba"
                password = "\u0038\u0036\u0032\u0037\u0063\u0035\u0031\u0030\u0033\u0034\u0032\u0063\u0061\u0033\u0065\u0061\u0031\u0032\u0034\u0064\u0065\u0066\u0039\u0062\u0034\u0030\u0063\u0063\u0037\u0039\u0038\u0063\u0038\u0038\u0066\u0034\u0031\u0032\u0061\u0038"
            }
        }

        ...
    }
}
```

Next, specify a list of dependencies in [app gradle](../app/build.gradle#L51) file.

```groovy
    def banubaPESdkVersion = '1.0.0'

    implementation "com.banuba.sdk:pe-sdk:${banubaPESdkVersion}"
```
## Launch
Create instance of ```BanubaVideoEditor```  by using the license token
``` kotlin
val videoEditorSDK = BanubaVideoEditor.initialize(LICENSE_TOKEN)
```

```kotlin

val photoEditorExportResult =
    registerForActivityResult(PhotoExportResultContract()) { exportResult ->
        Log.i(TAG, "Exported ${exportResult?.path}")
    }

photoEditorExportResult.launch(PhotoCreationActivity.startFromGallery(this))
```
