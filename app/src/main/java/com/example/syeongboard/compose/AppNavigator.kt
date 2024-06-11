package com.example.syeongboard.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.syeongboard.screen.AddRecordScreen
import com.example.syeongboard.screen.CalendarScreen
import com.example.syeongboard.screen.SettingsScreen
import com.example.syeongboard.screen.SwimmingRecordScreen
import java.time.LocalDate
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
@RequiresApi(Build.VERSION_CODES.O)
class SwimmingRecordViewModel : ViewModel() {
    private val _startTimeMap = MutableLiveData<Map<LocalDate, Pair<Int, Int>>>(emptyMap())
    private val _endTimeMap = MutableLiveData<Map<LocalDate, Pair<Int, Int>>>(emptyMap())
    private val _distanceMap = MutableLiveData<Map<LocalDate, Map<String, Int>>>(emptyMap())
    private val _swimRecords = MutableLiveData<List<SupabaseClient.SwimRecord>>(emptyList())
    private val _swimRecordsByDate = MutableLiveData<Map<LocalDate, List<SupabaseClient.SwimRecord>>>(emptyMap())
    val swimRecords get() = _swimRecords
    val swimRecordsByDate get() = _swimRecordsByDate

    fun setStartTime(date: LocalDate, hour: Int, minute: Int) {
        val newMap = _startTimeMap.value!!.toMutableMap()
        newMap[date] = Pair(hour, minute)
        _startTimeMap.value = newMap
    }
    fun setEndTime(date: LocalDate, hour: Int, minute: Int) {
        val newMap = _endTimeMap.value!!.toMutableMap()
        newMap[date] = Pair(hour, minute)
        _endTimeMap.value = newMap
    }
    fun setDistances(date: LocalDate, distances: Map<String, Int>) {
        val newMap = _distanceMap.value!!.toMutableMap()
        newMap[date] = distances
        _distanceMap.value = newMap
    }
    fun fetchAllSwimRecords() {
        viewModelScope.launch {
            val records = SupabaseClient.getAllSwimRecords()
            _swimRecords.postValue(records)
        }
    }
    fun fetchSwimRecordsByDate(date: LocalDate) {
        viewModelScope.launch {
            val record = SupabaseClient.getSwimRecordByDate(date.toString())
            val currentMap = _swimRecordsByDate.value!!.toMutableMap()
            if (record != null) {
                currentMap[date] = listOf(record)
            } else {
                currentMap[date] = emptyList()
            }
            _swimRecordsByDate.postValue(currentMap)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val swimmingRecordViewModel: SwimmingRecordViewModel = viewModel()

    NavHost(
        navController = navController, startDestination = "calendar",
        enterTransition = {
            slideIntoContainer(
                animationSpec = tween(300),
                towards = AnimatedContentTransitionScope.SlideDirection.Left
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                animationSpec = tween(300),
                towards = AnimatedContentTransitionScope.SlideDirection.Left
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                animationSpec = tween(300),
                towards = AnimatedContentTransitionScope.SlideDirection.Right
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                animationSpec = tween(300),
                towards = AnimatedContentTransitionScope.SlideDirection.Right
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable("calendar") {
            LaunchedEffect(Unit) {
                swimmingRecordViewModel.fetchAllSwimRecords()
            }
            val swimRecords by swimmingRecordViewModel.swimRecords.observeAsState(emptyList())

            CalendarScreen(
                onDateSelected = { selectedDate ->
                    navController.navigate("swimmingRecord/$selectedDate")
                },
                onShowSettings = { navController.navigate("settings") },
                swimRecords = swimRecords
            )
        }
        composable("settings") {
            SettingsScreen(onClose = { navController.popBackStack() })
        }
        composable("swimmingRecord/{date}") { backStackEntry ->
            val date = LocalDate.parse(backStackEntry.arguments?.getString("date"))
            SwimmingRecordScreen(
                date = date,
                onBack = {
                    navController.navigate("calendar") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onAddRecord = { navController.navigate("addRecord/$date") },
                viewModel = swimmingRecordViewModel,
                navController = navController
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