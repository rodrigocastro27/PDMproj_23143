package ipca.example.bookbox.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MyBottomBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar {
        NavigationBarItem(
            selected = true,
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home") },
            label = { Text("Home") },
            onClick = { navController.navigate(Screen.Home.route) }
        )

        NavigationBarItem(
            selected = true,
            icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
            label = { Text("Add") },
            onClick = { navController.navigate(Screen.AddBook.route) }
        )

        NavigationBarItem(

            selected = true,
            onClick = {
                navController.navigate(Screen.InProgress.route)
            },
            icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Progress") },
            label = { Text("In Progress") }
        )

        NavigationBarItem(
            selected = true,
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            onClick = { navController.navigate(Screen.Profile.route) }
        )
    }
}