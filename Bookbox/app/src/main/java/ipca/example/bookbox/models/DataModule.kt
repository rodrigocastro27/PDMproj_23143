package ipca.example.bookbox.models

import android.content.Context
import ipca.example.bookbox.models.book.BookDao
import ipca.example.bookbox.models.user.UserDao
import ipca.example.bookbox.models.review.ReviewDao
import ipca.example.bookbox.models.wishlistitem.WishlistItemDao
import ipca.example.bookbox.models.progress.ProgressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideBookDao(db: AppDatabase): BookDao = db.bookDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideReviewDao(db: AppDatabase): ReviewDao = db.reviewDao()

    @Provides
    fun provideWishlistDao(db: AppDatabase): WishlistItemDao = db.wishlistDao()

    @Provides
    fun provideProgressDao(db: AppDatabase): ProgressDao = db.progressDao()
}