@file:Suppress("DEPRECATION")

package com.bluberryboii.musicplayer.activities

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bluberryboii.musicplayer.R
import com.bluberryboii.musicplayer.adapter.ListSongAdapter
import com.bluberryboii.musicplayer.model.ModelListSong
import com.bluberryboii.musicplayer.networking.API
import kotlinx.android.synthetic.main.activity_song_list.*
import org.json.JSONException
import org.json.JSONObject

class SongListActivity : AppCompatActivity() {

    private var listSongAdapter: ListSongAdapter? = null
    var progressDialog: ProgressDialog? = null
    var modelListSong: MutableList<ModelListSong> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        //set Transparent Status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Mohon Tunggu")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Sedang Menampilkan Data...")

        rvListMusic!!.setHasFixedSize(true)
        rvListMusic!!.layoutManager = LinearLayoutManager(this)

//        get data Music
        listMusic
    }

    private val listMusic: Unit
        get() {
            progressDialog!!.show()
            AndroidNetworking.get(API.ListMusic)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            progressDialog!!.dismiss()
                            val playerArray = response.getJSONArray("post")
                            for (i in 0 until playerArray.length()) {
                                if (i > 3){
                                    val tmp = playerArray.getJSONObject(i)
                                    val dataApi = ModelListSong()
                                    dataApi.strId = tmp.getString("id")
                                    dataApi.strCoverSong = tmp.getString("coverartikel")
                                    dataApi.strNameBand = tmp.getString("namaband")
                                    dataApi.strSongTitle = tmp.getString("judulmusic")
                                    modelListSong.add(dataApi)
                                    showListMusic()
                                }
                            }
                        }catch (e: JSONException){
                            e.printStackTrace()
                            Toast.makeText(this@SongListActivity, "Gagal menampilkan data!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@SongListActivity,
                            "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show()
                    }
                })
        }

    private fun showListMusic() {
        listSongAdapter = ListSongAdapter(modelListSong)
        rvListMusic!!.adapter = listSongAdapter

        listSongAdapter!!.onSelected(object : ListSongAdapter.OnSelectedData {
            override fun onSelected(modelListSong: ModelListSong) {
                onSelectedSong(modelListSong)
            }

        })
    }

    fun onSelectedSong(modelListSong: ModelListSong) {
        val intent = Intent(this@SongListActivity, SongDetailActivity::class.java)
        intent.putExtra("songDetail", modelListSong)
        startActivity(intent)
    }

    companion object {
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
