package com.example.inclob.data

data class Pelatihan(
    var id:String? ="",
    var gambar:String? ="",
    var title:String? ="",
    var deskripsi:String =""
){
    constructor(): this("","","","")
}
