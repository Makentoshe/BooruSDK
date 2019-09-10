package com.makentoshe.boorusdk.gelbooru

import com.makentoshe.boorusdk.base.model.*
import retrofit2.Call
import retrofit2.http.*

interface GelbooruApi {

    @GET("index.php?page=dapi&s=post&q=index")
    fun getPost(@Query("id") id: Int, @Query("json") type: String): Call<ByteArray>

    @GET("index.php?page=dapi&s=post&q=index")
    fun getPosts(
        @Query("limit") count: Count? = null,
        @Query("pid") page: Page? = null,
        @Query("tags") tags: Tags? = null,
        @Query("json") json: Int? = null
    ): Call<ByteArray>

    @GET("https://gelbooru.com/index.php?page=post&s=view")
    fun getPostHttp(@Query("id") id: Int): Call<ByteArray>

    @GET("index.php?page=autocomplete")
    fun autocomplete(@Query("term") term: String): Call<ByteArray>

    @GET("index.php?page=dapi&s=comment&q=index")
    fun getComments(@Query("post_id") postId: Int? = null, @Query("json") type: Int?): Call<ByteArray>

    @GET("index.php?page=dapi&s=tag&q=index")
    fun tags(@Query("id") id: Id): Call<ByteArray>

    @GET("index.php?page=dapi&s=tag&q=index")
    fun tags(
        @Query("name_pattern") pattern: Term,
        @Query("limit") count: Count,
        @Query("order") order: Order?,
        @Query("orderby") orderby: Orderby?
    ): Call<ByteArray>

    @FormUrlEncoded
    @POST("index.php?page=account&s=login&code=00")
    fun login(
        @Field("user") user: Username,
        @Field("pass") password: Password,
        @Field("submit") submit: String = "Log+in"
    ): Call<ByteArray>

    @GET("index.php?page=post&s=vote&type=up")
    fun votePostUp(@Query("id") id: Id): Call<ByteArray>

    @FormUrlEncoded
    @POST("index.php?page=comment&s=save")
    fun commentPost(
        @Query("id") postId: Id,
        @Field("comment") text: Comment,
        @Field("csrf-token") csrfToken: String,
        @Field("post_anonymous") anon: String? = null,
        @Field("conf") conf: String? = "1",
        @Field("submit") submit: String = "Post+comment"
    ): Call<ByteArray>
}
