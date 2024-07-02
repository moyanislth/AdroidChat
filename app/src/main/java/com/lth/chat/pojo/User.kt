package com.lth.chat.pojo

import java.lang.Math.random

class User {
    val id: Int = random().toInt()
    var account = ""
    var username: String = ""
    var password: String = ""
}