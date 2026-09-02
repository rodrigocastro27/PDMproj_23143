package ipca.example.bookbox.models

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

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

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE userid = :id")
    suspend fun getUserById(id: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}