package com.truongdinh.drinkorder.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truongdinh.drinkorder.data.model.Category
import com.truongdinh.drinkorder.data.model.Drink
import com.truongdinh.drinkorder.data.repository.CategoryRepository
import com.truongdinh.drinkorder.data.repository.DrinkRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class MenuViewModel(
    private val drinkRepository: DrinkRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _drinks = MutableStateFlow<List<Drink>>(emptyList())
    val drinks: StateFlow<List<Drink>> = _drinks.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId.asStateFlow()


    init {
        seedCategoriesIfNeeded()
        observeCategories()
        observeDrinksBySelectedCategory()
        seedDrinksIfNeeded()
        observeRoomDrinks()
    }

    private fun seedDrinksIfNeeded() {
        viewModelScope.launch {
            val existingDrinks = drinkRepository.getAllDrinks().first()
            if (existingDrinks.isNotEmpty()) return@launch

                val drinks = listOf(
                    Drink(
                        name = "Cappuccino",
                        price = 35000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1773020123/cappuccino_l5ubuk.jpg",
                        idCategory = 1
                    ),

                     Drink(
                         name = "Cà phê đen",
                         price = 20000,
                         size = "S",
                         quantity = 1,
                         note = "",
                         image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1773020123/black_coffee_lbua5p.jpg",
                         idCategory = 1
                     ),

                    Drink(
                        name = "Cà phê sữa",
                        price = 25000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772070662/milk_coffee_kg7u5a.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Bạc xỉu",
                        price = 28000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1773020123/bac_xiu_f3tmwj.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Cà phê sữa tươi",
                        price = 30000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1773020123/fresh_milk__coffee_qdhme7.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Cà phê muối",
                        price = 30000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772070663/salt_coffee_qkuvwk.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Cà phê dừa",
                        price = 35000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772070663/coconut_coffee_ozlmyl.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Espresso",
                        price = 30000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772158365/espresso_vc0bxw.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Americano",
                        price = 30000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1773020518/americano_vjiyw2.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Mocha",
                        price = 38000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1773020518/mocha_hgqble.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Latte",
                        price = 30000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772158364/latte_utwzgj.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Cold Brew",
                        price = 40000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772591636/cold_brew_vky2xs.png",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Cold Brew cam",
                        price = 42000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1773020518/orange_cold_brew_wpixwl.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Cold Brew sữa",
                        price = 42000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772591627/cold_brew_milk_lzq9kq.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Cà phê phin giấy",
                        price = 30000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772675251/paper_filter_coffee_prfv8y.jpg",
                        idCategory = 1
                    ),

                    Drink(
                        name = "Trà đào cam sả",
                        price = 35000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772591359/peach_orange_lemongrass_tea_wvuylo.jpg",
                        idCategory = 2
                    ),

                    Drink(
                        name = "Trà đào",
                        price = 32000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772675258/peach_tea_brn6m6.jpg ",
                        idCategory = 2
                    ),

                    Drink(
                        name = "Trà vải",
                        price = 33000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772675261/lychee_tea_psaf6h.jpg",
                        idCategory = 2
                    ),

                    Drink(
                        name = "Trà chanh",
                        price = 25000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772675251/lemon_tea_cdm3k8.jpg",
                        idCategory = 2
                    ),

                    Drink(
                        name = "Trà tắc",
                        price = 27000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772675251/kumquat_tea_pshyrl.jpg",
                        idCategory = 2
                    ),

                    Drink(
                        name = "Trà sen",
                        price = 30000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772675251/lotus_tea_ucallg.jpg",
                        idCategory = 2
                    ),


                    Drink(
                        name = "Trà sữa truyền thống",
                        price = 35000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772762889/traditional_milk_tea_cisdra.jpg",
                        idCategory = 2
                    ),

                    Drink(
                        name = "Trà sữa matcha",
                        price = 40000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772762489/matcha_milk_tea_duhhlr.jpg",
                        idCategory = 2
                    ),

                    Drink(
                        name = "Trà sữa socola",
                        price = 40000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772762490/chocolate_milk_tea_rmjdqc.jpg",
                        idCategory = 2
                    ),

                    Drink(
                        name = "Sinh tố bơ",
                        price = 35000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772762526/avocado_smoothie_ys1rc1.jpg",
                        idCategory = 3
                    ),

                    Drink(
                        name = "Sinh tố dâu",
                        price = 35000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772762522/strawberry_smoothie_ymwoz6.jpg",
                        idCategory = 3
                    ),

                    Drink(
                        name = "Sinh tố xoài",
                        price = 35000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1773020123/mango_smoothie_gtmznt.jpg",
                        idCategory = 3
                    ),

                    Drink(
                        name = "Nước ép cam",
                        price = 30000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772762535/orange_juice_om7rh8.jpg",
                        idCategory = 4
                    ),

                    Drink(
                        name = "Nước ép dưa hấu",
                        price = 30000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772762541/watermelon_juice_gtvbkt.jpg",
                        idCategory = 4
                    ),

                    Drink(
                        name = "Bánh flan",
                        price = 25000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772591135/flan_nc0tvy.jpg",
                        idCategory = 5
                    ),

                    Drink(
                        name = "Bánh tiramisu",
                        price = 45000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772591135/tiramisu_xeauqb.jpg",
                        idCategory = 5
                    ),

                    Drink(
                        name = "Bánh cheesecake",
                        price = 45000,
                        size = "S",
                        quantity = 1,
                        note = "",
                        image = "https://res.cloudinary.com/dq1mpgagw/image/upload/v1772591135/cheesecake_yf8d6v.jpg",
                        idCategory = 5
                    )
                )

                drinkRepository.insertDrinks(drinks)
        }
    }

    fun selectCategory(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
    }

    private fun observeCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories()
                .collectLatest { list ->
                    _categories.value = list
                }
        }
    }

    private fun seedCategoriesIfNeeded() {
        viewModelScope.launch {
            val hasData = categoryRepository.getAllCategories()
                .first()
                .isNotEmpty()

            if (!hasData) {
                categoryRepository.insertCategories(
                    listOf(
                        Category(1, "Cà phê"),
                        Category(2, "Trà"),
                        Category(3, "Sinh tố"),
                        Category(4, "Nước ép"),
                        Category(5, "Bánh")
                    )
                )
            }
        }
    }

    private fun observeRoomDrinks() {
        viewModelScope.launch {
            drinkRepository.getAllDrinks()
                .collectLatest { list ->
                    _drinks.value = list
                }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeDrinksBySelectedCategory() {
        viewModelScope.launch {
            selectedCategoryId
                .flatMapLatest { categoryId ->
                    if (categoryId == null) {
                        drinkRepository.getAllDrinks()
                    } else {
                        drinkRepository.getDrinksByCategory(categoryId)
                    }
                }
                .collectLatest { list ->
                    _drinks.value = list
                }
        }
    }
}