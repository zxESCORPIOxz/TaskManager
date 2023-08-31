package com.errorcorp.taskmanager.Model

import java.util.Date

class CustomDate {
    var date: Date = Date()
    var recibido: Boolean = false
    constructor(date: Date, recibido: Boolean) {
        this.date = date
        this.recibido = recibido
    }
    constructor(){
        this.date = Date()
    }
}