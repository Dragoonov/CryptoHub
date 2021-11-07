package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.domain.models.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserConfigurationDataSource {

    fun getFavourites(): Flow<List<CryptoAsset>>

    suspend fun addFavourite(asset: CryptoAsset)

    suspend fun removeFavourite(asset: CryptoAsset)

    fun getUserSettings(): Flow<UserSettings>

    suspend fun updateUserSettings(userSettings: UserSettings)
}
