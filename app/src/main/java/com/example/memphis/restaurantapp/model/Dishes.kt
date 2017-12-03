package com.example.memphis.restaurantapp.model

import java.io.Serializable

class Dishes: Serializable {
    private var dishes = mutableListOf<Dish>()

    val count
        get() = dishes.size

    operator fun get(i: Int) = dishes[i]

    fun toArray() = dishes.toTypedArray()

    fun add(dish: Dish){
        dishes.add(dish)
    }
}