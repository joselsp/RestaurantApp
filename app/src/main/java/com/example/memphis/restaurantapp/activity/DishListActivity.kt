package com.example.memphis.restaurantapp.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.MultiAutoCompleteTextView
import com.example.memphis.restaurantapp.R
import com.example.memphis.restaurantapp.adapter.DishRecyclerViewAdapter
import com.example.memphis.restaurantapp.model.Dish
import com.example.memphis.restaurantapp.model.Dishes
import com.example.memphis.restaurantapp.model.Tables
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.json.JSONObject
import java.net.URL
import java.util.*

class DishListActivity : AppCompatActivity() {

    lateinit var dishList: RecyclerView
    val REQUEST_DISHES = 2

    var dishToShow: Dish? = null
    var dishesTableList: Dishes? = Dishes()

    companion object {

        val EXTRA_LIST_DISH = "EXTRA_LIST_DISH"
        val EXTRA_LIST_TABLE_POSITION = "EXTRA_LIST_TABLE_POSITION"

        fun intent(context: Context, position: Int): Intent {
            val intent = Intent(context, DishListActivity::class.java)
            intent.putExtra("EXTRA_LIST_TABLE_POSITION", position)
            return intent
        }
    }

    var dishes: Dishes? = null
        set(value) {
            field = value
            // Actualizamos la vista con el modelo
            if (value != null) {
                // Asignamos el adapter al RecyclerView ahora que tenemos datos
                val adapter = DishRecyclerViewAdapter(value)
                dishList.adapter = adapter

                // Le digo al RecyclerViewAdapter que me informe cuando pulsen una de sus vistas
                adapter.onClickListener = View.OnClickListener { v: View? ->
                    // Aquí me entero que se ha pulsado una de las vistas
                    val position = dishList.getChildAdapterPosition(v)
                    dishToShow = value[position]

                    // Lanzamos la actividad detalle
                    startActivityForResult(DishDetailActivity.intent(this, dishToShow), REQUEST_DISHES)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 1) Accedemos al RecyclerView con findViewById
        dishList = findViewById<RecyclerView>(R.id.dish_list)

        // 2) Le decimos cómo debe visualizarse el RecyclerView (su LayoutManager)
        dishList.layoutManager = GridLayoutManager(this, 1)

        // 3) Le decimos cómo debe animarse el RecyclerView (su ItemAnimator)
        dishList.itemAnimator = DefaultItemAnimator()

        // 4) Por último, un RecylerView necesita un adapter
        // Esto aún no lo hacemos aquí porque aquí aún no tenemos datos

        var position = intent.getIntExtra(EXTRA_LIST_TABLE_POSITION, 0)

        //Recuperamos los platos de la mesa pedidos previamente y asi añadir otros

        val tableDishes = Tables.get(position).dishes
        if (tableDishes != null) {
            dishesTableList = Tables.get(position).dishes
        }

        //TODO Download Dishes From Internet (use updateDishes() in this class)
        var listDish = Dishes()
            listDish.add(Dish(1, "plato1", listOf("fish", "lupin"), "icon1", 1f, null))
            listDish.add(Dish(2,"plato2", null, "icon2", 2f, null))
            listDish.add(Dish(3,"plato3", listOf("eggs","gluten","lactose","mustard"), "icon3", 3f, null))
            listDish.add(Dish(4,"plato4", listOf("nuts","peanuts"), "icon4", 4f, null))
            listDish.add(Dish(5,"plato5", null, "icon5", 5f, null))
            listDish.add(Dish(6,"plato6", listOf("celery","crustaceans"), "icon6", 6f, null))
            listDish.add(Dish(7,"plato7", listOf("sesame seeds","soya","Sulphur dioxide"), "icon7", 7f, null))
            listDish.add(Dish(8,"plato8", listOf("molluscs"), "icon8", 8f, null))

        dishes = listDish
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.add_list_dish, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.menu_add_list_dish -> {

            val returnIntent = Intent()
            returnIntent.putExtra(EXTRA_LIST_DISH, dishesTableList)
            //Utilizamos RESULT_OK para identificar que añadimos la lista
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_DISHES) {
            if (resultCode == Activity.RESULT_OK) {
                if (dishToShow != null) {

                    val dishComments = data?.getStringExtra(DishDetailActivity.EXTRA_COMMENTS)
                    if (dishComments != null) {
                        //Añadimos el plato
                        dishToShow?.comments = dishComments //Seteamos comentarios antes de añadirlo

                        dishesTableList?.add(dishToShow!!) //Hemos comprobado que dishToShow no es null previamente
                        Log.v("TAG", "Plato: " + dishToShow?.name + " " + dishComments + " " + dishesTableList?.count )
                    }
                }
            }
        }
    }

    private fun updateDishes() {

        async(UI) {
            val newDishes: Deferred<Dishes?> = bg {
                downloadDishes()
            }

            val downloadedDishes = newDishes.await()

            if (downloadedDishes != null) {
                // Tóh' ha ido bien, se lo asigno al atributo forecast
//                forecast = downloadedForecast
            }
            else {
                // Ha habido algún tipo de error, se lo decimos al usuario con un diálogo
                AlertDialog.Builder(this@DishListActivity)
                        .setTitle("Error")
                        .setMessage("No me pude descargar la información de los platostiempo")
                        .setPositiveButton("Reintentar", { dialog, _ ->
                            dialog.dismiss()
//                            updateForecast()
                        })
                        .setNegativeButton("Salir", { _, _ -> this@DishListActivity.finish() })
                        .show()
            }
        }
    }

    fun downloadDishes(): Dishes? {
        try {
            // Simulamos un retardo
            Thread.sleep(1000)

            // Nos descargamos la información del tiempo a machete
            val url = URL("https://www.mocky.io/")
            val jsonString = Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next()

            // Analizamos los datos que nos acabamos de descargar
            val jsonRoot = JSONObject(jsonString)
            val list = jsonRoot.getJSONArray("list")

            // Nos creamos la lista que vamos a ir rellenando con las predicciones del JSON
            val dishes = Dishes()

            // Recorremos la lista del objeto JSON
//            for (dayIndex in 0..list.length() - 1) {
            for (dayIndex in 0 until list.length()) {
                val dish = list.getJSONObject(dayIndex)
                val number = dish.getInt("number")
                val name = dish.getString("name")
                val icon = dish.getString("iconString")
                val price = dish.getString("price")
                val allergens = dish.getJSONArray("allergens")

                val allergenList = mutableListOf<String>()

                for (i in 0..(allergens.length() - 1)) {
                    val allergen = allergens.getJSONObject(i) as String
                    allergenList.add(allergen)
                    // Your code here
                }

                dishes.add(Dish(number, name, allergenList, icon, price.toFloat(), null))

                // Convertimos el texto iconString a un drawable
//                iconString = iconString.substring(0, 2)
//                val iconInt = iconString.toInt()
//                val iconResource = when (iconInt) {
//                    2 -> R.drawable.ico_02
//                    3 -> R.drawable.ico_03
//                    4 -> R.drawable.ico_04
//                    9 -> R.drawable.ico_09
//                    10 -> R.drawable.ico_10
//                    11 -> R.drawable.ico_11
//                    13 -> R.drawable.ico_13
//                    50 -> R.drawable.ico_50
//                    else -> R.drawable.ico_01
//                }
            }

            return dishes
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }


}
