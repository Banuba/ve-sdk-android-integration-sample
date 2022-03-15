package com.banuba.example.integrationapp;

import static org.koin.android.ext.koin.KoinExtKt.androidContext;
import static org.koin.core.context.GlobalContextExtKt.startKoin;

import android.app.Application;

import com.banuba.example.integrationapp.videoeditor.di.IntegrationKoinModule;
import com.banuba.sdk.arcloud.di.ArCloudKoinModule;
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule;
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule;
import com.banuba.sdk.export.di.VeExportKoinModule;
import com.banuba.sdk.gallery.di.GalleryKoinModule;
import com.banuba.sdk.playback.di.VePlaybackSdkKoinModule;
import com.banuba.sdk.token.storage.di.TokenStorageKoinModule;
import com.banuba.sdk.ve.di.VeSdkKoinModule;
import com.banuba.sdk.ve.flow.di.VeFlowKoinModule;

import org.koin.core.context.GlobalContext;

public class IntegrationJavaApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startKoin(GlobalContext.INSTANCE, koinApplication -> {
            androidContext(koinApplication, this);
            // pass the customized Koin module that implements required dependencies. Keep order of modules
            koinApplication.modules(
                    new VeSdkKoinModule().getModule(),
                    new VeExportKoinModule().getModule(),
                    new VePlaybackSdkKoinModule().getModule(),
                    new AudioBrowserKoinModule().getModule(), // use this module only if you bought it
                    new ArCloudKoinModule().getModule(),
                    new TokenStorageKoinModule().getModule(),
                    new VeFlowKoinModule().getModule(),
                    new IntegrationKoinModule().getModule(),
                    new GalleryKoinModule().getModule(),
                    new BanubaEffectPlayerKoinModule().getModule()
            );
            return null;
        });
    }
}
