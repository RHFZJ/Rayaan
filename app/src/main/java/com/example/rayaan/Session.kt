package com.example.rayaan

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class Session {

    internal var Type: String= ""
    internal var name: String= ""
    internal var bdate: String= ""
    internal var longi: String= ""
    internal var latt: String= ""
    internal var desc: String= ""
    internal var mail: String= ""
    internal var image: String= ""
    internal var mobile: String= ""

    internal var email: String= ""
    internal var username: String= ""

    internal var Id: String = ""

    lateinit var sp: SharedPreferences
    private var spEditor: SharedPreferences.Editor? = null


    fun setUsername(n: String) {
        this.username = n
        spEditor = sp.edit()
        spEditor!!.putString("username", username)
        spEditor!!.commit()
    }
    fun getUsername(): String? {
        return sp.getString("username", username)
    }


    fun setEmail(n: String) {
        this.email = n
        spEditor = sp.edit()
        spEditor!!.putString("email", email)
        spEditor!!.commit()
    }
    fun getEmail(): String? {
        return sp.getString("email", email)
    }


    fun setMobile(n: String) {
        this.mobile = n
        spEditor = sp.edit()
        spEditor!!.putString("mobile", mobile)
        spEditor!!.commit()
    }

    fun getMobile(): String? {
        return sp.getString("mobile", mobile)
    }
    fun setImage(n: String) {
        this.image = n
        spEditor = sp.edit()
        spEditor!!.putString("image", image)
        spEditor!!.commit()
    }

    fun getImage(): String? {
        return sp.getString("image", image)
    }
    fun setMail(n: String) {
        this.mail = n
        spEditor = sp.edit()
        spEditor!!.putString("mail", mail)
        spEditor!!.commit()
    }

    fun getMail(): String? {
        return sp.getString("mail", mail)
    }

    fun setDesc(n: String) {
        this.desc = n
        spEditor = sp.edit()
        spEditor!!.putString("desc", desc)
        spEditor!!.commit()
    }

    fun getDesc(): String? {
        return sp.getString("desc", desc)
    }
    fun setLongi(n: String) {
        this.longi = n
        spEditor = sp.edit()
        spEditor!!.putString("longi", longi)
        spEditor!!.commit()
    }

    fun getLongi(): String? {
        return sp.getString("longi", longi)
    }
    fun setLatt(n: String) {
        this.latt = n
        spEditor = sp.edit()
        spEditor!!.putString("latt", latt)
        spEditor!!.commit()
    }

    fun getLatt(): String? {
        return sp.getString("latt", latt)
    }
    fun setName(n: String) {
        this.name = n
        spEditor = sp.edit()
        spEditor!!.putString("name", name)
        spEditor!!.commit()
    }

    fun getName(): String? {
        return sp.getString("name", name)
    }
    fun setBdate(n: String) {
        this.bdate = n
        spEditor = sp.edit()
        spEditor!!.putString("bdate", bdate)
        spEditor!!.commit()
    }

    fun getBdate(): String? {
        return sp.getString("bdate", bdate)
    }

    constructor(context: Context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context)
    }
    fun setId(id: String) {
        Id = id
        spEditor = sp.edit()
        spEditor!!.putString("Id", Id)
        spEditor!!.commit()
    }

    fun getId(): String? {
        return sp.getString("Id", Id)
    }

    fun setType(type: String) {
        Type = type
        spEditor = sp.edit()
        spEditor!!.putString("Type", Type)
        spEditor!!.commit()
    }

    fun getType(): String? {
        return sp.getString("Type", Type)
    }

    fun setLogin(status: Boolean): Boolean {
        spEditor = sp.edit()
        spEditor!!.putBoolean("is_logged_in", status)
        spEditor!!.commit()
        return true
    }

    fun getLoggedIn(): Boolean {
        return sp.getBoolean("is_logged_in", false)
    }

}
