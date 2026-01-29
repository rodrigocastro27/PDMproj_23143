package ipca.example.bookbox.ui.Homepage

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ipca.example.bookbox.models.book.Book
import ipca.example.bookbox.repository.BookRepository
import ipca.example.bookbox.repository.ResultWrapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class HomeViewState(
    var exploreBooks: List<Book> = emptyList(),
    var userBooks: List<Book> = emptyList(),
    var filteredUserBooks: List<Book> = emptyList(),
    var searchQuery: String = "",
    var error: String? = null,
    var isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    var uiState = mutableStateOf(HomeViewState())
        private set

    init {
        fetchExploreBooks()
        fetchUserBooks()
    }

    fun onSearchQueryChange(query: String) {
        uiState.value = uiState.value.copy(searchQuery = query)


        val filteredLocal = if (query.isBlank()) {
            uiState.value.userBooks
        } else {
            uiState.value.userBooks.filter {
                it.title?.contains(query, ignoreCase = true) == true ||
                        it.author?.contains(query, ignoreCase = true) == true
            }
        }
        uiState.value = uiState.value.copy(filteredUserBooks = filteredLocal)


        fetchExploreBooks(query)
    }

    fun fetchExploreBooks(query: String = "subject:fiction") {
        val searchFor = if (query.isBlank()) "subject:fiction" else query
        bookRepository.fetchExploreBooks(searchFor).onEach { result ->
            when (result) {
                is ResultWrapper.Loading -> uiState.value = uiState.value.copy(isLoading = true)
                is ResultWrapper.Success -> uiState.value = uiState.value.copy(
                    exploreBooks = result.data ?: emptyList(),
                    isLoading = false,
                    error = null
                )
                is ResultWrapper.Error -> uiState.value = uiState.value.copy(isLoading = false, error = result.message)
            }
        }.launchIn(viewModelScope)
    }

    fun fetchUserBooks() {
        bookRepository.fetchUserBooks().onEach { result ->
            when (result) {
                is ResultWrapper.Loading -> uiState.value = uiState.value.copy(isLoading = true)
                is ResultWrapper.Success -> {
                    val books = result.data ?: emptyList()
                    uiState.value = uiState.value.copy(
                        userBooks = books,
                        filteredUserBooks = books,
                        isLoading = false
                    )
                }
                is ResultWrapper.Error -> uiState.value = uiState.value.copy(isLoading = false, error = result.message)
            }
        }.launchIn(viewModelScope)
    }
}