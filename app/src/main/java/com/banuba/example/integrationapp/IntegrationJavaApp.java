package com.banuba.example.integrationapp;

import android.app.Application;

import com.banuba.example.integrationapp.videoeditor.di.VideoEditorKoinModule;

import org.koin.core.context.GlobalContext;

import static org.koin.android.ext.koin.KoinExtKt.androidContext;
import static org.koin.core.context.ContextFunctionsKt.startKoin;

public class IntegrationJavaApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startKoin(new GlobalContext(), koinApplication -> {
            androidContext(koinApplication, this);
            koinApplication.modules(new VideoEditorKoinModule().getModule());
            return null;
        });
    }
}
