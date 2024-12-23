package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MathApp()
        }
    }
}

@Composable
fun MathApp() {
    var correctAnswers by remember { mutableStateOf(0) }
    var incorrectAnswers by remember { mutableStateOf(0) }
    var questionState by remember { mutableStateOf(QuestionState()) }

    var userAnswer by remember { mutableStateOf("") }
    var isQuestionActive by remember { mutableStateOf(false) }

    val percentageCorrect = if (correctAnswers + incorrectAnswers > 0) {
        (correctAnswers.toDouble() / (correctAnswers + incorrectAnswers) * 100).toString()
    } else {
        "0.00"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Итого решено примеров", fontSize = 24.sp)
        Text(text = "Правильных: $correctAnswers", fontSize = 20.sp)
        Text(text = "Неправильных: $incorrectAnswers", fontSize = 20.sp)
        Text(text = "Процент правильных ответов: $percentageCorrect%", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = questionState.text,
            fontSize = 32.sp,
            modifier = Modifier
                .background(questionState.color)
                .padding(16.dp)
                .fillMaxWidth(),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (isQuestionActive) {
            TextField(
                value = userAnswer,
                onValueChange = { userAnswer = it },
                label = { Text("Ваш ответ") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    // Проверка ответа
                    val isCorrect = userAnswer.toIntOrNull() == questionState.answer
                    if (isCorrect) {
                        correctAnswers++
                        questionState = questionState.copy(color = Color.Green)
                    } else {
                        incorrectAnswers++
                        questionState = questionState.copy(color = Color.Red)
                    }
                    isQuestionActive = false
                }
            ) {
                Text("ПРОВЕРКА")
            }
        } else {
            Button(
                onClick = {
                    // Генерация нового вопроса
                    questionState = generateQuestion()
                    userAnswer = ""
                    isQuestionActive = true
                }
            ) {
                Text("СТАРТ")
            }
        }
    }
}

data class QuestionState(
    val text: String = "00 + 00 = 00",
    val answer: Int = 0,
    val color: Color = Color.White
)

fun generateQuestion(): QuestionState {
    val operand1 = Random.nextInt(10, 100)
    val operand2 = Random.nextInt(10, 100)
    val operator = listOf("+", "-", "*", "/").random()
    val answer = when (operator) {"+" -> operand1 + operand2
        "-" -> operand1 - operand2
        "*" -> operand1 * operand2
        "/" -> {
            // Обеспечение целого результата
            val divisor = Random.nextInt(1, operand1) // Чтобы деление было корректным
            operand1 / divisor
        }
        else -> operand1 + operand2 // безопасный случай
    }
    return QuestionState(
        text = "$operand1 $operator $operand2 =",
        answer = answer
    )
}