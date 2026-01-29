package ipca.example.bookbox.models.progress


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM progress WHERE bookid = :bookId LIMIT 1")
    suspend fun getProgressByBook(bookId: String): Progress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProgress(progress: Progress)
}