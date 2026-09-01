package ipca.example.bookbox.models.review

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey var reviewid: String = "",
    var userid: String? = null,
    var bookid: String? = null,
    var bookTitle: String? = null,  // NOVO
    var bookAuthor: String? = null, // NOVO
    var bookCover: String? = null,  // NOVO
    var rating: Int = 0,
    var reviewText: String? = null,
    var timestamp: Long? = null
)

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE userid = :uid")
    fun getReviewsByUser(uid: String): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)

    @Delete
    suspend fun deleteReview(review: Review)

    @Query("DELETE FROM reviews")
    suspend fun deleteAll()
}