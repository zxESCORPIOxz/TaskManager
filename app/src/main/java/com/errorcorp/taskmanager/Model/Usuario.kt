package com.errorcorp.taskmanager.Model

import android.widget.Button

class Usuario {

    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var mail: String

    constructor(name: String, phone: String, mail: String) {
        this.name = name
        this.phone = phone
        this.mail = mail
    }

    constructor() {
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getPhone(): String {
        return phone
    }

    fun setPhone(phone: String) {
        this.phone = phone
    }

    fun getMail(): String {
        return mail
    }

    fun setMail(mail: String) {
        this.mail = mail
    }
}