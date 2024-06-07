package com.example.syeongboard.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.syeongboard.screen.AddRecordScreen
import com.example.syeongboard.screen.CalendarScreen
import com.example.syeongboard.screen.SettingsScreen
import com.example.syeongboard.screen.SwimmingRecordScreen
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "calendar") {
        composable("calendar") {
            CalendarScreen(
                onDateSelected = { date -> navController.navigate("swimmingRecord/$date") },
                onShowSettings = { navController.navigate("settings") }
            )
        }
        composable("settings") {
            SettingsScreen(onClose = { navController.popBackStack() })
        }
        composable("swimmingRecord/{date}") { backStackEntry ->
            val date = LocalDate.parse(backStackEntry.arguments?.getString("date"))
            SwimmingRecordScreen(date = date, onBack = { navController.popBackStack() }, onAddRecord = { navController.navigate("addRecord") })
        }
        composable("addRecord") {
            AddRecordScreen(onBack = { navController.popBackStack() })
        }
    }
}