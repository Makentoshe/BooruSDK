package com.makentoshe.boorusdk.gelbooru

import okhttp3.Cookie
import okhttp3.HttpUrl

interface CookieStorage {

    //PHPSESSID=s214sqq3iqm9n8dkv3afmqbsv5; path=/
    fun setCookie(url: HttpUrl, cookies: List<Cookie>)

    fun getCookie(url: HttpUrl): List<Cookie>

    fun getCookie(name: String): Cookie

    fun hasCookie(name: String): Boolean
}
