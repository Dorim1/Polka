package ru.anlyashenko.core.database

import android.database.sqlite.SQLiteConstraintException
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ItemDaoTest : DatabaseTest() {

    private var categoryId = 0L

    @Before
    fun insertCategory() = runTest {
        categoryId = categoryDAO.insertCategory(testCategory(name = "Молочка"))
    }

    @Test
    fun insertItem_withoutCategory_failForeignKey() = runTest {
        assertFailsWith<SQLiteConstraintException> {
            itemDAO.insertItem(testItem(categoryId = 999L, name = "Без категории"))
        }
    }

    @Test
    fun getItemsByCategory_returnsOnlyItsItems() = runTest {
        val otherId = categoryDAO.insertCategory(testCategory(name = "Ягоды"))
        itemDAO.insertItem(testItem(categoryId = categoryId, name = "Сыр"))
        itemDAO.insertItem(testItem(categoryId = otherId, name = "Голубика"))

        val items = itemDAO.getItemsByCategory(categoryId).first()

        assertEquals(listOf("Сыр"), items.map { it.name })
    }

    @Test
    fun updateItem_changesFields() = runTest {
        val id = itemDAO.insertItem(testItem(categoryId = categoryId, name = "Сыр", quantity = 1.0))

        itemDAO.updateItem(testItem(id = id, categoryId = categoryId, name = "Сыр", quantity = 5.0))

        assertEquals(5.0, itemDAO.getItemsByCategory(categoryId).first().single().quantity)
    }

    @Test
    fun updateAttentionFlag_updatesOnlyThatItem() = runTest {
        val first = itemDAO.insertItem(testItem(categoryId = categoryId, name = "Сыр"))
        val second = itemDAO.insertItem(testItem(categoryId = categoryId, name = "Масло"))

        itemDAO.updateAttentionFlag(first, true)

        val items = itemDAO.getItemsByCategory(categoryId).first().associateBy { it.id }
        assertTrue(items.getValue(first).needsAttention)
        assertFalse(items.getValue(second).needsAttention)
    }

    @Test
    fun deleteItem_removesIt() = runTest {
        val id = itemDAO.insertItem(testItem(categoryId = categoryId, name = "Сыр"))

        itemDAO.deleteItem(testItem(id = id, categoryId = categoryId, name = "Сыр"))

        assertTrue(itemDAO.getItemsByCategory(categoryId).first().isEmpty())
    }
}
