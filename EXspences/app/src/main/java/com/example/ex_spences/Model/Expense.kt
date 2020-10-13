package com.example.ex_spences.Model

import java.util.*

class Expense(
    val eid: String?,
    val aid: String,
    val cid: String,
    val amount: Double,
    val date: Date,
    val details: String,
    val place: String,
    val uid: String?
) {
    constructor() : this("", "", "", 0.0, Date(0,0,0,0,0), "", "", "")
}