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
import ipca.example.bookbox.ui.bookdetails.BookDetailView
import ipca.example.bookbox.ui.Profile.ProfileView
import ipca.example.bookbox.ui.components.MyBottomBar
import ipca.example.bookbox.ui.components.Screen
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
            var navTitle by remember { mutableStateOf("BookBox") }
            var showChrome by remember { mutableStateOf(false) }
            var showBackButton by remember { mutableStateOf(false) }

            BookboxTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (showChrome) {
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
                    },
                    bottomBar = {
                        if (showChrome && !showBackButton) {
                            MyBottomBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.Login.route
                    ) {
                        composable(Screen.Login.route) {
                            showChrome = false
                            LoginView(navController)
                        }
                        composable(Screen.Register.route) {
                            showChrome = false
                            RegisterView(navController)
                        }
                        composable(Screen.ForgotPassword.route) {
                            showChrome = false
                            ForgotPasswordView(navController)
                        }
                        composable(Screen.Home.route) {
                            navTitle = "BookBox"; showChrome = true; showBackButton = false
                            HomeView(navController)
                        }
                        composable(Screen.AddBook.route) {
                            navTitle = "Add Book"; showChrome = true; showBackButton = false
                            AddBookView(navController)
                        }
                        composable(Screen.InProgress.route) {
                            navTitle = "Reading Progress"; showChrome = true; showBackButton = false
                            InProgressView(navController)
                        }
                        composable(
                            route = Screen.UpdateProgress.route,
                            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            navTitle = "Update Progress"; showChrome = true; showBackButton = true
                            UpdateProgressView(navController, backStackEntry.arguments?.getString("bookId") ?: "")
                        }
                        composable(Screen.Profile.route) {
                            navTitle = "Profile"; showChrome = true; showBackButton = false
                            ProfileView(navController)
                        }
                        composable(
                            route = Screen.BookDetails.route,
                            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            navTitle = "Book Details"; showChrome = true; showBackButton = true
                            BookDetailView(navController, backStackEntry.arguments?.getString("bookId") ?: "")
                        }
                        composable(Screen.CreateBook.route) {
                            navTitle = "Add Book"; showChrome = true; showBackButton = true
                            CreateBookView(navController)
                        }
                        composable(
                            route = Screen.EditBook.route,
                            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            navTitle = "Edit Book"; showChrome = true; showBackButton = true
                            EditBookView(navController, backStackEntry.arguments?.getString("bookId") ?: "")
                        }
                        composable(Screen.EditProfile.route) {
                            navTitle = "Edit Profile"; showChrome = true; showBackButton = true
                            EditProfileView(navController)
                        }
                        composable(Screen.AccountSettings.route) {
                            navTitle = "Account Settings"; showChrome = true; showBackButton = true
                            AccountSettingsView(navController)
                        }
                        composable(
                            route = Screen.MakeReview.route,
                            arguments = listOf(
                                navArgument("bookId") { type = NavType.StringType },
                                navArgument("bookTitle") { type = NavType.StringType },
                                navArgument("bookAuthor") { type = NavType.StringType },
                                navArgument("bookCover") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            navTitle = "Write a Review"; showChrome = true; showBackButton = true
                            MakeReviewView(
                                navController = navController,
                                bookId = backStackEntry.arguments?.getString("bookId") ?: "",
                                bookTitle = backStackEntry.arguments?.getString("bookTitle") ?: "",
                                bookAuthor = backStackEntry.arguments?.getString("bookAuthor") ?: "",
                                bookCover = backStackEntry.arguments?.getString("bookCover") ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}