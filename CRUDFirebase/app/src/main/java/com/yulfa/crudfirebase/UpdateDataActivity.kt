package com.yulfa.crudfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yulfa.crudfirebase.MainActivity.Companion.MALE
import kotlinx.android.synthetic.main.activity_update_data.*

class UpdateDataActivity : AppCompatActivity() {

    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null

    private lateinit var extraNim: String
    private lateinit var extraNama: String
    private lateinit var extraJurusan: String
    private lateinit var extraTtl: String
    private lateinit var extraAlamat: String
    private lateinit var extraAgama: String
    private lateinit var extraNoHp: String
    private lateinit var extraGender: String
    private lateinit var extraKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        getData()

        update.setOnClickListener {
            val getNim = detail_nim.text.toString()
            val getNama = detail_nama.text.toString()
            val getJurusan = detail_jurusan.text.toString()
            val getTtl = detail_ttl.text.toString()
            val getAlamat = detail_alamat.text.toString()
            val getAgama = detail_agama.text.toString()
            val getNoHp = detail_no_hp.text.toString()
            val gender = if (detail_rb_male.isChecked) MALE else MainActivity.FEMALE

            when {
                getNim.isEmpty() -> detail_nim.error = "NIM belum diisi"
                getNama.isEmpty() -> detail_nama.error = "Nama belum diisi"
                getJurusan.isEmpty() -> detail_jurusan.error = "Jurusan belum diisi"
                getTtl.isEmpty() -> detail_ttl.error = "Tempat Tanggal Lahir belum diisi"
                getAlamat.isEmpty() -> detail_alamat.error = "Alamat belum diisi"
                getAgama.isEmpty() -> detail_agama.error = "Agama belum diisi"
                getNoHp.isEmpty() -> detail_no_hp.error = "No HP belum diisi"
                else -> {
                    updateMahasiswa(DataMahasiswa(getNim, getNama, getJurusan, getTtl, getAlamat, getAgama, getNoHp, gender))
                }
            }
        }
    }

    private fun updateMahasiswa(mahasiswa: DataMahasiswa) {
        val userID = auth!!.uid
        database!!.child("Admin")
            .child(userID!!)
            .child("Mahasiswa")
            .child(extraKey)
            .setValue(mahasiswa)
            .addOnSuccessListener {
                detail_nim.setText("")
                detail_nama.setText("")
                detail_jurusan.setText("")
                detail_ttl.setText("")
                detail_alamat.setText("")
                detail_agama.setText("")
                detail_no_hp.setText("")
                Toast.makeText(this, "Data Berhasil diubah",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun getData() {
        val extraData = intent.getParcelableExtra<DataMahasiswa>(EXTRA_DATA)!!
        extraNim = extraData.nim ?: ""
        extraNama = extraData.nama ?: ""
        extraJurusan = extraData.jurusan ?: ""
        extraKey = extraData.key ?: ""
        extraTtl = extraData.ttl ?: ""
        extraAgama = extraData.agama ?: ""
        extraAlamat = extraData.alamat ?: ""
        extraGender = extraData.gender ?: ""
        extraNoHp = extraData.noHp ?: ""
        detail_nim.setText(extraNim)
        detail_nama.setText(extraNama)
        detail_jurusan.setText(extraJurusan)
        detail_ttl.setText(extraTtl)
        detail_alamat.setText(extraAlamat)
        detail_agama.setText(extraAgama)
        detail_no_hp.setText(extraNoHp)
        if (extraGender == MALE) {
            detail_rb_male.isChecked = true
            detail_rb_female.isChecked = false
        } else {
            detail_rb_male.isChecked = false
            detail_rb_female.isChecked = true
        }
    }

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
    }

}