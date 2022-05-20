package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository

class GetCollectionUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    operator fun invoke(name: String) = userCollectionsRepository.getCollection(name)
}
