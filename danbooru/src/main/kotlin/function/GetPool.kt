package function

import DanbooruApi
import com.makentoshe.boorusdk.base.request.pool.GetPoolRequest

internal class GetPool(
    private val danbooruApi: DanbooruApi
) : Function<GetPoolRequest, String>() {
    override fun apply(t: GetPoolRequest): String {
        if (t.poolId < 0) {
            throw IllegalArgumentException("Pool id should not be less 0")
        }
        return danbooruApi.getPool(
            type = t.type.name.toLowerCase(),
            poolId = t.poolId
        ).execute().extractBody()
    }
}
