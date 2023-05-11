package com.moonlightbutterfly.cryptohub

import com.google.firebase.auth.FirebaseUser
import com.moonlightbutterfly.cryptohub.models.User

internal fun FirebaseUser.toUser(): User {
    return User(userId = this.uid, name = this.displayName ?: "", email = this.email ?: "")
}
