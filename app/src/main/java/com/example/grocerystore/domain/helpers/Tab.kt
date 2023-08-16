package com.example.grocerystore.domain.helpers

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment

data class Tab(
    var id: Long,
    var name: String,
    var icon : Drawable?,
    var fragment: Fragment
)