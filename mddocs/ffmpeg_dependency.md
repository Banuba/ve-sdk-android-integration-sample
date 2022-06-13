# How to use different FFmpeg build

## If you want to use your custom build of the FFmpeg libraries, follow the next steps:

### Step 1

Remove the com.banuba.sdk:ffmpeg dependency
```groovy
implementation "com.banuba.sdk:ffmpeg:$VERSION"
```

### Step 2

Add your own FFmpeg dependency. For example:
```groovy
implementation 'com.arthenica:ffmpeg-kit-full:$VERSION'
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

:exclamation: Your version of libraries should be the same as com.banuba.sdk:ffmpeg dependency version. Also your build should include the mp3lame library.