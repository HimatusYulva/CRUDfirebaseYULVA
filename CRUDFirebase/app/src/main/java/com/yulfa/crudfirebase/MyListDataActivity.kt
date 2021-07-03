package com.yulfa.crudfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_my_list_data.*

class MyListDataActivity : AppCompatActivity(), MahasiswaAdapter.DataListener {

    private lateinit var adapter: MahasiswaAdapter

    private val database = FirebaseDatabase.getInstance()
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list_data)

        supportActionBar!!.title = "Data Mahasiswa"
        auth = FirebaseAuth.getInstance()
        setupRecyclerView()
        getData()

    }

    private fun getData() {
        Toast.makeText(applicationContext, "Mohon Tunggu Sebentar...", Toast.LENGTH_LONG).show()
        val getUserID: String = auth?.currentUser?.uid.toString()
        val getReference = database.reference
        getReference.child("Admin").child(getUserID).child("Mahasiswa")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val data = mutableListOf<DataMahasiswa>()
                        dataSnapshot.children.forEach {
                            val mahasiswa = it.getValue(DataMahasiswa::class.java)
                            mahasiswa?.key = it.key
                            data.add(mahasiswa!!)
                        }
                        adapter = MahasiswaAdapter(
                            data,
                            this@MyListDataActivity,
                            this@MyListDataActivity
                        )
                        //Memasang Adapter pada RecyclerView
                        rv_mahasiswa.adapter = adapter
                        adapter.notifyDataSetChanged()
                        Toast.makeText(
                            applicationContext,
                            "Data Berhasil Dimuat",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Kode ini akan dijalankan ketika ada error, simpan ke LogCat
                    Toast.makeText(applicationContext, "Data Gagal Dimuat", Toast.LENGTH_LONG)
                        .show()
                    Log.e("MyListActivity", databaseError.details + " " + databaseError.message)
                }
            })
    }

    private fun setupRecyclerView() {
        rv_mahasiswa.setHasFixedSize(true)
        val itemDecoration =
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.line)!!)
        rv_mahasiswa.addItemDecoration(itemDecoration)
    }

    override fun onDeleteData(data: DataMahasiswa?, position: Int) {
        val getUserID: String = auth?.currentUser?.uid.toString()
        val getReference = database.reference
        getReference.child("Admin")
            .child(getUserID)
            .child("Mahasiswa")
            .child(data?.key.toString())
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
            }
    }

}