package com.example.grocerystore.data.helpers.UIstates.item

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment

data class TabUIState(
    var id: Long,
    var name: String,
    var icon : Drawable?,
    var fragment: Fragment
)