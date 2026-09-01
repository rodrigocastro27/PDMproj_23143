package ipca.example.bookbox.api.data

import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 100,
        @Query("key") apiKey: String = "AIzaSyDh3EZjbU6DNaACOMV8D4l4dbaw7PnkNiA"
    ): GoogleBooks
}