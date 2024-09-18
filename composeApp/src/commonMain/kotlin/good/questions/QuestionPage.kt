package good.questions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    audience: String,
    isRandom: Boolean,
    isLoopEnabled: Boolean,
    navController: NavHostController
) {

    val questions = remember { mutableStateOf<List<String>>(emptyList()) }
    val viewModel: QuestionsViewModel = remember { QuestionsViewModel(questions.value) }



    val currentQuestion = rememberSaveable { mutableStateOf(viewModel.onNextClick(isRandom, isLoopEnabled)) }
    val isEndOfList by viewModel.endOfQuestionList.collectAsState()

    LaunchedEffect(audience) {
        questions.value = getQuestionsForAudience(audience)
        viewModel.importQuestions(questions.value)
        currentQuestion.value = viewModel.onNextClick(isRandom, isLoopEnabled)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QuestionControls({ currentQuestion.value = viewModel.onBackClick(currentQuestion.value) },
            {  currentQuestion.value =viewModel.onNextClick(isRandom, isLoopEnabled) },  currentQuestion.value)

       // Stopwatch(currentQuestion.value)


        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = Shapes().large,
            //modifier = Modifier.sizeIn(minWidth =300.dp , minHeight =300.dp )
        ) {

            Column(
                Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = audience, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = currentQuestion.value, style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(16.dp))
            }



        }

      /*  AnimatedVisibility(!isEndOfList){
            Button(onClick = {
                currentQuestion.value = viewModel.getNextQuestion(isRandom, isLoopEnabled)
            }) {
                Text("Next Question")
            }
        }*/


        if (isEndOfList) {
            FloatingActionButton(
                onClick = { navController.navigate("main") },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Main")
            }
        }
    }


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
fun Stopwatch(currentQuestion: String) {
    var timeInSeconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(true) }
    var isCollapsed by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            timeInSeconds++
        }
    }
    LaunchedEffect(currentQuestion) {
        timeInSeconds = 0
        isRunning = true
    }

    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds % 60

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = !isCollapsed,
            enter= fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { isCollapsed = true },
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text =  "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        AnimatedVisibility(visible = isCollapsed,
            enter= fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()) {
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
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    currentQuestion: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        // Stopwatch (Placeholder for actual stopwatch implementation)
       Stopwatch(currentQuestion)

        // Next Button
        IconButton(onClick = onNextClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}

suspend fun getQuestionsForAudience(audience: String): List<String> {
    return when (audience) {
        "University Students" ->getStringArray(Res.array.questions_university)
        "Close Friends" ->getStringArray(Res.array.questions_friends)
        "Couples" -> getStringArray(Res.array.questions_lovers)
        "Coworkers" -> getStringArray(Res.array.team_building_questions)
        "The 36 Questions That Lead to Love" -> getStringArray(Res.array.Thirtisixquestions_that_lead_to_love)
        else -> listOf("There should have been an audience selected!")
    }
}