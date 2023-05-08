package com.banuba.example.integrationapp

import android.app.Application
import android.util.Log
import com.banuba.sdk.token.storage.license.BanubaVideoEditor

class SampleApp : Application() {

    companion object {
        const val TAG = "BanubaVideoEditor"

        // Please set your license token for Banuba Video Editor SDK
        const val LICENSE_TOKEN = "ocoqjp9zm0Tn+sFnu4wKdDRKfEC8pkAjGbiOTzWJzkaZsh2Bt0IyMX7GHayeHqInma8/JFrzNLUKAnW0QijPG41nwZ3BY/KFV44weLeBzHMR0rMRfn60iDminhfGlOK5B0HtMqFZuZkH2V/BXSMbOXH2mQZASutPkc7ij5e7sTB2emQnRnfvMRvmIkDNGbonLoVZOecsnYSPIIyriz+NMWMMBV/kAs+jESjSmCWaoF3rDfQ21K70P9Bd/cQ8kVOqgCwIs/RshLuRhedn7Mjvs7ZQwVoLwGRY9uy87YBmiBQBV3D67qRvX7NG9DmsxeZrRDTudvK5uVgi/5vsE1ruFd1/u7X2+WdmNTAarkPZ40JcVmgg+EWTu2pytfN/RGwhHd0uqQK2jGLnr82ffA00K7c18ri2LPyeR147kqFQYf4/iVpHyxCSyLjecWpvAdimXkzArZBOHczQN54qqFhoiILh8RcgzM2r01YSa7Biplu55mZvWGPwej8zi6tR9zViOXgHV71/4FKaBa3f3y2Nd8PmQlkuLTcCetFnBT67DkPKKOaF+krKqiTWzE3z/0tPGYBJhTbYtCfeaMfLIihP+EUFYYtCedyzTSlAUKVgxfnOXmdksqBVDK8VwiOwKVxMCCvWvPldYX6Dj9ER1n01woO7kZp41ywJOpAUg/MJruUtMFQ3WE0qWgNCfDa3alTDX+LbWOTy0/hID6kIP8xsM6h2C7S8GdQlo004IM97P0UK9tzzV9/LaClFh1xb3VB4UZhgeIwX6Q6TZmHOn/yUczMjk8dBpjGMpYSSNfZYlJBDyIeMqzeUu9mUCWLR3/IXOd+HN9sTJLqLfEvvOpIupwOL7y5d4BZhWix/BaFWokhZRAoKc49luSK/7NA99FJaQw+9MRJbo91AO7vBrx10WXVj1optige3WAvkFRpxzwFYc+4YNu8jQi8XsN3peE671QcPpdOhxK0jrv/9oBUndwANujZFFMuB8/2f+xJw4817zfn737BebU/ujNa61b4GYjMISnjGch0ubCLoOL9QKPI49NEI8oEItRA2wOZz8owSz62OkBg4Qd55alwsZYL1d+9a3eX2vQ6R3ZyYRWdtKdRvqiBJjCWBwTlBU0B50KFfWWXR9i9ngSsf52UhcRyEEuhlZ3JGdoewtYHcjlu6U1NyTdD+hhhbqR675O4Q3gJMpEZW5WsI+KP+qyUQaprAqcLdd6QW3CU4qpOU65mrpdBYeYzfvV2Sw8OoRLYskOGyHT58KgxhekNUArD9kioSC+Aq568/y9QhVb8gehjDGbJcl6nyOLesVBVMBN/CFUBS16QjEKyxlU7xRDHNgj+D/XfwOt663eLUebph033oQNBXL+kiSqH3IcvMEsjFaC2uyxfDC67Hdo/bZID1jpQqlfQ+lydck5/jrHashoonaeknGaWBfOF6LX9kY3WOHs771GWJz9zOAfkyC9KB2Fhza8sFb5+1cO+6GTKp7/u+RvSJNE3jpBb98qAODGcb2LmPAxMgknuNWBW9ut070DgtUbP2OM//u+G7NJpHyYH5oFAajc2Yt55S4c7/cTOQ2w8+mFRGkCjHnEc1VNnACvrwpPZO79FGlGVHURFecgCEkg=="

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
