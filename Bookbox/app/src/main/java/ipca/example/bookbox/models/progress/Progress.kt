package ipca.example.bookbox.models.progress


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
data class Progress(
    @PrimaryKey var progressid: String = "",
    var userid: String? = null,
    var bookid: String? = null,
    var currentPage: Int = 0,
    var notes: String = "",
    var lastRead: Long? = null
)