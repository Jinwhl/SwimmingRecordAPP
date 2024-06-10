package com.example.syeongboard.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import com.example.syeongboard.utils.MyColor
import kotlinx.datetime.DayOfWeek
import java.time.temporal.TemporalAdjusters

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarHeader(
    yearMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToday: () -> Unit,
    onShowSettings: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "기록",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onShowSettings) {
            Icon(Icons.Default.Settings, contentDescription = "설정")
        }
    }

    val userId = "Jinwon"
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "이번 달 $userId 의 수영 기록",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${yearMonth.year}년 ${yearMonth.monthValue}월",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Default.ArrowBack, contentDescription = "이전 달")
        }
        TextButton(onClick = onToday) {
            Text(text = "오늘", color = MyColor.Blue)
        }
        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.ArrowForward, contentDescription = "다음 달")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthGrid(
    currentYearMonth: YearMonth,
    daysInMonth: Int,
    firstDayOfMonth: Int,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    cells: Int,
    filteredSwimRecords: List<SupabaseClient.SwimRecord>
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

                DayCell(
                    date = date,
                    isToday = isToday,
                    isClickable = !date.isAfter(today),
                    onClick = { onDateSelected(date) },
                    filteredSwimRecords = filteredSwimRecords
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekGrid(
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    filteredSwimRecords: List<SupabaseClient.SwimRecord>
) {
    val startOfWeek = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    val daysOfWeek = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // 간격 추가
    ) {
        daysOfWeek.forEach { date ->
            val isToday = date == today

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                DayCell(
                    date = date,
                    isToday = isToday,
                    isClickable = !date.isAfter(today),
                    onClick = { onDateSelected(date) },
                    filteredSwimRecords = filteredSwimRecords
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCell(
    date: LocalDate,
    isToday: Boolean,
    isClickable: Boolean,
    onClick: (() -> Unit)?,
    filteredSwimRecords: List<SupabaseClient.SwimRecord>
) {
    val recordsForDay = filteredSwimRecords.filter { it.swimDate == date.toString() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isToday) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(MyColor.Blue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        } else {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (isClickable) Color.Black else Color.LightGray
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isToday) MyColor.Blue else MyColor.SkyBlue)
                .clickable(enabled = isClickable && onClick != null) { onClick?.invoke() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                recordsForDay.forEach { record ->
                    Text(text = "접: ${record.butterflyDistance}m", fontSize = 8.sp)
                    Text(text = "배: ${record.backstrokeDistance}m", fontSize = 8.sp)
                    Text(text = "평: ${record.breaststrokeDistance}m", fontSize = 8.sp)
                    Text(text = "자: ${record.freestyleDistance}m", fontSize = 8.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}