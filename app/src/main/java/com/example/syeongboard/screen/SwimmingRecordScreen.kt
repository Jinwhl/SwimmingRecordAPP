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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syeongboard.compose.SwimmingRecordViewModel
import com.example.syeongboard.compose.WeekDaysRow
import com.example.syeongboard.compose.convertTimeToString
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwimmingRecordScreen(
    date: LocalDate,
    onBack: () -> Unit,
    onAddRecord: (LocalDate) -> Unit,
    viewModel: SwimmingRecordViewModel
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
            Spacer(modifier = Modifier.height(16.dp))

            // Section 2 : Grid for One week
            // TODO

            // Section 3 : Title
            val userID = "Jinwon"
            Text(
                text = "${userID}의 수영 아카이빙",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Section 4 : Show Information
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(Color.LightGray)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Section 4-1 : Show Current Date
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = date.toString(),
                        fontSize = 18.sp,
                        color = Color.Gray
                    )

                    // Section 4-2 : Distances of Each Stroke
                    Spacer(modifier = Modifier.height(8.dp))
                    val distances by viewModel.distanceMap.observeAsState(emptyMap())
                    val distance = distances[date] ?: emptyMap()
                    if (distance.isNotEmpty()) {
                        Text("수영 거리", fontWeight = FontWeight.Bold)
                        distance.forEach { (stroke, dist) ->
                            Text("$stroke: $dist m")
                        }
                    } else {
                        Text("수영 거리 정보 없음")
                    }

                    // Section 4-3 : Total Distance
                    //TODO

                    // Section 4-4 : Swimming Time
                    val startTimeMap by viewModel.startTimeMap.observeAsState()
                    val endTimeMap by viewModel.endTimeMap.observeAsState()
                    val startTime = startTimeMap?.get(date)
                    val endTime = endTimeMap?.get(date)
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = startTime?.let { (startHour, startMin) ->
                                    convertTimeToString(startHour, startMin)
                                } ?: "시작 시간 미설정",
                            )
                            Text(
                                text = endTime?.let { (endHour, endMin) ->
                                    convertTimeToString(endHour, endMin)
                                } ?: "종료 시간 미설정",
                            )
                            val totalSwimTime = if (startTime != null && endTime != null) {
                                val (startHour, startMin) = startTime
                                val (endHour, endMin) = endTime
                                val startTotalMinutes = startHour * 60 + startMin
                                val endTotalMinutes = endHour * 60 + endMin
                                endTotalMinutes - startTotalMinutes
                            } else {
                                null
                            }

                            if (totalSwimTime != null && totalSwimTime >= 0) {
                                val hours = totalSwimTime / 60
                                val minutes = totalSwimTime % 60
                                Text(
                                    text = "총 수영 시간 : ${hours}시간 ${minutes}분"
                                )
                            } else {
                                Text(
                                    text = "총 수영 시간 미설정"
                                )
                            }
                        }
                    }
                    // Section 4-5 : 평균 페이스
                    // TODO
                }
            }

            // Section 6 : Start Record Button
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onAddRecord(date) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "수영 기록 추가하기")
            }
        }
    }
}
