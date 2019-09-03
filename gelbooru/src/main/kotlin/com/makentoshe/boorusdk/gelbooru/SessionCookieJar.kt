package com.makentoshe.boorusdk.gelbooru

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

internal class SessionCookieJar : CookieJar {

    private val cookies = ArrayList<Cookie>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies.toMutableSet().addAll(cookies)
    }

    override fun loadForRequest(url: HttpUrl) = cookies
}