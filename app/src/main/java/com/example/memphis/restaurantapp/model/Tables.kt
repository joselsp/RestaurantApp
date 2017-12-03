package com.example.memphis.restaurantapp.model

import java.io.Serializable

/**
 * Created by Memphis on 02/12/2017.
 */
object Tables {
    var tables: List<Table> = listOf(
            Table(1, null),
            Table(2, null),
            Table(3, null),
            Table(4, null),
            Table(5, null),
            Table(6, null),
            Table(7, null),
            Table(8, null)
    )

    val count
        get() = tables.size

    operator fun get(i: Int) = tables[i]

    fun toArray() = tables.toTypedArray()
}