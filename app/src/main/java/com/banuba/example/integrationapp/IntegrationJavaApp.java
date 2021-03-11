package com.banuba.example.integrationapp;

import android.app.Application;

import com.banuba.example.integrationapp.videoeditor.di.VideoEditorKoinModule;
import com.banuba.sdk.arcloud.di.ArCloudKoinModule;
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule;
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule;
import com.banuba.sdk.token.storage.TokenStorageKoinModule;

import org.koin.core.context.GlobalContext;

import static org.koin.android.ext.koin.KoinExtKt.androidContext;
import static org.koin.core.context.ContextFunctionsKt.startKoin;

public class IntegrationJavaApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startKoin(new GlobalContext(), koinApplication -> {
            androidContext(koinApplication, this);
            koinApplication.modules(
                    new VideoEditorKoinModule().getModule(),
                    new ArCloudKoinModule().getModule(),
                    new AudioBrowserKoinModule().getModule(), // use this module only if you bought it
                    new BanubaEffectPlayerKoinModule().getModule(),
                    new TokenStorageKoinModule().getModule());
            return null;
        });
    }
}
