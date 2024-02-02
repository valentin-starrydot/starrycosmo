package com.starrydot.starrycosmo.design.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starrydot.starrycosmo.R
import com.starrydot.starrycosmo.design.color.ColorPalette
import com.starrydot.starrycosmo.design.font.FallingSky

/**
 * Custom View used to display a specific information
 */

data class InformationSection(val title: String, val iconResId: Int? = null)

@Composable
fun InformationCard(
    modifier: Modifier = Modifier,
    header: String,
    sections: List<InformationSection>
) {
    Column(
        modifier = modifier
            .background(
                color = ColorPalette.Secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Start),
            text = header,
            color = ColorPalette.Tertiary,
            fontSize = 24.sp,
            fontFamily = FallingSky,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column(modifier = Modifier.wrapContentSize()) {
            sections.forEachIndexed { index, section ->
                //Add separator if not first item
                if (index != 0) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                //Add section content
                InformationCardSectionView(
                    modifier = Modifier.wrapContentSize(),
                    informationSection = section
                )
            }
        }
    }
}

@Composable
fun InformationCardSectionView(modifier: Modifier, informationSection: InformationSection) {
    Row(modifier = modifier) {
        informationSection.iconResId?.let {
            Image(
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = informationSection.iconResId),
                contentDescription = stringResource(id = R.string.information_card_icon_content_description)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically),
            text = informationSection.title,
            color = ColorPalette.Tertiary,
            fontSize = 16.sp,
            fontFamily = FallingSky,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
fun InformationCard_Preview() {
    InformationCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        header = "Ride",
        sections = listOf(
            InformationSection(title = "Ride Lite"),
            InformationSection(title = "4921201e38d5")
        ),
    )
}

@Preview
@Composable
fun InformationCard_WithIcon_Preview() {
    InformationCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        header = "Ride",
        sections = listOf(
            InformationSection(title = "Ride Lite", iconResId = R.drawable.ic_product_identifier),
            InformationSection(title = "4921201e38d5", iconResId = R.drawable.ic_mac_address)
        ),
    )
}

