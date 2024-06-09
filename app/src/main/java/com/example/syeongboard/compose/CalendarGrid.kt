package com.example.syeongboard.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.syeongboard.screen.DayCell
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarGrid(
    currentYearMonth: YearMonth,
    daysInMonth: Int,
    firstDayOfMonth: Int,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    cells: Int
) {
    val days = (1..daysInMonth).map { it.toString() }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(cells) { index ->
            if (index >= firstDayOfMonth && index < firstDayOfMonth + daysInMonth) {
                val day = days[index - firstDayOfMonth]
                val date = LocalDate.of(currentYearMonth.year, currentYearMonth.month, day.toInt())
                val isToday = date == today
                val isClickable = !date.isAfter(today)
                DayCell(
                    day = day,
                    isToday = isToday,
                    isClickable = isClickable,
                    onClick = { onDateSelected(date) }
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                )
            }
        }
    }
}