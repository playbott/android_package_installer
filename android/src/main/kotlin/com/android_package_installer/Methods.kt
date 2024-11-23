package com.android_package_installer

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import android.util.Log

internal class MethodCallHandler(private val installer: Installer) : MethodChannel.MethodCallHandler {
    companion object {
        lateinit var callResult: MethodChannel.Result
        fun resultSuccess(data: Any) {
            callResult.success(data)
        }

        fun nothing() {
            callResult.notImplemented()
        }

        /*
        fun resultError(s0: String, s1: String, o: Any) {
            callResult.error(s0, s1, o)
        }
        */
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        callResult = result
        when (call.method) {
            "installApk" -> {
                try {
                    val apkFilePaths = call.arguments.toString().split(",").map { it.trim() }.toTypedArray()
                    installer.installPackage(apkFilePaths)
                } catch (e: Exception) {
                    Log.e("E0", e.toString())
                    resultSuccess(installStatusUnknown)
                }
            }
            else -> {
                nothing()
            }
        }
    }
}
