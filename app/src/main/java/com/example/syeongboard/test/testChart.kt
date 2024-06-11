package com.example.syeongboard.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.example.syeongboard.utils.MyColor

@Composable
fun SwimArchiveBars(
    butterflyDistance: Int?,
    backstrokeDistance: Int?,
    breaststrokeDistance: Int?,
    freestyleDistance: Int?,
    scale: Float,
    clipSize: Int,
    boxColor: Color,
    width: Int,
    height: Int,
    tagFlag: Boolean
) {
    val bars = listOf(
        MyColor.Blue to (butterflyDistance ?: 0),
        MyColor.SkyBlue to (backstrokeDistance ?: 0),
        MyColor.Orange to (breaststrokeDistance ?: 0),
        MyColor.Green to (freestyleDistance ?: 0)
    )
    val tags = listOf(
        '접' to (butterflyDistance ?: 0),
        '배' to (backstrokeDistance ?: 0),
        '평' to (breaststrokeDistance ?: 0),
        '자' to (freestyleDistance ?: 0)
    )

    val barWidth = width.dp
    val scaledWidth = barWidth * scale
    val barHeight = (height.dp * scale)

    Box(
        modifier = Modifier
            .size(scaledWidth)
            .clip(RoundedCornerShape(clipSize.dp))
            .background(boxColor)
    ) {
        var xOffset = 0.dp
        var yOffset = 0.dp

        bars.forEachIndexed { index, (color, value) ->
            var remainingValue = value
            while (remainingValue > 0) {
                val availableWidth = scaledWidth - xOffset
                val barWidth = min(remainingValue.dp * scale, availableWidth)
                Box(
                    modifier = Modifier
                        .offset(xOffset, yOffset)
                        .size(barWidth, barHeight)
                        .background(color)
                ) {
                    if (tagFlag && remainingValue == value) {
                        Text(
                            text = "  ${tags[index].first} ${value}m",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }
                }
                val consumedValue = (barWidth / scale).value.toInt()
                remainingValue -= consumedValue
                xOffset += barWidth
                if (xOffset >= scaledWidth) {
                    xOffset = 0.dp
                    yOffset += barHeight
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}




