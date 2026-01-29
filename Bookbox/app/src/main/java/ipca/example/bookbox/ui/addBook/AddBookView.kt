package ipca.example.bookbox.ui.addBook

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ipca.example.bookbox.ui.Homepage.HomeViewModel
import ipca.example.bookbox.ui.components.MyBottomBar
import ipca.example.bookbox.ui.Homepage.BookItem
import ipca.example.bookbox.ui.Homepage.SectionHeader

@Composable
fun AddBookView(navController: NavController) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val uiState by homeViewModel.uiState

    Scaffold(
        bottomBar = { MyBottomBar(navController, "add") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Button(
                onClick = { navController.navigate("create_book") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Create New Book")
            }

            SectionHeader("Manage Your Books")

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.userBooks) { book ->

                    BookItem(
                        book = book,
                        isMyBook = true,
                        onAddToWishlist = { },
                        onAddToProgress = {  },
                        onMakeReview = {  },
                        onClick = {

                            navController.navigate("edit_book/${book.bookid}")
                        }
                    )
                }
            }
        }
    }
}