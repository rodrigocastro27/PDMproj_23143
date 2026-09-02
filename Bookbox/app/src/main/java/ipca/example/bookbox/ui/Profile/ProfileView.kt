package ipca.example.bookbox.ui.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import ipca.example.bookbox.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ipca.example.bookbox.models.Review
import ipca.example.bookbox.ui.Homepage.BookItem
import ipca.example.bookbox.ui.components.Screen



@Composable
fun ProfileView(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState
    var selectedTab by remember { mutableStateOf("reviews") }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate(Screen.EditProfile.route) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                }
                Text(
                    text = "@${uiState.userProfile.username ?: "user"}",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { navController.navigate(Screen.AccountSettings.route) }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            }

            AsyncImage(
                model = uiState.userProfile.photoUrl ?: R.drawable.default_avatar,
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "${uiState.userProfile.firstName ?: ""} ${uiState.userProfile.lastName ?: ""}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = uiState.userProfile?.bio ?: "Add a bio to your profile",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp)
            )


            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { selectedTab = "reviews" },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == "reviews") Color(0xFF625b71) else Color(0xFFEADDFF),
                        contentColor = if (selectedTab == "reviews") Color.White else Color(0xFF21005D)
                    )
                ) { Text("Reviews") }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = { selectedTab = "wishlist" },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == "wishlist") Color(0xFF625b71) else Color(0xFFEADDFF),
                        contentColor = if (selectedTab == "wishlist") Color.White else Color(0xFF21005D)
                    )
                ) { Text("Wishlist") }
            }


            if (selectedTab == "reviews") {
                if (uiState.myReviews.isEmpty()) {
                    Text("No reviews yet.", modifier = Modifier.padding(top = 20.dp))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.myReviews) { review -> ReviewCard(review) }
                    }
                }
            } else {
                if (uiState.myWishlist.isEmpty()) {
                    Text("Wishlist is empty.", modifier = Modifier.padding(top = 20.dp))
                } else {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.myWishlist) { book ->
                            BookItem(
                                book = book,
                                isMyBook = false,
                                onAddToWishlist = {  },
                                onAddToProgress = {
                                    viewModel.addToProgress(book)
                                },
                                onMakeReview = {
                                    navController.navigate(Screen.MakeReview.createRoute(book.bookid, book.title ?: "", book.author ?: "", book.coverUrl ?: "" ))
                                },
                                onClick = { navController.navigate(Screen.BookDetails.createRoute(book.bookid))
                                }
                            )
                        }
                    }
                }
            }
        }
}


@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F0F5)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = review.bookCover ?: R.drawable.book_placeholder,
                contentDescription = null,
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(review.bookTitle ?: "Unknown Book", fontWeight = FontWeight.Bold, maxLines = 1)
                Text(review.bookAuthor ?: "Unknown Author", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    repeat(5) { index ->
                        Icon(Icons.Default.Star, null, tint = if (index < review.rating) Color(0xFFFFD700) else Color.LightGray, modifier = Modifier.size(16.dp))
                    }
                }
                Text(review.reviewText ?: "", style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}