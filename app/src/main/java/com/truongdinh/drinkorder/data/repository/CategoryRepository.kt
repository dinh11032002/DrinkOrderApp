package com.truongdinh.drinkorder.data.repository

import com.truongdinh.drinkorder.data.dao.CategoryDao
import com.truongdinh.drinkorder.data.model.Category

class CategoryRepository(private val categoryDao: CategoryDao) {
    fun getAllCategories() = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    suspend fun insertCategories(categories: List<Category>) = categoryDao.insertCategories(categories)
}