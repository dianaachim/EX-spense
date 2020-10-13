package com.example.ex_spences

import com.example.ex_spences.Model.Expense

interface ExpensesListCallback {
    fun onCallback(value: List<Expense>)
}