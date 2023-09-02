package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //List of questions
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))
    private var currentIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set listeners to buttons.
        binding.trueButton.setOnClickListener { view: View ->
            checkQuestion(true)
        }

        binding.falseButton.setOnClickListener{ view: View ->
            checkQuestion(false)
        }

        binding.nextButton.setOnClickListener{
            //cycles through the questions and loops once we reach the end
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        //initialize question
        updateQuestion()
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkQuestion(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        val messageId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(
            this,
            messageId,
            Toast.LENGTH_SHORT
        ).show()
    }
}
