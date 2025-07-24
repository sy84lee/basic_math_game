package com.example.basicmathgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.basicmathgame.ui.theme.BasicMathGameTheme
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random
import com.example.basicmathgame.MathGameModel

class MainActivity : ComponentActivity() {
    var timer: CountDownTimer? = null
    var isRunning = false
    var timeLeft = 100

    private lateinit var btnStart: Button
    private lateinit var tvTimer: TextView
    private lateinit var tvFirstNumber: TextView
    private lateinit var tvOperator: TextView
    private lateinit var tvSecondNumber: TextView
    private lateinit var btnChoices: List<Button>
    private lateinit var tvScore: TextView
    private val model = MathGameModel()

    fun updateScore() {
        tvScore.text = "${model.correctCount}/${model.totalCount}"
    }

    fun setChoicesEnabled(enabled: Boolean) {
        btnChoices.forEach { it.isEnabled = enabled }
    }

    fun showProblem() {
        setChoicesEnabled(false)
        model.makeProblem()
        tvFirstNumber.text = model.first.toString()
        tvOperator.text = model.operator
        tvSecondNumber.text = model.second.toString()
        val choiceList = model.getChoices()
        btnChoices.forEachIndexed { idx, btn ->
            btn.text = choiceList[idx].toString()
        }
        setChoicesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById<Button>(R.id.btn_start)
        tvTimer = findViewById<TextView>(R.id.tv_timer)
        tvFirstNumber = findViewById<TextView>(R.id.tv_first_number)
        tvOperator = findViewById<TextView>(R.id.tv_operator)
        tvSecondNumber = findViewById<TextView>(R.id.tv_second_number)
        btnChoices = listOf(
            findViewById<Button>(R.id.btn_choice_1),
            findViewById<Button>(R.id.btn_choice_2),
            findViewById<Button>(R.id.btn_choice_3),
            findViewById<Button>(R.id.btn_choice_4),
            findViewById<Button>(R.id.btn_choice_5),
            findViewById<Button>(R.id.btn_choice_6)
        )
        tvScore = findViewById<TextView>(R.id.tv_score)

        btnChoices.forEach { btn ->
            btn.setOnClickListener {
                setChoicesEnabled(false)
                try {
                    val selected = btn.text.toString().toInt()
                    model.checkAnswer(selected)
                    updateScore()
                    showProblem()
                } catch (e: Exception) {
                    // 예외 발생 시 앱이 죽지 않게 처리
                }
            }
        }


        btnStart.setOnClickListener {
            if (!isRunning) {
                model.resetScore()
                updateScore()
                showProblem()

                btnStart.text = "STOP"
                isRunning = true
                timer = object : CountDownTimer(timeLeft * 1000L, 1000L) {
                    override fun onTick(millisUntilFinished: Long) {
                        timeLeft = (millisUntilFinished / 1000).toInt()
                        tvTimer.text = "$timeLeft"
                    }
                    override fun onFinish() {
                        tvTimer.text = "0"
                        btnStart.text = "시작"
                        isRunning = false
                        timeLeft = 100
                        tvTimer.text = "100"
                        setChoicesEnabled(false)
                    }
                }.start()
                showProblem() // 시작할 때 새 문제 출제
            } else {
                setChoicesEnabled(false)
                timer?.cancel()
                btnStart.text = "START"
                isRunning = false
                timeLeft = 100
            }
        }

        setChoicesEnabled(false)
    }
}