package com.makentoshe.boorusdk.base.request

import com.makentoshe.boorusdk.base.model.Term
import com.makentoshe.boorusdk.base.model.Type

interface AutocompleteRequest {
    val term: Term
    val type: Type

    companion object {
        fun build(term: String, type: Type): AutocompleteRequest {
            return object : AutocompleteRequest {
                override val term = Term(term)
                override val type = type
            }
        }
    }
}
