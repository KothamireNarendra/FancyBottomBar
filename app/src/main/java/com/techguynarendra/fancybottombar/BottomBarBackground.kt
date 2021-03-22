package com.techguynarendra.fancybottombar

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val path = Path()

@Composable
fun BottomBarBackground(
    modifier: Modifier,
    indicatorValue: Float,
    radius_: Dp,
    diameter_: Dp,
    offset_: Dp
){

    Canvas(modifier = modifier) {

        val width = size.width
        val height = size.height

        val tenPx = 10.dp.toPx()

        val radius = radius_.toPx()
        val diameter = diameter_.toPx()
        val offset = offset_.toPx()

        path.apply {
            reset()
            moveTo(0f, 0f)

            val x = diameter * (indicatorValue + 1)
            val xValueInset = diameter * 0.05f
            val yValueOffset = radius * 4.0f / 3.0f


            val p1 = (x + xValueInset - diameter to yValueOffset)
            val p2 = (x - xValueInset to yValueOffset)
            val p3 = (x to 0f)

            /*cubicTo(
                (half).toFloat(), (radius * 2 * .73).toFloat(), // initially it was w as second param
                (half + radius * 2).toFloat(), (radius * 2  * .73).toFloat(),
                (half + radius * 2).toFloat(), 0f
            )*/

            //println("Narendra "+indicatorIndex.value)
            //println("Narendra "+ map(indicatorValue, 0f, 3f, 1f, 0f))

            //first min curve


            /*val c0 = ((radius * 2f * indicatorValue) + tenPx - offset to 0f)
            val c1 = ((radius * 2f * indicatorValue) + tenPx to 0f)
            val c2 = ((radius * 2f * indicatorValue) + tenPx to offset)*/

            lineTo(( diameter * indicatorValue) + tenPx, 0f)
            lineTo(( diameter * indicatorValue) + tenPx, offset)
            cubicTo(
                p1.first + tenPx, p1.second + offset, // initially it was w as second param
                p2.first + tenPx, p2.second + offset,
                p3.first + tenPx, p3.second + offset
            )
            lineTo(p3.first + tenPx, 0f)

            lineTo(width, 0f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(path,
            color = Color.White,
            style = Fill)

        drawCircle(
            color = Color.White,
            center = Offset(diameter * (indicatorValue) + radius + tenPx , offset),
            radius = radius - 30)
    }


}