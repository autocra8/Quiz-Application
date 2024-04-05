package com.shivam.assignment3qualitycheck
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.shivam.assignment3qualitycheck.R

class MainActivity : AppCompatActivity() {

    private lateinit var questionContainer: LinearLayout
    private lateinit var pieChart: PieChart
    private val questionsWithOptions = listOf(
        QuestionWithOptions("Question 1?", listOf("Yes","No")),
        QuestionWithOptions("Question 2?", listOf("Yes","No")),
        QuestionWithOptions("Question 3?", listOf("Yes","No")),
        QuestionWithOptions("Question 4?", listOf("Yes","No")),
        QuestionWithOptions("Question 5?", listOf("Yes","No"))
    )
    private val answers = mutableMapOf<Int, Int>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionContainer = findViewById(R.id.questionContainer)
        pieChart = findViewById(R.id.pieChart)

        populateQuestions()

        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            showSummary()
            showPieChart()
        }
    }

    private fun populateQuestions() {
        for ((index, questionWithOptions) in questionsWithOptions.withIndex()) {
            val questionTextView = TextView(this)
            questionTextView.text = questionWithOptions.question
            questionContainer.addView(questionTextView)

            val radioGroup = RadioGroup(this)
            radioGroup.orientation = RadioGroup.VERTICAL

            for ((optionIndex, option) in questionWithOptions.options.withIndex()) {
                val radioButton = RadioButton(this)
                radioButton.text = option
                radioButton.id = View.generateViewId()
                radioGroup.addView(radioButton)

                radioButton.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        answers[index] = optionIndex
                    }
                }
            }

            questionContainer.addView(radioGroup)
        }
    }

    private fun showSummary() {
        val summary = StringBuilder()
        for ((index, answerIndex) in answers) {
            val selectedOption = questionsWithOptions[index].options[answerIndex]
            summary.append("Question ${index + 1}: $selectedOption\n")
        }
        Toast.makeText(this, summary.toString(), Toast.LENGTH_LONG).show()
    }

    private fun showPieChart() {
        val totalQuestions = questionsWithOptions.size.toFloat()
        val correctAnswers = answers.count { it.value == 0 }.toFloat() // Assuming the first option is correct

        val pieEntries = listOf(
            PieEntry(correctAnswers, "OK"),
            PieEntry(totalQuestions - correctAnswers, "NOT OK")
        )

        val dataSet = PieDataSet(pieEntries, "Results")
        dataSet.colors = listOf(Color.GREEN, Color.RED)

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate()
    }
}

data class QuestionWithOptions(val question: String, val options: List<String>)
