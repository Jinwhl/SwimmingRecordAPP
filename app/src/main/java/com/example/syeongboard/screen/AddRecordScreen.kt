package com.example.syeongboard.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
fun AddRecordScreen(
    date: LocalDate,
    onBack: () -> Unit,
    onSave: (Int, Int, Int, Int, Map<String, Int>) -> Unit
) {
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
            // Section 1 : Show Date (Essential Data *)
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

            // Section 2 : Record Swimming Start & End Time (Essential Data *)
            var isTimeSelected by remember { mutableStateOf(false) }
            var startHour by remember { mutableIntStateOf(0) }
            var startMin by remember { mutableIntStateOf(0) }
            var endHour by remember { mutableIntStateOf(0) }
            var endMin by remember { mutableIntStateOf(0) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "수영 시간*", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                TimePicker(onTimeSet = { enabled, inputStartHour, inputStartMin, inputEndHour, inputEndMin ->
                    isTimeSelected = enabled
                    startHour = inputStartHour
                    startMin = inputStartMin
                    endHour = inputEndHour
                    endMin = inputEndMin
                })
            }

            // Section 3 : Record Distances of Each Stroke
            var isDistanceSelected by remember { mutableStateOf(false) }
            var butterflyDistance by remember { mutableStateOf("0") }
            var backstrokeDistance by remember { mutableStateOf("0") }
            var breaststrokeDistance by remember { mutableStateOf("0") }
            var freestyleDistance by remember { mutableStateOf("0") }
            fun updateDistances(butterfly: String, backstroke: String, breaststroke: String, freestyle: String) {
                butterflyDistance = butterfly
                backstrokeDistance = backstroke
                breaststrokeDistance = breaststroke
                freestyleDistance = freestyle
                isDistanceSelected = listOf(butterfly, backstroke, breaststroke, freestyle).any { (it.toIntOrNull() ?: 0) > 0 }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "수영 거리* (m)", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                var showDialog by remember { mutableStateOf(false) }
                if (showDialog) {
                    StrokeDistanceDialog(
                        onDismissRequest = { showDialog = false },
                        onConfirmation = { showDialog = false },
                        dialogTitle = "거리 입력",
                        updateDistances = ::updateDistances
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

            // Section 6 : Save Button
            val isButtonEnabled = isTimeSelected && isDistanceSelected
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val distances = mapOf(
                        "butterfly" to (butterflyDistance.toIntOrNull() ?: 0),
                        "backstroke" to (backstrokeDistance.toIntOrNull() ?: 0),
                        "breaststroke" to (breaststrokeDistance.toIntOrNull() ?: 0),
                        "freestyle" to (freestyleDistance.toIntOrNull() ?: 0)
                    )
                    onSave(startHour, startMin, endHour, endMin, distances)
                },
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