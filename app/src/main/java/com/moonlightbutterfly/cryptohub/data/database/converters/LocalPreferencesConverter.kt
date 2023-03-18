package com.moonlightbutterfly.cryptohub.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration
import java.lang.reflect.Type

/**
 * Class used for converting objects to Room database types and vice versa.
 */
class LocalPreferencesConverter {

    @TypeConverter
    fun fromLocalPreferences(localPreferences: LocalPreferences): String {
        return Gson().toJson(localPreferences)
    }

    @TypeConverter
    fun stringToLocalPreferences(serializedPreferences: String): LocalPreferences {
        return Gson().fromJson(serializedPreferences, LocalPreferences::class.java)
    }

    @TypeConverter
    fun fromNotificationsConfigurationList(assets: Set<NotificationConfiguration>): String {
        return Gson().toJson(assets)
    }

    @TypeConverter
    fun stringToNotificationsConfigurationList(serializedAsset: String): Set<NotificationConfiguration> {
        val set: Type = object : TypeToken<Set<NotificationConfiguration>>() {}.type
        return Gson().fromJson(serializedAsset, set)
    }
}
