package ipca.example.bookbox.ui.components

import java.net.URLEncoder

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object AddBook : Screen("add_book")
    object InProgress : Screen("in_progress")
    object Profile : Screen("profile")
    object CreateBook : Screen("create_book")
    object EditProfile : Screen("edit_profile")
    object AccountSettings : Screen("account_setting")

    object UpdateProgress : Screen("update_progress/{bookId}") {
        fun createRoute(bookId: String) = "update_progress/$bookId"
    }

    object EditBook : Screen("edit_book/{bookId}") {
        fun createRoute(bookId: String) = "edit_book/$bookId"
    }

    object BookDetails : Screen("book_details/{bookId}") {
        fun createRoute(bookId: String) = "book_details/$bookId"
    }

    object MakeReview : Screen("make_review/{bookId}/{bookTitle}/{bookAuthor}/{bookCover}") {
        fun createRoute(bookId: String, title: String, author: String, cover: String): String {
            val eTitle = URLEncoder.encode(title, "UTF-8")
            val eAuthor = URLEncoder.encode(author, "UTF-8")
            val eCover = URLEncoder.encode(cover, "UTF-8")
            return "make_review/$bookId/$eTitle/$eAuthor/$eCover"
        }
    }

}