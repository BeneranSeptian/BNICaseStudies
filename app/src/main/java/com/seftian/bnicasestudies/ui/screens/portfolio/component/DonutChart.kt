import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.atan2

@Composable
fun DonutChart(sections: List<DonutChartData>, modifier: Modifier = Modifier) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
    var startAngle = 0f
    val context = LocalContext.current

    Canvas(modifier = modifier.size(300.dp).pointerInput(Unit) {
        detectTapGestures { offset ->
            val section = getTouchedSection(sections, offset, size)
            section?.let {
                Toast.makeText(
                    context,
                    "You tapped on ${section.label}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }) {
        for ((index, section) in sections.withIndex()) {
            val sweep = section.percentage / 100f * 360f
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(width = 40f)
            )
            startAngle += sweep
        }
    }
}

fun getTouchedSection(
    sections: List<DonutChartData>,
    offset: Offset,
    canvasSize: IntSize
): DonutChartData? {
    val sizeFloat = Size(canvasSize.width.toFloat(), canvasSize.height.toFloat())
    val centerX = sizeFloat.width / 2
    val centerY = sizeFloat.height / 2

    val touchAngle = Math.toDegrees(
        atan2(
            (centerY - offset.y).toDouble(),
            (offset.x - centerX).toDouble()
        )
    ).let {
        if (it < 0) it + 360 else it
    }

    var startAngle = 0f
    for (section in sections) {
        val sweep = section.percentage / 100f * 360f
        if (touchAngle in startAngle..(startAngle + sweep)) {
            return section
        }
        startAngle += sweep
    }
    return null
}
