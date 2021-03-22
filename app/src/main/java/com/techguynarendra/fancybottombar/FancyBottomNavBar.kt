package com.techguynarendra.fancybottombar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun FancyBottomBarNavbar() {

    val springSpec = remember {
        SpringSpec<Float>(
            stiffness = 800f,
            dampingRatio = 0.6f
        )
    }

    val animSpec = remember {
        TweenSpec<Float>(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    }

    val selected = remember { mutableStateOf(0) }
    val prevSelected = remember { mutableStateOf(0) }

    // Animate the position of the indicator
    val indicatorIndex = remember { Animatable(0f) }
    val targetIndicatorIndex = selected.value.toFloat()
    LaunchedEffect(targetIndicatorIndex) {
        indicatorIndex.animateTo(targetIndicatorIndex, springSpec)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(56.dp)
    ) {

        val radius = remember { (maxWidth - 20.dp) / 10 }
        val diameter = remember { radius * 2f }
        val offset = remember { (maxHeight - radius) / 2 }
        val iconWidth = remember {radius - 15.dp }
        BottomBarBackground(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            indicatorValue = indicatorIndex.value,
            radius,
            diameter,
            offset
        )

        val t = diameter * (indicatorIndex.value) + radius

        val alphaAnimatable = remember { Animatable(0f) }
        var currentIcon = icons[prevSelected.value]

        (0..4).forEach {
            if (t <= (diameter * it) + radius + iconWidth && t >= (diameter * it) + radius - iconWidth) {
                currentIcon = icons[it]
                LaunchedEffect(true) {
                    alphaAnimatable.snapTo(0f)
                    alphaAnimatable.animateTo(1f, animSpec)
                }
            }
        }

        Box(
            modifier = Modifier
                .width(diameter)
                .height(diameter)
                .graphicsLayer {
                    translationX = (t - radius + 10.dp).toPx()
                    translationY = (-maxHeight / 2 + offset).toPx()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = currentIcon,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
                    .graphicsLayer {
                        alpha = alphaAnimatable.value
                        scaleY = alphaAnimatable.value
                        scaleX = alphaAnimatable.value

                    },
                tint = color
            )
        }


        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.BottomStart)
                .padding(horizontal = 10.dp)
        ) {
            (0..4).forEach {
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        prevSelected.value = selected.value
                        selected.value = it
                    }
                ) {
                    var a = 1f
                    if (t <= (diameter * it) + radius + iconWidth && t >= (diameter * it) + radius - iconWidth) {
                        a = 0f
                    }
                    if (selected.value == it) {
                        if (prevSelected.value < selected.value && t > (diameter * selected.value) + radius + iconWidth) {
                            a = 0f
                        } else if (prevSelected.value > selected.value && t < (diameter * selected.value) + radius - iconWidth) {
                            a = 0f
                        }
                    }

                    Icon(
                        imageVector = icons[it],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp)
                            .graphicsLayer {
                                alpha = a

                            },
                        tint = Color.Gray
                    )
                }
            }
        }

    }

}

val icons = listOf(
    Icons.Outlined.Home,
    Icons.Outlined.Search,
    Icons.Outlined.Share,
    Icons.Outlined.Place,
    Icons.Outlined.Face
)
val color = Color(78, 97, 236)