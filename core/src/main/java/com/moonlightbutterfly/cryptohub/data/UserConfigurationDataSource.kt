package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import kotlinx.coroutines.flow.Flow

interface UserConfigurationDataSource {

    fun getRecents(): Flow<List<CryptoAsset>>

    suspend fun addRecent(asset: CryptoAsset)

    suspend fun removeRecents()

    fun getFavourites(): Flow<List<CryptoAsset>>

    suspend fun addFavourite(asset: CryptoAsset)

    suspend fun removeFavourite(asset: CryptoAsset)

    suspend fun removeRecent(asset: CryptoAsset)
}
