package cookie

import okhttp3.Cookie
import okhttp3.HttpUrl

interface CookieStorage {

    fun setCookie(url: HttpUrl, cookies: List<Cookie>)

    fun getCookie(url: HttpUrl): List<Cookie>

    fun getCookie(name: String): Cookie

    fun hasCookie(name: String): Boolean
}
