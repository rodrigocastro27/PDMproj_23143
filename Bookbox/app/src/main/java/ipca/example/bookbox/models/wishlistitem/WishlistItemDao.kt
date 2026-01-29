package ipca.example.bookbox.models.wishlistitem

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistItemDao {
    @Query("SELECT * FROM wishlist WHERE userid = :uid")
    fun getMyWishlist(uid: String): Flow<List<WishlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlist(item: WishlistItem)

    @Delete
    suspend fun removeWishlist(item: WishlistItem)
}