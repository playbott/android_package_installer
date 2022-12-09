package com.android_package_installer

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.os.Build
import java.io.FileInputStream
import java.io.IOException

internal class Installer(private val context: Context, private var activity: Activity?) {
    private var sessionId: Int = 0
    private lateinit var session: PackageInstaller.Session
    private lateinit var packageManager: PackageManager
    private lateinit var packageInstaller: PackageInstaller

    fun setActivity(activity: Activity?) {
        this.activity = activity
    }

    fun installPackage(apkPath: String) {
        try {
            session = createSession(activity!!)
            loadAPKFile(apkPath, session)
            val intent = Intent(context, activity!!.javaClass)
            intent.action = packageInstalledAction
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val statusReceiver = pendingIntent.intentSender
            session.commit(statusReceiver)
            session.close()
        } catch (e: IOException) {
            throw RuntimeException("IO exception", e)
        } catch (e: Exception) {
            session.abandon()
            throw e
        }
    }

    private fun createSession(activity: Activity): PackageInstaller.Session {
        try {
            packageManager = activity.packageManager
            packageInstaller = packageManager.packageInstaller
            val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.setInstallReason(PackageManager.INSTALL_REASON_USER)
            }

            sessionId = packageInstaller.createSession(params)
            session = packageInstaller.openSession(sessionId)
        } catch (e: Exception) {
            throw e
        }
        return session
    }

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
}

