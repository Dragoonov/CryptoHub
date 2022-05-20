package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset

class AddFavouriteUseCase(private val addAssetToCollectionUseCase: AddAssetToCollectionUseCase) {
    suspend operator fun invoke(asset: CryptoAsset) = addAssetToCollectionUseCase.invoke(asset, UserCollectionsRepository.FAVOURITES_COLLECTION_NAME)
}
