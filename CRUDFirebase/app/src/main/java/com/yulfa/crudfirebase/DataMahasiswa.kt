package com.yulfa.crudfirebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataMahasiswa(
        var nim: String? = null,
        var nama: String? = null,
        var jurusan: String? = null,
        var ttl: String? = null,
        var alamat: String? = null,
        var agama: String? = null,
        var noHp: String? = null,
        var gender: String? = null,
        var key: String? = null
): Parcelable