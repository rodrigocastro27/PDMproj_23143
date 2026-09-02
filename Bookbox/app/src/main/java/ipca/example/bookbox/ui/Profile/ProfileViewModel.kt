package ipca.example.bookbox.ui.Profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ipca.example.bookbox.models.Book
import ipca.example.bookbox.models.Review
import ipca.example.bookbox.models.User
import ipca.example.bookbox.repository.AuthenticationRepository
import ipca.example.bookbox.repository.ProfileRepository
import ipca.example.bookbox.repository.ProgressRepository
import ipca.example.bookbox.repository.ResultWrapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class ProfileViewState(
    val userProfile: User = User(),
    val myReviews: List<Review> = emptyList(),
    val myWishlist: List<Book> = emptyList()
)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val authRepository : AuthenticationRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {


    private val _uiState = mutableStateOf<ProfileViewState>(ProfileViewState())
    val uiState: State<ProfileViewState> = _uiState

    private val currentUid = authRepository.getCurrentUid()


    init {
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

    fun addToProgress(book: Book){
        val uid = currentUid ?: return
        progressRepository.updateProgress(uid, book, 0, "").launchIn(viewModelScope)
    }
}