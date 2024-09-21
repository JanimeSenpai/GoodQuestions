package good.questions

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import goodquestions.composeapp.generated.resources.BuyMeACoffee
import goodquestions.composeapp.generated.resources.Res
import goodquestions.composeapp.generated.resources.compose_multiplatform
import goodquestions.composeapp.generated.resources.repeat
import goodquestions.composeapp.generated.resources.repeat_on
import goodquestions.composeapp.generated.resources.shuffleIcon
import goodquestions.composeapp.generated.resources.shuffleIcon_on
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreen(
    onPlayClick: (String, Boolean, Boolean) -> Unit
) {
    val audiences = listOf(
        "University Students",
        "Getting acquainted",
        "Friends",
        "Close Friends",
        "Coworkers",
        "Recap: Long time no see",
        "Speed dating",
        "First date",
        "Couples",
        "The 36 Questions That Lead to Love",
    )
    var selectedAudience by rememberSaveable { mutableStateOf<String?>(null) }
    var isMixEnabled by rememberSaveable { mutableStateOf(false) }
    var isLoopEnabled by rememberSaveable { mutableStateOf(false) }
    BoxWithConstraints {
        val itemWidth = 160.dp
        val spacing = 16.dp
        val totalItemWidth = itemWidth + spacing

        // Calculate how many items fit in the available width
        val itemsPerRow = (maxWidth / totalItemWidth).toInt().coerceIn(1,6)

        Column(
            modifier = Modifier.fillMaxSize().padding(6.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),//todo görgetés helyett jobb lenne egy lapozós megoldás
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                audiences.chunked(itemsPerRow).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        rowItems.forEach { audience ->
                            AudienceSurface(
                                audience = audience,
                                isSelected = selectedAudience == audience,
                                onSelect = { selectedAudience = audience }
                            )
                        }
                    }
                }
            }

            ControlButtons(
                selectedAudience = selectedAudience,
                isMixEnabled = isMixEnabled,
                isLoopEnabled = isLoopEnabled,
                onMixToggle = { isMixEnabled = !isMixEnabled },
                onLoopToggle = { isLoopEnabled = !isLoopEnabled },
                onPlayClick = {
                    selectedAudience?.let { audience ->
                        onPlayClick(audience, isMixEnabled, isLoopEnabled)
                    }
                }
            )
            Image(
                painter = painterResource(Res.drawable.BuyMeACoffee),
                contentDescription = "Buy Me a Coffee",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        // Define the action, e.g., open a URL or navigate
                    }.sizeIn(maxWidth = 300.dp, maxHeight = 150.dp)
            )
        }
    }
}

@Composable
fun AudienceSurface(
    audience: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val targetColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    val animatedColor by animateColorAsState(targetColor)

    Surface(
        modifier = Modifier
            .padding(8.dp)
            .size(160.dp)
            .clickable { onSelect() },
        shape = MaterialTheme.shapes.medium,
        color = animatedColor
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = audience,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ControlButtons(
    selectedAudience: String?,
    isMixEnabled: Boolean,
    isLoopEnabled: Boolean,
    onMixToggle: () -> Unit,
    onLoopToggle: () -> Unit,
    onPlayClick: () -> Unit
) {
    Surface(
        modifier = Modifier .wrapContentWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier

                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMixToggle) {
                Icon(
                    painter = if (isMixEnabled) painterResource(Res.drawable.shuffleIcon_on) else painterResource(
                        Res.drawable.shuffleIcon
                    ),
                    contentDescription = "Mix"
                )
            }
            Spacer(modifier =Modifier.width(16.dp))
            IconButton(onClick = onLoopToggle) {
                Icon(
                    painter = if (isLoopEnabled) painterResource(Res.drawable.repeat_on) else painterResource(
                        Res.drawable.repeat
                    ),
                    contentDescription = "Loop"
                )
            }
            Spacer(modifier =Modifier.width(16.dp))

            IconButton(
                onClick = onPlayClick,
                enabled = selectedAudience != null
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        }
    }
}