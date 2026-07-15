package ru.anlyashenko.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.anlyashenko.core.model.Category

@Entity(tableName = "categories")
data class CategoryDBO(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
)

fun CategoryDBO.asExternalModel() = Category(
    id = id,
    name = name,
)

