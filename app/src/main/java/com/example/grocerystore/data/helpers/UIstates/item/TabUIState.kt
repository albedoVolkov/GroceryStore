package com.example.grocerystore.data.helpers.UIstates.item


import androidx.fragment.app.Fragment

data class TabUIState(
    var id: String,
    var name: String,
    var icon : Int,
    var fragment: Fragment
)