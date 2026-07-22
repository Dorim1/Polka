package ru.anlyashenko.feature.inventory.impl

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.anlyashenko.core.designsystem.R
import ru.anlyashenko.core.designsystem.theme.PolkaAppTheme
import ru.anlyashenko.core.model.Category
import ru.anlyashenko.core.model.CategoryInventory
import ru.anlyashenko.core.model.Item
import ru.anlyashenko.core.ui.DevicePreviews
import ru.anlyashenko.core.ui.InventoryListPreviewParameterProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showAddSheet by remember { mutableStateOf(false) }

    InventoryScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onMoreClick = { showAddSheet = true },
        onCategoryFilterClick = viewModel::onCategoryFilterSelected,
        onItemClick = viewModel::onItemClicked,
        modifier = modifier,
    )

    if (showAddSheet) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            AddItemSheetContent(
                categories = (uiState as? InventoryUiState.Success)?.filterCategories ?: emptyList(),
                onSave = { name, categoryId, quantity, unit ->
                    viewModel.onItemAdd(name, categoryId, quantity, unit)
                    showAddSheet = false
                },
                onCancel = { showAddSheet = false }
            )
        }
    }
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
                        selectedCategory = uiState.selectedCategory,
                        onCategoryClick = onCategoryFilterClick,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 320.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        uiState.inventory.forEach { categoryInventory ->
                            item(
                                span = { GridItemSpan(maxLineSpan) },
                                key = "header_${categoryInventory.category.id}"
                            ) {
                                Text(
                                    text = categoryInventory.category.name,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 8.dp)
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
                .joinToString(" ")
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
                color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(
                    alpha = 0.2f
                )
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

private fun formatQuantity(quantity: Double?): String? {
    if (quantity == null) return null
    return if (quantity % 1.0 == 0.0) {
        quantity.toInt().toString()
    } else {
        quantity.toString()
    }
}

@Preview
@Composable
private fun ProductItemCardPreview(
    @PreviewParameter(InventoryListPreviewParameterProvider::class)
    inventory: List<CategoryInventory>
) {
    val sampleItem = inventory.first().items.first()
    PolkaAppTheme {
        ProductItemCard(
            item = sampleItem,
            onClick = {}
        )
    }
}

@DevicePreviews
@Composable
private fun InventoryScreenPopulated(
    @PreviewParameter(InventoryListPreviewParameterProvider::class)
    inventory: List<CategoryInventory>
) {
    PolkaAppTheme {
        InventoryScreen(
            uiState = InventoryUiState.Success(
                inventory = inventory,
                filterCategories = inventory.map { it.category },
            ),
            onBackClick = {},
            onMoreClick = {},
            onCategoryFilterClick = {},
            onItemClick = {}
        )
    }
}

@DevicePreviews
@Composable
private fun InventoryScreenLoading() {
    PolkaAppTheme {
        InventoryScreen(
            uiState = InventoryUiState.Loading,
            onBackClick = {},
            onMoreClick = {},
            onCategoryFilterClick = {},
            onItemClick = {}
        )
    }
}
