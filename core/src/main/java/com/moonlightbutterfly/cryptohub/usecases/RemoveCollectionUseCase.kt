package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository

class RemoveCollectionUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    suspend operator fun invoke(name: String) = userCollectionsRepository.removeCollection(name)
}
