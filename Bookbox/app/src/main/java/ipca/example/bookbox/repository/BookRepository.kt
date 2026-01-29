package ipca.example.bookbox.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import ipca.example.bookbox.api.data.BookApiService
import ipca.example.bookbox.api.data.BookItem
import ipca.example.bookbox.models.book.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val apiService: BookApiService,
    private val db: FirebaseFirestore
) {
    fun fetchExploreBooks(query: String): Flow<ResultWrapper<List<Book>>> = flow {
        try {
            emit(ResultWrapper.Loading())
            val response = apiService.searchBooks(query = query, maxResults = 20)
            val books = response.items?.map { it.toBook() } ?: emptyList()
            emit(ResultWrapper.Success(books))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "API Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun fetchUserBooks(): Flow<ResultWrapper<List<Book>>> = flow {
        try {
            emit(ResultWrapper.Loading())
            val currentUserId = Firebase.auth.currentUser?.uid ?: throw Exception("User not logged in")
            db.collection("books")
                .whereEqualTo("createdBy", currentUserId)
                .snapshotFlow()
                .collect { snapshot ->
                    val books = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Book::class.java)?.apply { if (bookid.isEmpty()) bookid = doc.id }
                    }
                    emit(ResultWrapper.Success(books))
                }
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Firestore Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun saveManualBook(book: Book): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            val userId = Firebase.auth.currentUser?.uid ?: throw Exception("Login required")
            val finalId = book.bookid.ifEmpty { db.collection("books").document().id }
            val bookToSave = book.copy(bookid = finalId, createdBy = userId, isManual = true)
            db.collection("books").document(finalId).set(bookToSave).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Error Saving"))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteManualBook(bookId: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("books").document(bookId).delete().await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Error Deleting"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getBookById(bookId: String): Book? {
        return try {
            db.collection("books").document(bookId).get().await().toObject(Book::class.java)
        } catch (e: Exception) { null }
    }

    fun addToWishlist(book: Book): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            val userId = Firebase.auth.currentUser?.uid ?: throw Exception("Login required")
            db.collection("users").document(userId).collection("wishlist").document(book.bookid).set(book).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) { emit(ResultWrapper.Error(e.localizedMessage ?: "Wishlist Error")) }
    }.flowOn(Dispatchers.IO)

    fun saveReview(bookId: String, rating: Int, comment: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            val userId = Firebase.auth.currentUser?.uid ?: throw Exception("Login required")
            val reviewData = mapOf("userId" to userId, "rating" to rating, "comment" to comment, "timestamp" to System.currentTimeMillis())
            db.collection("books").document(bookId).collection("reviews").document(userId).set(reviewData).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) { emit(ResultWrapper.Error(e.localizedMessage ?: "Error review")) }
    }.flowOn(Dispatchers.IO)
}

fun BookItem.toBook(): Book = Book(
    bookid = this.id,
    title = this.volumeInfo.title ?: "No Title",
    author = this.volumeInfo.authors?.joinToString(", ") ?: "Unknown Author",
    coverUrl = this.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:"),
    isManual = false
)