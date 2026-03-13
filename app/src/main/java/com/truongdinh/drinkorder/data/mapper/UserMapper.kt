package com.truongdinh.drinkorder.data.mapper

import com.truongdinh.drinkorder.data.model.User

fun User.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        phone = phone,
        password = password,
        dob = dob,
        gender = gender,
        address = address
    )
}