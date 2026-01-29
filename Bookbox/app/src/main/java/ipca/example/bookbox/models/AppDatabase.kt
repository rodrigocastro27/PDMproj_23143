package ipca.example.bookbox.models


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ipca.example.bookbox.models.book.Book
import ipca.example.bookbox.models.book.BookDao
import ipca.example.bookbox.models.progress.ProgressDao
import ipca.example.bookbox.models.progress.Progress
import ipca.example.bookbox.models.user.User
import ipca.example.bookbox.models.user.UserDao
import ipca.example.bookbox.models.wishlistitem.WishlistItem
import ipca.example.bookbox.models.wishlistitem.WishlistItemDao
import ipca.example.bookbox.models.review.Review
import ipca.example.bookbox.models.review.ReviewDao



@Database(
    entities = [
        Book::class,
        User::class,
        Progress::class,
        WishlistItem::class,
        Review::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun userDao(): UserDao
    abstract fun progressDao(): ProgressDao
    abstract fun wishlistDao(): WishlistItemDao
    abstract fun reviewDao(): ReviewDao
    companion object {

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {


            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bookbox_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}