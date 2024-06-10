package com.example.syeongboard.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.syeongboard.compose.SupabaseClient
import com.example.syeongboard.compose.SwimmingRecordViewModel
import com.example.syeongboard.compose.WeekDaysRow
import com.example.syeongboard.compose.WeekGrid
import com.example.syeongboard.test.StackedBars
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
            Text(text = "${userID}의 수영 아카이빙", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Section 4 : Show Information
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.LightGray)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LaunchedEffect(date) { viewModel.fetchSwimRecordsByDate(date) }
                    val swimRecordsByDate by viewModel.swimRecordsByDate.observeAsState(emptyMap())
                    val swimRecords = swimRecordsByDate[date] ?: emptyList()

                    // Section 4-1 : Show Current Date
                    Text(text = date.toString(), fontSize = 18.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Section 4-2 : Display fetched swim records for the date
                    when {
                        swimRecords.isNotEmpty() -> swimRecords.forEach { RecordInfo(it) }
                        else -> Text("No swim records for this date.")
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StackedBars(
                data.butterflyDistance,
                data.backstrokeDistance,
                data.breaststrokeDistance,
                data.freestyleDistance,
                0.5f, 0, Color.LightGray
            )
        Text("${data.butterflyDistance}")
        Text("${data.backstrokeDistance}")
        Text("${data.breaststrokeDistance}")
        Text("${data.freestyleDistance}")
        Text(text = "Start Time >> ${data.startTime}")
        Text(text = "End Time >> ${data.endTime}")
        Text(text = "Pool >> ${data.poolName}")
        Text(text = "Notes \n ${data.notes}")
    }
}