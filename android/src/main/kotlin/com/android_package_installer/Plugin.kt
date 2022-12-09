package com.android_package_installer

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodChannel

const val packageName = "com.android_package_installer"
const val installStatusUnknown = -2
var packageInstalledAction = "${packageName}.content.SESSION_API_PACKAGE_INSTALLED"

/** Plugin */
class Plugin : FlutterPlugin, ActivityAware {
    private lateinit var installer: Installer
    private lateinit var channel: MethodChannel
    private val channelName: String = "android_package_installer"

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, channelName)
        installer = Installer(flutterPluginBinding.applicationContext, null)
        val handler = MethodCallHandler(installer)
        channel.setMethodCallHandler(handler)
    }

    override fun onAttachedToActivity(activityPluginBinding: ActivityPluginBinding) {
        installer.setActivity(activityPluginBinding.activity)
        activityPluginBinding.addOnNewIntentListener(OnNewIntentListener(activityPluginBinding.activity))
    }

    override fun onDetachedFromEngine(flutterPluginBinding: FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onDetachedFromActivity() {
        installer.setActivity(null)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        channel.setMethodCallHandler(null)
    }

    override fun onReattachedToActivityForConfigChanges(activityPluginBinding: ActivityPluginBinding) {
    }
}

