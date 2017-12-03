package com.example.memphis.restaurantapp.model

import java.io.Serializable

data class Table (val tableNumber: Int, var dishes: Dishes?) : Serializable {
}