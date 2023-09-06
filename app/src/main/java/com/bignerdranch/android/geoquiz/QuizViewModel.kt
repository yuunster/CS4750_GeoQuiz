package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val QUESTIONS_ANSWERED_KEY = "QUESTIONS_ANSWERED_KEY"
const val QUESTIONS_CORRECT_KEY = "QUESTIONS_CORRECT_KEY"
class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    //List of questions
    private val questionBank = listOf(
        Question(R.string.question_australia, true, false),
        Question(R.string.question_oceans, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_africa, false, false),
        Question(R.string.question_americas, true, false),
        Question(R.string.question_asia, true, false)
    )

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)
    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)
    var questionsAnswered: Int
        get() = savedStateHandle.get(QUESTIONS_ANSWERED_KEY) ?: 0
        set(value) = savedStateHandle.set(QUESTIONS_ANSWERED_KEY, value)
    var questionsCorrect: Int
        get() = savedStateHandle.get(QUESTIONS_CORRECT_KEY) ?: 0
        set(value) = savedStateHandle.set(QUESTIONS_CORRECT_KEY, value)
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    fun questionAnswered() {
        questionBank[currentIndex].answered = true
    }

    val numQuestions: Int
        get() = questionBank.size

    val currentQuestionAnswered: Boolean
        get() = questionBank[currentIndex].answered
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        currentIndex--
        if (currentIndex < 0) currentIndex = questionBank.size - 1
    }

    fun incrementQuestionsCorrect() {
        questionsCorrect++
    }

    fun incrementAnswers() {
        questionsAnswered++
    }
}