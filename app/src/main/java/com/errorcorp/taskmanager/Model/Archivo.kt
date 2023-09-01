package com.errorcorp.taskmanager.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Archivo {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var nombre: String = ""
    var extension: String = ""
    var patch: String = ""
    var url: String = ""
    var sizeFormat: String = ""
    var sizeBytes: Int = 0
    var date: String = ""
    constructor(
        nombre: String,
        url: String,
    ) {
        this.nombre = nombre
        this.extension = extension
        this.url = url
    }

    constructor()
}