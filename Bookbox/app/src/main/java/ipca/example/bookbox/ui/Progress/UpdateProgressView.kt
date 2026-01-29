package ipca.example.bookbox.ui.Progress

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProgressView(navController: NavController, bookId: String) {
    val viewModel: ProgressViewModel = hiltViewModel()
    val uiState by viewModel.uiState


    val book = uiState.booksInProgress.find { it.bookid == bookId }


    var currentPage by remember { mutableStateOf("") }
    var totalPages by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }


    LaunchedEffect(book) {
        book?.let {
            currentPage = it.readPages.toString()
            totalPages = (it.pageCount ?: 0).toString()
            notes = it.personalNotes ?: ""
        }
    }


    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.resetSuccess()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Progress") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        book?.let { currentBook ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = currentBook.title ?: "",
                    onValueChange = {},
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    trailingIcon = { Icon(Icons.Default.Lock, null) }
                )

                OutlinedTextField(
                    value = currentBook.author ?: "",
                    onValueChange = {},
                    label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    trailingIcon = { Icon(Icons.Default.Lock, null) }
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                    OutlinedTextField(
                        value = currentPage,
                        onValueChange = { currentPage = it },
                        label = { Text("Current Page") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )


                    OutlinedTextField(
                        value = totalPages,
                        onValueChange = { totalPages = it },
                        label = { Text("Total Pages") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        enabled = true
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Personal Notes") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    placeholder = { Text("Notes") }
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {

                        val updatedBook = currentBook.copy(
                            pageCount = totalPages.toIntOrNull() ?: 0
                        )
                        viewModel.updateBookProgress(
                            book = updatedBook,
                            currentPage = currentPage.toIntOrNull() ?: 0,
                            notes = notes
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Save Progress")
                }

                TextButton(
                    onClick = { viewModel.deleteProgress(currentBook.bookid) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Remove from Progress")
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
}