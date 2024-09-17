package good.questions

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainScreen { audience, isRandom, isLoopEnabled ->
                navController.navigate("questions/$audience?isRandom=$isRandom&isLoopEnabled=$isLoopEnabled")
            }
        }
        composable(
            "questions/{audience}?isRandom={isRandom}&isLoopEnabled={isLoopEnabled}",
            arguments = listOf(
                navArgument("isRandom") { type = NavType.BoolType },
                navArgument("isLoopEnabled") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val audience = backStackEntry.arguments?.getString("audience") ?: "Unknown"
            val isRandom = backStackEntry.arguments?.getBoolean("isRandom") ?: false
            val isLoopEnabled = backStackEntry.arguments?.getBoolean("isLoopEnabled") ?: false
            QuestionsPage(audience, isRandom, isLoopEnabled,navController)
        }
    }
}