package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.domain.models.UserSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository class aggregating external data sources and providing coherent interface for application.
 */
class UserConfigurationRepository(
    private val userConfigurationDataSource: UserConfigurationDataSource
) {
    fun getFavourites(): Flow<List<CryptoAsset>> = userConfigurationDataSource.getFavourites()

    suspend fun addFavourite(asset: CryptoAsset) = userConfigurationDataSource.addFavourite(asset)

    suspend fun removeFavourite(asset: CryptoAsset) = userConfigurationDataSource.removeFavourite(asset)

    suspend fun updateUserSettings(userSettings: UserSettings) = userConfigurationDataSource.updateUserSettings(userSettings)

    fun getUserSettings(): Flow<UserSettings> = userConfigurationDataSource.getUserSettings()
}
