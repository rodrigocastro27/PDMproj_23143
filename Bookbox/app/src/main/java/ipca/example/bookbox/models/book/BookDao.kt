package ipca.example.bookbox.models.book

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {


    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Book>>


    @Query("SELECT * FROM books WHERE bookid = :id")
    suspend fun getBookById(id: String): Book?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)


    @Delete
    suspend fun deleteBook(book: Book)


    @Query("DELETE FROM books")
    suspend fun deleteAll()
}