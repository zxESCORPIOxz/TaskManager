package com.errorcorp.taskmanager.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class BarCode {
    companion object {
        const val ORG_SCANED: Int = 1000
        const val ORG_CREATE: Int = 2000
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var nombre: String = ""
    var valor: String = ""
    var format: Int = 0
    var origen: Int = 0
    var datemilles: Long = 0

    constructor(nombre: String, valor: String, format: Int, origen: Int, datemilles: Long) {
        this.nombre = nombre
        this.valor = valor
        this.format = format
        this.origen = origen
        this.datemilles = datemilles
    }

    constructor()

}