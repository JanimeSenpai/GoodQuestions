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
            MainScreen { audience,audienceID, isRandom, isLoopEnabled ->
                navController.navigate("questions/$audience/$audienceID?isRandom=$isRandom&isLoopEnabled=$isLoopEnabled")
            }
        }
        composable(
            "questions/{audience}/{audienceID}?isRandom={isRandom}&isLoopEnabled={isLoopEnabled}",
            arguments = listOf(
                navArgument("audience") { type = NavType.StringType },
                navArgument("audienceID") { type = NavType.IntType },
                navArgument("isRandom") { type = NavType.BoolType },
                navArgument("isLoopEnabled") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val audience = backStackEntry.arguments?.getString("audience") ?: "Unknown"
            val audienceID = backStackEntry.arguments?.getInt("audienceID") ?: 86
            val isRandom = backStackEntry.arguments?.getBoolean("isRandom") ?: false
            val isLoopEnabled = backStackEntry.arguments?.getBoolean("isLoopEnabled") ?: false
            QuestionsPage(audience,audienceID, isRandom, isLoopEnabled,navController)
        }
    }
}