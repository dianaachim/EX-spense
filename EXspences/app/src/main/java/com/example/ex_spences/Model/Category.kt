package com.example.ex_spences.Model

class Category(
    val cid: String,
    val name: String,
    val icon: String
) {
    constructor() : this("", "", "")

    override fun toString(): String {
        return this.name
    }
}