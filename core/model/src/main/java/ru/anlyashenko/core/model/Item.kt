package ru.anlyashenko.core.model

data class Item(
    val id: Long = 0,
    val categoryId: Long,
    val name: String,
    val icon: String?,
    val quantity: Double?,
    val unit: String?,
    val expirationDate: Long?,
    val storageConditions: String?,
    val needsAttention: Boolean = false,
)
