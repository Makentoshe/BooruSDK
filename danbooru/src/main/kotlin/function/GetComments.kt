package function

import DanbooruApi
import com.makentoshe.boorusdk.base.request.GetCommentsRequest

internal class GetComments(
    private val danbooruApi: DanbooruApi
): Function<GetCommentsRequest, String>() {

    override fun apply(t: GetCommentsRequest): String {
        return danbooruApi.getComments(
            type = t.type.name.toLowerCase(),
            count = t.checkCount().limit,
            page = t.checkPage().page,
            postsTagMatch = t.postTagMatch,
            creatorName = t.creatorName,
            creatorId = t.checkCreatorId().creatorId,
            isDeleted = t.isDeleted
        ).execute().extractBody()
    }

    private fun GetCommentsRequest.checkCount(): GetCommentsRequest {
        if (limit != null && limit!! < 0) throw IllegalArgumentException("Limit should not be less 0")
        return this
    }

    private fun GetCommentsRequest.checkPage(): GetCommentsRequest {
        if (page != null && page!! < 0) throw IllegalArgumentException("Page should not be less 0")
        return this
    }

    private fun GetCommentsRequest.checkCreatorId(): GetCommentsRequest {
        if (creatorId != null && creatorId!! < 0) throw IllegalArgumentException("Creator id should not be less 0")
        return this
    }
}