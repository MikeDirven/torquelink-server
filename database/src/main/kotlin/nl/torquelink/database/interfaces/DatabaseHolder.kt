package nl.torquelink.database.interfaces

abstract class DatabaseHolder {
    abstract fun <T> execute(execution: () -> T) : T

    abstract suspend fun <T> executeAsync(execution: suspend () -> T) : T
}