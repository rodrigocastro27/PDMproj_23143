package ipca.example.bookbox.repository

import com.google.firebase.firestore.FirebaseFirestore
import ipca.example.bookbox.models.Book
import ipca.example.bookbox.models.BookDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val bookDao: BookDao
) {

    fun fetchReadingProgress(uid: String): Flow<ResultWrapper<List<Book>>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("users").document(uid).collection("inprogress").snapshotFlow().collect { snapshot ->
                val books = snapshot.toObjects(Book::class.java)
                books.forEach { bookDao.insertBook(it) }
                emit(ResultWrapper.Success(books))
            }
        } catch (e: Exception) {
            val cached = bookDao.getBooksByUser(uid).filter { it.readPages > 0 }
            if (cached.isNotEmpty()) emit(ResultWrapper.Success(cached))
            else emit(ResultWrapper.Error(e.localizedMessage ?: "Error loading progress"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateProgress(uid: String, book: Book, currentPage: Int, notes: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            val progressData = book.copy(readPages = currentPage, personalNotes = notes)
            db.collection("users").document(uid).collection("inprogress").document(book.bookid).set(progressData).await()
            bookDao.insertBook(progressData)
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Error removing from progress"))
        }
    }.flowOn(Dispatchers.IO)

    fun removeFromProgress(uid: String, bookId: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("users").document(uid).collection("inprogress").document(bookId).delete().await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Error deleting"))
        }
    }.flowOn(Dispatchers.IO)
}