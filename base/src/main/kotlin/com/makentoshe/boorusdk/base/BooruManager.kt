package com.makentoshe.boorusdk.base

import com.makentoshe.boorusdk.base.request.*

interface BooruManager {

    /** Returns a post http page */
    fun getPostHttp(request: PostsRequest): String

    /** Returns a list of posts in one of a types represented in the request */
    fun getPosts(request: PostsRequest): String

    /** Returns a list of related tags in one of a types represented in the request */
    fun getAutocomplete(request: AutocompleteRequest): String

    /** Returns a list of comments in one of a types represented in the request */
    fun getComments(request: CommentsRequest): String

    /** Return a list of tags */
    fun getTags(request: TagsRequest): String

    /** Performs a login and returns a result */
    fun login(request: LoginRequest): Boolean

    /** Performs a post voting and returns a new post score */
    fun votePost(request: VotePostRequest, parser: (ByteArray) -> Int): Int

    /** Performs a post commenting and returns a new list of the comments for this post */
    fun newComment(request: NewCommentRequest): String
}
