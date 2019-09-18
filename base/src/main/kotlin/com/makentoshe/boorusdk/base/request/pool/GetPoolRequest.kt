package com.makentoshe.boorusdk.base.request.pool

import com.makentoshe.boorusdk.base.model.Type

open class GetPoolRequest(
    override val type: Type,
    val poolId: Int
): PoolRequest