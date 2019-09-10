package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Type

interface AutocompleteRequest {
    val term: String
    val type: Type

    companion object {
        fun build(term: String, type: Type): AutocompleteRequest {
            return object : AutocompleteRequest {
                override val term = term
                override val type = type
            }
        }
    }
}
