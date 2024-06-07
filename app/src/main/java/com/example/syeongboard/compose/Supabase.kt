package com.example.syeongboard.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.syeongboard.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.mySupabaseURL,
    supabaseKey = BuildConfig.mySupabaseAnonKey,
) { install(Postgrest) }

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun TestScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Spacer(modifier = Modifier.height(24.dp))
            InsertData()
            //ShowData()

            //val localDate: LocalDate = LocalDate.now()
            //SearchDataOf(date = localDate.toString())
        }
    }
}

@Composable
fun ShowData() {
    var records by remember { mutableStateOf<List<SwimRecords>>(listOf()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            records = supabase.from("SwimRecords").select().decodeList<SwimRecords>()
        }
    }
    LazyColumn {
        items(records, key = { it.id ?: 0 }) { record ->
            Column {
                Text(text = "Start Time: ${record.startTime}")
                Text(text = "End Time: ${record.endTime}")
                Text(text = "접영 거리 : ${record.butterflyDistance}")
                Text(text = "배영 거리 : ${record.backstrokeDistance}")
                Text(text = "평영 거리 : ${record.breaststrokeDistance}")
                Text(text = "자유형 거리 : ${record.freestyleDistance}")
            }
        }
    }
}

@Serializable
data class SwimRecords(
    val id: Int? = null, // id is now nullable to allow automatic ID generation
    val swimDate: String?,
    val startTime: String,
    val endTime: String,
    val butterflyDistance: Int?,
    val backstrokeDistance: Int?,
    val breaststrokeDistance: Int?,
    val freestyleDistance: Int?,
)

@Composable
fun InsertData() {
    var inputSwimDate by remember { mutableStateOf("") }
    var inputStartTime by remember { mutableStateOf("") }
    var inputEndTime by remember { mutableStateOf("") }

    var inputButterflyDistance by remember { mutableStateOf<Int?>(null) }
    var inputBackstrokeDistance by remember { mutableStateOf<Int?>(null) }
    var inputBreaststrokeDistance by remember { mutableStateOf<Int?>(null) }
    var inputFreestyleDistance by remember { mutableStateOf<Int?>(null) }

    val scope = rememberCoroutineScope()

    fun addDataToSupabase() {
        scope.launch {
//            try {
                withContext(Dispatchers.IO) {
                    supabase.from("SwimRecords").insert(
                        SwimRecords(
                            swimDate = inputSwimDate,
                            startTime = inputStartTime,
                            endTime = inputEndTime,

                            butterflyDistance = inputButterflyDistance ?: 0,
                            backstrokeDistance = inputBackstrokeDistance ?: 0,
                            breaststrokeDistance = inputBreaststrokeDistance ?: 0,
                            freestyleDistance = inputFreestyleDistance ?: 0,
                        )
                    )
                }
                // Clear the input fields after insertion
                inputSwimDate = ""
                inputStartTime = ""
                inputEndTime = ""

                inputButterflyDistance = 0
                inputBackstrokeDistance = 0
                inputBreaststrokeDistance = 0
                inputFreestyleDistance = 0
//            }
//            // Handle the exception (show error message, log it, etc.)
//            catch (e: Exception) {
//                e.printStackTrace()
//            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = inputStartTime,
            onValueChange = { inputStartTime = it },
            label = { Text("Start Time") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = inputEndTime,
            onValueChange = { inputEndTime = it },
            label = { Text("End Time") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        TextField(
            value = "${inputButterflyDistance ?: ""}",
            onValueChange = { inputButterflyDistance = it.toIntOrNull() ?: 1 },
            label = { Text("접영 거리") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = "${inputBackstrokeDistance ?: ""}",
            onValueChange = { inputBackstrokeDistance = it.toIntOrNull() ?: 1 },
            label = { Text("배영 거리") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = "${inputBreaststrokeDistance ?: ""}",
            onValueChange = { inputBreaststrokeDistance = it.toIntOrNull() ?: 1 },
            label = { Text("평영 거리") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = "${inputFreestyleDistance ?: ""}",
            onValueChange = { inputFreestyleDistance = it.toIntOrNull() ?: 1 },
            label = { Text("자유형 거리") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                if (inputStartTime.isNotEmpty() && inputEndTime.isNotEmpty()) {
                    addDataToSupabase()
                }
            }, modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun InsertDataNew() {
    var inputSwimDate by remember { mutableStateOf("") }
    var inputStartTime by remember { mutableStateOf("") }
    var inputEndTime by remember { mutableStateOf("") }

    val inputButterflyDistance = remember { mutableStateOf<Int?>(null) }
    val inputBackstrokeDistance = remember { mutableStateOf<Int?>(null) }
    val inputBreaststrokeDistance = remember { mutableStateOf<Int?>(null) }
    val inputFreestyleDistance = remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = inputStartTime,
            onValueChange = { inputStartTime = it },
            label = { Text("Start Time") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = inputEndTime,
            onValueChange = { inputEndTime = it },
            label = { Text("End Time") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        NumberField(inputFreestyleDistance, "접영 거리")
        NumberField(inputBackstrokeDistance, "배영 거리")
        NumberField(inputBreaststrokeDistance, "평영 거리")
        NumberField(inputFreestyleDistance, "자유형 거리")

        val scope = rememberCoroutineScope()
        Button(
            onClick = {
                if (inputStartTime.isNotEmpty() && inputEndTime.isNotEmpty()) {
                    scope.launch(Dispatchers.IO) {
                        supabase.from("SwimRecords").insert<SwimRecords>(
                            SwimRecords(
                                swimDate = inputSwimDate,
                                startTime = inputStartTime,
                                endTime = inputEndTime,

                                butterflyDistance = inputButterflyDistance.value ?: 0,
                                backstrokeDistance = inputBackstrokeDistance.value ?: 0,
                                breaststrokeDistance = inputBreaststrokeDistance.value ?: 0,
                                freestyleDistance = inputFreestyleDistance.value ?: 0,
                            )
                        )
                        // Clear the input fields after insertion
                        inputSwimDate = ""
                        inputStartTime = ""
                        inputEndTime = ""

                        inputButterflyDistance.value = 0
                        inputBackstrokeDistance.value = 0
                        inputBreaststrokeDistance.value = 0
                        inputFreestyleDistance.value = 0
                    }

                }
            }, modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }
    }
}

@Composable
private fun NumberField(
    value: MutableState<Int?>,
    text: String
) {
    TextField(
        value = "${value.value ?: ""}",
        onValueChange = { value.value = it.toIntOrNull() ?: 1 },
        label = { Text(text) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}


@Composable
fun SearchDataOf(date: String) {
    // val currentDate = LocalDate.now()

    var records by remember { mutableStateOf<SwimRecords?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            records = supabase.from("SwimRecords")
                .select {
                    filter {
                        eq("swimDate", date)
                    }
                }
                .decodeSingle<SwimRecords>()
        }
    }

    records?.let {
        Column {
            Text(text = "시간: ${it.swimDate}", modifier = Modifier.padding(8.dp))
            Text(text = "거리: ${it.butterflyDistance}", modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun InsertDate() {
    var inputSwimDate by remember { mutableStateOf("") }
}