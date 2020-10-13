package com.example.ex_spences

import com.example.ex_spences.Model.PiggyBank

interface SavingsListCallback {
    fun onCallback(value: List<PiggyBank>)
}
