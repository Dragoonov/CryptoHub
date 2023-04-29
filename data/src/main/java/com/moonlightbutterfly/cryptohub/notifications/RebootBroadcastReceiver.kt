package com.moonlightbutterfly.cryptohub.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class RebootBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var localPreferencesRepository: LocalPreferencesRepository

    override fun onReceive(context: Context?, p1: Intent?) {
        if (p1?.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }
        runBlocking {
            localPreferencesRepository.getLocalPreferences()
                .first()
                .unpack(LocalPreferences.DEFAULT).notificationsConfiguration
                .forEach { configuration ->
                    configuration.notificationTime?.let { notificationTime ->
                        context?.let {
                            val time = calculateNotificationTime(notificationTime)
                            setAlarm(configuration.symbol, time, it)
                        }
                    }
                }
        }
    }
}
