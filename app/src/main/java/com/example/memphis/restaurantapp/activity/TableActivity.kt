package com.example.memphis.restaurantapp.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.memphis.restaurantapp.R
import com.example.memphis.restaurantapp.fragment.TableFragment

class TableActivity : AppCompatActivity() {

    //TODO Rotation Control using saveInstanceState
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)

        val fragment = TableFragment()
        fragmentManager.beginTransaction()
                .add(R.id.table_list_fragment, fragment)
                .commit()
    }
}
