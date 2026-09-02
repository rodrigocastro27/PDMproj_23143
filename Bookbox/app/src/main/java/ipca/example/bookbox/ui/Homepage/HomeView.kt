package ipca.example.bookbox.ui.Homepage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ipca.example.bookbox.ui.components.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                SearchBarSection(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) }
                )
            }

            item { SectionHeader("Explore New Books") }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.exploreBooks) { book ->
                        BookItem(
                            book = book,
                            isMyBook = false,
                            onAddToWishlist = { viewModel.addToWishlist(book) },
                            onAddToProgress = { viewModel.addToProgress(book) },
                            onMakeReview = {
                                navController.navigate(
                                    Screen.MakeReview.createRoute(book.bookid, book.title ?: "", book.author ?: "", book.coverUrl ?: "")
                                )
                            },
                            onClick = { navController.navigate(Screen.BookDetails.createRoute(book.bookid)) }
                        )
                    }
                }
            }


            item { SectionHeader("From You") }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(uiState.filteredUserBooks) { book ->
                        BookItem(
                            book = book,
                            isMyBook = false,
                            onAddToWishlist = { viewModel.addToWishlist(book) },
                            onAddToProgress = { viewModel.addToProgress(book) },
                            onMakeReview = {
                                navController.navigate(
                                    Screen.MakeReview.createRoute(book.bookid, book.title ?: "", book.author ?: "", book.coverUrl ?: "")
                                )
                            },
                            onClick = { navController.navigate(Screen.EditBook.createRoute(book.bookid)) }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
}
