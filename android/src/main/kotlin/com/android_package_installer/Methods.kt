package com.android_package_installer

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

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
                    val apkFilePath = call.arguments.toString()
                    installer.installPackage(apkFilePath)
                } catch (e: Exception) {
                    resultSuccess(installStatusUnknown)
                }
            }
            else -> {
                nothing()
            }
        }
    }
}
