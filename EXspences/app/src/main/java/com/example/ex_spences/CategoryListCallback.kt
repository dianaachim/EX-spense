package com.example.ex_spences

import com.example.ex_spences.Model.Category

interface CategoryListCallback {
    fun onCallback(value: List<Category>)
}