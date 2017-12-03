package com.example.memphis.restaurantapp.fragment

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.memphis.restaurantapp.R
import com.example.memphis.restaurantapp.activity.TableDetailActivity
import com.example.memphis.restaurantapp.adapter.TableRecyclerViewAdapter
import com.example.memphis.restaurantapp.model.Table
import com.example.memphis.restaurantapp.model.Tables

class TableFragment : Fragment() {

    lateinit var root: View
    lateinit var tableList: RecyclerView
    val TAG = TableFragment::class.java.canonicalName

    companion object {
        val REQUEST_TABLE = 1
    }

    var table: Tables? = null
        set(value) {
            field = value
            // Actualizamos la vista con el modelo
            if (value != null) {
                // Asignamos el adapter al RecyclerView ahora que tenemos datos
                val adapter = TableRecyclerViewAdapter(value)
                tableList.adapter = adapter

                // Le digo al RecyclerViewAdapter que me informe cuando pulsen una de sus vistas
                adapter.onClickListener = View.OnClickListener { v: View? ->
                    // Aquí me entero que se ha pulsado una de las vistas
                    val position = tableList.getChildAdapterPosition(v)

                    // Lanzamos la actividad detalle
                    startActivityForResult(TableDetailActivity.intent(activity, position), REQUEST_TABLE)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (inflater != null) {
            root = inflater.inflate(R.layout.fragment_table, container, false)
            Log.v(TAG, "rooot")

            // 1) Accedemos al RecyclerView con findViewById
            tableList = root.findViewById(R.id.table_list)

            // 2) Le decimos cómo debe visualizarse el RecyclerView (su LayoutManager)
            tableList.layoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.recycler_columns))

            // 3) Le decimos cómo debe animarse el RecyclerView (su ItemAnimator)
            tableList.itemAnimator = DefaultItemAnimator()

            // 4) Por último, un RecylerView necesita un adapter
            // Esto aún no lo hacemos aquí porque aquí aún no tenemos datos

            table = Tables
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TABLE) {
            if (resultCode == Activity.RESULT_OK) {
                val tableSelected = data?.getSerializableExtra(TableDetailActivity.EXTRA_TABLE) as Table
                val tableNumber = tableSelected.tableNumber

                Tables.get(tableNumber-1).dishes = tableSelected.dishes
            }
        }
    }
}
