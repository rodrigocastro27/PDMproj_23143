package ipca.example.bookbox.ui.Profile

import ipca.example.bookbox.models.book.Book
import ipca.example.bookbox.models.review.Review
import ipca.example.bookbox.models.user.User


data class ProfileViewState(
    val userProfile: User = User(),
    val myReviews: List<Review> = emptyList(),
    val myWishlist: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)