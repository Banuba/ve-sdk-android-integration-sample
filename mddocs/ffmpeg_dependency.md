# How to use different FFmpeg build

:exclamation: Banuba VE SDK uses FFmpeg 4.4.\
**It is highly required to use the same version of your custom FFmpeg dependency to keep VE SDK stable.**\
**Additionally, your dependency must include mp3blame library.**

## Please follow these guidelines to integrate custom FFmpeg dependency in your app:

### Step 1

Remove Banuba FFmpeg dependency
```groovy
implementation "com.banuba.sdk:ffmpeg:4.4"
```

### Step 2

Add your custom FFmpeg dependency in app/build.gradle. For example:
```groovy
implementation 'com.arthenica:ffmpeg-kit-full:4.4.LTS'
```

### Step 3

Add the following code in your Activity to check whether everything is correct:

```kotlin
com.banuba.sdk.ve.processing.FFmpeg(context = this).execute(emptyArray()).run {
    waitFor()
    Log.d("FFmpeg", errorStream.reader().readText())
}
```
The output should contains information about version of FFmpeg libraries

Here is an example of the PR sample for this steps: [PR sample](https://github.com/Banuba/ve-sdk-android-integration-sample/pull/187)
