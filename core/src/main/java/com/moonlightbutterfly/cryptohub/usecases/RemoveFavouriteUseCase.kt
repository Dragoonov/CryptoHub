package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset

class RemoveFavouriteUseCase(private val removeAssetFromCollectionUseCase: RemoveAssetFromCollectionUseCase) {
    suspend operator fun invoke(asset: CryptoAsset) = removeAssetFromCollectionUseCase.invoke(asset, UserCollectionsRepository.FAVOURITES_COLLECTION_NAME)
}
