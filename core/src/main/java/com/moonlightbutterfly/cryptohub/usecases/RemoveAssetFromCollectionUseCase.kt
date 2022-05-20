package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset

class RemoveAssetFromCollectionUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    suspend operator fun invoke(asset: CryptoAsset, collectionName: String) = userCollectionsRepository.removeFromCollection(asset, collectionName)
}
