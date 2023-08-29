package com.errorcorp.taskmanager.Model

import java.util.Date

class Recordatorio {

    var id: String = ""
    var titulo: String = ""
    var descripcion: String = ""
    var fechaModificacion: Date = Date()
    var fechasProgramadas: ArrayList<Date> = ArrayList()

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
