package ipca.example.bookbox.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "books")
data class Book(
    @PrimaryKey
    var bookid: String = "",
    var title: String? = null,
    var author: String? = null,
    var description: String? = null,
    var pageCount: Int? = 0,
    var coverUrl: String? = null,
    var isManual: Boolean = false,
    var createdBy: String? = null,
    var readPages: Int = 0,
    var personalNotes: String? = null
)

@Dao
interface BookDao {

    @Query("SELECT * FROM books WHERE createdBy = :uid")
    suspend fun getBooksByUser(uid: String): List<Book>

    @Query("SELECT * FROM books WHERE bookid = :id")
    suspend fun getBookById(id: String): Book?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("DELETE FROM books")
    suspend fun deleteAll()
}
