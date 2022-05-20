package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset

class AddAssetToCollectionUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    suspend operator fun invoke(asset: CryptoAsset, collectionName: String) = userCollectionsRepository.addToCollection(asset, collectionName)
}
