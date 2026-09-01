package ipca.example.bookbox.models.progress


import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "progress")
data class Progress(
    @PrimaryKey var progressid: String = "",
    var userid: String? = null,
    var bookid: String? = null,
    var currentPage: Int = 0,
    var notes: String = "",
    var lastRead: Long? = null
)

@Dao
interface ProgressDao {
    @Query("SELECT * FROM progress WHERE bookid = :bookId LIMIT 1")
    suspend fun getProgressByBook(bookId: String): Progress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProgress(progress: Progress)
}