package ru.anlyashenko.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.anlyashenko.core.model.Item

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = CategoryDBO::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["category_id"])
    ]
)
data class ItemDBO(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon") val icon: String?,
    @ColumnInfo(name = "quantity") val quantity: Double?,
    @ColumnInfo(name = "unit") val unit: String?,
    @ColumnInfo(name = "expiration_date") val expirationDate: Long?,
    @ColumnInfo(name = "storage_conditions") val storageConditions: String?,
    @ColumnInfo(name = "needs_attention") val needsAttention: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
)

fun ItemDBO.asExternalModel() = Item(
    id = id,
    categoryId = categoryId,
    name = name,
    icon = icon,
    quantity = quantity,
    unit = unit,
    expirationDate = expirationDate,
    storageConditions = storageConditions,
    needsAttention = needsAttention,
)
