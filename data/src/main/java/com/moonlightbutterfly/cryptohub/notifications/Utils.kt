package com.moonlightbutterfly.cryptohub.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.moonlightbutterfly.cryptohub.Constants
import com.moonlightbutterfly.cryptohub.data.R
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.models.NotificationTime
import java.util.Calendar

internal fun postNotification(cryptoGetter: () -> CryptoAssetMarketInfo?, context: Context) {
    val info = cryptoGetter() ?: return
    val builder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.launcher_foreground)
        .setContentTitle(info.asset.name)
        .setContentText(
            context.getString(
                R.string.notification_data,
                info.asset.symbol,
                info.price
            )
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notify(0, builder.build())
        }
    }
}

internal fun calculateNotificationTime(time: NotificationTime): Long {
    val calendar = Calendar.getInstance()
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)
    calendar.set(Calendar.HOUR_OF_DAY, time.hour)
    calendar.set(Calendar.MINUTE, time.minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return when {
        (hourOfDay < time.hour) || (hourOfDay == time.hour && currentMinute < time.minute) -> {
            calendar.time.time
        }
        else -> {
            calendar.time.time + ONE_DAY_INTERVAL
        }
    }
}

internal fun setAlarm(symbol: String, startingTime: Long, context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra(AlarmReceiver.SYMBOL_KEY, symbol)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        symbol.hashCode(),
        intent,
        (PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    )
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP, startingTime,
        AlarmManager.INTERVAL_DAY, pendingIntent
    )
}

const val ONE_DAY_INTERVAL = (1000L * 60 * 60 * 24)
