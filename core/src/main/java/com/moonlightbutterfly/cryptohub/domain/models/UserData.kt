package com.moonlightbutterfly.cryptohub.domain.models

class UserData(id: String = "", name: String = "", mail: String = "") {

    var userId: String = id
        private set

    var username: String = name
        private set

    var email: String = mail
        private set

    fun set(userData: UserData) {
        this.userId = userData.userId
        this.username = userData.username
        this.email = userData.email
    }

    fun clear() {
        username = ""
        userId = ""
        email = ""
    }

    fun isUserSignedIn() = userId.isNotEmpty()
}
