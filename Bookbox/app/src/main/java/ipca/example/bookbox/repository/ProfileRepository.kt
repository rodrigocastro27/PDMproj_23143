package ipca.example.bookbox.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import ipca.example.bookbox.models.book.Book
import ipca.example.bookbox.models.review.Review
import ipca.example.bookbox.models.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    fun fetchProfile(uid: String): Flow<ResultWrapper<User>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("users").document(uid).snapshotFlow().collect { snapshot ->
                val user = snapshot.toObject(User::class.java) ?: User(userid = uid)
                emit(ResultWrapper.Success(user))
            }
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Erro ao carregar perfil"))
        }
    }.flowOn(Dispatchers.IO)


    fun fetchWishlist(uid: String): Flow<ResultWrapper<List<Book>>> = flow {
        try {
            emit(ResultWrapper.Loading())

            db.collection("users").document(uid)
                .collection("wishlist")
                .snapshotFlow()
                .collect { snapshot ->
                    val books = snapshot.toObjects(Book::class.java)
                    emit(ResultWrapper.Success(books))
                }
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Erro na Wishlist"))
        }
    }.flowOn(Dispatchers.IO)


    fun addReview(review: Review): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("reviews").document(review.reviewid).set(review).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Erro ao gravar review"))
        }
    }.flowOn(Dispatchers.IO)


    fun fetchUserReviews(uid: String): Flow<ResultWrapper<List<Review>>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("reviews")
                .whereEqualTo("userid", uid)
                .snapshotFlow()
                .collect { snapshot ->
                    val reviews = snapshot.toObjects(Review::class.java)
                    emit(ResultWrapper.Success(reviews))
                }
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Erro nas reviews"))
        }
    }.flowOn(Dispatchers.IO)


    fun addToWishlist(uid: String, book: Book): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("users").document(uid)
                .collection("wishlist").document(book.bookid).set(book).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Erro ao adicionar"))
        }
    }.flowOn(Dispatchers.IO)



    fun updateProfile(uid: String, updates: Map<String, Any>): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("users").document(uid).update(updates).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Error updating the profile" ))
        }
    }.flowOn(Dispatchers.IO)


    fun updateAccountSettings(uid: String, username: String, email: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("users").document(uid).update(
                "username", username,
                "email", email
            ).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Error updating the account"))
        }
    }.flowOn(Dispatchers.IO)


    fun deleteUserAccount(uid: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("users").document(uid).delete().await()
            Firebase.auth.currentUser?.delete()?.await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Error deleting the account"))
        }
    }.flowOn(Dispatchers.IO)


    fun updatePassword(newPassword: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            val user = Firebase.auth.currentUser
            user?.updatePassword(newPassword)?.await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Error changing password"))
        }
    }.flowOn(Dispatchers.IO)
}

