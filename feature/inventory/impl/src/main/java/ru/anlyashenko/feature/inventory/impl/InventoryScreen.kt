package ru.anlyashenko.feature.inventory.impl

import android.R.attr.text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.anlyashenko.core.designsystem.R
import ru.anlyashenko.core.designsystem.theme.PolkaAppTheme
import ru.anlyashenko.core.model.Category
import ru.anlyashenko.core.model.CategoryInventory
import ru.anlyashenko.core.model.Item
import java.nio.file.WatchEvent

@Composable
fun InventoryScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InventoryScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onMoreClick = { /*TODO*/ },
        onCategoryFilterClick = viewModel::onCategoryFilterSelected,
        onItemClick = viewModel::onItemClicked,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InventoryScreen(
    uiState: InventoryUiState,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit,
    onCategoryFilterClick: (Category) -> Unit,
    onItemClick: (Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Polka",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onMoreClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_more),
                            contentDescription = "More",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is InventoryUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is InventoryUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CategoryFilterRow(
                        categories = uiState.filterCategories,
                        selectedCategory = uiState.selectedCategory, // todo ?
                        onCategoryClick = onCategoryFilterClick,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        uiState.inventory.forEach { categoryInventory ->
                            item(key = "header_${categoryInventory.category.id}") {
                                Text(
                                    text = categoryInventory.category.name,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            items(
                                items = categoryInventory.items,
                                key = { it.id }
                            ) { product ->
                                ProductItemCard(
                                    item = product,
                                    onClick = { onItemClick(product) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItemCard(
    item: Item,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.icon ?: "📦",
                fontSize = 20.sp
            )

            Spacer(Modifier.width(16.dp))

            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            val quantityText = formatQuantity(item.quantity)
            val unitText = item.unit ?: ""
            val displayText = listOfNotNull(quantityText, unitText)
                .joinToString()
                .trim()

            if (item.needsAttention) {
                Text(
                    text = "! $displayText",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            } else {
                Text(
                    text = displayText,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            }
        }
    }
}

private fun formatQuantity(quantity: Double?): String? {
    if (quantity == null) return null
    return if (quantity % 1.0 == 0.0) {
        quantity.toInt().toString()
    } else {
        quantity.toString()
    }
}

@Composable
fun CategoryFilterRow(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategoryClick: (Category) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(categories, key = { it.id }) { category ->
            val isSelected = category.id == selectedCategory?.id

            Surface(
                onClick = { onCategoryClick(category) },
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
            ) {
                Text(
                    text = category.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InventoryScreenPreview() {
    PolkaAppTheme {
        InventoryScreen(
            uiState = InventoryUiState.Success(
                filterCategories = listOf(
                    Category(1, "Молочка"),
                    Category(2, "Соусы"),
                    Category(3, "Ягоды"),
                    Category(4, "Крупы"),
                    Category(5, "Рыба и м...")
                ),
                inventory = listOf(
                    CategoryInventory(
                        category = Category(10, "Всякое"),
                        items = listOf(
                            Item(1, 10, "Мюсли", "🥥", 1.0, "пачка", expirationDate = null, storageConditions = null),
                            Item(2, 10, "Пыльца", "🌾", 100.0, "г", expirationDate = null, storageConditions = null),
                            Item(3, 10, "Мёд", "🍯", null, "много", expirationDate = null, storageConditions = null),
                            Item(4, 10, "Голубика", "🫐", 170.0, "г", expirationDate = null, storageConditions = null)
                        )
                    ),
                    CategoryInventory(
                        category = Category(11, "Молочка"),
                        items = listOf(
                            Item(5, 11, "Сметана", "🍶", 2.0, "банки", expirationDate = null, storageConditions = null),
                            Item(6, 11, "Масло", "🧈", 170.0, "г", expirationDate = null, storageConditions = null),
                            Item(7, 11, "Сыр", "🧀", 260.0, "г", expirationDate = null, storageConditions = null),
                            Item(8, 11, "Яйца", "🥚", 7.0, "шт", needsAttention = true, expirationDate = null, storageConditions = null)
                        )
                    ),
                    CategoryInventory(
                        category = Category(12, "Соусы"),
                        items = listOf(
                            Item(9, 12, "Аджика", "🌶️", 100.0, "г", expirationDate = null, storageConditions = null),
                            Item(10, 12, "Горчица", "🫙", 100.0, "г", expirationDate = null, storageConditions = null)
                        )
                    )
                )
            ),
            onBackClick = {},
            onMoreClick = {},
            onCategoryFilterClick = {},
            onItemClick = {}
        )
    }
}
