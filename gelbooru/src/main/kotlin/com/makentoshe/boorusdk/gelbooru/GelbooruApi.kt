package com.makentoshe.boorusdk.gelbooru

import com.makentoshe.boorusdk.base.model.*
import retrofit2.Call
import retrofit2.http.*

interface GelbooruApi {

    @GET("index.php?page=dapi&s=post&q=index")
    fun getPost(@Query("id") id: Int, @Query("json") type: String): Call<ByteArray>

    @GET("index.php?page=dapi&s=post&q=index")
    fun getPosts(
        @Query("id") id: Int? = null,
        @Query("limit") count: Count? = null,
        @Query("pid") page: Page? = null,
        @Query("tags") tags: Tags? = null,
        @Query("json") type: Int? = null
    ): Call<ByteArray>

    @GET("https://gelbooru.com/index.php?page=post&s=view")
    fun getPostHttp(@Query("id") id: Int): Call<ByteArray>

    @GET("index.php?page=autocomplete")
    fun autocomplete(@Query("term") term: String): Call<ByteArray>

    @GET("index.php?page=dapi&s=comment&q=index")
    fun getPostComments(@Query("post_id") postId: Int): Call<ByteArray>

    @GET("index.php?page=dapi&s=comment&q=index")
    fun getComments(): Call<ByteArray>

    @GET("index.php?page=dapi&s=tag&q=index")
    fun getTags(
        @Query("id") tagId: Int? = null,
        @Query("name_pattern") pattern: String? = null,
        @Query("limit") count: Int? = null,
        @Query("order") order: Order? = null,
        @Query("orderby") orderby: Orderby? = null
    ): Call<ByteArray>

    @FormUrlEncoded
    @POST("index.php?page=account&s=login&code=00")
    fun login(
        @Field("user") username: String,
        @Field("pass") password: String,
        @Field("submit") submit: String = "Log+in"
    ): Call<ByteArray>

    @GET("index.php?page=post&s=vote&type=up")
    fun votePostUp(@Query("id") id: Int): Call<ByteArray>

    @FormUrlEncoded
    @POST("index.php?page=comment&s=save")
    fun commentPost(
        @Query("id") postId: Int,
        @Field("comment") body: String,
        @Field("csrf-token") csrfToken: String,
        @Field("post_anonymous") postAsAnonymous: String? = null,
        @Field("conf") conf: String? = "1",
        @Field("submit") submit: String = "Post+comment"
    ): Call<ByteArray>
}
