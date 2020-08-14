@file:Suppress("DEPRECATION")

package com.hanif.foodrecipe.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hanif.foodrecipe.R
import com.hanif.foodrecipe.adapter.FilterFoodAdapter
import com.hanif.foodrecipe.model.ModelFilter
import com.hanif.foodrecipe.model.ModelMain
import com.hanif.foodrecipe.networking.Api
import kotlinx.android.synthetic.main.activity_filter_food.*
import org.json.JSONException
import org.json.JSONObject

class FilterFoodActivity : AppCompatActivity() {

    private var filterFoodAdapter: FilterFoodAdapter? = null
    var progressDialog: ProgressDialog? = null
    var modelFilter: MutableList<ModelFilter> = ArrayList()
    private var modelMain: ModelMain? = null
    private var strCategory: String? = null
    private var strCategoryDescription: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_food)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        toolbar.title = null
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Please Wait...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Displaying Data")

        var mLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_filter.layoutManager = mLayoutManager
        rv_filter.setHasFixedSize(true)

        @Suppress("CAST_NEVER_SUCCEEDS")
        modelMain = intent.getSerializableExtra("showFilter") as ModelMain
        if (modelMain != null) {
            strCategory = modelMain!!.strCategory
            strCategoryDescription = modelMain!!.strCategoryDescription

            tv_title.text = getString(R.string.food_list) + strCategory
            tv_description_categories.text = strCategoryDescription

            //Get image background
            Glide.with(this)
                .load(modelMain!!.strCategoryThumb)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_categories_background)

            //Get image source
            Glide.with(this)
                .load(modelMain!!.strCategoryThumb)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_categories)
        }
        meal
    }

    private val meal: Unit
        get() {
            progressDialog!!.show()
            AndroidNetworking.get(Api.Filter)
                .addPathParameter("strCategory", strCategory)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        try {
                            progressDialog!!.dismiss()
                            val playerArray = response!!.getJSONArray("meals")
                            for (i in 0 until playerArray.length()) {
                                val temp = playerArray.getJSONObject(i)
                                val dataApi = ModelFilter()
                                dataApi.idMeal = temp.getString("idMeal")
                                dataApi.strMeal = temp.getString("strMeal")
                                dataApi.strMealThumb = temp.getString("strMealThumb")
                                modelFilter.add(dataApi)
                                showFilter()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@FilterFoodActivity,
                                "Failed To Displaying Data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        progressDialog!!.dismiss()
                        Toast.makeText(
                            this@FilterFoodActivity,
                            "No Connection Internet",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun showFilter() {
        filterFoodAdapter = FilterFoodAdapter(modelFilter)
        rv_filter.adapter = filterFoodAdapter

        filterFoodAdapter!!.onSelected(object : FilterFoodAdapter.OnSelectedData {
            override fun onSelected(modelFilter: ModelFilter) {
                onSelectedRecipe(modelFilter)
            }
        })
    }

    fun onSelectedRecipe(modelFilter: ModelFilter) {
        val intent = Intent(this@FilterFoodActivity, DetailRecipeActivity::class.java)
        intent.putExtra("detailRecipe", modelFilter)
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
