package com.errorcorp.taskmanager.Model

import java.util.*

class Recordatorio {
    var id: Int = 0
    var titulo: String = ""
    var descripcion: String = ""
    var fechaModificacion: Date = Date()
    var fechasProgramadas: ArrayList<Date> = ArrayList()
    var archivosAdjuntos: ArrayList<String> = ArrayList()
    var imagenes: ArrayList<String> = ArrayList()

    constructor(
        titulo: String,
        descripcion: String,
        fechaModificacion: Date,
        fechasProgramadas: ArrayList<Date>
    ) {
        this.titulo = titulo
        this.descripcion = descripcion
        this.fechaModificacion = fechaModificacion
        this.fechasProgramadas = fechasProgramadas
    }

    constructor()
}
