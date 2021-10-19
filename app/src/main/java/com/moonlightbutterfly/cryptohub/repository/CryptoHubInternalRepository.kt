package com.moonlightbutterfly.cryptohub.repository

import androidx.lifecycle.LiveData
import com.moonlightbutterfly.cryptohub.dataobjects.AppConfig
import com.moonlightbutterfly.cryptohub.dataobjects.FavouriteCryptoasset

interface CryptoHubInternalRepository {

    val appConfig: LiveData<AppConfig>

    val favourites: LiveData<List<FavouriteCryptoasset>>

    suspend fun updateAppConfig(appConfig: AppConfig)

    suspend fun insertFavourite(favourite: FavouriteCryptoasset)

    suspend fun removeFavourite(favourite: FavouriteCryptoasset)
}
