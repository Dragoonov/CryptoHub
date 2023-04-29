package com.moonlightbutterfly.cryptohub.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.common.getOrNull
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@HiltWorker
class FetchCryptoInfoWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cryptoAssetsDataSource: CryptoAssetsDataSource,
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val symbol = inputData.getString(SYMBOL_KEY)!!
        val info = {
            runBlocking {
                cryptoAssetsDataSource.getCryptoAssetsMarketInfo(listOf(symbol)).first().getOrNull()
                    ?.first()
            }
        }
        postNotification(info, applicationContext)
        return Result.success()
    }

    companion object {
        const val SYMBOL_KEY = "symbol"
    }
}
