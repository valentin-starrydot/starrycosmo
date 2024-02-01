package com.starrydot.starrycosmo.presentation.design.button

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starrydot.starrycosmo.R
import com.starrydot.starrycosmo.presentation.design.color.ColorPalette
import com.starrydot.starrycosmo.presentation.design.font.FallingSky

/**
 * Custom button with StarryCosmo'style
 */

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    title: String,
    iconResId: Int? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(
                color = if (enabled) ColorPalette.Secondary else ColorPalette.Secondary.copy(alpha = .3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(),
                onClick = {
                    if (enabled) {
                        onClick.invoke()
                    }
                }
            )
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically),
            text = title,
            fontFamily = FallingSky,
            color = ColorPalette.Tertiary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        iconResId?.let {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp),
                painter = painterResource(id = iconResId),
                contentDescription = "",
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(color = ColorPalette.Tertiary)
            )
        }
    }
}

@Preview
@Composable
fun SecondaryButton_Enabled_Preview() {
    SecondaryButton(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        title = "Grant permissions",
        iconResId = R.drawable.ic_next,
        enabled = true,
        onClick = {
            //Do nothing
        }
    )
}


@Preview
@Composable
fun SecondaryButton_Disabled_Preview() {
    SecondaryButton(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        title = "Grant permissions",
        iconResId = R.drawable.ic_next,
        enabled = false,
        onClick = {
            //Do nothing
        }
    )
}
