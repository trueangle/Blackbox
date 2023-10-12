package io.github.trueangle.blackbox.sample.movie.shared.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InterestTag(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    style: TextStyle = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
    onClick: () -> Unit = {}
) {
    val tagModifier = modifier
        .padding(4.dp)
        .clickable(onClick = onClick)
        .clip(shape = shape)
        .background(MaterialTheme.colorScheme.tertiaryContainer)
        .padding(horizontal = 8.dp, vertical = 4.dp)
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = tagModifier,
        style = style
    )
}
