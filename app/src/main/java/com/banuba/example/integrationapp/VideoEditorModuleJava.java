package com.banuba.example.integrationapp;

import static org.koin.android.ext.koin.KoinExtKt.androidContext;
import static org.koin.core.context.DefaultContextExtKt.startKoin;

import android.app.Application;
import android.util.Log;

import com.banuba.sdk.arcloud.di.ArCloudKoinModule;
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule;
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule;
import com.banuba.sdk.export.di.VeExportKoinModule;
import com.banuba.sdk.gallery.di.GalleryKoinModule;
import com.banuba.sdk.playback.di.VePlaybackSdkKoinModule;
import com.banuba.sdk.ve.di.VeSdkKoinModule;
import com.banuba.sdk.ve.flow.di.VeFlowKoinModule;
import com.banuba.sdk.veui.di.VeUiSdkKoinModule;

public class VideoEditorModuleJava extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("VideoEditorModuleJava", "Koin Started");
        startKoin(koinApplication -> {
            androidContext(koinApplication, this);
            koinApplication.modules(
                    new VeSdkKoinModule().getModule(),
                    new VeExportKoinModule().getModule(),
                    new VePlaybackSdkKoinModule().getModule(),
                    new AudioBrowserKoinModule().getModule(),
                    new ArCloudKoinModule().getModule(),
                    new VeUiSdkKoinModule().getModule(),
                    new VeFlowKoinModule().getModule(),
                    new GalleryKoinModule().getModule(),
                    new BanubaEffectPlayerKoinModule().getModule()
//                    new SampleIntegrationKoinJavaModule().getModule()
            ).allowOverride(true);
            return null;
        });
    }
}

