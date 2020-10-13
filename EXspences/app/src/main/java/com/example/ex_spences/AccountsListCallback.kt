package com.example.ex_spences

import com.example.ex_spences.Model.Account

interface AccountsListCallback {
    fun onCallback(value: List<Account>)
}
