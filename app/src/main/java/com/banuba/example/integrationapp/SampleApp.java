package com.banuba.example.integrationapp;

import static org.koin.core.context.DefaultContextExtKt.stopKoin;
import android.util.Log;

import com.banuba.sdk.core.EditorUtilityManager;
import com.banuba.sdk.core.license.BanubaVideoEditor;
import com.banuba.sdk.pe.BanubaPhotoEditor;
import org.koin.java.KoinJavaComponent;

public class SampleApp extends VideoEditorModuleJava {

    public static final String TAG = "BanubaSdk";

    // Please set your license token for Banuba Video Editor SDK or Photo Editor SDK
    private static final String LICENSE_TOKEN = SET YOUR LICENCE TOKEN;

    public static final String ERR_SDK_NOT_INITIALIZED =
            "Banuba Video Editor SDK or Photo Editor SDK is not initialized: license token is unknown or incorrect.\nPlease check your license token or contact Banuba";
    public static final String ERR_LICENSE_REVOKED =
            "License is revoked or expired. Please contact Banuba https://www.banuba.com/faq/kb-tickets/new";

    // Manages Video Editor SDK
    public BanubaVideoEditor videoEditor = null;

    // Manages Photo Editor SDK
    public BanubaPhotoEditor photoEditor = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // Prepare Video Editor in the beginning for simplicity
        prepareVideoEditor();
    }

    public void prepareVideoEditor() {
        // Video and Photo SDK share license management system.
        // It is required to keep only one instance.

        if (photoEditor != null) {
            BanubaPhotoEditor.Companion.release();
            photoEditor = null;
        }

        if (videoEditor == null) {
            Log.d(TAG, "Prepare Video Editor SDK");

            // Initialize Banuba Video Editor SDK
            videoEditor = BanubaVideoEditor.Companion.initialize(LICENSE_TOKEN);

            if (videoEditor == null) {
                // Token you provided is not correct - empty or truncated
                Log.e(TAG, ERR_SDK_NOT_INITIALIZED);
            }
        }
    }

    public void preparePhotoEditor() {
        // Video and Photo SDK share license management system.
        // It is required to keep only one instance.

        if (videoEditor != null) {
            releaseVideoEditor();
        }

        Log.d(TAG, "Prepare Photo Editor SDK");
        // Photo Editor SDK auto releases its resources
        photoEditor = BanubaPhotoEditor.Companion.initialize(LICENSE_TOKEN);
    }

    // Call it to release VE SDK before opening PE
    private void releaseVideoEditor() {
        Log.d(TAG, "Release Video Editor SDK");
        releaseUtilityManager();
        stopKoin();
        videoEditor = null;
    }

    private void releaseUtilityManager() {
        EditorUtilityManager utilityManager = KoinJavaComponent.get(EditorUtilityManager.class);

        // EditorUtilityManager is NULL when the token is expired or revoked.
        // This dependency is not explicitly created in DI.
        if (utilityManager != null) {
            utilityManager.release();
        } else {
            Log.w(TAG, "EditorUtilityManager was not initialized or is null!");
        }
    }
}
