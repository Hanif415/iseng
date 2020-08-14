package com.hanif.firebasecrud.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.hanif.firebasecrud.R
import com.hanif.firebasecrud.adapter.MainAdapter
import com.hanif.firebasecrud.model.StuffModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_stuff_layout.*

class MainActivity : AppCompatActivity() {

    private var mainAdapter: MainAdapter? = null
    private var stuffList: ArrayList<StuffModel>? = null
    private var databaseReference: DatabaseReference? = null
    private var firebaseInstance: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView
                .systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        if (Build.VERSION.SDK_INT >= 21){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        FirebaseApp.initializeApp(this)
        firebaseInstance = FirebaseDatabase.getInstance()
        databaseReference = firebaseInstance!!.getReference("barang")
        databaseReference!!.child("data_barang").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                stuffList = ArrayList()
                for (mDataSnapshot in dataSnapshot.children) {
                    val stuff: StuffModel? = dataSnapshot.getValue<StuffModel>(StuffModel::class.java)
                    stuff!!.key = dataSnapshot.key
                    stuffList!!.add(stuff)
                }

                mainAdapter = MainAdapter(this@MainActivity, stuffList)
                recycler_view.adapter = mainAdapter
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.details + " " + error.message, Toast.LENGTH_SHORT).show()
            }
        })

        fa_add_stuff.setOnClickListener{
            addStuffDialog()
        }
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win: Window = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    fun onDataClick(stuff: StuffModel, position: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Action")

        builder.setPositiveButton("UPDATE"){
            _, _ -> editStuffDialog(stuff)
        }

        builder.setNegativeButton("DELETE"){
            _, _ -> deleteStuff(stuff)
        }

        builder.setNeutralButton("CANCEL"){
            dialog, _ -> dialog.dismiss()
        }

        val dialog: Dialog = builder.create()
        dialog.show()
    }

    private fun deleteStuff(stuff: StuffModel) {
        if (databaseReference!= null){databaseReference!!.child(stuff.key!!)
            .removeValue().addOnSuccessListener {
                Toast.makeText(this@MainActivity, "Data was deleted successfully", Toast.LENGTH_SHORT).show()
            }}
    }

    @SuppressLint("InflateParams")
    fun addStuffDialog(){
        val builder =  AlertDialog.Builder(this)
        builder.setTitle("Add New Data")

        val view: View = layoutInflater.inflate(R.layout.edit_stuff_layout, null)
        builder.setView(view)

        val mEditNama = view.findViewById<EditText>(R.id.edt_stuff_name)
        val mEditMerk = view.findViewById<EditText>(R.id.edt_stuff_brand)
        val mEditHarga = view.findViewById<EditText>(R.id.edt_stuff_price)

        builder.setPositiveButton(
            "SAVE"
        ) { _, _ ->
            val stuffName = mEditNama.text.toString()
            val stuffBrand = mEditMerk.text.toString()
            val stuffPrice = mEditHarga.text.toString()

            if(stuffName.isNotEmpty() && stuffBrand.isNotEmpty() && stuffPrice.isNotEmpty()) {
                stuffDataSubmit(StuffModel(stuffName, stuffBrand, stuffPrice))
            } else {
                Toast.makeText(this@MainActivity, "Data must be filled in", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("CANCEL"){
            dialog, _ -> dialog.dismiss()
        }

        val dialog: Dialog = builder.create()
        dialog.show()
    }

    @SuppressLint("InflateParams")
    fun editStuffDialog(stuff: StuffModel){
        val builder =  AlertDialog.Builder(this)
        builder.setTitle("Edit Data")

        val view: View = layoutInflater.inflate(R.layout.edit_stuff_layout, null)
        builder.setView(view)

        val stuffName: String = edt_stuff_name.text.toString()
        val stuffBrand: String = edt_stuff_brand.text.toString()
        val stuffPrice: String = edt_stuff_price.text.toString()

        builder.setPositiveButton(
            "SAVE"
        ) { _, _ ->
            stuff.name = stuffName
            stuff.brand = stuffBrand
            stuff.price = stuffPrice

            updateStuff(stuff)
        }

        builder.setNegativeButton("CANCEL"){
                dialog, _ -> dialog.dismiss()
        }

        val dialog: Dialog = builder.create()
        dialog.show()
    }

    private fun updateStuff(stuff: StuffModel) {
        databaseReference!!.child("data_barang").child(stuff.key!!)
            .setValue(stuff).addOnSuccessListener {
                Toast.makeText(this@MainActivity, "Data was updated successfully", Toast.LENGTH_SHORT).show()
            }
    }

    private fun stuffDataSubmit(stuff: StuffModel) {
        databaseReference!!.child("data_barang").push()
            .setValue(stuff).addOnSuccessListener {
                Toast.makeText(this@MainActivity, "Data was updated successfully", Toast.LENGTH_SHORT).show()
            }
    }
}
