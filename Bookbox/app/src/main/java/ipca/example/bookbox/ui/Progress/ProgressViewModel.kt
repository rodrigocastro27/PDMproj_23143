package ipca.example.bookbox.ui.Progress

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import ipca.example.bookbox.models.book.Book
import ipca.example.bookbox.repository.ProgressRepository
import ipca.example.bookbox.repository.ResultWrapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class ProgressViewState(
    val booksInProgress: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val repository: ProgressRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(ProgressViewState())
    val uiState: State<ProgressViewState> = _uiState

    private val uid = Firebase.auth.currentUser?.uid ?: ""

    init {
        if (uid.isNotEmpty()) loadProgress()
    }

    private fun loadProgress() {
        repository.fetchReadingProgress(uid).onEach { result ->
            when (result) {
                is ResultWrapper.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is ResultWrapper.Success -> _uiState.value = _uiState.value.copy(
                    booksInProgress = result.data ?: emptyList(),
                    isLoading = false
                )
                is ResultWrapper.Error -> _uiState.value = _uiState.value.copy(
                    error = result.message,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun updateBookProgress(book: Book, currentPage: Int, notes: String) {
        repository.updateProgress(uid, book, currentPage, notes).onEach { result ->
            if (result is ResultWrapper.Success) _uiState.value = _uiState.value.copy(isSuccess = true)
        }.launchIn(viewModelScope)
    }

    fun deleteProgress(bookId: String) {
        repository.removeFromProgress(uid, bookId).onEach { result ->
            if (result is ResultWrapper.Success) _uiState.value = _uiState.value.copy(isSuccess = true)
        }.launchIn(viewModelScope)
    }

    fun resetSuccess() { _uiState.value = _uiState.value.copy(isSuccess = false) }
}