package com.hpcang.base.common.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import com.hpcang.base.common.extensions.showToast
import java.io.File

/**
 * Created by Js on 2016/5/23.
 */
object SystemUtil {

    /**
     * 获得状态栏的高度
     */
    fun getStatusHeight(context: Context): Int {
        var statusHeight = 0
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = clazz.getField("status_bar_height")[`object`].toString().toInt()
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusHeight
    }

    /**
     * 通知相册更新
     */
    fun updatePhoto(context: Context, path: String) {
        context.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$path")
            )
        )
    }

    /**
     * 复制到剪切板
     */
    fun clipboard(context: Context, copy: String) {
        //获取剪贴版
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //创建ClipData对象
        //第一个参数只是一个标记，随便传入。
        //第二个参数是要复制到剪贴版的内容
        val clip = ClipData.newPlainText("copy text", copy)
        //传入clipdata对象.
        clipboard.setPrimaryClip(clip)
        showToast { "复制成功" }
    }

    /**
     * 浏览器打开
     */
    fun openUrl(context: Context, url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(context.packageManager) != null) {
            ActivityCompat.startActivity(context, Intent.createChooser(intent, "请选择浏览器"), null)
        } else {
            showToast { "请下载浏览器" }
        }
    }

    /**
     * 打电话
     */
    @SuppressLint("MissingPermission")
    fun callPhone(context: Activity, phone: String) {
        val uri = Uri.parse("tel:$phone")
        val intent = Intent(Intent.ACTION_CALL, uri)
        ActivityCompat.startActivity(context, intent, null)
    }

    /**
     * 发邮件
     */
    fun sendEmail(context: Context, emailAddress: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(String.format("mailto:%s", emailAddress))
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.putExtra(Intent.EXTRA_TEXT, "")
        ActivityCompat.startActivity(context, intent, null)
    }

    /**
     * 发送短信
     */
    fun sendSMS(context: Context, phone: String) {
        val intent = Intent(
            Intent.ACTION_SENDTO, Uri
                .parse("smsto:$phone")
        )
        // 如果需要将内容传过去增加如下代码
//        intent .putExtra("sms_body", body);
        ActivityCompat.startActivity(context, intent, null)
    }

    /**
     * 当前进程是否是主进程
     */
    fun isMainProcess(context: Context): Boolean {
        return currentProcessName(context) == context.packageName
    }

    /**
     * 当前进程名称
     */
    fun currentProcessName(context: Context): String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfo = am.runningAppProcesses
        val myPid = Process.myPid()
        for (info in processInfo) {
            if (info.pid == myPid) {
                return info.processName
            }
        }
        return ""
    }

    /**
     * 判断当前应用是否是debug状态
     */
    fun isDebug(context: Context): Boolean {
        return try {
            val info = context.applicationInfo
            info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 版本名
     */
    fun getVersionName(context: Context): String? {
        return getPackageInfo(context)?.versionName
    }

    /**
     * 版本号
     */
    fun getVersionCode(context: Context): Long? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getPackageInfo(context)?.longVersionCode
        else
            getPackageInfo(context)?.versionCode?.toLong()
    }

    private fun getPackageInfo(context: Context): PackageInfo? {
        var pi: PackageInfo? = null
        try {
            val pm = context.packageManager
            pi = pm.getPackageInfo(
                context.packageName, PackageManager.GET_CONFIGURATIONS
            )
            return pi
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pi
    }

    /**
     * 检查下载器是否存在
     */
    private fun canDownloadState(context: Context): Boolean {
        try {
            val state = context.packageManager
                .getApplicationEnabledSetting("com.android.providers.downloads")
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED
            ) {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * 安装apk
     */
    fun installApk(context: Context, file: File) {
        installApk(context, Uri.fromFile(file))
    }

    fun installApk(context: Context, uri: Uri) {
        installApk(
            context, uriToFile(context, uri).path
        )
    }

    fun installApk(context: Context, path: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            val contentUri = FileProvider.getUriForFile(
                context, context.applicationInfo.packageName + ".fileProvider", File(path)
            )
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(
                Uri.fromFile(File(path)), "application/vnd.android.package-archive"
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        ActivityCompat.startActivity(context, intent, null)
    }

    fun uriToFile(context: Context, uri: Uri): File {
        val img_path: String?
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val actualimagecursor = context.contentResolver.query(
            uri, proj, null, null, null
        )
        img_path = if (actualimagecursor == null) {
            uri.path
        } else {
            val actual_image_column_index = actualimagecursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            actualimagecursor.moveToFirst()
            actualimagecursor.getString(actual_image_column_index)
        }
        return File(img_path)
    }

    /**
     * 网络状态
     */
    fun isNetwork(context: Context): Boolean {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            val info = cm.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * 是否打开gps
     */
    fun isGPS(context: Context): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 设置gps界面
     */
    fun settingGPS(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ActivityCompat.startActivity(context, intent, null)
    }

    /**
     * 是否打开通知
     */
    fun isNotificationEnabled(context: Context): Boolean {
        val notification = NotificationManagerCompat.from(context)
        return notification.areNotificationsEnabled()
    }

    /**
     * 设置通知界面
     */
    fun settingNotification(context: Context) {
        val intent = Intent()
        //直接跳转到应用通知设置的代码：
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> { //8.0及以上
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts(
                    "package", context.applicationInfo.packageName, null
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> { //5.0以上到8.0以下
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", context.applicationInfo.packageName)
                intent.putExtra("app_uid", context.applicationInfo.uid)
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT -> { //4.4
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:" + context.applicationInfo.packageName)
            }
            else -> {
                //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts(
                    "package", context.applicationInfo.packageName, null
                )
            }
        }
        ActivityCompat.startActivity(context, intent, null)
    }

    /**
     * 背景变暗
     */
    fun darkenWindow(context: Activity, alpha: Float) {
        val lp = context.window.attributes
        lp.alpha = alpha
        lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        context.window.attributes = lp
    }

    /**
     * 保存屏幕快照
     */
    fun activitySnapShoot(context: Activity): Bitmap {
        /*获取windows中最顶层的view*/
        val view = context.window.decorView

        //允许当前窗口保存缓存信息
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()

        //获取状态栏高度
        val rect = Rect()
        view.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight = rect.top
        val windowManager = context.windowManager

        //获取屏幕宽和高
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        val width = outMetrics.widthPixels
        val height = outMetrics.heightPixels

        //去掉状态栏
        val bitmap = Bitmap.createBitmap(
            view.drawingCache, 0, statusBarHeight, width,
            height - statusBarHeight
        )

        //销毁缓存信息
        view.destroyDrawingCache()
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    /**
     * 保存组件快照
     */
    fun viewSnapShoot(view: View): Bitmap {
        //允许当前窗口保存缓存信息
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()

        //去掉状态栏
        val bitmap = Bitmap.createBitmap(
            view.drawingCache, 0, 0, view.width,
            view.height
        )

        //销毁缓存信息
        view.destroyDrawingCache()
        view.isDrawingCacheEnabled = false
        return bitmap
    }
}