package com.moonlightbutterfly.cryptohub.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.common.getOrNull
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.notifications.Notifier
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration
import com.moonlightbutterfly.cryptohub.models.NotificationInterval
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class NotifierImpl @Inject constructor(
    private val context: Context,
    private val localPreferencesRepository: LocalPreferencesRepository,
    private val workManager: WorkManager
) : Notifier {

    private val workManagerConstraints = Constraints
        .Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val timeMap = mapOf(
        NotificationInterval.WEEK to Pair(SEVEN, TimeUnit.DAYS),
        NotificationInterval.DAY to Pair(ONE, TimeUnit.DAYS),
        NotificationInterval.HOURS_5 to Pair(FIVE, TimeUnit.HOURS),
        NotificationInterval.HOURS_2 to Pair(TWO, TimeUnit.HOURS),
        NotificationInterval.HOUR to Pair(ONE, TimeUnit.HOURS),
        NotificationInterval.MINUTES_30 to Pair(THIRTY, TimeUnit.MINUTES),
    )

    override fun configure(notificationConfiguration: Set<NotificationConfiguration>): Answer<Unit> {
        cancelCurrentWork()
        notificationConfiguration.forEach { configuration ->
            handleDateRequest(configuration)
            handleIntervalRequest(configuration)
        }
        return Answer.Success(Unit)
    }

    private fun cancelCurrentWork() {
        workManager.cancelAllWorkByTag(WORKERS_TAG)
        removeAlarms()
    }

    private fun handleIntervalRequest(notificationConfiguration: NotificationConfiguration) {
        notificationConfiguration.notificationInterval?.let {
            val notificationTime = timeMap.getValue(it)
            val inputData = Data.Builder()
                .putString(FetchCryptoInfoWorker.SYMBOL_KEY, notificationConfiguration.symbol)
                .build()
            val request = PeriodicWorkRequestBuilder<FetchCryptoInfoWorker>(
                notificationTime.first,
                notificationTime.second
            )
                .setInputData(inputData)
                .addTag(WORKERS_TAG)
                .setConstraints(workManagerConstraints)
                .build()
            workManager.enqueue(request)
        }
    }

    private fun handleDateRequest(configuration: NotificationConfiguration) {
        configuration.notificationTime?.let {
            val time = calculateNotificationTime(it)
            setAlarm(configuration.symbol, time, context)
        }
    }

    private fun removeAlarms() {
        runBlocking {
            val preferences = localPreferencesRepository.getLocalPreferences().first().getOrNull()
            preferences?.notificationsConfiguration?.forEach {
                removeAlarm(it.symbol)
            }
        }
    }

    private fun removeAlarm(symbol: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, symbol.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        private const val WORKERS_TAG = "tag_cryptohub"
        private const val SEVEN = 7L
        private const val ONE = 1L
        private const val TWO = 2L
        private const val FIVE = 5L
        private const val THIRTY = 30L
    }
}
