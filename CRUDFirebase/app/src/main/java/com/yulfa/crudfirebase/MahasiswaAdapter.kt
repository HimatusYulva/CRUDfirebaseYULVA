package com.yulfa.crudfirebase

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yulfa.crudfirebase.MainActivity.Companion.MALE
import com.yulfa.crudfirebase.UpdateDataActivity.Companion.EXTRA_DATA

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
class MahasiswaAdapter(private val listMahasiswa: List<DataMahasiswa>, private val context: Context, private val onDelete: DataListener):
    RecyclerView.Adapter<MahasiswaAdapter.ViewHolder>() {

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val nim: TextView = itemView.findViewById(R.id.nimx)
        val nama: TextView = itemView.findViewById(R.id.namax)
        val jurusan: TextView = itemView.findViewById(R.id.jurusanx)
        val avatar: ImageView = itemView.findViewById(R.id.avatar)
        val listItem: LinearLayout = itemView.findViewById(R.id.list_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Membuat View untuk Menyiapkan & Memasang Layout yang digunakan pada RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_mahasiswa, parent, false
        )
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nim.text = "NIM: ${listMahasiswa[position].nim}"
        holder.nama.text = "Nama: ${listMahasiswa[position].nama}"
        holder.jurusan.text = "Jurusan: ${listMahasiswa[position].jurusan}"
        if (listMahasiswa[position].gender == MALE) {
            holder.avatar.setImageResource(R.drawable.avatar)
        } else {
            holder.avatar.setImageResource(R.drawable.avatar_female)
        }

        holder.listItem.setOnLongClickListener {
            val action = arrayOf("Update", "Delete")
            val alert = AlertDialog.Builder(it.context)
            alert.setItems(action) { _, i ->
                when (i) {
                    0 -> {
                        context.startActivity(Intent(context, UpdateDataActivity::class.java).apply {
                            putExtra(EXTRA_DATA, listMahasiswa[position])
                        })
                    }
                    1 -> {
                        onDelete.onDeleteData(listMahasiswa[position], position)
                    }
                }
            }
            alert.create()
            alert.show()
            true
        }
    }

    interface DataListener {
        fun onDeleteData(data: DataMahasiswa?, position: Int)
    }

    override fun getItemCount(): Int {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return listMahasiswa.size
    }

}