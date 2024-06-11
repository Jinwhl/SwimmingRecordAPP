package com.example.syeongboard.screen

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syeongboard.compose.StrokeDistanceDialog
import com.example.syeongboard.compose.SupabaseClient
import com.example.syeongboard.compose.TimePicker
import com.example.syeongboard.utils.MyColor
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordScreen(
    date: LocalDate,
    onBack: () -> Unit,
    onSave: (Int, Int, Int, Int, Map<String, Int>) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()

    var startHour by remember { mutableIntStateOf(-1) }
    var startMin by remember { mutableIntStateOf(-1) }
    var endHour by remember { mutableIntStateOf(-1) }
    var endMin by remember { mutableIntStateOf(-1) }
    var butterflyDistance by remember { mutableStateOf(0) }
    var backstrokeDistance by remember { mutableStateOf(0) }
    var breaststrokeDistance by remember { mutableStateOf(0) }
    var freestyleDistance by remember { mutableStateOf(0) }
    var poolName by remember { mutableStateOf<String?>(null) }
    var notes by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(date) {
        val record = SupabaseClient.getSwimRecordByDate(date.toString())
        record?.let {
            startHour = it.startTime.substringBefore(":").toInt()
            startMin = it.startTime.substringAfter(":").toInt()
            endHour = it.endTime.substringBefore(":").toInt()
            endMin = it.endTime.substringAfter(":").toInt()
            butterflyDistance = it.butterflyDistance ?: 0
            backstrokeDistance = it.backstrokeDistance ?: 0
            breaststrokeDistance = it.breaststrokeDistance ?: 0
            freestyleDistance = it.freestyleDistance ?: 0
            poolName = it.poolName
            notes = it.notes
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "수영 기록 추가하기",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                Text(
                    text = "수영 날짜*",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(MyColor.SkyBlue),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(date.toString())
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Section 2 : Record Swimming Start & End Time (Essential Data *)
            var isTimeSelected by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "수영 시간*",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                TimePicker(
                    startHour,
                    startMin,
                    endHour,
                    endMin,
                    onTimeSet = { enabled, inputStartHour, inputStartMin, inputEndHour, inputEndMin ->
                        isTimeSelected = enabled
                        startHour = inputStartHour
                        startMin = inputStartMin
                        endHour = inputEndHour
                        endMin = inputEndMin
                    })
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Section 3 : Record Distances of Each Stroke (Essential Data *)
            var isDistanceSelected by remember { mutableStateOf(false) }
            fun updateDistances(
                butterfly: Int,
                backstroke: Int,
                breaststroke: Int,
                freestyle: Int
            ) {
                butterflyDistance = butterfly
                backstrokeDistance = backstroke
                breaststrokeDistance = breaststroke
                freestyleDistance = freestyle
                isDistanceSelected = listOf(butterfly, backstroke, breaststroke, freestyle).any {
                    (it) > 0
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "수영 거리* (m)",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
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
                    colors = ButtonDefaults.buttonColors(MyColor.Blue),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp)
                ) {
                    Text("거리 입력")
                }
            }

            // Section 4 : Record Pool Name (Optional Data)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "수영장 추가",
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = poolName ?: "",
                onValueChange = { poolName = it.ifBlank { null } },
                placeholder = { Text(text = "수영장 이름", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MyColor.Blue,
                    unfocusedBorderColor = MyColor.Blue,
                )
            )

            // Section 5 : Daily Note (Optional Data)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "수영 일기", fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = notes ?: "",
                onValueChange = { notes = it.ifBlank { null } },
                placeholder = { Text(text = "오늘 수영은 어땠나요?", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MyColor.Blue,
                    unfocusedBorderColor = MyColor.Blue,
                )
            )

            // Section 6 : Save Button
            val isButtonEnabled = isTimeSelected && isDistanceSelected
//            Text(text = "isTimeSelected : $isTimeSelected")
//            Text(text = "isDistanceSelected : $isDistanceSelected")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val distances = mapOf(
                        "butterfly" to (butterflyDistance),
                        "backstroke" to (backstrokeDistance),
                        "breaststroke" to (breaststrokeDistance),
                        "freestyle" to (freestyleDistance)
                    )
                    onSave(startHour, startMin, endHour, endMin, distances)
                    scope.launch {
                        SupabaseClient.addDataToSupabase(
                            swimDate = date.toString(),
                            startTime = "$startHour:$startMin",
                            endTime = "$endHour:$endMin",
                            distances = distances,
                            poolName = poolName,
                            notes = notes,
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(if (isButtonEnabled) MyColor.Blue else MyColor.SkyBlue),
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