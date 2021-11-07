package com.moonlightbutterfly.cryptohub.repository

import com.moonlightbutterfly.cryptohub.data.UserConfigurationDataSource
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.domain.models.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserConfigurationDataSourceImpl : UserConfigurationDataSource {

    private val favourites: MutableList<CryptoAsset> = mutableListOf()
    private var userSettings: UserSettings = UserSettings.EMPTY

    private val favouritesFlow = MutableStateFlow(favourites)
    private val settingsFlow = MutableStateFlow(userSettings)

    override fun getFavourites(): Flow<List<CryptoAsset>> {
        return favouritesFlow
    }

    override suspend fun addFavourite(asset: CryptoAsset) {
        favourites.add(asset)
        favouritesFlow.value = favourites.toMutableList()
    }

    override suspend fun removeFavourite(asset: CryptoAsset) {
        favourites.remove(asset)
        favouritesFlow.value = favourites.toMutableList()
    }

    override fun getUserSettings(): Flow<UserSettings> {
        return settingsFlow
    }

    override suspend fun updateUserSettings(userSettings: UserSettings) {
        this.userSettings = userSettings
        settingsFlow.value = userSettings.copy()
    }
}
