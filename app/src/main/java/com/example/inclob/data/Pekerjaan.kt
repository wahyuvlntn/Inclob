package com.example.inclob.data

data class Pekerjaan(
    var id: String?="",
    var gambar: String?="",
    var title: String? = "",
    var perusahaan: String? = "",
    var kota: String? = "",
    var kategori: String? = "",
    var jenis: String? = "",
    var waktu: String? = "",
    var tempat: String? = "",
    var alamat: String? = "",
    var deskripsi: String? = "",
) {
    // Konstruktor tanpa argumen diperlukan oleh Firestore
    constructor() : this("", "", "", "","","","","","","","")
}
