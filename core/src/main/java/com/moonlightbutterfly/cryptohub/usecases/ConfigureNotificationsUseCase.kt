package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.notifications.Notifier
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration

class ConfigureNotificationsUseCase(
    private val notifier: Notifier
) {
    operator fun invoke(notificationConfiguration: Set<NotificationConfiguration>) =
        notifier.configure(notificationConfiguration)
}
