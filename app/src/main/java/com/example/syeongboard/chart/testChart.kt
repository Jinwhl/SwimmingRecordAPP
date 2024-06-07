package com.example.syeongboard.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BarChart(
    barData: List<Pair<String, Float>>, // Pair of label and value
    colors: List<Color>, // List of colors for the bars
    modifier: Modifier = Modifier
) {
    val maxValue = barData.maxOf { it.second }

    Column(modifier = modifier.padding(16.dp)) {
        // Display each bar
        barData.forEachIndexed { index, data ->
            val label = data.first
            val value = data.second
            val percentage = value / maxValue
            val barColor = colors.getOrElse(index) { Color.Gray } // Default to gray if not enough colors

            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth()
                ) {
                    drawRect(
                        color = barColor,
                        size = Size(this.size.width * percentage, this.size.height)
                    )
                    drawContext.canvas.nativeCanvas.apply {
                        val text = "$label ${value.toInt()}m"
                        val paint = Paint().asFrameworkPaint().apply {
                            var isAntiAlias = true
                            textSize = 8.sp.toPx()
                            color = android.graphics.Color.WHITE // Text color
                        }
                        drawText(
                            text,
                            10.dp.toPx(), // Text x position
                            (this@Canvas.size.height / 2) + (paint.textSize / 2), // Text y position
                            paint
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBarChart() {
    val sampleData = listOf(
        "접" to 400f,
        "배" to 0f,
        "평" to 400f,
        "자" to 200f,
    )
    val colors = listOf(
        Color(0xFF4382F5), // Blue
        Color(0xFFB4CDFB), // Light Blue
        Color(0xFFFFA900), // Orange
        Color(0xFF22D081), // Green
    )
    BarChart(barData = sampleData, colors = colors)
}