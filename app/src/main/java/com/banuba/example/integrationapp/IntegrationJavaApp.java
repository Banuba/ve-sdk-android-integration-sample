package com.banuba.example.integrationapp;

import static org.koin.android.ext.koin.KoinExtKt.androidContext;
import static org.koin.core.context.DefaultContextExtKt.startKoin;

import android.app.Application;
import android.util.Log;

import com.banuba.example.integrationapp.videoeditor.di.IntegrationKoinModule;
import com.banuba.sdk.arcloud.di.ArCloudKoinModule;
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule;
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule;
import com.banuba.sdk.export.di.VeExportKoinModule;
import com.banuba.sdk.gallery.di.GalleryKoinModule;
import com.banuba.sdk.playback.di.VePlaybackSdkKoinModule;
import com.banuba.sdk.token.storage.di.TokenStorageKoinModule;
import com.banuba.sdk.token.storage.license.BanubaVideoEditor;
import com.banuba.sdk.ve.di.VeSdkKoinModule;
import com.banuba.sdk.ve.flow.di.VeFlowKoinModule;
import com.banuba.sdk.veui.di.VeUiSdkKoinModule;

public class IntegrationJavaApp extends Application {

    public static final String TAG = "BanubaVideoEditor";
    // Please set your license token for Banuba Video Editor SDK
    public static final String LICENSE_TOKEN = "YOUR LICENSE TOKEN";

    public static final String ERR_SDK_NOT_INITIALIZED = "Banuba Video Editor SDK is not initialized: license token is unknown or incorrect.\nPlease check your license token or contact Banuba";
    public static final String ERR_LICENSE_REVOKED = "License is revoked or expired. Please contact Banuba https://www.banuba.com/faq/kb-tickets/new";

    public BanubaVideoEditor videoEditor;
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Banuba Video Editor SDK
        videoEditor = BanubaVideoEditor.Companion.initialize(LICENSE_TOKEN);
        if (videoEditor == null) {
            // Token you provided is not correct - empty or truncated
            Log.e(TAG, ERR_SDK_NOT_INITIALIZED);
        } else {
            initVideoEditorDependencies();
        }
    }

    private void initVideoEditorDependencies() {
        startKoin(koinApplication -> {
            androidContext(koinApplication, this);
            // pass the customized Koin module that implements required dependencies. Keep order of modules
            koinApplication.modules(
                    new VeSdkKoinModule().getModule(),
                    new VeExportKoinModule().getModule(),
                    new VePlaybackSdkKoinModule().getModule(),
                    new AudioBrowserKoinModule().getModule(), // use this module only if you bought it
                    new ArCloudKoinModule().getModule(),
                    new TokenStorageKoinModule().getModule(),
                    new VeUiSdkKoinModule().getModule(),
                    new VeFlowKoinModule().getModule(),
                    new GalleryKoinModule().getModule(),
                    new BanubaEffectPlayerKoinModule().getModule(),
                    new IntegrationKoinModule().getModule()
            ).allowOverride(true);
            return null;
        });
    }
}
