package com.example.ex_spences.Model

class Income (
    val iid: String,
    val amount: Long,
    val aid: String,
    val uid: String?
) {
    constructor() : this("", 0, "", "")
}