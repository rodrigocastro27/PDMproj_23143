package ipca.example.bookbox.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MyBottomBar(navController: NavController) {
    BottomAppBar {
        NavigationBarItem(
            selected = true,
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home") },
            label = { Text("Home") },
            onClick = { navController.navigate("home") }
        )

        NavigationBarItem(
            selected = true,
            icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
            label = { Text("Add") },
            onClick = { navController.navigate("add_book") }
        )

        NavigationBarItem(

            selected = true,
            onClick = {
                navController.navigate("inprogress")
            },
            icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Progress") },
            label = { Text("In Progress") }
        )

        NavigationBarItem(
            selected = true,
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            onClick = { navController.navigate("profile") }
        )
    }
}