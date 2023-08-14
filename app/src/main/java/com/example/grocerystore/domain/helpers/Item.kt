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
    override fun toString(): String {
        return "Section [id: ${this.id}," +
                " name: ${this.name}," +
                " price: ${this.price}," +
                " weight: ${this.weight}," +
                " description: ${this.description}," +
                " image_url: ${this.image_url}," +
                " tegs: ${this.tegs}]"
    }
}