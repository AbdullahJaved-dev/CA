package com.logicielhouse.ca.utils

/**
 * Created by Abdullah on 9/30/2020.
 */
class AppConstants {
    companion object {
        private const val BASE_URL = "https://burnlyfc.haseebihsan.com/"
        const val GET_NEWS = BASE_URL + "api/getNews"
        const val GET_SOURCES = BASE_URL + "api/getSources"
        const val GET_POINTS = BASE_URL + "api/getClubPoints"
        const val UPLOAD_TOKEN = BASE_URL + "api/addDevice"
        const val APP_SESSION = "CA"
        const val FIREBASE_TOKEN = "token"
        const val FIREBASE_TOKEN_UPLOADED = "token_uploaded"
        const val CHANNEL_ID = "CA"
        const val CHANNEL_NAME = "CA App"
        const val URL = "url"
        const val ACTIVITY = "activity"
    }
}