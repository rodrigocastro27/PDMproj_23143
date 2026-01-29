package ipca.example.bookbox

import ipca.example.bookbox.models.user.UserDao
import ipca.example.bookbox.models.book.BookDao
import ipca.example.bookbox.models.wishlistitem.WishlistItemDao
import ipca.example.bookbox.models.progress.ProgressDao
import ipca.example.bookbox.api.data.BookApiService
import ipca.example.bookbox.repository.BookRepository
import ipca.example.bookbox.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        userDao: UserDao
    ): AuthenticationRepository = AuthenticationRepository(auth, firestore)


    @Provides
    @Singleton
    fun provideBookRepository(
        apiService: BookApiService,
        bookDao: BookDao,
        firestore: FirebaseFirestore
    ): BookRepository {

        return BookRepository(apiService, firestore)
    }
}