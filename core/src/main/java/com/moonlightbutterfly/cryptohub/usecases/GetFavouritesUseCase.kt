package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository

class GetFavouritesUseCase(private val getCollectionUseCase: GetCollectionUseCase) {
    operator fun invoke() = getCollectionUseCase.invoke(UserCollectionsRepository.FAVOURITES_COLLECTION_NAME)
}
