package com.example.ex_spences.Model

class Account (
    var aid: String ?,
    var name: String,
    var type: String,
    var balance: Double,
    var uid: String ?
) {
    constructor() : this("", "", "", 0.0, "")
    override fun toString(): String {
        return this.name + " - " + this.type
    }
}