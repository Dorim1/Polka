package ru.anlyashenko.core.database.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import ru.anlyashenko.core.data.model.asEntity
import ru.anlyashenko.core.database.entity.ItemDBO
import ru.anlyashenko.core.database.entity.asExternalModel
import ru.anlyashenko.core.model.Item

/**
 * Unit tests for mapping database models (DBO) to external models and vice versa.
 */
class InventoryMappingTest {

    @Test
    fun itemDbo_can_be_mapped_to_external_model() {
        val itemDbo = ItemDBO(
            id = 1L,
            categoryId = 2L,
            name = "Milk",
            icon = "icon_milk",
            quantity = 1.5,
            unit = "l",
            expirationDate = 1672531200000L,
            storageConditions = "in the fridge",
            needsAttention = true,
            createdAt = 1000L,
            updatedAt = 2000L
        )

        val item = itemDbo.asExternalModel()

        val expectedItem = Item(
            id = 1L,
            categoryId = 2L,
            name = "Milk",
            icon = "icon_milk",
            quantity = 1.5,
            unit = "l",
            expirationDate = 1672531200000L,
            storageConditions = "in the fridge",
            needsAttention = true,
        )

        assertThat(item).isEqualTo(expectedItem)
    }

    @Test
    fun item_can_be_mapped_to_dbo_entity() {
        val item = Item(
            id = 10L,
            categoryId = 5L,
            name = "Chees",
            icon = null,
            quantity = 500.0,
            unit = "g",
            expirationDate = null,
            storageConditions = null,
            needsAttention = false
        )

        val currentTime = 55555L
        val itemDbo = item.asEntity(createdAt = currentTime, updatedAt = currentTime)

        assertThat(itemDbo.id).isEqualTo(10L)
        assertThat(itemDbo.name).isEqualTo("Chees")
        assertThat(itemDbo.icon).isNull()
        assertThat(itemDbo.createdAt).isEqualTo(currentTime)
        assertThat(itemDbo.updatedAt).isEqualTo(currentTime)
    }
}
