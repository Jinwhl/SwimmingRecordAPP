package com.example.syeongboard.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.syeongboard.screen.AddRecordScreen
import com.example.syeongboard.screen.CalendarScreen
import com.example.syeongboard.screen.SettingsScreen
import com.example.syeongboard.screen.SwimmingRecordScreen
import java.time.LocalDate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SwimmingRecordViewModel : ViewModel() {
    private val _startTimeMap = MutableLiveData<Map<LocalDate, Pair<Int, Int>>>(emptyMap())
    val startTimeMap: LiveData<Map<LocalDate, Pair<Int, Int>>> get() = _startTimeMap

    private val _endTimeMap = MutableLiveData<Map<LocalDate, Pair<Int, Int>>>(emptyMap())
    val endTimeMap: LiveData<Map<LocalDate, Pair<Int, Int>>> get() = _endTimeMap

    private val _distanceMap = MutableLiveData<Map<LocalDate, Map<String, Int>>>(emptyMap())
    val distanceMap: LiveData<Map<LocalDate, Map<String, Int>>> get() = _distanceMap

    @RequiresApi(Build.VERSION_CODES.O)
    fun setStartTime(date: LocalDate, hour: Int, minute: Int) {
        val newMap = _startTimeMap.value!!.toMutableMap()
        newMap[date] = Pair(hour, minute)
        _startTimeMap.value = newMap
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setEndTime(date: LocalDate, hour: Int, minute: Int) {
        val newMap = _endTimeMap.value!!.toMutableMap()
        newMap[date] = Pair(hour, minute)
        _endTimeMap.value = newMap
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDistances(date: LocalDate, distances: Map<String, Int>) {
        val newMap = _distanceMap.value!!.toMutableMap()
        newMap[date] = distances
        _distanceMap.value = newMap
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val swimmingRecordViewModel: SwimmingRecordViewModel = viewModel()

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
            SwimmingRecordScreen(
                date = date,
                onBack = { navController.popBackStack() },
                onAddRecord = { navController.navigate("addRecord/${date.toString()}") },
                viewModel = swimmingRecordViewModel
            )
        }
        composable("addRecord/{date}") { backStackEntry ->
            val date = LocalDate.parse(backStackEntry.arguments?.getString("date"))
            AddRecordScreen(
                date = date,
                onBack = { navController.popBackStack() },
                onSave = { startHour, startMin, endHour, endMin, distances ->
                    swimmingRecordViewModel.setStartTime(date, startHour, startMin)
                    swimmingRecordViewModel.setEndTime(date, endHour, endMin)
                    swimmingRecordViewModel.setDistances(date, distances)
                    navController.popBackStack()
                }
            )
        }
    }
}