package com.example.memphis.restaurantapp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import com.example.memphis.restaurantapp.R
import com.example.memphis.restaurantapp.model.Dish

class DishDetailActivity : AppCompatActivity() {

    companion object {

        val EXTRA_DISH = "EXTRA_DISH"
        val EXTRA_COMMENTS = "EXTRA_COMMENTS"

        fun intent(context: Context, dish: Dish?): Intent {
            val intent = Intent(context, DishDetailActivity::class.java)
            intent.putExtra(EXTRA_DISH, dish)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dish = intent.getSerializableExtra(EXTRA_DISH) as? Dish

        if (dish != null) {

            // Actualizamos la interfaz
            val dishTitle = findViewById<TextView>(R.id.dish_detail_title)
            val allergenList = findViewById<TextView>(R.id.allergen_list)

           // val tableImage = findViewById<ImageView>(R.id.table_image)

            dishTitle.text = dish.name
            if (dish?.allergens != null) {
                var allergensList: String? = ""
                for (i in 0..dish.allergens?.size - 1) {
                    allergensList = allergensList + dish.allergens[i] + "\n"
                }
                allergenList.text = allergensList
            }


        }
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.menu_add_dish -> {

            val dishComments = findViewById<MultiAutoCompleteTextView>(R.id.comments)

            val returnIntent = Intent()
            returnIntent.putExtra(EXTRA_DISH, true)
            returnIntent.putExtra(EXTRA_COMMENTS, dishComments.text.toString())
            //Utilizamos RESULT_OK para identificar que añadimos
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.add_dish, menu)

        return true
    }


}
