package ru.laushkina.rates.data

interface LastUpdateDataSource {
    fun save(lastUpdateTimestamp: Long)
    fun get(): Long
}