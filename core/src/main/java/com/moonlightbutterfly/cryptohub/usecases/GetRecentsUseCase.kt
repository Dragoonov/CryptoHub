package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository

class GetRecentsUseCase(private val getCollectionUseCase: GetCollectionUseCase) {
    operator fun invoke() = getCollectionUseCase.invoke(UserCollectionsRepository.RECENTS_COLLECTION_NAME)
}
