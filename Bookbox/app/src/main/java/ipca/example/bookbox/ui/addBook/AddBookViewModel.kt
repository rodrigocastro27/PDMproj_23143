package ipca.example.bookbox.ui.addBook

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ipca.example.bookbox.models.book.Book
import ipca.example.bookbox.repository.BookRepository
import ipca.example.bookbox.repository.ResultWrapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AddBookState(
    var title: String = "",
    var author: String = "",
    var description: String = "",
    var pages: String = "",
    var coverUrl: String = "",
    var isLoading: Boolean = false,
    var isSuccess: Boolean = false,
    var error: String? = null
)

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    var uiState = mutableStateOf(AddBookState())
        private set


    fun onTitleChange(new: String) { uiState.value = uiState.value.copy(title = new) }
    fun onAuthorChange(new: String) { uiState.value = uiState.value.copy(author = new) }
    fun onDescriptionChange(new: String) { uiState.value = uiState.value.copy(description = new) }
    fun onPagesChange(new: String) { uiState.value = uiState.value.copy(pages = new) }
    fun onCoverUrlChange(new: String) { uiState.value = uiState.value.copy(coverUrl = new) }

    fun resetState() { uiState.value = AddBookState() }


    fun loadBook(bookId: String) {
        viewModelScope.launch {
            val book = repository.getBookById(bookId)
            book?.let {
                uiState.value = uiState.value.copy(
                    title = it.title ?: "",
                    author = it.author ?: "",
                    description = it.description ?: "",
                    pages = it.pageCount?.toString() ?: "",
                    coverUrl = it.coverUrl ?: ""
                )
            }
        }
    }


    fun saveBook(bookId: String? = null) {
        val book = Book(
            bookid = bookId ?: "",
            title = uiState.value.title,
            author = uiState.value.author,
            description = uiState.value.description,
            pageCount = uiState.value.pages.toIntOrNull(),
            coverUrl = uiState.value.coverUrl,
            isManual = true
        )

        repository.saveManualBook(book).onEach { result ->
            when(result) {
                is ResultWrapper.Loading -> uiState.value = uiState.value.copy(isLoading = true)
                is ResultWrapper.Success -> uiState.value = uiState.value.copy(isLoading = false, isSuccess = true)
                is ResultWrapper.Error -> uiState.value = uiState.value.copy(isLoading = false, error = result.message)
            }
        }.launchIn(viewModelScope)
    }

    fun deleteBook(bookId: String) {
        repository.deleteManualBook(bookId).onEach { result ->
            if (result is ResultWrapper.Success) uiState.value = uiState.value.copy(isSuccess = true)
        }.launchIn(viewModelScope)
    }
}