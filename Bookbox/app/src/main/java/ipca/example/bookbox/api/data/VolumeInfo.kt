package ipca.example.bookbox.api.data


data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val pageCount: Int?,
    val imageLinks: ImageLinks?
)

