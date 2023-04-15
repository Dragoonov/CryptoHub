package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration
fun interface ConfigureNotificationsUseCase: (Set<NotificationConfiguration>) -> Answer<Unit>