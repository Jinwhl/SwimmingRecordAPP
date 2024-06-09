package com.example.syeongboard.screen

import androidx.compose.foundation.background
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syeongboard.compose.StrokeDistanceDialog
import com.example.syeongboard.compose.TimePicker
import java.time.LocalDate


val myBlueColor = Color(0xFF3382F5)
val mySkyColor = Color(0xFFB4CDFB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordScreen(date: LocalDate, onBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "수영 기록 추가하기",
                        fontSize = 16.sp,
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
            // Section 1 : Show Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "수영 날짜*", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(mySkyColor),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(date.toString())
                }
            }

            // Section 2 : Record Swimming Start & End Time
            var isTimeSelected by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "수영 시간*", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                TimePicker(onTimeSet = { enabled -> isTimeSelected = enabled })
            }

            // Section : Choose Pool Lane Length
            /*
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "레인 길이*", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                var poolLaneLength by remember { mutableStateOf("25m") }
                Button(
                    onClick = { poolLaneLength = "25m" },
                    colors = ButtonDefaults.buttonColors(if (poolLaneLength == "25m") myBlueColor else Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "25m", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { poolLaneLength = "50m" },
                    colors = ButtonDefaults.buttonColors(if (poolLaneLength == "50m") myBlueColor else Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "50m", color = Color.White)
                }
            }
             */


            // Section 3 : Record Swimming Distance
            var isDistanceSelected by remember { mutableStateOf(false) }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "수영 거리* (m)", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                var showDialog by remember { mutableStateOf(false) }
                if (showDialog) {
                    StrokeDistanceDialog(
                        onDismissRequest = { showDialog = false },
                        onConfirmation = { /* 적용하기 로직 추가 */ showDialog = false },
                        dialogTitle = "거리 입력",
                        onDistanceSet = {enabled -> isDistanceSelected = enabled}
                    )
                }
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(myBlueColor),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("거리 입력")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "수영장 추가", fontSize = 16.sp, color = Color.Gray)
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(text = "수영장 검색", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "수영 일기", fontSize = 16.sp, color = Color.Gray)
            TextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        text = "오늘 수영은 어땠나요?",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color.LightGray)
            )

            val isButtonEnabled = isTimeSelected && isDistanceSelected
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* 저장 로직 */ },
                colors = ButtonDefaults.buttonColors(if (isButtonEnabled) myBlueColor else mySkyColor),
                enabled = isButtonEnabled,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(168.dp)
                    .height(56.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(text = "저장하기", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}