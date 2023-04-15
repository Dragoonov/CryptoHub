package com.moonlightbutterfly.cryptohub.data.notifications

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration

/**
 * Interface for notifier that will fire notifications based on the configuration
 * passed to [configure].
 */
interface Notifier {
    fun configure(notificationConfiguration: Set<NotificationConfiguration>): Answer<Unit>
}
