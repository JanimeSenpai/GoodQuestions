package good.questions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import goodquestions.composeapp.generated.resources.Res
import goodquestions.composeapp.generated.resources.Thirtisixquestions_that_lead_to_love
import goodquestions.composeapp.generated.resources.questions_friends
import goodquestions.composeapp.generated.resources.questions_lovers
import goodquestions.composeapp.generated.resources.questions_university
import goodquestions.composeapp.generated.resources.team_building_questions
import goodquestions.composeapp.generated.resources.timer
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.getStringArray
import org.jetbrains.compose.resources.painterResource

@Composable
fun QuestionsPage(
    audience: String, isRandom: Boolean, isLoopEnabled: Boolean, navController: NavHostController
) {

    val questions = remember { mutableStateOf<List<String>>(emptyList()) }
    val viewModel: QuestionsViewModel = remember { QuestionsViewModel(questions.value) }


    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val isEndOfList by viewModel.endOfQuestionList.collectAsState()

    LaunchedEffect(audience) {
        questions.value = getQuestionsForAudience(audience)
        viewModel.importQuestions(questions.value)
        viewModel.onNextClick(isRandom, isLoopEnabled)
    }

    Scaffold(
        floatingActionButton = {
            if (isEndOfList) {
                FloatingActionButton(
                    onClick = { navController.navigate("main") },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Main")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center, // Position the FAB at the center bottom
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QuestionControls(
                    onBackClick = { viewModel.onBackClick() },
                    onNextClick = { viewModel.onNextClick(isRandom, isLoopEnabled) },
                    currentQuestion = currentQuestion,
                    isEndOfList = isEndOfList
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = Shapes().large,
                        modifier = Modifier
                            .animateContentSize()
                            .wrapContentSize()
                    ) {
                        Column(
                            Modifier
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = audience, style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (isEndOfList) {
                                    "We reached the end of the question list"
                                } else currentQuestion,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    )
}




/*fun getQuestionsForAudienceB( audience: String): List<String> {

    val typedArrayId = when (audience) {
        "University Students" -> Res.array.questions_university
        "Close Friends" -> Res.array.questions_friends
        // Add other audiences as needed
        else -> null
    } ?: return listOf("There should have been an audience selected!")

    val typedArray = Res.obtainTypedArray(typedArrayId)
    val questions = mutableListOf<String>()
    for (i in 0 until typedArray.length()) {
        val resId = typedArray.getResourceId(i, 0)
        if (resId != 0) {
            questions.add(res.getString(resId))
        }
    }
    typedArray.recycle()
    return questions
}*/
@Composable
fun Stopwatch(currentQuestion: String, isEndOfList: Boolean) {
    var timeInSeconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(true) }
    var isCollapsed by remember { mutableStateOf(false) }
    var totalTimeInSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(isRunning, isEndOfList) {
        while (isRunning && !isEndOfList) {
            delay(1000L)
            timeInSeconds++
            totalTimeInSeconds++

        }
    }

    LaunchedEffect(currentQuestion) {
        timeInSeconds = 0
        isRunning = true
    }
    LaunchedEffect(isEndOfList) {
        if (isEndOfList) {
            isRunning = false
        }
    }
    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds % 60

    val totalMinutes = totalTimeInSeconds / 60
    val totalSeconds = totalTimeInSeconds % 60
    Column(
        modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = !isCollapsed, enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()
        ) {
            Surface(
                modifier = Modifier.width(150.dp) // Set width for rectangle
                    .height(60.dp) // Set height for rectangle
                    .clickable { isCollapsed = true },
                tonalElevation = 4.dp,
                shadowElevation = 4.dp,
                shape = RoundedCornerShape(32.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (!isEndOfList) Text(
                        text = "${minutes.toString().padStart(2, '0')}:${
                            seconds.toString().padStart(2, '0')
                        }",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    if (isEndOfList) {
                        Text(
                            text = "${
                                totalMinutes.toString().padStart(2, '0')
                            }:${totalSeconds.toString().padStart(2, '0')}",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isCollapsed, enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()
        ) {
            IconButton(onClick = { isCollapsed = false }) {
                Icon(
                    painterResource(Res.drawable.timer),
                    contentDescription = "Timer Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun QuestionControls(
    onBackClick: () -> Unit, onNextClick: () -> Unit, currentQuestion: String, isEndOfList: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button

        AnimatedVisibility(!isEndOfList) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                )
            }
        }


        // Stopwatch (Placeholder for actual stopwatch implementation)
        Stopwatch(currentQuestion, isEndOfList)

        AnimatedVisibility(!isEndOfList) {
            IconButton(onClick = onNextClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }

    }
}

suspend fun getQuestionsForAudience(audience: String): List<String> {
    return when (audience) {
        "University Students" -> getStringArray(Res.array.questions_university)
        "Close Friends" -> getStringArray(Res.array.questions_friends)
        "Couples" -> getStringArray(Res.array.questions_lovers)
        "Coworkers" -> getStringArray(Res.array.team_building_questions)
        "The 36 Questions That Lead to Love" -> getStringArray(Res.array.Thirtisixquestions_that_lead_to_love)
        else -> listOf("There should have been an audience selected!")
    }
}