package com.example.memphis.restaurantapp.adapter

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.memphis.restaurantapp.R
import com.example.memphis.restaurantapp.model.Dish
import com.example.memphis.restaurantapp.model.Dishes


class DishRecyclerViewAdapter(val dishes: Dishes?): RecyclerView.Adapter<DishRecyclerViewAdapter.DishViewHolder>() {

    var onClickListener: View.OnClickListener? = null

    override fun onBindViewHolder(holder: DishViewHolder?, position: Int) {
        if (dishes != null) {
            holder?.bindForecast(dishes[position], position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_view_items, parent, false)
        view.setOnClickListener(onClickListener)
        return DishViewHolder(view)
    }

    override fun getItemCount() = dishes?.count ?: 0

    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val dishTitle = itemView.findViewById<TextView>(R.id.dishName)
        val dishPrice = itemView.findViewById<TextView>(R.id.dishPrice)
//        val dishImage = itemView.findViewById<ImageView>(R.id.table_image)

        val allergenIconList = itemView.findViewById<LinearLayout>(R.id.allergen_icon_list)



        fun bindForecast(dish: Dish, position: Int) {

            // Actualizamos la vista con el modelo
//            tableImage.setBackgroundColor(0x39add1)
            //tableImage.setImageResource(forecast.icon)

            val resources = itemView.context.resources
            val metrics = resources.displayMetrics


            dishTitle.text = dish.name
            dishPrice.text = dish.price.toString()

            if (dish.allergens != null && dish.allergens.isNotEmpty()) {

                val mainHandler = Handler(itemView.context.mainLooper)
                mainHandler.post({

                    // Chuleta para saber los detalles físicos del dispositivo donde se está ejecutando esto
                    val metrics = resources.displayMetrics
                    val width = 20
                    val height = 20
                    val dpWidth = (width / metrics.density).toInt()
                    val dpHeight = (height / metrics.density).toInt()

                    //TODO Change font style in layout
                    if (dish?.allergens != null) {
                        for (i in 0..dish.allergens?.size - 1) {

                            val allergenImage = ImageView(itemView.context)

                            if (allergenImage.parent != null) {
                                (allergenImage.parent as ViewGroup).removeView(allergenImage)
                            }

                            Log.v("TAG", "Allergen Size" + dish.allergens?.size.toString())
                            allergenImage.setImageResource(
                                    when (dish.allergens[i])
                                    {
                                        "celery" -> R.drawable.celery
                                        "crustaceans" -> R.drawable.crustaceans
                                        "eggs" -> R.drawable.eggs
                                        "fish" -> R.drawable.fish
                                        "gluten" -> R.drawable.gluten
                                        "lactose" -> R.drawable.lactose
                                        "lupin" -> R.drawable.lupin
                                        "molluscs" -> R.drawable.molluscs
                                        "mustard" -> R.drawable.mustard
                                        "nuts" -> R.drawable.nuts
                                        "peanuts" -> R.drawable.peanuts
                                        "sesame seeds" -> R.drawable.sesame_seeds
                                        "soya" -> R.drawable.soya
                                        "Sulphur dioxide" -> R.drawable.sulphur_dioxide
                                        else -> R.drawable.allergy_free
                                    }
                            )

                            if (allergenImage.parent != null) {
                                (allergenImage.parent as ViewGroup).removeView(allergenImage)
                            }

                            allergenIconList.layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                            allergenImage.scaleType = ImageView.ScaleType.FIT_END
                            allergenIconList.addView(allergenImage)
                        }
                    }
                })
            }
        }
    }
}