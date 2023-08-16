package com.example.grocerystore.domain.helpers

data class Item(
    var id: Long,
    var name : String,
    var price : Int,
    var weight : Int,
    var description : String,
    var image_url : String,
    var tegs : List<String>
){
}