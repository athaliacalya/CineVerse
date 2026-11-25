package com.athalia.cineverse.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    fun getLocalizedContext(context: Context, languageCode: String): Context {
        val locale = if (languageCode.equals("in", ignoreCase = true) || languageCode.equals("id", ignoreCase = true)) {
            Locale.Builder().setLanguage("in").setRegion("ID").build()
        } else {
            Locale.Builder().setLanguage("en").setRegion("US").build()
        }
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}
