package com.example.grocerystore

import kotlin.reflect.KClass

object ServiceLocator {

    private lateinit var instance : MutableMap<KClass<*>,Any>

    fun init(){
        instance = mutableMapOf<KClass<*>,Any>()
    }


    inline fun<reified T:Any> register(instance : T) = register(T::class, instance)

    fun <T : Any> register(kClass : KClass<T>, item : T){
        instance[kClass] = item
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(kClass : KClass<T>) : T = instance[kClass] as T
}