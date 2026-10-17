package ru.anlyashenko.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import ru.anlyashenko.core.database.dao.CategoryDAO
import ru.anlyashenko.core.database.dao.ItemDAO

internal abstract class DatabaseTest {

    private lateinit var db: AppDatabase

    protected lateinit var categoryDAO: CategoryDAO

    protected lateinit var itemDAO: ItemDAO

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java,
            ).build()
        }
        categoryDAO = db.categoryDao()
        itemDAO = db.itemDao()
    }

    @After
    fun teardown() = db.close()
}
