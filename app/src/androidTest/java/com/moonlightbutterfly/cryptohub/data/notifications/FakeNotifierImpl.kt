package com.moonlightbutterfly.cryptohub.data.notifications

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration

class FakeNotifierImpl: Notifier {
    override fun configure(notificationConfiguration: Set<NotificationConfiguration>): Answer<Unit> {
        return Answer.Success(Unit)
    }
}