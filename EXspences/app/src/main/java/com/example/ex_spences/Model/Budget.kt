package com.example.ex_spences.Model

class Budget(
    val bid: String,
    var amount: Long,
    val current_amount: Double,
    val month: Long,
    val year: Long,
    val uid: String?
) {
    constructor() : this("", 0, 0.0, 0, 0, "")
}