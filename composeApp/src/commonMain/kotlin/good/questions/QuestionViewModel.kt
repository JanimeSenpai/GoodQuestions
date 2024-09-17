package good.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionsViewModel(
    private var questions: List<String>
) : ViewModel() {

    private val _remainingQuestions = MutableStateFlow(questions.toMutableList())
    val remainingQuestions: StateFlow<List<String>> = _remainingQuestions.asStateFlow()

    private val _askedQuestions = MutableStateFlow(mutableListOf<String>())
    val askedQuestions: StateFlow<List<String>> = _askedQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _endOfQuestionList = MutableStateFlow(false)
    val endOfQuestionList: StateFlow<Boolean> = _endOfQuestionList.asStateFlow()

    fun getNextQuestion(isRandom: Boolean, isLoopEnabled: Boolean): String {
        if (_remainingQuestions.value.isNotEmpty()) {
            val nextIndex = if (isRandom) {
                _remainingQuestions.value.indices.random()
            } else {
                _currentQuestionIndex.value
            }

            val nextQuestion = _remainingQuestions.value[nextIndex]
            _remainingQuestions.value = _remainingQuestions.value.toMutableList().apply { removeAt(nextIndex) }
            _askedQuestions.value = _askedQuestions.value.toMutableList().apply { add(nextQuestion) }

            _currentQuestionIndex.value = nextIndex

            return nextQuestion
        } else {
            if (isLoopEnabled) {
                resetForLooping()
                return getNextQuestion(isRandom, isLoopEnabled)
            } else {
                _endOfQuestionList.value = true
                return "We reached the end of the question list"
            }
        }
    }

    fun importQuestions(newQuestions: List<String>) {
        questions  = newQuestions.toMutableList()
        resetForLooping()
    }
    fun resetForLooping() {
        _remainingQuestions.value = questions.toMutableList()
        _askedQuestions.value.clear()
        _currentQuestionIndex.value = 0
        _endOfQuestionList.value = false
    }
}