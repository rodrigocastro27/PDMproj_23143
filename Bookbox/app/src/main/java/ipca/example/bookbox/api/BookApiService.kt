package ipca.example.bookbox.api.data



import retrofit2.http.GET
import retrofit2.http.Query
import ipca.example.bookbox.BuildConfig
interface BookApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 100,
        @Query("key") apiKey: String = BuildConfig.GOOGLE_BOOKS_API_KEY
    ): GoogleBooks
}