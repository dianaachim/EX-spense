package com.example.ex_spences

import java.util.*
import kotlin.collections.HashMap

interface DailyExpensesHashMapCallback {
    fun onCallback(value: HashMap<Int, Double>)
}