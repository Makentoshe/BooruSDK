package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Password
import com.makentoshe.boorusdk.base.model.Username

interface LoginRequest {
    val username: Username
    val password: Password

    companion object {
        fun build(username: String, password: String): LoginRequest {
            return object : LoginRequest {
                override val password = Password(password)
                override val username = Username(username)
            }
        }
    }
}
