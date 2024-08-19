package com.alvin.quiz.data.model

data class ValidationField(
    val value: String,
    val regExp: String,
    val errorMessage: String
)
