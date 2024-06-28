package com.lth.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.lth.chat.pojo.Message
import com.lth.chat.ui.MessageCard

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                showMessage()
                showMessage()
            }
        }
    }

}

@Composable
fun showMessage() {
    MessageCard(Message().apply {
        sender = "张三"
        text = "你好"
        time = "2021-10-10"
        isMe = false
    })
}
