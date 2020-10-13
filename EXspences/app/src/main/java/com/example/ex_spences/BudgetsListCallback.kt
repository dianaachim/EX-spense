package com.example.ex_spences

import com.example.ex_spences.Model.Budget

interface BudgetsListCallback {
    fun onCallback(value: List<Budget>)
}
