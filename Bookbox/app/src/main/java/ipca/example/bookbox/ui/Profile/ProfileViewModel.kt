package ipca.example.bookbox.ui.Profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import ipca.example.bookbox.models.book.Book
import ipca.example.bookbox.models.review.Review
import ipca.example.bookbox.repository.ProfileRepository
import ipca.example.bookbox.repository.ResultWrapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {


    private val _uiState = mutableStateOf<ProfileViewState>(ProfileViewState())
    val uiState: State<ProfileViewState> = _uiState

    private val currentUid = Firebase.auth.currentUser?.uid

    init {
        fetchData()
    }

    private fun fetchData() {
        currentUid?.let { uid ->

            repository.fetchProfile(uid).onEach { result ->
                if (result is ResultWrapper.Success) _uiState.value = _uiState.value.copy(userProfile = result.data!!)
            }.launchIn(viewModelScope)


            repository.fetchUserReviews(uid).onEach { result ->
                if (result is ResultWrapper.Success) _uiState.value = _uiState.value.copy(myReviews = result.data ?: emptyList())
            }.launchIn(viewModelScope)


            repository.fetchWishlist(uid).onEach { result ->
                if (result is ResultWrapper.Success) {
                    _uiState.value = _uiState.value.copy(myWishlist = result.data ?: emptyList())
                }
            }.launchIn(viewModelScope)
        }
    }


    fun addToWishlist(book: Book) {
        val uid = currentUid ?: return
        repository.addToWishlist(uid, book).onEach { result ->
            if (result is ResultWrapper.Error) {
                _uiState.value = _uiState.value.copy(error = result.message)
            }
        }.launchIn(viewModelScope)
    }

    fun addReview(bookId: String, title: String, author: String, cover: String, rating: Int, text: String) {
        val uid = currentUid ?: return
        val review = Review(
            reviewid = java.util.UUID.randomUUID().toString(),
            userid = uid,
            bookid = bookId,
            bookTitle = title,
            bookAuthor = author,
            bookCover = cover,
            rating = rating,
            reviewText = text,
            timestamp = System.currentTimeMillis()
        )

        repository.addReview(review).onEach { result ->
            when (result) {
                is ResultWrapper.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is ResultWrapper.Success -> _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                is ResultWrapper.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
            }
        }.launchIn(viewModelScope)
    }

    fun updateProfile(fName: String, lName: String, bioStr: String, photo: String?) {
        currentUid?.let { uid ->
            val updates = mapOf(
                "firstName" to fName,
                "lastName" to lName,
                "bio" to bioStr,
                "photoUrl" to (photo ?: "")
            )
            repository.updateProfile(uid, updates).onEach { result ->
                if (result is ResultWrapper.Success) {
                    _uiState.value = _uiState.value.copy(isSuccess = true)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun updateAccountSettings(newUsername: String, newEmail: String) {
        currentUid?.let { uid ->
            repository.updateAccountSettings(uid, newUsername, newEmail).onEach { result ->
                if (result is ResultWrapper.Success) {
                    _uiState.value = _uiState.value.copy(isSuccess = true)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun deleteUserAccount(onDeleted: () -> Unit) {
        currentUid?.let { uid ->
            repository.deleteUserAccount(uid).onEach { result ->
                if (result is ResultWrapper.Success) onDeleted()
            }.launchIn(viewModelScope)
        }
    }

    fun updatePassword(newPass: String) {
        if (newPass.length < 6) {
            _uiState.value = _uiState.value.copy(error = "Password must be at least 6 characters long")
            return
        }
        repository.updatePassword(newPass).onEach { result ->
            when (result) {
                is ResultWrapper.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is ResultWrapper.Success -> _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                is ResultWrapper.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
            }
        }.launchIn(viewModelScope)
    }

    fun resetSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}