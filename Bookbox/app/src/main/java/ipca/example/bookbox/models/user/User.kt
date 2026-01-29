package ipca.example.bookbox.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey var userid: String = "",
    var firstName: String? = null,
    var lastName: String? = null,
    var username: String? = null,
    var birthDate: Long? = null,
    var photoUrl: String? = null,
    var booksReadCount: Int = 0,
    var email: String = "",
    var bio: String? = null
)