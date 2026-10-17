package ru.anlyashenko.core.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.anlyashenko.core.database.entity.CategoryDBO
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CategoryDaoTest : DatabaseTest() {

    @Test
    fun getAllCategories_areOrderedByName() = runTest {
        categoryDAO.insertCategory(testCategory(name = "Ягоды"))
        categoryDAO.insertCategory(testCategory(name = "Молочка"))

        val names = categoryDAO.getAllCategories().first().map { it.name }

        assertEquals(listOf("Молочка", "Ягоды"), names)
    }

    @Test
    fun getCategoriesWithItems_groupsItemsByCategory() = runTest {
        val dairyId = categoryDAO.insertCategory(testCategory(name = "Молочка"))
        val berriesId = categoryDAO.insertCategory(testCategory(name = "Ягоды"))
        itemDAO.insertItem(testItem(categoryId = dairyId, name = "Сыр"))
        itemDAO.insertItem(testItem(categoryId = dairyId, name = "Масло"))
        itemDAO.insertItem(testItem(categoryId = berriesId, name = "Голубика"))

        val result = categoryDAO.getCategoriesWithItems().first()

        assertEquals(listOf("Молочка", "Ягоды"), result.map { it.categoryDBO.name })
        assertEquals(setOf("Сыр", "Масло"), result[0].items.map { it.name }.toSet())
        assertEquals(listOf("Голубика"), result[1].items.map { it.name })
    }

    @Test
    fun getCategoriesWithItems_categoryWithoutItems_hasEmptyList() = runTest {
        categoryDAO.insertCategory(testCategory(name = "Пустая"))

        val result = categoryDAO.getCategoriesWithItems().first()

        assertEquals(1, result.size)
        assertTrue(result.single().items.isEmpty())
    }

    @Test
    fun insertCategory_sameId_replacesExisting() = runTest {
        val id = categoryDAO.insertCategory(testCategory(name = "Старое"))
        categoryDAO.insertCategory(testCategory(id = id, name = "Новое"))

        val categories = categoryDAO.getAllCategories().first()

        assertEquals(listOf("Новое"), categories.map { it.name })
    }

    @Test
    fun deleteCategory_cascadeToItems() = runTest {
        val id = categoryDAO.insertCategory(testCategory(name = "Молочка"))
        itemDAO.insertItem(testItem(categoryId = id, name = "Сыр"))

        categoryDAO.deleteCategory(testCategory(id = id, name = "Молочка"))

        assertTrue(itemDAO.getItemsByCategory(id).first().isEmpty())
    }
}

