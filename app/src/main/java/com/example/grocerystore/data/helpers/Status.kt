package com.example.grocerystore.data.helpers

sealed class Status<out R> {

    data class Available<out T>(val data: T) : Status<T>()
    data class NotAvailable(val exception: Exception) : Status<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Available<*> -> "Available[data=$data]"
            is NotAvailable -> "NotAvailable[error=$exception]"
            else -> {"not defined"}
        }
    }
}

/**
 * `true` if [Status] is of type [Available] & holds non-null [Status.data].
 */
val Status<*>.succeeded
    get() = this is Status.Available && data != null