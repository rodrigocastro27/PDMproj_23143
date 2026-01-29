package ipca.example.bookbox.ui.Profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx. navigation.NavController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeReviewView(
    navController: NavController,
    bookId: String,
    bookTitle: String,
    bookAuthor: String,
    bookCover: String
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState


    val decodedTitle = remember(bookTitle) {
        URLDecoder.decode(bookTitle, StandardCharsets.UTF_8.toString())
    }
    val decodedAuthor = remember(bookAuthor) {
        URLDecoder.decode(bookAuthor, StandardCharsets.UTF_8.toString())
    }
    val decodedCover = remember(bookCover) {
        URLDecoder.decode(bookCover, StandardCharsets.UTF_8.toString())
    }

    var rating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }


    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.resetSuccess()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Make a Review") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = decodedTitle, style = MaterialTheme.typography.headlineSmall)
            Text(text = "by $decodedAuthor", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))


            Text(text = "Rating: $rating / 5", style = MaterialTheme.typography.titleMedium)
            Row {
                repeat(5) { index ->
                    IconButton(onClick = { rating = index + 1 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < rating) Color(0xFFFFD700) else Color.LightGray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }


            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                label = { Text("Add a Comment") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )


            Button(
                onClick = {
                    viewModel.addReview(
                        bookId = bookId,
                        title = decodedTitle,
                        author = decodedAuthor,
                        cover = decodedCover,
                        rating = rating,
                        text = reviewText
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = rating > 0 && reviewText.isNotBlank() && !uiState.isLoading,
                shape = MaterialTheme.shapes.medium
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Submit Review")
                }
            }


            uiState.error?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}