## Video Editor SDK integration troubleshooting

### FFmpeg build issue (Error compressed Native Libs)
Below are the steps to resolve the issue while building the project.
1. Add the ```android.bundle.enableUncompressedNativeLibs=false``` in the ```gradle.properties```

``` properties
android.bundle.enableUncompressedNativeLibs=false
```

2. Add ```android:extractNativeLibs="true"``` in ```AndroidManifest.xml``` file
``` xml
<application
    ...
    android:extractNativeLibs="true"
    ...
>
```

More issues explained in [FAQ](faq.md)