package com.example.memphis.restaurantapp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.example.memphis.restaurantapp.R
import com.example.memphis.restaurantapp.model.Dish
import com.example.memphis.restaurantapp.model.Dishes
import com.example.memphis.restaurantapp.model.Table
import com.example.memphis.restaurantapp.model.Tables

class TableDetailActivity : AppCompatActivity() {

    companion object {

        val EXTRA_TABLE = "EXTRA_TABLE"
        val REQUEST_LIST_DISHES = 1
        var table: Table? = null
        var position: Int? = null
        fun intent(context: Context, position: Int): Intent {
            val intent = Intent(context, TableDetailActivity::class.java)
            intent.putExtra(EXTRA_TABLE, position)
            return intent
        }
    }

    //TODO Review dishes Quantity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        position = intent.getIntExtra(EXTRA_TABLE, 1)
        table = Tables.get(position ?: 0)

        if (table != null) {
            findViewById<View>(R.id.table_card).setOnClickListener { addDishToTable( table?.tableNumber ) }

            // Actualizamos la interfaz
            val tableTitle = findViewById<TextView>(R.id.table_title)
            val tableImage = findViewById<ImageView>(R.id.table_image)
            val dishesQuatity = findViewById<TextView>(R.id.dishes_quantity)

            tableTitle.text = "Mesa " + table?.tableNumber

            updateInterface()
        }
    }

    private fun addDishToTable(tableNumber: Int?) {

        val intent = DishListActivity.intent(this, position ?: 0)

        // Esto lo hacemos porque la pantalla de platos nos tiene que devolver unos valores
        startActivityForResult(intent, REQUEST_LIST_DISHES)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LIST_DISHES) {
            if (resultCode == Activity.RESULT_OK) {
                val dishesTableList = data?.getSerializableExtra(DishListActivity.EXTRA_LIST_DISH) as Dishes

                Log.v("TAG", "Tamaño lista de platos: " + dishesTableList.count)
                table?.dishes = dishesTableList
            }
        }
        updateInterface()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.confirm_table, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.menu_confirm_table -> {

            val returnIntent = Intent()
            returnIntent.putExtra(EXTRA_TABLE, table)
            //Utilizamos RESULT_OK para identificar que añadimos la lista
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
            true
        }
        R.id.menu_delete_table -> {
            val returnIntent = Intent()
            table?.dishes = null
            returnIntent.putExtra(EXTRA_TABLE, table)
            //Utilizamos RESULT_OK para identificar que añadimos la lista
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun updateDishesQuantity(){
        val dishesQuatity = findViewById<TextView>(R.id.dishes_quantity)

        var dishesCount = 0

        if (table?.dishes != null) {
            dishesCount = table?.dishes?.count ?: 0
        }

        dishesQuatity.text = "Número de platos: " + dishesCount
    }

    fun updateDishesList(){
        val list = findViewById<ListView>(R.id.order_list)

        if (table?.dishes != null) {
            val adapter = ArrayAdapter<Dish>(this, android.R.layout.simple_list_item_1, table?.dishes?.toArray())
            list.adapter = adapter
        }
    }

    fun updateInterface(){
        updateDishesQuantity()
        updateDishesList()
    }

}
