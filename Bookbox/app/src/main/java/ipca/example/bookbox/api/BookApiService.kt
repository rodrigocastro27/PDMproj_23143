package ipca.example.bookbox.api.data

import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 100,
        @Query("key") apiKey: String = "AIzaSyCPR_uyIYSzMdVL8u5h5f71Uw80j9ny-Hw"
    ): GoogleBooks
}