package com.example.syeongboard.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.syeongboard.utils.MyColor

@Composable
fun StackedBars(
    butterflyDistance: Int?,
    backstrokeDistance: Int?,
    breaststrokeDistance: Int?,
    freestyleDistance: Int?,
    scale: Float,
    clipSize: Int,
    boxColor: Color,
) {
    val bars = listOf(
        MyColor.Blue to (butterflyDistance ?: 0),
        MyColor.SkyBlue to (backstrokeDistance ?: 0),
        MyColor.Orange to (breaststrokeDistance ?: 0),
        MyColor.Green to (freestyleDistance ?: 0)
    )

    val baseSize = 440.dp
    val scaledSize = baseSize * scale
    val barHeight = (88.dp * scale)

    Box(
        modifier = Modifier
            .size(scaledSize)
            .clip(RoundedCornerShape(clipSize.dp))
            .background(boxColor)
    ) {
        var xOffset = 0.dp
        var yOffset = 0.dp

        bars.forEach { (color, value) ->
            var remainingValue = value
            while (remainingValue > 0) {
                val availableWidth = scaledSize - xOffset
                val barWidth = min(remainingValue.dp * scale, availableWidth)
                Box(
                    modifier = Modifier
                        .offset(xOffset, yOffset)
                        .size(barWidth, barHeight)
                        .background(color)
                )
                val consumedValue = (barWidth / scale).value.toInt()
                remainingValue -= consumedValue
                xOffset += barWidth
                if (xOffset >= scaledSize) {
                    xOffset = 0.dp
                    yOffset += barHeight
                }
            }
        }
    }
}