package com.example.basicmathgame

import kotlin.random.Random

class MathGameModel {
    var correctCount = 0
    var totalCount = 0
    var answer = 0
    var first = 0
    var second = 0
    var operator = "+"

    fun makeProblem() {
        val operators = listOf("+", "-", "×", "÷")
        operator = operators.random()
        when (operator) {
            "+" -> {
                first = Random.nextInt(1, 51)
                second = Random.nextInt(1, 51)
                answer = first + second
            }
            "-" -> {
                first = Random.nextInt(50, 100)
                second = Random.nextInt(0, 50)
                answer = first - second
            }
            "×" -> {
                first = Random.nextInt(1, 21)
                second = Random.nextInt(1, 21)
                answer = first * second
            }
            "÷" -> {
                second = Random.nextInt(1, 21)
                answer = Random.nextInt(1, 21)
                first = second * answer
            }
        }
    }

    fun checkAnswer(selected: Int): Boolean {
        totalCount++
        if (selected == answer) {
            correctCount++
            return true
        }
        return false
    }

    fun getChoices(): List<Int> {
        val choiceSet = mutableSetOf<Int>()
        choiceSet.add(answer)
        var tryCount = 0
        while (choiceSet.size < 6 && tryCount < 100) {
            val fake = answer + Random.nextInt(-20, 21)
            if (fake != answer && fake >= -100 && fake <= 100) {
                choiceSet.add(fake)
            }
            tryCount++
        }
        // 만약 6개가 안 채워졌으면, 중복 허용해서라도 채움
        while (choiceSet.size < 6) {
            choiceSet.add(Random.nextInt(-100, 101))
        }
        return choiceSet.shuffled()
    }

    fun resetScore() {
        correctCount = 0
        totalCount = 0
    }
} 