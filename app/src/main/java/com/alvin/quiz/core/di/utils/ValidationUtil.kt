package com.alvin.quiz.core.di.utils

import com.alvin.quiz.data.model.ValidationField

object ValidationUtil {
    fun validate(vararg fields: ValidationField): String? {
        fields.forEach { field ->
            if(!Regex(field.regExp).matches(field.value)) {
                return field.errorMessage
            }
        }
        return null
    }
}