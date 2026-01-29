package ipca.example.bookbox.ui.Homepage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ipca.example.bookbox.ui.Profile.ProfileViewModel
import ipca.example.bookbox.ui.Progress.ProgressViewModel
import ipca.example.bookbox.ui.components.MyBottomBar
import java.net.URLEncoder


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val progressViewModel: ProgressViewModel = hiltViewModel()

    val uiState by homeViewModel.uiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("BookBox", fontWeight = FontWeight.Bold) }
            )
        },
        bottomBar = { MyBottomBar(navController, "home") }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            item {
                SearchBarSection(
                    query = uiState.searchQuery,
                    onQueryChange = { homeViewModel.onSearchQueryChange(it) }
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
                            onAddToWishlist = { profileViewModel.addToWishlist(book) },
                            onAddToProgress = { progressViewModel.updateBookProgress(book, 0, "") },
                            onMakeReview = {
                                val eTitle = URLEncoder.encode(book.title ?: "", "UTF-8")
                                val eAuthor = URLEncoder.encode(book.author ?: "", "UTF-8")
                                val eCover = URLEncoder.encode(book.coverUrl ?: "", "UTF-8")
                                navController.navigate("make_review/${book.bookid}/$eTitle/$eAuthor/$eCover")
                            },
                            onClick = { navController.navigate("book_details/${book.bookid}") }
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
                            isMyBook = true,
                            onAddToWishlist = { profileViewModel.addToWishlist(book) },
                            onAddToProgress = { progressViewModel.updateBookProgress(book, 0, "") },
                            onMakeReview = {
                                val eTitle = URLEncoder.encode(book.title ?: "", "UTF-8")
                                val eAuthor = URLEncoder.encode(book.author ?: "", "UTF-8")
                                val eCover = URLEncoder.encode(book.coverUrl ?: "", "UTF-8")
                                navController.navigate("make_review/${book.bookid}/$eTitle/$eAuthor/$eCover")
                            },
                            onClick = { navController.navigate("edit_book/${book.bookid}") }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}