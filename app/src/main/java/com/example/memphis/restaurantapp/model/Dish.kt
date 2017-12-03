package com.example.memphis.restaurantapp.model

import java.io.Serializable

class Dish (val number: Int, val name: String, val allergens: List<String>?, val icon: String, val price: Float, var comments: String?): Serializable{

    override fun toString() = generateToStringFormat()

    fun generateToStringFormat() : String {
        if (comments == null || comments == ""){
            return "name: $name"
        }else {
           return "name: $name\nComments: $comments"
        }
    }
}
