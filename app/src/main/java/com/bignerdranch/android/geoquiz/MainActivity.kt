package com.bignerdranch.android.geoquiz

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        //Set listeners to buttons.
        binding.trueButton.setOnClickListener { view: View ->
            checkQuestion(true)
        }

        binding.falseButton.setOnClickListener{ view: View ->
            checkQuestion(false)
        }

        binding.nextButton.setOnClickListener{
            //cycles through the questions and loops once we reach the end
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.previousButton.setOnClickListener{
            //cycle back a question
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener{
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }
        //initialize question
        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        if (quizViewModel.currentQuestionAnswered)
            greyButtons()
        else
            normalButtons()
    }

    private fun greyButtons() {
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false
        binding.cheatButton.isEnabled = false
        binding.trueButton.setBackgroundColor(getColor(R.color.material_dynamic_neutral60))
        binding.falseButton.setBackgroundColor(getColor(R.color.material_dynamic_neutral60))
        binding.cheatButton.setBackgroundColor(getColor(R.color.material_dynamic_neutral60))
    }

    private fun normalButtons() {
        binding.trueButton.isEnabled = true
        binding.falseButton.isEnabled = true
        binding.cheatButton.isEnabled = true
        binding.trueButton.setBackgroundColor(getColor(R.color.design_default_color_primary))
        binding.falseButton.setBackgroundColor(getColor(R.color.design_default_color_primary))
        binding.cheatButton.setBackgroundColor(getColor(R.color.design_default_color_primary))
    }

    private fun checkQuestion(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        if (userAnswer == correctAnswer)
            quizViewModel.incrementQuestionsCorrect()

        Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        ).show()

        quizViewModel.questionAnswered()
        greyButtons()
        quizViewModel.incrementAnswers()
        if (quizViewModel.questionsAnswered == quizViewModel.numQuestions) {
            Toast.makeText(
                this,
                "Your score is: " + quizViewModel.questionsCorrect + "/" + quizViewModel.numQuestions,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
