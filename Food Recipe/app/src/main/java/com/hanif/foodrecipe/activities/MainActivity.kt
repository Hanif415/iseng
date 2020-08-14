@file:Suppress("DEPRECATION")

package com.hanif.foodrecipe.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hanif.foodrecipe.R
import com.hanif.foodrecipe.adapter.MainAdapter
import com.hanif.foodrecipe.model.ModelMain
import com.hanif.foodrecipe.networking.Api
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var mainAdapter: MainAdapter? = null
    var progressDialog: ProgressDialog? = null
    var modelMain: MutableList<ModelMain> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(
                this,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                false
            )
            window.statusBarColor = Color.TRANSPARENT
        }

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Please Wait...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Displaying Data")

        val mLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        rv_main_menu.layoutManager = mLayoutManager
        rv_main_menu.setHasFixedSize(true)

        //Methods get data
        categories
    }

    private val categories: Unit
        get() {
            progressDialog!!.show()
            AndroidNetworking.get(Api.Categories)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        try {
                            progressDialog!!.dismiss()
                            val playerArray = response!!.getJSONArray("categories")
                            for (i in 0 until playerArray.length()) {
                                val temp = playerArray.getJSONObject(i)
                                val dataApi = ModelMain()
                                dataApi.strCategory = temp.getString("strCategory")
                                dataApi.strCategoryThumb = temp.getString("strCategoryThumb")
                                dataApi.strCategoryDescription =
                                    temp.getString("strCategoryDescription")
                                modelMain.add(dataApi)
                                showCategories()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@MainActivity,
                                "Failed to Displaying Data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        progressDialog!!.dismiss()
                        Toast.makeText(
                            this@MainActivity,
                            "No Connection Internet", Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

    private fun showCategories() {
        mainAdapter = MainAdapter(modelMain)
        rv_main_menu.adapter = mainAdapter

        mainAdapter!!.onSelected(object : MainAdapter.OnSelectedData {
            override fun onSelected(modelMain: ModelMain) {
                onSelectedRecipe(modelMain)
            }
        })
    }

    fun onSelectedRecipe(modelMain: ModelMain) {
        val intent = Intent(this@MainActivity, FilterFoodActivity::class.java)
        intent.putExtra("showFilter", modelMain)
        startActivity(intent)
    }

    companion object {
        //Set Transparent Status bar
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }
}
