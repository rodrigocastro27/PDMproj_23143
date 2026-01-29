package ipca.example.bookbox.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import ipca.example.bookbox.models.book.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    fun fetchReadingProgress(uid: String): Flow<ResultWrapper<List<Book>>> =
        db.collection("users").document(uid)
            .collection("inprogress")
            .snapshots()
            .map { snapshot ->
                ResultWrapper.Success(snapshot.toObjects(Book::class.java))
            }
            .flowOn(Dispatchers.IO)


    fun updateProgress(uid: String, book: Book, currentPage: Int, notes: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())


            val progressData = book.copy(
                readPages = currentPage,
                personalNotes = notes
            )

            db.collection("users").document(uid)
                .collection("inprogress").document(book.bookid)
                .set(progressData).await()

            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Error removing from progress"))
        }
    }.flowOn(Dispatchers.IO)


    fun removeFromProgress(uid: String, bookId: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("users").document(uid)
                .collection("inprogress").document(bookId).delete().await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Error deleting"))
        }
    }.flowOn(Dispatchers.IO)
}