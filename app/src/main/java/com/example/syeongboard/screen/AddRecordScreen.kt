package com.example.syeongboard.screen

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordScreen(onBack: () -> Unit) {
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
            // Section 1 : Record Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "수영 날짜*", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = "2024.6.4",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.width(
                        IntrinsicSize.Min
                    )
                )
            }

            // Section 2 : Swimming Start & End Time
            Spacer(modifier = Modifier.height(8.dp))
            TimePickerExample()

            // Section 3 : Choose Pool Lane Length
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "레인 길이*", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                var poolLaneLength by remember { mutableStateOf("25m") }
                Button(
                    onClick = { poolLaneLength = "25m" },
                    colors = ButtonDefaults.buttonColors(if (poolLaneLength == "25m") Color.Blue else Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "25m", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { poolLaneLength = "50m" },
                    colors = ButtonDefaults.buttonColors(if (poolLaneLength == "50m") Color.Blue else Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "50m", color = Color.White)
                }
            }


            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "수영 거리* (m)", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                var showDialog by remember { mutableStateOf(false) }
                if (showDialog) {
                    AlertDialogExample(
                        onDismissRequest = { showDialog = false },
                        onConfirmation = { /* 적용하기 로직 추가 */ showDialog = false },
                        dialogTitle = "거리 입력",
                    )
                }
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(Color.Blue),
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
                placeholder = { Text(text = "수영장 검색") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "사진 추가", fontSize = 16.sp, color = Color.Gray)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "수영 일기", fontSize = 16.sp, color = Color.Black)
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(text = "오늘 수영은 어땠나요?") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = { /* 저장 로직 */ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "저장하기")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(dialogTitle, fontSize = 20.sp, modifier = Modifier.weight(1f))
                    IconButton(onClick = onDismissRequest) {
                        Icon(Icons.Filled.Close, contentDescription = "뒤로 가기")
                    }
                }
                Divider(color = Color.Gray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                DistanceInputRow(label = "접영")
                DistanceInputRow(label = "배영")
                DistanceInputRow(label = "평영")
                DistanceInputRow(label = "자유형")
                DistanceInputRow(label = "킥판")
                DistanceInputRow(label = "기타")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onConfirmation) {
                    Text("적용하기")
                }
            }
        }
    }
}


@Composable
fun DistanceInputRow(label: String) {
    var value by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(text = label, modifier = Modifier.width(60.dp))
        BasicTextField(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            singleLine = true,
            // Only Number Keyboard Available
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(text = "m", modifier = Modifier.width(24.dp))
    }
}


@Composable
private fun TimePickerExample() {
    var startHour by remember { mutableStateOf(-1) }
    var startMin by remember { mutableStateOf(-1) }
    var endHour by remember { mutableStateOf(-1) }
    var endMin by remember { mutableStateOf(-1) }
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        Button(onClick = {
            showTimePicker(context) { hour, minute ->
                startHour = hour
                startMin = minute
            }
        }) {
            when {
                startHour > 0 -> Text(text = convertTimeToString(startHour, startMin))
                else -> Text(text = "Start Time")
            }
        }

        Text(
            text = "~",
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 8.dp)
        )

        Button(onClick = {
            showTimePicker(context) { hour, minute ->
                endHour = hour
                endMin = minute
            }
        }) {
            when {
                endHour > 0 -> Text(text = convertTimeToString(endHour, endMin))
                else -> Text(text = "End Time")
            }
        }
    }
}

private fun convertTimeToString(hour: Int, minute: Int): String {
    return String.format(
        "%02d:%02d %s",
        if (hour % 12 == 0) 12 else hour % 12,
        minute,
        if (hour < 12) "AM" else "PM"
    )
}

private fun showTimePicker(context: android.content.Context, onTimeSelected: (Int, Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            onTimeSelected(selectedHour, selectedMinute)
        },
        hour,
        minute,
        false  // 12시간 형식
    ).show()
}