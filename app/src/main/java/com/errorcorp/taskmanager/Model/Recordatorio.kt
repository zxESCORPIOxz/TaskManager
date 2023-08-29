package com.errorcorp.taskmanager.Model

import java.util.Date

class Recordatorio {

    var id: String = ""
    var titulo: String = ""
    var descripcion: String = ""
    var categoria: String = ""
    var fechaModificacion: Date = Date()
    var fechasProgramadas: ArrayList<CustomDate> = ArrayList()

    constructor(
        titulo: String,
        descripcion: String,
        categoria: String,
        fechaModificacion: Date,
        fechasProgramadas: ArrayList<CustomDate>
    ) {
        this.titulo = titulo
        this.descripcion = descripcion
        this.categoria = categoria
        this.fechaModificacion = fechaModificacion
        this.fechasProgramadas = fechasProgramadas
    }

    constructor()
}
