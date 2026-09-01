package ipca.example.bookbox.models.wishlistitem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "wishlist")
data class WishlistItem(
    @PrimaryKey var wishlistid: String = "",
    var userid: String? = null,
    var bookid: String? = null,
    var addedDate: Long? = null
)

@Dao
interface WishlistItemDao {
    @Query("SELECT * FROM wishlist WHERE userid = :uid")
    fun getMyWishlist(uid: String): Flow<List<WishlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlist(item: WishlistItem)

    @Delete
    suspend fun removeWishlist(item: WishlistItem)

    @Query("DELETE FROM wishlist")
    suspend fun deleteAll()
}