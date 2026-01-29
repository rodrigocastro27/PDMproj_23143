package ipca.example.bookbox.models.wishlistitem

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistItem(
    @PrimaryKey var wishlistid: String = "",
    var userid: String? = null,
    var bookid: String? = null,
    var addedDate: Long? = null
)