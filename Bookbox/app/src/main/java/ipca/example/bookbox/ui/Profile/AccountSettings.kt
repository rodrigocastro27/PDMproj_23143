package ipca.example.bookbox.ui.Profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsView(navController: NavController) {

    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState


    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("********") }
    var isEditingPassword by remember { mutableStateOf(false) }


    LaunchedEffect(uiState.userProfile) {
        username = uiState.userProfile.username ?: ""
    }


    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.resetSuccess()

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account Settings") },
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
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Account Information",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )

            Spacer(Modifier.height(16.dp))


            OutlinedTextField(
                value = uiState.userProfile.email,
                onValueChange = {},
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            Spacer(Modifier.height(12.dp))


            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.Edit, null) }
            )

            Spacer(Modifier.height(12.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { if (isEditingPassword) password = it },
                label = { Text(if (isEditingPassword) "New Password" else "Password") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditingPassword,
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        isEditingPassword = true
                        password = ""
                    }) {
                        Icon(if (isEditingPassword) Icons.Default.Lock else Icons.Default.Edit, null)
                    }
                }
            )


            Button(
                onClick = {

                    viewModel.updateAccountSettings(username, uiState.userProfile.email)


                    if (isEditingPassword && password.isNotBlank()) {
                        viewModel.updatePassword(password)
                        isEditingPassword = false
                        password = "********"
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Update Account")
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            // ÁREA DE PERIGO (Account Status)
            Text(
                text = "Account Status",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red
            )

            Text(
                text = "Warning: Deleting your account will permanently remove all your book data, reviews, and profile information. This action cannot be undone.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    viewModel.deleteUserAccount {

                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Delete Account Permanently", color = Color.White)
            }
        }
    }
}