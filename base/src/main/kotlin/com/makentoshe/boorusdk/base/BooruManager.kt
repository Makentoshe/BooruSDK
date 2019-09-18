package com.makentoshe.boorusdk.base

import com.makentoshe.boorusdk.base.request.*
import com.makentoshe.boorusdk.base.request.comment.*
import com.makentoshe.boorusdk.base.request.pool.GetPoolRequest
import com.makentoshe.boorusdk.base.request.pool.GetPoolsRequest
import com.makentoshe.boorusdk.base.request.post.GetPostRequest
import com.makentoshe.boorusdk.base.request.post.GetPostsRequest

interface BooruManager {

    /** Returns a post http page */
    fun getPostHttp(request: GetPostRequest): String

    /** Returns a single post */
    fun getPost(request: GetPostRequest): String

    /** Returns a list of posts */
    fun getPosts(request: GetPostsRequest): String

    /** Returns a list of related tags in one of a types represented in the request */
    fun getAutocomplete(request: AutocompleteRequest): String

    /** Return a list of tags */
    fun getTags(request: TagsRequest): String

    /** Performs a login and returns a result */
    fun login(request: LoginRequest): Boolean

    /** Performs a post voting and returns a new post score */
    fun votePost(request: VotePostRequest, parser: (ByteArray) -> Int): Int

    /** Returns a list of comments */
    fun getComments(request: GetCommentsRequest): String

    /** Returns a single comment */
    fun getComment(request: GetCommentRequest): String

    /** Returns a list of comments related to specified post */
    fun getPostComments(request: GetPostCommentsRequest): String

    /** Performs a post commenting and returns a new list of the comments for this post */
    fun newComment(request: NewCommentRequest): String

    /** Performs a comment deletion and returns a deleted comment */
    fun deleteComment(request: DeleteCommentRequest): String

    /** Returns a list of pools */
    fun getPools(request: GetPoolsRequest): String

    /** Returns a single pool */
    fun getPool(request: GetPoolRequest): String
}
