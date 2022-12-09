package com.android_package_installer

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
//import io.flutter.embedding.android.FlutterView.FlutterEngineAttachmentListener
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
//import io.flutter.plugin.common.ActivityLifecycleListener
//import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.FileInputStream
import java.io.IOException

/** Plugin */
class Plugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private val packageName = "com.android_package_installer"
    private val PACKAGE_INSTALLED_ACTION = "${packageName}.content.SESSION_API_PACKAGE_INSTALLED"
    private val LOG_TAG = "${packageName}.logger"
    private val INSTALL_STATUS_CODE_UNKNOWN = -2
//    private val CHANNEL1 = "${packageName}.event_channel"

    private lateinit var channel: MethodChannel
    private lateinit var packageManager: PackageManager
    private lateinit var appContext: Context
    private var activity: Activity? = null
    private lateinit var callResult: Result

    @Throws(IOException::class)
    private fun loadAPKFile(apkPath: String, session: PackageInstaller.Session) {
        session.openWrite("package", 0, -1).use { packageInSession ->
            FileInputStream(apkPath).use { `is` ->
                val buffer = ByteArray(16384)
                var n: Int
                var o = 1
                while (`is`.read(buffer).also { n = it } >= 0) {
                    packageInSession.write(buffer, 0, n)
                    o++
                }
            }
        }
    }

    private fun installPackage(apkPath: String) {
        var session: PackageInstaller.Session? = null
        try {
            val packageInstaller: PackageInstaller = activity!!.packageManager.packageInstaller
            val params = PackageInstaller.SessionParams(
                PackageInstaller.SessionParams.MODE_FULL_INSTALL
            )
            val sessionId = packageInstaller.createSession(params)
            session = packageInstaller.openSession(sessionId)

            loadAPKFile(apkPath, session)

            val intent = Intent(appContext, activity!!.javaClass)
            intent.action = PACKAGE_INSTALLED_ACTION
            val pendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0)
            val statusReceiver = pendingIntent.intentSender

            session.commit(statusReceiver)
            session.close()
        } catch (e: IOException) {
            throw RuntimeException("Couldn't install package", e)
        } catch (e: RuntimeException) {
            session!!.abandon()
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        this.channel = MethodChannel(flutterPluginBinding.binaryMessenger, "android_package_installer")
        this.appContext = flutterPluginBinding.applicationContext
        this.channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        callResult = result
        when (call.method) {
            "installApk" -> {
                installPackage(call.arguments.toString())
            }

            else -> {
                callResult.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        appContext = binding.activity.applicationContext
        binding.addOnNewIntentListener(fun(intent: Intent?): Boolean {
            val extras = intent?.extras
            if (intent != null) {
                if (PACKAGE_INSTALLED_ACTION == intent.action) {
                    val status = extras!!.getInt(PackageInstaller.EXTRA_STATUS)
                    when (status) {
                        PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                            var confirmIntent = extras[Intent.EXTRA_INTENT] as Intent
                            confirmIntent =
                                confirmIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            activity!!.startActivity(confirmIntent)
                        }

                        else -> {
                            callResult.success(status)
                        }
                    }
                }
            }
            return true
        })
    }

    override fun onDetachedFromActivityForConfigChanges() {
        channel.setMethodCallHandler(null)
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }
}
