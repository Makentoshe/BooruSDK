package function

import DanbooruApi
import com.makentoshe.boorusdk.base.request.GetCommentsRequest

internal class GetComments(
    private val danbooruApi: DanbooruApi
): Function<GetCommentsRequest, String>() {

    override fun apply(t: GetCommentsRequest): String {
        return danbooruApi.getComments(
            type = t.type.name.toLowerCase(),
            count = t.limit,
            page = t.page,
            postsTagMatch = t.postTagMatch,
            creatorName = t.creatorName,
            creatorId = t.creatorId,
            isDeleted = t.isDeleted
        ).execute().extractBody()
    }
}