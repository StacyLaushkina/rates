package ru.laushkina.rates.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.laushkina.rates.model.Rate
import java.util.*

@Entity(tableName = "rates")
class RateEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val shortName: String,
        val amount: Float,
        val isBase: Boolean) {

    constructor(rate: Rate) : this(Random().nextInt(), rate.shortName.name, rate.amount, rate.isBase)
}