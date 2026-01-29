package ipca.example.bookbox.ui.Progress

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ipca.example.bookbox.models.book.Book
import ipca.example.bookbox.ui.components.MyBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InProgressView(navController: NavController) {
    val viewModel: ProgressViewModel = hiltViewModel()
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Reading Progress", fontWeight = FontWeight.Bold) })
        },
        bottomBar = { MyBottomBar(navController, "inprogress") }
    ) { padding ->

        if (uiState.booksInProgress.isEmpty() && !uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No books in progress.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.booksInProgress) { book ->
                    ProgressBookItem(book, onEdit = {
                        navController.navigate("update_progress/${book.bookid}")
                    })
                }
            }
        }
    }
}

@Composable
fun ProgressBookItem(book: Book, onEdit: () -> Unit) {
    val totalPages = book.pageCount ?: 0
    val readPages = book.readPages
    val progressFraction = if (totalPages > 0) (readPages.toFloat() / totalPages.toFloat()).coerceIn(0f, 1f) else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F0F5)),
        shape = RoundedCornerShape(16.dp),
        onClick = onEdit
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = book.coverUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(book.title ?: "Unknown", fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = Color(0xFF6750A4),
                    trackColor = Color(0xFFEADDFF)
                )
                Text("${(progressFraction * 100).toInt()}% ($readPages / $totalPages)", fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}