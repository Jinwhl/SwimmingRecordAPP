package com.example.syeongboard.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
val supabase = createSupabaseClient(
    supabaseUrl = "",
    supabaseKey = ""
) { install(Postgrest) }

@Composable
fun TestScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Spacer(modifier = Modifier.height(24.dp))
            AddData()
            ShowSwimDate()
            ShowStartTime()
            ShowEndTime()
        }
    }
}
@Serializable
data class SwimRecords(
    val id: Int? = null, // id is now nullable to allow automatic ID generation
    val swimDate: String,
    val startTime: String,
    val endTime: String,
)

@Composable
fun ShowSwimDate() {
    var swimDate by remember { mutableStateOf<List<SwimRecords>>(listOf()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            swimDate = supabase.from("SwimRecords")
                .select().decodeList<SwimRecords>()
        }
    }

    LazyColumn {
        items( swimDate, key = { swimDate -> swimDate.id ?: 0 } ) {
            swimDate ->
            Text( text = swimDate.swimDate )
        }
    }
}
@Composable
fun ShowStartTime() {
    var startTime by remember { mutableStateOf<List<SwimRecords>>(listOf()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            startTime = supabase.from("SwimRecords")
                .select().decodeList<SwimRecords>()
        }
    }

    LazyColumn {
        items( startTime, key = { startTime -> startTime.id ?: 0 } ) {
                startTime ->
            Text( text = startTime.startTime )
        }
    }
}
@Composable
fun ShowEndTime() {
    var endTime by remember { mutableStateOf<List<SwimRecords>>(listOf()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            endTime = supabase.from("SwimRecords")
                .select().decodeList<SwimRecords>()
        }
    }

    LazyColumn {
        items( endTime, key = { endTime -> endTime.id ?: 0 } ) {
                endTime ->
            Text( text = endTime.endTime )
        }
    }
}
@Composable
fun AddData() {
    var swimDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    fun AddDataToSupabase() {
        scope.launch {
            try {
                withContext(Dispatchers.IO) {
                    supabase.from("SwimRecords").insert(SwimRecords(swimDate = swimDate, startTime = startTime, endTime = endTime))
                }
                // Clear the input fields after insertion
                swimDate = ""
                startTime = ""
                endTime = ""
            } catch (e: Exception) {
                // Handle the exception (show error message, log it, etc.)
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = swimDate,
            onValueChange = { swimDate = it },
            label = { Text("Swim Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = startTime,
            onValueChange = { startTime = it },
            label = { Text("Start Time") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = endTime,
            onValueChange = { endTime = it },
            label = { Text("End Time") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (swimDate.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty()) {
                    AddDataToSupabase()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }
    }
}