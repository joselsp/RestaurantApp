package com.example.memphis.restaurantapp.adapter

import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.memphis.restaurantapp.R
import com.example.memphis.restaurantapp.model.Table
import com.example.memphis.restaurantapp.model.Tables

class TableRecyclerViewAdapter(val tables: Tables?) : RecyclerView.Adapter<TableRecyclerViewAdapter.TableViewHolder>() {

    var onClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TableViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.content_table, parent, false)
        view.setOnClickListener(onClickListener)
        return TableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TableViewHolder?, position: Int) {
        if (tables != null) {
            holder?.bindForecast(tables[position], position)
        }
    }

    override fun getItemCount() = tables?.count ?: 0

    inner class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tableTitle = itemView.findViewById<TextView>(R.id.table_title)
        val tableImage = itemView.findViewById<ImageView>(R.id.table_image)

        //TODO Set Background color in card Table
        fun bindForecast(table: Table, position: Int) {

            // Actualizamos la vista con el modelo
            tableImage.setBackgroundColor(0x39add1)
            //tableImage.setImageResource(forecast.icon)

            tableTitle.text = generateDayText(position)
        }


        private fun generateDayText(position: Int) = when(position) {
            0 -> "Mesa " +  (position + 1)
            1 -> "Mesa " +  (position + 1)
            2 -> "Mesa " +  (position + 1)
            3 -> "Mesa " +  (position + 1)
            4 -> "Mesa " +  (position + 1)
            5 -> "Mesa " +  (position + 1)
            6 -> "Mesa " +  (position + 1)
            7 -> "Mesa " +  (position + 1)
            8 -> "Mesa " +  (position + 1)
            else -> "Pues yoquesetio xD"
        }
    }
}