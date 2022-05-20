package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository

class RemoveRecentsUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    suspend operator fun invoke() = userCollectionsRepository.clearCollection(UserCollectionsRepository.RECENTS_COLLECTION_NAME)
}
