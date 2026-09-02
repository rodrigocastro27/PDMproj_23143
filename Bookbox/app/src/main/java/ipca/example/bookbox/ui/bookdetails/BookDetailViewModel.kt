package ipca.example.bookbox.ui.bookdetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ipca.example.bookbox.models.Book
import ipca.example.bookbox.repository.BookRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookDetailViewState(
    val book: Book? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    var uiState = mutableStateOf(BookDetailViewState())
        private set

    fun loadBook(bookId: String) {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true)
            val book = repository.getBookById(bookId)
            uiState.value = if (book != null) {
                uiState.value.copy(book = book, isLoading = false)
            } else {
                uiState.value.copy(isLoading = false, error = "Livro não encontrado")
            }
        }
    }
}