package function

import DanbooruApi
import com.makentoshe.boorusdk.base.request.pool.GetPoolsRequest

internal class GetPools(
    private val danbooruApi: DanbooruApi
) : Function<GetPoolsRequest, String>() {
    override fun apply(t: GetPoolsRequest): String {
        return danbooruApi.getPools(
            type = t.type.name.toLowerCase(),
            nameMatches = t.nameMatches,
            ids = t.id.convertIds(),
            descriptionMatches = t.descriptionMatches,
            creatorName = t.creatorName,
            creatorId = t.creatorId,
            isActive = t.isActive,
            isDeleted = t.isDeleted,
            category = t.category,
            order = t.order
        ).execute().extractBody()
    }

    private fun List<Int>?.convertIds(): String? {
        return if (this != null && this.isNotEmpty()) {
            joinToString(",")
        } else {
            null
        }
    }
}
