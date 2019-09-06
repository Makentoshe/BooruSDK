package com.makentoshe.boorusdk.base

import com.makentoshe.boorusdk.base.model.ParseResult
import com.makentoshe.boorusdk.base.request.*

interface BooruManager {

    /** Returns a post http page */
    fun getPostHttp(request: PostHttpRequest, parser: (ByteArray) -> String): String

    /** Returns a list of posts */
    fun getPosts(request: PostsRequest, parser: (ByteArray) -> List<ParseResult>): List<ParseResult>

    /** Returns a single post */
    fun getPost(request: PostRequest, parser: (ByteArray) -> ParseResult): ParseResult

    /** Returns a list of related tags */
    fun getAutocomplete(request: AutocompleteRequest, parser: (ByteArray) -> Array<String>): Array<String>

    /** Returns a single comment */
    fun getComment(request: CommentRequest, parser: (ByteArray) -> ParseResult): ParseResult

    /** Return a list of comments */
    fun getComments(request: CommentsRequest, parser: (ByteArray) -> List<ParseResult>): List<ParseResult>

    /** Return a single tag */
    fun getTag(request: TagRequest, parser: (ByteArray) -> ParseResult): ParseResult

    /** Return a list of tags */
    fun getTags(request: TagsRequest, parser: (ByteArray) -> List<ParseResult>): List<ParseResult>

}
