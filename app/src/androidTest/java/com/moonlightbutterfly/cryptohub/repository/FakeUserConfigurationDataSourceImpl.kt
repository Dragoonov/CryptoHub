package com.moonlightbutterfly.cryptohub.repository

import com.moonlightbutterfly.cryptohub.data.UserConfigurationDataSource
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserConfigurationDataSourceImpl : UserConfigurationDataSource {

    private val favourites: MutableList<CryptoAsset> = mutableListOf()
    private val recents: MutableList<CryptoAsset> = mutableListOf()

    private val favouritesFlow = MutableStateFlow(favourites)
    private val recentsFlow = MutableStateFlow(recents)

    override fun getRecents(): Flow<List<CryptoAsset>> {
        return recentsFlow
    }

    override suspend fun addRecent(asset: CryptoAsset) {
        recents.add(asset)
        recentsFlow.value = recents.toMutableList()
    }

    override suspend fun removeRecents() {
        recents.clear()
        recentsFlow.value = mutableListOf()
    }

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

    override suspend fun removeRecent(asset: CryptoAsset) {
        recents.remove(asset)
        recentsFlow.value = recents.toMutableList()
    }
}
