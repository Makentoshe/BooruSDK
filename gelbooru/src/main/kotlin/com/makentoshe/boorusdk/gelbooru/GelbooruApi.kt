package com.makentoshe.boorusdk.gelbooru

import com.makentoshe.boorusdk.base.model.*
import retrofit2.Call
import retrofit2.http.*

interface GelbooruApi {

    @GET("index.php?page=dapi&s=post&q=index")
    fun getPost(@Query("id") id: Id): Call<ByteArray>

    @GET("index.php?page=dapi&s=post&q=index")
    fun getPosts(
        @Query("limit") count: Count,
        @Query("pid") page: Page,
        @Query("tags") tags: Tags,
        @Query("json") json: Int
    ): Call<ByteArray>

    @GET("https://gelbooru.com/index.php?page=post&s=view")
    fun getPostHttp(@Query("id") id: Id): Call<ByteArray>

    @GET("index.php?page=autocomplete")
    fun autocomplete(@Query("term") term: Term): Call<ByteArray>

    @GET("index.php?page=dapi&s=comment&q=index")
    fun comments(@Query("post_id") postId: Id): Call<ByteArray>

    @GET("index.php?page=dapi&s=comment&q=index")
    fun comments(): Call<ByteArray>

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
        @Field("user") user: String,
        @Field("pass") password: String,
        @Field("submit") submit: String = "Log+in"
    ): Call<ByteArray>

    @GET("index.php?page=post&s=vote&type=up")
    fun votePostUp(@Query("id") id: Id): Call<ByteArray>

    @FormUrlEncoded
    @POST("index.php?page=comment&s=save")
    fun commentPost(
        @Header("user_id") userId: String,
        @Header("pass_hash") passHash: String,
        @Query("id") postId: Id,
        @Field("comment") text: String,
        @Field("csrf-token") csrfToken: String,
        @Field("post_anonymous") anon: String? = "on",
        @Field("conf") conf: String? = "1",
        @Field("submit") submit: String = "Post+comment"
    ): Call<ByteArray>
}

interface GelbooruApiJson {

    @GET("index.php?page=dapi&s=post&q=index&json=1")
    fun getPost(@Query("id") id: Id): Call<ByteArray>

    @GET("index.php?page=dapi&s=post&q=index&json=1")
    fun getPosts(
        @Query("limit") limit: Int, @Query("pid") page: Page, @Query("tags") tags: Tags
    ): Call<ByteArray>

}

interface GelbooruApiXml {

}
