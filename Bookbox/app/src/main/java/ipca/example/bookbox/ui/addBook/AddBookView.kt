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
import ipca.example.bookbox.ui.Homepage.BookItem
import ipca.example.bookbox.ui.Homepage.SectionHeader
import ipca.example.bookbox.ui.components.Screen

@Composable
fun AddBookView(navController: NavController) {
    val viewModel: AddBookViewModel = hiltViewModel()
    val uiState by viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.fetchUserBooks()
    }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = { navController.navigate(Screen.CreateBook.route) },
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

                            navController.navigate(Screen.EditBook.createRoute(book.bookid))
                        }
                    )
                }
            }
        }
}
