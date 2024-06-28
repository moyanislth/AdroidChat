package com.lth.chat.pojo

import java.lang.Math.random

class Group {
    val id: Int = random().toInt()
    var name: String = ""
    var users: List<User> = listOf()
}