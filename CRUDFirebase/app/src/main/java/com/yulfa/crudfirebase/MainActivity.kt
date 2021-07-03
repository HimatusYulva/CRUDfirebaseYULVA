package com.yulfa.crudfirebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1

    companion object {
        const val MALE = "Laki-laki"
        const val FEMALE = "Perempuan"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logout.setOnClickListener(this)
        save.setOnClickListener(this)
        showdata.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.save -> {
                val getUserID = auth!!.currentUser!!.uid

                val database = FirebaseDatabase.getInstance()

                val getNim = nim.text.toString()
                val getNama = nama.text.toString()
                val getJurusan = jurusan.text.toString()
                val getTtl = ttl.text.toString()
                val getAlamat = alamat.text.toString()
                val getAgama = agama.text.toString()
                val getNoHp = no_hp.text.toString()
                val gender = if (rb_male.isChecked) MALE else FEMALE

                val getReference = database.reference

                when {
                    getNim.isEmpty() -> nim.error = "NIM belum diisi"
                    getNama.isEmpty() -> nama.error = "Nama belum diisi"
                    getJurusan.isEmpty() -> jurusan.error = "Jurusan belum diisi"
                    getTtl.isEmpty() -> ttl.error = "Tempat Tanggal Lahir belum diisi"
                    getAlamat.isEmpty() -> alamat.error = "Alamat belum diisi"
                    getAgama.isEmpty() -> agama.error = "Agama belum diisi"
                    getNoHp.isEmpty() -> no_hp.error = "No HP belum diisi"
                    else -> {
                        getReference.child("Admin").child(getUserID).child("Mahasiswa").push()
                            .setValue(DataMahasiswa(getNim, getNama, getJurusan, getTtl, getAlamat, getAgama, getNoHp, gender))
                            .addOnCompleteListener(this) {
                                nim.setText("")
                                nama.setText("")
                                jurusan.setText("")
                                ttl.setText("")
                                alamat.setText("")
                                agama.setText("")
                                no_hp.setText("")
                                Toast.makeText(this, "Data Tersimpan ", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
            R.id.logout ->
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            Toast.makeText(this, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                            finish()
                        }
            R.id.showdata -> {
                startActivity(Intent(this, MyListDataActivity::class.java))
            }
        }
    }
}