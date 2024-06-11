package com.example.syeongboard.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.syeongboard.compose.LottieSwimAnimation
import com.example.syeongboard.compose.SupabaseClient
import com.example.syeongboard.compose.SwimmingRecordViewModel
import com.example.syeongboard.compose.WeekDaysRow
import com.example.syeongboard.compose.WeekGrid
import com.example.syeongboard.compose.SwimArchiveBars
import com.example.syeongboard.utils.MyColor
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwimmingRecordScreen(
    date: LocalDate,
    onBack: () -> Unit,
    onAddRecord: (LocalDate) -> Unit,
    viewModel: SwimmingRecordViewModel,
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "${date.year}년 ${date.monthValue}월 ${date.dayOfMonth}일",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Section 1 : Mon ~ Sun
            WeekDaysRow()
            Spacer(modifier = Modifier.height(4.dp))

            // Section 2 : Grid for One week
            WeekGrid(
                selectedDate = date,
                today = LocalDate.now(),
                onDateSelected = { selectedDate ->
                    viewModel.fetchSwimRecordsByDate(selectedDate)
                    navController.navigate("swimmingRecord/${selectedDate}") // 선택된 날짜로 이동합니다.
                },
                filteredSwimRecords = viewModel.swimRecords.value ?: emptyList()
            )

            // Section 3 : Title
            val userID = "Jinwon"
            Text(text = "${userID}의 수영 Archive", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Section 4 : Show Information
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                val swimRecordsByDate by viewModel.swimRecordsByDate.observeAsState(emptyMap())
                val swimRecords = swimRecordsByDate[date] ?: emptyList()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LaunchedEffect(date) { viewModel.fetchSwimRecordsByDate(date) }
                    val swimRecordsByDate by viewModel.swimRecordsByDate.observeAsState(emptyMap())
                    val swimRecords = swimRecordsByDate[date] ?: emptyList()
                    when {
                        swimRecords.isNotEmpty() -> swimRecords.forEach { RecordInfo(it) }
                        else -> Text("No swim records for this date.")
                    }
                }
                swimRecords.forEach { data ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val totalDistance = (data.butterflyDistance ?: 0) +
                                (data.backstrokeDistance ?: 0) +
                                (data.breaststrokeDistance ?: 0) +
                                (data.freestyleDistance ?: 0)
                        LottieSwimAnimation(100, (totalDistance / 50f))
                    }
                }

            }
            // Section 6 : Start Record Button
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onAddRecord(date) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(MyColor.Blue)
            ) { Text(text = "수영 기록 추가하기") }
        }
    }
}

@Composable
fun RecordInfo(data: SupabaseClient.SwimRecord) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Section 4-1 : ArchiveBars
        SwimArchiveBars(
            data.butterflyDistance,
            data.backstrokeDistance,
            data.breaststrokeDistance,
            data.freestyleDistance,
            0.8f, 32, MyColor.Gray,
            500, 50, true
        )

        // Section 4-2 : SwimDate , Total Distance
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = data.swimDate.toString() + "   ",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.TopEnd)
            )
            val totalDistance = (data.butterflyDistance ?: 0) +
                    (data.backstrokeDistance ?: 0) +
                    (data.breaststrokeDistance ?: 0) +
                    (data.freestyleDistance ?: 0)
            val totalDistanceText = convertDistanceFormat(totalDistance)
            Text(
                text = "  " + totalDistanceText + "m",
                style = TextStyle(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                ),
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Section 4-3 : Swimming Time
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                val timeDifferenceText = calculateTimeDifference(data.startTime, data.endTime)
                Text(
                    text = "총 시간",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                )
                Text(
                    text = timeDifferenceText,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }
            Spacer(modifier = Modifier.width(32.dp))
            Column {
                Text(
                    text = "수영장",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (data.poolName != null) {
                    Text(
                        text = "${data.poolName}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                } else {
                    Text(text = "-", style = TextStyle(fontSize = 16.sp), color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        // Section 4-4 : Daily Notes
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(
                    text = "수영 일기",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (data.notes.isNullOrEmpty()) "-" else "${data.notes}",
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

    }
}

fun convertDistanceFormat(totalDistance: Int?): String {
    return if (totalDistance == null) {
        "0"
    } else if (totalDistance < 1000) {
        totalDistance.toString()
    } else {
        "%,d".format(totalDistance)
    }
}

fun calculateTimeDifference(startTime: String, endTime: String): String {
    val start = parseTime(startTime)
    val end = parseTime(endTime)

    var startMinutes = start.first * 60 + start.second
    var endMinutes = end.first * 60 + end.second

    if (endMinutes < startMinutes) {
        endMinutes += 24 * 60 // 다음 날로 넘어가는 경우를 처리
    }

    val difference = endMinutes - startMinutes
    val hours = difference / 60
    val minutes = difference % 60

    return String.format("%d시간 %d분", hours, minutes)
}

fun parseTime(time: String): Pair<Int, Int> {
    val parts = time.split(":")
    val hours = parts[0].toInt()
    val minutes = parts[1].toInt()
    return Pair(hours, minutes)
}