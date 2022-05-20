package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository

class GetAllCollectionNamesUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    operator fun invoke() = userCollectionsRepository.getAllCollectionNames()
}
