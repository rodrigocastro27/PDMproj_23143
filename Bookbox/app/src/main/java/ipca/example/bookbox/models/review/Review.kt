package ipca.example.bookbox.models.review

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey var reviewid: String = "",
    var userid: String? = null,
    var bookid: String? = null,
    var bookTitle: String? = null,  // NOVO
    var bookAuthor: String? = null, // NOVO
    var bookCover: String? = null,  // NOVO
    var rating: Int = 0,
    var reviewText: String? = null,
    var timestamp: Long? = null
)