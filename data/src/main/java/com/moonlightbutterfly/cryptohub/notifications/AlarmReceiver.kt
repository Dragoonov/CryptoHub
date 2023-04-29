package com.moonlightbutterfly.cryptohub.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.common.getOrNull
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var cryptoAssetsDataSource: CryptoAssetsDataSource

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p0 == null || p1 == null) {
            return
        }
        val symbol = p1.getStringExtra(SYMBOL_KEY)!!
        val getter = {
            runBlocking {
                cryptoAssetsDataSource
                    .getCryptoAssetsMarketInfo(listOf(symbol)).first().getOrNull()?.first()
            }
        }
        postNotification(getter, p0)
    }

    companion object {
        const val SYMBOL_KEY = "symbol"
    }
}
