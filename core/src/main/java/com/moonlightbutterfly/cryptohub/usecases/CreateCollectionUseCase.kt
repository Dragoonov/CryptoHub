package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository

class CreateCollectionUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    suspend operator fun invoke(name: String) = userCollectionsRepository.createCollection(name)
}
