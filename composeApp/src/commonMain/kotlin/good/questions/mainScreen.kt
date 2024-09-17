package good.questions

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
        "Close Friends",
        "Couples",
        "Coworkers",
        "The 36 Questions That Lead to Love",
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        audiences.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowItems.forEach { audience ->
                    AudienceSurface(
                        audience = audience,
                        onPlayClick = onPlayClick
                    )
                }
            }
        }
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

@Composable
fun AudienceSurface(
    audience: String,
    onPlayClick: (String, Boolean, Boolean) -> Unit
) {
    var isMixEnabled by rememberSaveable { mutableStateOf(false) }
    var isLoopEnabled by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .padding(8.dp)
            .size(180.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = audience, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)

          Row  {// Play Button
                IconButton(onClick = {
                    onPlayClick(audience, isMixEnabled, isLoopEnabled)
                }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                }

                // Mix Toggle Button
                IconButton(onClick = {
                    isMixEnabled = !isMixEnabled
                }) {
                    Icon(
                        if (isMixEnabled) painterResource(Res.drawable.shuffleIcon_on) else painterResource(
                            Res.drawable.shuffleIcon
                        ),
                        contentDescription = "Mix"
                    )
                }

                // Loop Toggle Button
                IconButton(onClick = {
                    isLoopEnabled = !isLoopEnabled
                }) {
                    Icon(
                        if (isLoopEnabled) painterResource(Res.drawable.repeat_on) else painterResource(
                            Res.drawable.repeat
                        ),
                        contentDescription = "Loop"
                    )
                }
            }


        }

    }
}