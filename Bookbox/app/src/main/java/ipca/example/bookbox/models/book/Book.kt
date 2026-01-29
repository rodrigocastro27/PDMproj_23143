package ipca.example.bookbox.models.book

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "books")
data class Book(
    @PrimaryKey
    var bookid: String = "",
    var title: String? = null,
    var author: String? = null,
    var description: String? = null,
    var pageCount: Int? = 0,
    var coverUrl: String? = null,
    var isManual: Boolean = false,
    var createdBy: String? = null,
    var readPages: Int = 0,
    var personalNotes: String? = null
)


