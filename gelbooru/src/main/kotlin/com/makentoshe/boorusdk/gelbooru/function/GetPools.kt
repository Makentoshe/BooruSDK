package com.makentoshe.boorusdk.gelbooru.function

import com.makentoshe.boorusdk.base.model.Type
import com.makentoshe.boorusdk.base.request.GetPoolsRequest
import com.makentoshe.boorusdk.gelbooru.GelbooruApi
import kotlin.math.absoluteValue

internal class GetPools(
    private val gelbooruApi: GelbooruApi
): Function<GetPoolsRequest, String>() {

    /* Hardcoded on gelbooru */
    private val limit = 25

    override fun apply(t: GetPoolsRequest): String {
        val page = if (t.page != null) t.page!! * limit else null
        val response = gelbooruApi.getPools(page).execute().extractBody()
        return when(t.type) {
            Type.XML -> html2xml(response)
            Type.JSON -> html2json(response)
            else -> response
        }
    }

    private fun html2xml(html: String): String {
        //TODO
        return html
    }

    private fun html2json(html: String): String {
        //TODO
        return html
    }
}
