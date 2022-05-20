package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset

class AddRecentUseCase(private val addAssetToCollectionUseCase: AddAssetToCollectionUseCase) {
    suspend operator fun invoke(asset: CryptoAsset) = addAssetToCollectionUseCase.invoke(asset, UserCollectionsRepository.RECENTS_COLLECTION_NAME)
}
