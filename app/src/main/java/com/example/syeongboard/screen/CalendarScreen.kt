package com.example.syeongboard.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.syeongboard.compose.MonthGrid
import com.example.syeongboard.compose.CalendarHeader
import com.example.syeongboard.compose.SupabaseClient
import com.example.syeongboard.compose.WeekDaysRow
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    onDateSelected: (LocalDate) -> Unit,
    onShowSettings: () -> Unit,
    swimRecords: List<SupabaseClient.SwimRecord>)
{
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfMonth = currentYearMonth.atDay(1).dayOfWeek.value % 7
    val today = LocalDate.now()

    val filteredSwimRecords = selectedDate?.let { date ->
        swimRecords.filter { it.swimDate == date.toString() }
    } ?: swimRecords

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CalendarHeader(
            yearMonth = currentYearMonth,
            onPreviousMonth = { currentYearMonth = currentYearMonth.minusMonths(1) },
            onNextMonth = { currentYearMonth = currentYearMonth.plusMonths(1) },
            onToday = { currentYearMonth = YearMonth.now() },
            onShowSettings = onShowSettings
        )
        Spacer(modifier = Modifier.height(8.dp))
        WeekDaysRow()
        Spacer(modifier = Modifier.height(8.dp))
        MonthGrid(
            currentYearMonth = currentYearMonth,
            daysInMonth = daysInMonth,
            firstDayOfMonth = firstDayOfMonth,
            today = today,
            onDateSelected = { date ->
                selectedDate = date
                onDateSelected(date)
            },
            cells = 6 * 7,
            filteredSwimRecords = filteredSwimRecords
        )
    }
}