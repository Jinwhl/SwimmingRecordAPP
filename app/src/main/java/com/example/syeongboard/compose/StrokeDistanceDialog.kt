package com.example.syeongboard.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.syeongboard.screen.myBlueColor

@Composable
fun StrokeDistanceDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    updateDistances: (String, String, String, String) -> Unit
) {
    var butterflyDistance by remember { mutableStateOf("0") }
    var backstrokeDistance by remember { mutableStateOf("0") }
    var breaststrokeDistance by remember { mutableStateOf("0") }
    var freestyleDistance by remember { mutableStateOf("0") }

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
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                DistanceInputRow(label = "접영", value = butterflyDistance) { butterflyDistance = it }
                DistanceInputRow(label = "배영", value = backstrokeDistance) { backstrokeDistance = it }
                DistanceInputRow(label = "평영", value = breaststrokeDistance) { breaststrokeDistance = it }
                DistanceInputRow(label = "자유형", value = freestyleDistance) { freestyleDistance = it }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onConfirmation()
                        updateDistances(butterflyDistance, backstrokeDistance, breaststrokeDistance, freestyleDistance)
                    },
                    colors = ButtonDefaults.buttonColors(myBlueColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("적용하기")
                }
            }
        }
    }
}

@Composable
private fun DistanceInputRow(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(text = label, modifier = Modifier.width(60.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Only Number Keyboard Available
        )
        Text(text = "m", modifier = Modifier.width(24.dp))
    }
}