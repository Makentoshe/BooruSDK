import com.makentoshe.boorusdk.base.model.*
import retrofit2.Call
import retrofit2.http.*

interface DanbooruApi {

    @GET("posts/{id}.{type}")
    fun getPost(
        @Path("id") id: Int,
        @Path("type") type: String
    ): Call<ByteArray>

    @GET("posts.{type}")
    fun getPosts(
        @Path("type") type: String,
        @Query("limit") count: Count? = Count(10),
        @Query("page") page: Page? = Page(0),
        @Query("tags") tags: Tags? = Tags(emptySet()),
        @Query("md5") md5: Md5? = null,
        @Query("random") random: Random? = null,
        @Query("raw") raw: String? = null
    ): Call<ByteArray>

    @GET("tags/autocomplete.{type}?search[name_matches]=tou")
    fun getAutocomplete(
        @Path("type") type: String,
        @Query("search[name_matches]") term: String
    ): Call<ByteArray>

    @GET("posts/{postId}")
    fun getPostHttp(
        @Path("postId") id: Int
    ): Call<ByteArray>

    @GET("tags/{id}.{type}")
    fun getTag(
        @Path("id") id: Int,
        @Path("type") type: String
    ): Call<ByteArray>

    @GET("tags.{type}")
    fun getTags(
        @Path("type") type: String,
        @Query("search[name_matches]") pattern: String? = null,
        @Query("search[name]") name: String? = null,
        @Query("search[hide_empty]") hideEmpty: String? = null,
        @Query("search[has_wiki]") hasWiki: String? = null,
        @Query("search[has_artist]") hasArtist: String? = null,
        @Query("search[order]") order: String? = null,
        @Query("search[category]") category: Int? = null
    ): Call<ByteArray>

    @FormUrlEncoded
    @POST("session")
    fun login(
        @Field("authenticity_token") token: String,
        @Field("name") username: String,
        @Field("password") password: String,
        @Field("remember") remember: Int = 1,
        @Field("commit") commit: String = "Submit",
        @Field("url") url: String = ""
    ): Call<ByteArray>

    @GET("session/new")
    fun newSession(): Call<ByteArray>

    // TODO implement using Gold Account permissions
    @FormUrlEncoded
    @POST("posts/{id}/votes.json")
    fun votePost(
        @Path("id") id: Int,
        @Field("score") score: String,
        @Field("authenticity_token") token: String
    ): Call<ByteArray>

    @GET("comments/{id}.{type}")
    fun getComment(
        @Path("type") type: String,
        @Path("id") commentId: Int
    ) : Call<ByteArray>

    @GET("comments.{type}?group_by=comment")
    fun getComments(
        @Path("type") type: String,
        @Query("limit") count: Int? = null,
        @Query("page") page: Int? = null,
        @Query("search[post_id]") postId: Int? = null,
        @Query("search[post_tag_match]") postsTagMatch: String? = null,
        @Query("search[creator_name]") creatorName: String? = null,
        @Query("search[creator_id]") creatorId: Int? = null,
        @Query("search[is_deleted]") isDeleted: Boolean? = null
    ): Call<ByteArray>

    @FormUrlEncoded
    @POST("comments.{type}")
    fun createComment(
        @Path("type") type: String,
        @Field("comment[post_id]") postId: Int,
        @Field("comment[body]") body: String,
        @Field("comment[do_not_bump_post]") doNotBumpPost: Boolean = false,
        @Field("authenticity_token") token: String,
        @Field("commit") commit: String? = "Submit"
    ): Call<ByteArray>

    @DELETE("/comments/{id}.{type}")
    fun deleteComment(
        @Path("id") commentId: Int,
        @Path("type") type: String,
        @Header("X-CSRF-Token") token: String
    ): Call<ByteArray>
}
