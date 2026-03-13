package com.truongdinh.drinkorder.data.repository

import com.truongdinh.drinkorder.data.dao.UserDao
import com.truongdinh.drinkorder.data.mapper.toDomain
import com.truongdinh.drinkorder.data.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun register(user: User): Result<Unit> {
        val existedUser = userDao.getUserByEmail(user.email)

        if (existedUser != null) {
            return Result.failure(Exception("Email đã tồn tại"))
        }

        userDao.insertUser(user)
        return Result.success(Unit)
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val user = userDao.getUserByEmail(email)

            if (user == null) {
                Result.failure(Exception("Email không tồn tại"))
            } else if (user.password != password) {
                Result.failure(Exception("Mật khẩu không tồn tại"))
            } else {
                Result.success(user.toDomain())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}