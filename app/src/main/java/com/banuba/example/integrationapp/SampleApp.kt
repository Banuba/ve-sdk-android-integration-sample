package com.banuba.example.integrationapp

import android.app.Application
import android.util.Log
import com.banuba.sdk.token.storage.license.BanubaVideoEditor

class SampleApp : Application() {

    companion object {
        const val TAG = "BanubaVideoEditor"

        // Please set your license token for Banuba Video Editor SDK
        const val LICENSE_TOKEN = "tnQOsopaUGPZK/OuZsy3UzRKfEC8pkAjGbiOTzWJ3kacmUvI9F8BMWTEALGaCd9uo+AOJlujd/4eTDjiTQjuVKNp2bWXQ+iSB9stbuC5zHMI2qYrR324gCiinl3Iz+S0HHvhI5lYuMhJhQ6eCy0DD0O05AJATPZUwZGkgY+E8gtIe2onXXa4ZUmLbl3aF6EcDLtoOfE879ffZ8m0iyS8MFkua0f9DKi+FQLomiab7BO4SeB1mJzuOtZBtpoPs3PtgCw5uPhejf7GlPtm6JCksbl82B5GgURSsaWv7YBCgy4ReHPg+Z5+ea0NqXyhw7A3FUiTAbHl8jAR1afQLmraafBdkJPHxxxecmgXniCu2XRodlsGxCnv0FtOiuBqUAl0Ec1K4VTqjGi+9t2UIE5CfechqeLnXZj8XBc79uJaf8Q/gEN9zQuAyL/QckwiSea2PkyK6rlbAczmL9V+sBVBnpXhkFdt98mm9XcPfrZpsFu55mhcbiWYPQ0EsIBu1yR0JxVoMKZN0WOLQvKJ7A2wW8fySytQQDwmR+ZrN3n7SXr/HcHU5Cv2tnaJr2zn6EprErJtuECG/wzQfcaqYXh41GBUKM1MYsO3bRVbV6cljKmgGzVEjo9/WuZTzTCsNVwqOTLE+bANDCLEsvEXz2wJ5Ji1kZhvhyIHeaEBg+8Cv8k2K1BmIB0lXAdCUXD+PGPVR+jbJaa+w/9TDJAheaI8Or5mOpmxG8clsht7cMN+OH8f35myENfNcQJBjUZ7kRgoWIN8YKwwok3DamTJpem9NnJkkdJOtjWCubuLHbEA06Bazpm2kSupmvLTJmzfi8MhJcDxaNhFNqGeNh6MK5gRqxXHoGJfwC8BG2FcD61ntXxIeiUrN6ZtkCqfy9V3wHl1N2XJHVEU5OFLMqPBrx1aRUthwapCpVa5QE/jFxNd2UYTPMkrCr1gEj0WjsrAPwD4g1lYpoP1ua4kt+DgjQ86YSES/WoGAcaU6eGUqgp+tddvwamIjqZMcFrvisO97Lg4FE9mHCaRe15sKjz8OqRgPuM/0McY9JwVqREV9c8j/JRf2LyXgEl9DMNPdV4mWrqkGaFf2qu/+li+hNvMDBZ6N4YlvnBeoCWC1z9RUU9oyLFEXnWA7jcjlDUZ52UhcRyEEuhlZ3J0Rbub1M6K2BPtUBAyHMap2kpHrhq4yfs39gdev0Ru5WYUu+ul/W1HadmA+NSKK/YJ3hQtnJ+nvdCr6/kcLJ/frh7Vn4L3Be5u4aD6RWANdFInPQoXEb/z12pWQvM09vB7iZFiFftiFEaGWc0byA=="

        const val ERR_SDK_NOT_INITIALIZED = "Banuba Video Editor SDK is not initialized: license token is unknown or incorrect.\nPlease check your license token or contact Banuba"
        const val ERR_LICENSE_REVOKED = "License is revoked or expired. Please contact Banuba https://www.banuba.com/faq/kb-tickets/new"
    }

    var videoEditor: BanubaVideoEditor? = null

    override fun onCreate() {
        super.onCreate()

        // Initialize Banuba Video Editor SDK
        videoEditor = BanubaVideoEditor.initialize(LICENSE_TOKEN)

        if (videoEditor == null) {
            // Token you provided is not correct - empty or truncated
            Log.e(TAG, ERR_SDK_NOT_INITIALIZED)
        } else {
            VideoEditorModule().initialize(this@SampleApp)
        }
    }
}
