package com.makentoshe.boorusdk.gelbooru

import com.makentoshe.boorusdk.base.*
import retrofit2.Call
import retrofit2.http.*

interface GelbooruApi {

    @GET("index.php")
    fun custom(@QueryMap query: Map<String, String>): Call<ByteArray>

    @GET("index.php?page=dapi&s=post&q=index")
    fun posts(@Query("id") id: Id): Call<ByteArray>

    @GET("index.php?page=dapi&s=post&q=index")
    fun posts(
        @Query("limit") limit: Int,
        @Query("pid") page: Page,
        @Query("tags") tags: Tags
    ): Call<ByteArray>

    @GET("index.php?page=autocomplete")
    fun autocomplete(@Query("term") term: String): Call<ByteArray>

    @GET("index.php?page=dapi&s=comment&q=index")
    fun comments(@Query("post_id") postId: Id): Call<ByteArray>

    @GET("index.php?page=dapi&s=comment&q=index")
    fun comments(): Call<ByteArray>

    @GET("index.php?page=dapi&s=tag&q=index")
    fun tags(@Query("id") id: Id): Call<ByteArray>

    @GET("index.php?page=dapi&s=tag&q=index")
    fun tags(@Query("name_pattern") pattern: String, @Query("limit") limit: Int): Call<ByteArray>

    @GET("index.php?page=dapi&s=tag&q=index")
    fun tags(
        @Query("name_pattern") pattern: String,
        @Query("limit") limit: Int,
        @Query("order") order: Order,
        @Query("orderby") orderby: Orderby
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
}