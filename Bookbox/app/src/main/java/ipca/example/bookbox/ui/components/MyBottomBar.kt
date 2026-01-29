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
fun MyBottomBar(navController: NavController, currentRoute: String) {
    BottomAppBar {
        NavigationBarItem(
            selected = currentRoute == "home",
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            onClick = { if (currentRoute != "home") navController.navigate("home") }
        )
        NavigationBarItem(
            selected = currentRoute == "add",
            icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
            label = { Text("Add") },
            onClick = { if (currentRoute != "add") navController.navigate("add_book") }
        )
        NavigationBarItem(

            selected = currentRoute == "inprogress",
            onClick = {
                if (currentRoute != "inprogress") {
                    navController.navigate("inprogress") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Progress") },
            label = { Text("In Progress") }
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            onClick = { if (currentRoute != "profile") navController.navigate("profile") }
        )
    }
}