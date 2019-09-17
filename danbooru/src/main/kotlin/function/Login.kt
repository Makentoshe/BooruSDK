package function

import DanbooruApi
import com.makentoshe.boorusdk.base.request.LoginRequest
import org.jsoup.Jsoup
import retrofit2.Response

/**
 * Performs a log in action for danbooru
 */
internal class Login(
    private val danbooruApi: DanbooruApi
) : Function<LoginRequest, Boolean>() {

    /**
     * Performs a log in action
     * @return true if log in success and false otherwise
     */
    override fun apply(t: LoginRequest): Boolean {
        return danbooruApi.login(
            token = getAuthenticityToken(),
            username = t.username,
            password = t.password
        ).execute().let { response ->
            response.hasUserName() && response.hasPasswordHash()
        }
    }

    private fun getAuthenticityToken(): String {
        val httpLoginPage = danbooruApi.newSession().execute().extractBody()
        return Jsoup.parse(httpLoginPage).select("[name=authenticity_token]").attr("value")
    }

    private fun Response<ByteArray>.hasUserName(): Boolean {
        val cookies = raw().priorResponse?.headers("set-cookie")
        return cookies?.any { it.contains("user_name") } ?: false
    }

    private fun Response<ByteArray>.hasPasswordHash(): Boolean {
        val cookies = raw().priorResponse?.headers("set-cookie")
        return cookies?.any { it.contains("password_hash") } ?: false
    }

}