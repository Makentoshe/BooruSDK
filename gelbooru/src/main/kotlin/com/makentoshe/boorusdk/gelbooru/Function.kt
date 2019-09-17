package com.makentoshe.boorusdk.gelbooru

import retrofit2.Response

internal abstract class Function<T, R> : java.util.function.Function<T, R> {

    protected fun Response<ByteArray>.extractBody(): String {
        return if (isSuccessful) {
            String(body() ?: byteArrayOf())
        } else {
            throw Exception(errorBody()?.string() ?: message())
        }
    }
}
