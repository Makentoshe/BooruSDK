package com.makentoshe.boorusdk.base.request

interface LoginRequest {
    val username: String
    val password: String

    companion object {
        fun build(username: String, password: String): LoginRequest {
            return object : LoginRequest {
                override val password = password
                override val username = username
            }
        }
    }
}
