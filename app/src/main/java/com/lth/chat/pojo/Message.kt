package com.lth.chat.pojo

import androidx.compose.foundation.Image
import java.lang.Math.random

class Message {

    val id: Int = random().toInt()
    var date: String = ""

    var sender: String = ""
    var receiver: String = ""
    var text: String = ""

}