package com.truongdinh.drinkorder.data.repository


import com.truongdinh.drinkorder.data.dao.DrinkDao
import com.truongdinh.drinkorder.data.model.Drink

class DrinkRepository(
    private val drinkDao: DrinkDao,
) {
    fun getAllDrinks() = drinkDao.getAllDrinks()
    fun getDrinksByCategory(categoryId: Int) = drinkDao.getDrinksByCategory(categoryId)
    suspend fun insertDrinks(drinks: List<Drink>) = drinkDao.insertDrinks(drinks)

}