package ipca.example.bookbox.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ipca.example.bookbox.models.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {

    fun login(id: String, pass: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            var email = id
            if (!id.contains("@")) {
                val snapshot = db.collection("users").whereEqualTo("username", id).get().await()
                if (snapshot.isEmpty) throw Exception("Username not found")
                email = snapshot.documents[0].getString("email") ?: ""
            }
            auth.signInWithEmailAndPassword(email, pass).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Login failed"))
        }
    }.flowOn(Dispatchers.IO)


    fun register(user: User, pass: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())


            val usernameQuery = db.collection("users")
                .whereEqualTo("username", user.username)
                .get()
                .await()

            if (!usernameQuery.isEmpty) {
                throw Exception("This username is already taken. Please choose another.")
            }


            val authResult = auth.createUserWithEmailAndPassword(user.email!!, pass).await()
            val uid = authResult.user?.uid ?: throw Exception("UID failure")

            val newUser = user.copy(userid = uid)
            db.collection("users").document(uid).set(newUser).await()

            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Registration failed"))
        }
    }.flowOn(Dispatchers.IO)


    fun resetPassword(email: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            auth.sendPasswordResetEmail(email).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Error sending link"))
        }
    }.flowOn(Dispatchers.IO)
}