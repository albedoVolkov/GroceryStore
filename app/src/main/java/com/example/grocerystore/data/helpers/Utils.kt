package com.example.grocerystore.data.helpers

class Utils {

    enum class UserType { CUSTOMER, SELLER }

    enum class OrderStatus { CONFIRMED, PACKAGING, PACKED, SHIPPING, SHIPPED, ARRIVING, DELIVERED }

    enum class StoreDataStatus { LOADING, ERROR, DONE }

    enum class ConnectionStatus { INTERNET_TRANSPORT_WIFI, INTERNET_TRANSPORT_CELLULAR, INTERNET_TRANSPORT_ETHERNET, NO_CONNECTION }

}