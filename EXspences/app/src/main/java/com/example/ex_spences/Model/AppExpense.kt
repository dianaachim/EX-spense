package com.example.ex_spences.Model

class AppExpense (
    val eid: String?,
    val account: String,
    val categoryImage: String,
    val amount: Double,
    val date: String,
    val details: String,
    val place: String,
    val uid: String?
) {
    constructor() : this("", "", "", 0.0, "", "", "", "")
}