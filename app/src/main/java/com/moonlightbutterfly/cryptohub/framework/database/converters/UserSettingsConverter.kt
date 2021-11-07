package com.moonlightbutterfly.cryptohub.framework.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.moonlightbutterfly.cryptohub.domain.models.UserSettings

/**
 * Class used for converting objects to Room database types and vice versa.
 */
class UserSettingsConverter {

    @TypeConverter
    fun fromUserSettings(userSettings: UserSettings): String {
        return Gson().toJson(userSettings)
    }

    @TypeConverter
    fun stringToUserSettings(serializedSettings: String): UserSettings {
        return Gson().fromJson(serializedSettings, UserSettings::class.java)
    }
}
