package com.example.syeongboard.compose

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syeongboard.utils.MyColor
import java.util.Calendar

@Composable
fun TimePicker(
    startHour: Int,
    startMin: Int,
    endHour: Int,
    endMin: Int,
    onTimeSet: (Boolean, Int, Int, Int, Int) -> Unit,
) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            onClick = {
                showTimePicker(startHour, startMin, context) { hour, minute ->
                    val isTimeSelected = hour > 0 && minute > 0 && endHour > 0 && endMin > 0
                    onTimeSet(isTimeSelected, hour, minute, endHour, endMin)
                }
            },
            colors = ButtonDefaults.buttonColors(MyColor.Blue),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.width(120.dp).height(40.dp)
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
                showTimePicker(endHour, endMin, context) { hour, minute ->
                    val isTimeSelected = startHour > 0 && startMin > 0 && hour > 0 && minute > 0
                    onTimeSet(isTimeSelected, startHour, startMin, hour, minute)
                }
            },
            colors = ButtonDefaults.buttonColors(MyColor.Blue),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.width(120.dp).height(40.dp)
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

private fun showTimePicker(
    prevHour: Int,
    prevMinute: Int,
    context: android.content.Context, onTimeSelected: (Int, Int) -> Unit,
) {
    val (hour, minute) = when {
        prevHour == -1 || prevMinute == -1 -> {
            val calendar = Calendar.getInstance()
            calendar.get(Calendar.HOUR_OF_DAY) to calendar.get(Calendar.MINUTE)
        }
        else -> prevHour to prevMinute
    }

    TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            onTimeSelected(selectedHour, selectedMinute)
        },
        hour,
        minute,
        true  // 12 hours format
    ).show()
}