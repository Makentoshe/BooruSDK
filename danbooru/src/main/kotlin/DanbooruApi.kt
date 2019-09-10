import com.makentoshe.boorusdk.base.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DanbooruApi {

    @GET("posts/{id}.{type}")
    fun getPost(@Path("id") id: Int, @Path("type") type: String): Call<ByteArray>

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

    @GET("https://danbooru.donmai.us/comments.{type}?group_by=comment")
    fun getComments(
        @Path("type") type: String,
        @Query("limit") count: Int? = null,
        @Query("page") page: Int? = null,
        @Query("search[post_id]") postId: Int? = null,
        @Query("search[post_tag_match]") postsTagMatch: String? = null,
        @Query("search[creator_name]") creatorName: String? = null,
        @Query("search[creator_id]") creatorId: Int? = null,
        @Query("search[is_deleted]") isDeleted: Boolean? = null
    ): String
}