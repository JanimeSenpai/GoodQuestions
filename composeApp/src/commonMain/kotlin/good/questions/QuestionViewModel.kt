package good.questions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuestionsViewModel(
    private var questions: List<String>
) : ViewModel() {

    private val _remainingQuestions = MutableStateFlow(questions.toMutableList())
    val remainingQuestions: StateFlow<List<String>> = _remainingQuestions.asStateFlow()

    private val _askedQuestions = MutableStateFlow(mutableListOf<String>())
    val askedQuestions: StateFlow<List<String>> = _askedQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _currentQuestion = MutableStateFlow("")//todo itt folytatni
    val currentQuestion: StateFlow<String> = _currentQuestion.asStateFlow()

    private val _endOfQuestionList = MutableStateFlow(false)
    val endOfQuestionList: StateFlow<Boolean> = _endOfQuestionList.asStateFlow()

    private val _firstback = MutableStateFlow(false)
    val firstback: StateFlow<Boolean> = _firstback.asStateFlow()



    fun onBackClick(currentQ: String):String{
        var lastAskedQuestion: String = currentQuestion.value
        if (_askedQuestions.value.size>=2&&firstback.value) {
             lastAskedQuestion = _askedQuestions.value.removeLast()
            _remainingQuestions.value = _remainingQuestions.value.toMutableList().apply { add(0, lastAskedQuestion) }
            lastAskedQuestion = _askedQuestions.value.removeLast()
            _remainingQuestions.value = _remainingQuestions.value.toMutableList().apply { add(0, lastAskedQuestion) }
            _currentQuestionIndex.value =0// questions.indexOf(lastAskedQuestion)
            _firstback.value=false
        }else{
            if(_askedQuestions.value.size>=2) {//asked questions contains the current question and must contain another one to go back to
                lastAskedQuestion = _askedQuestions.value.removeLast()
                _remainingQuestions.value =
                    _remainingQuestions.value.toMutableList().apply { add(0, lastAskedQuestion) }
                _currentQuestionIndex.value =0// questions.indexOf(lastAskedQuestion)
            }


        }
     return   lastAskedQuestion
    }

    fun onNextClick(isRandom: Boolean, isLoopEnabled: Boolean) :String{
        _firstback.value=true
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
                resetForLooping(true)//deprecated
                return if (_remainingQuestions.value.isNotEmpty()) {
                    onNextClick(isRandom, isLoopEnabled)
                } else {
                    "No questions available after reset"
                }
            } else {
                _endOfQuestionList.value = true
                return "We reached the end of the question list"
            }
        }
    }


    fun getNextQuestion(isRandom: Boolean, isLoopEnabled: Boolean): String {//deprecated
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
                resetForLooping(false)//deprecated
                return if (_remainingQuestions.value.isNotEmpty()) {
                    getNextQuestion(isRandom, isLoopEnabled)
                } else {
                    "No questions available after reset"
                }
            } else {
                _endOfQuestionList.value = true
                return "We reached the end of the question list"
            }
        }
    }

    fun importQuestions(newQuestions: List<String>) {
        questions  = newQuestions.toMutableList()
        resetForLooping(preserveLastQuestion = false)
    }
    fun resetForLooping(preserveLastQuestion: Boolean) {
        val lastQuestion = if (preserveLastQuestion && _askedQuestions.value.isNotEmpty()) {
            _askedQuestions.value.last()
        } else {
            null
        }

        _remainingQuestions.value = questions.toMutableList()
        _askedQuestions.value.clear()

        lastQuestion?.let {
            _askedQuestions.value.add(it)
            //_remainingQuestions.value.remove(it)
            _currentQuestionIndex.value = 0
        } ?: run {
            _currentQuestionIndex.value = 0
        }

        _endOfQuestionList.value = false
    }
}