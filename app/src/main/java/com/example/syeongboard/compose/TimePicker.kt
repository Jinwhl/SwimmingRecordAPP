package com.example.syeongboard.compose

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syeongboard.screen.myBlueColor
import java.util.Calendar

@Composable
fun TimePicker(onTimeSet: (Boolean, Int, Int, Int, Int) -> Unit) {
    val context = LocalContext.current

    var startHour by remember { mutableStateOf(-1) }
    var startMin by remember { mutableStateOf(-1) }
    var endHour by remember { mutableStateOf(-1) }
    var endMin by remember { mutableStateOf(-1) }

    // When user select time, change to true
    val isTimeSelected = startHour > 0 && endHour > 0
    onTimeSet(isTimeSelected, startHour, startMin, endHour, endMin)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        Button(
            onClick = {
                showTimePicker(context) { hour, minute ->
                    startHour = hour
                    startMin = minute
                }
            },
            colors = ButtonDefaults.buttonColors(myBlueColor),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            when {
                startHour > 0 -> Text(text = convertTimeToString(startHour, startMin))
                else -> Text(text = "시작 시간")
            }
        }

        Text(
            text = "~",
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 8.dp)
        )

        Button(
            onClick = {
                showTimePicker(context) { hour, minute ->
                    endHour = hour
                    endMin = minute
                }
            },
            colors = ButtonDefaults.buttonColors(myBlueColor),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            when {
                endHour > 0 -> Text(text = convertTimeToString(endHour, endMin))
                else -> Text(text = "종료 시간")
            }
        }
    }
}
fun convertTimeToString(hour: Int, minute: Int): String {
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
        false  // 12 hours format
    ).show()
}