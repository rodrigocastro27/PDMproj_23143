package ipca.example.bookbox.models.user


import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE userid = :id")
    suspend fun getUserById(id: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}