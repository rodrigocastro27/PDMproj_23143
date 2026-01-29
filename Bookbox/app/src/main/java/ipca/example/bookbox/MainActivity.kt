package ipca.example.bookbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import ipca.example.bookbox.ui.Authentication.ForgotPasswordView
import ipca.example.bookbox.ui.Authentication.LoginView
import ipca.example.bookbox.ui.Authentication.RegisterView
import ipca.example.bookbox.ui.addBook.AddBookView
import ipca.example.bookbox.ui.addBook.CreateBookView
import ipca.example.bookbox.ui.addBook.EditBookView
import ipca.example.bookbox.ui.Profile.ProfileView
import ipca.example.bookbox.ui.components.MyBottomBar
import ipca.example.bookbox.ui.Profile.AccountSettingsView
import ipca.example.bookbox.ui.Profile.EditProfileView
import ipca.example.bookbox.ui.Profile.MakeReviewView
import ipca.example.bookbox.ui.Homepage.HomeView
import ipca.example.bookbox.ui.theme.BookboxTheme
import ipca.example.bookbox.ui.Progress.InProgressView
import ipca.example.bookbox.ui.Progress.UpdateProgressView

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var navTitle by remember { mutableStateOf("Bookbox") }
            var isAuthScreen by remember { mutableStateOf(true) }
            var showBackButton by remember { mutableStateOf(false) }
            var hideGlobalTopBar by remember { mutableStateOf(false) }

            BookboxTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (!isAuthScreen && !hideGlobalTopBar) {
                            TopAppBar(
                                title = { Text(navTitle) },
                                navigationIcon = {
                                    if (showBackButton) {
                                        IconButton(onClick = { navController.popBackStack() }) {
                                            Icon(Icons.Default.ArrowBack, "Back")
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = "login"
                    ) {
                        // --- Ecrãs de Autenticação ---
                        composable("login") {
                            isAuthScreen = true
                            LoginView(navController)
                        }
                        composable("register") {
                            isAuthScreen = true
                            RegisterView(navController)
                        }
                        composable("forgot_password") {
                            isAuthScreen = true
                            ForgotPasswordView(navController)
                        }

                        // --- Ecrãs Principais ---
                        composable("home") {
                            navTitle = "Home"
                            isAuthScreen = false
                            showBackButton = false
                            hideGlobalTopBar = false
                            HomeView(navController)
                        }
                        composable("add_book") {
                            navTitle = "Add Book"
                            isAuthScreen = false
                            showBackButton = false
                            hideGlobalTopBar = false
                            AddBookView(navController)
                        }
                        composable("inprogress") { // Antes estava "progress"
                            navTitle = "Reading Progress"
                            isAuthScreen = false
                            showBackButton = false
                            hideGlobalTopBar = false
                            InProgressView(navController)
                        }
                        composable(
                            route = "update_progress/{bookId}",
                            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                            UpdateProgressView(navController, bookId)
                        }
                        composable("profile") {
                            isAuthScreen = false
                            hideGlobalTopBar = true
                            ProfileView(navController)
                        }

                        // --- Fluxos Secundários ---
                        composable("create_book") {
                            isAuthScreen = false
                            hideGlobalTopBar = true
                            CreateBookView(navController)
                        }

                        composable(
                            route = "edit_book/{bookId}",
                            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                            isAuthScreen = false
                            hideGlobalTopBar = true
                            EditBookView(navController, bookId)
                        }

                        composable("edit_profile") {
                            isAuthScreen = false
                            hideGlobalTopBar = true
                            EditProfileView(navController)
                        }

                        composable("account_settings") {
                            isAuthScreen = false
                            hideGlobalTopBar = true
                            AccountSettingsView(navController)
                        }


                        composable(
                            route = "make_review/{bookId}/{bookTitle}/{bookAuthor}/{bookCover}",
                            arguments = listOf(
                                navArgument("bookId") { type = NavType.StringType },
                                navArgument("bookTitle") { type = NavType.StringType },
                                navArgument("bookAuthor") { type = NavType.StringType },
                                navArgument("bookCover") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                            val bookTitle = backStackEntry.arguments?.getString("bookTitle") ?: ""
                            val bookAuthor = backStackEntry.arguments?.getString("bookAuthor") ?: ""
                            val bookCover = backStackEntry.arguments?.getString("bookCover") ?: ""

                            MakeReviewView(
                                navController = navController,
                                bookId = bookId,
                                bookTitle = bookTitle,
                                bookAuthor = bookAuthor,
                                bookCover = bookCover
                            )
                        }
                    }
                }
            }
        }
    }
}