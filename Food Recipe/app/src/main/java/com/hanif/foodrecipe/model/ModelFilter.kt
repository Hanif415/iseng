package com.hanif.foodrecipe.model

import java.io.Serializable

class ModelFilter : Serializable {
    var idMeal: String? = null

    @JvmField
    var strMeal: String? = null

    @JvmField
    var strMealThumb: String? = null
}