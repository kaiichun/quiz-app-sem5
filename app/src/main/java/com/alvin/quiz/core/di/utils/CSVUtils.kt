package com.alvin.quiz.core.di.utils

import android.content.Context
import android.net.Uri
import com.alvin.quiz.data.model.Question
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object CSVUtils {
    fun readCSVFile(context: Context, uri: Uri): List<Question> {
        val questions = mutableListOf<Question>()
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).use { reader ->
                    reader.readLine()
                    reader.forEachLine { line ->
                        val csvContent = line.split(",").map { it.replace("\"", "").trim() }
                        if (csvContent.size >= 9) {
                            val question = Question(
                                questionId = csvContent[0],
                                questionText = csvContent[1],
                                options = csvContent.subList(2, 6),
                                correctAnswer = csvContent[6],
                                timeLimit = csvContent[7].toIntOrNull() ?: 15,
                                mark = csvContent[8].toIntOrNull() ?: 1
                            )
                            questions.add(question)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return questions
    }
}