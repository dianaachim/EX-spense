package com.example.ex_spences

import com.example.ex_spences.Model.AppExpense

interface AppExpensesListCallback {
    fun onCallback(value: List<AppExpense>)
}