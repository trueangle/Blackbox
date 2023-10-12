package io.github.trueangle.blackbox.sample.movie.ticketing.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FlowTopAppBar(
    stepIndex: Int,
    totalStepsCount: Int,
    onClosePressed: () -> Unit,
    onBackPressed: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CenterAlignedTopAppBar(
            title = {
                WizardAppBarTitle(
                    stepIndex = stepIndex,
                    totalStepsCount = totalStepsCount,
                )
            },
            navigationIcon = {
                if (stepIndex > 0) {
                    IconButton(
                        onClick = onBackPressed,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(0.6F)
                        )
                    }
                }
            },
            actions = {
                IconButton(
                    onClick = onClosePressed,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(0.6F)
                    )
                }
            }
        )

        val animatedProgress by animateFloatAsState(
            targetValue = (stepIndex + 1) / totalStepsCount.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
        )

        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth(),
            trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
        )
    }
}

@Composable
internal fun Heading(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6F),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = MaterialTheme.shapes.small
            )
    )
}

@Composable
private fun WizardAppBarTitle(
    stepIndex: Int,
    totalStepsCount: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = (stepIndex + 1).toString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6F)
        )
        Text(
            text = "\u00A0of $totalStepsCount",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }
}
