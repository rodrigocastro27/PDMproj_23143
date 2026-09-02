package ipca.example.bookbox.ui.Profile


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ipca.example.bookbox.models.Review
import ipca.example.bookbox.repository.AuthenticationRepository
import ipca.example.bookbox.repository.ProfileRepository
import ipca.example.bookbox.repository.ResultWrapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.UUID
import javax.inject.Inject


data class MakeReviewViewState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MakeReviewViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val authrepository: AuthenticationRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(MakeReviewViewState())
    val uiState: State<MakeReviewViewState> = _uiState

    fun addReview(bookid: String, title: String, author: String, cover: String, rating: Int, text:String) {
        val uid = authrepository.getCurrentUid() ?: return
        val review = Review(
            reviewid = UUID.randomUUID().toString(), userid = uid, bookid = bookid, bookTitle = title, bookAuthor = author, bookCover = cover, rating = rating, reviewText = text, timestamp = System.currentTimeMillis()
        )
        repository.addReview(review).onEach { result ->
            when (result){
                is ResultWrapper.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is ResultWrapper.Success -> _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                is ResultWrapper.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
            }
        }.launchIn(viewModelScope)
    }
}