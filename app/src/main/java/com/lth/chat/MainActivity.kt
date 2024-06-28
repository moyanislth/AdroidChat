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
                ShowMessage()
                ShowMessage()
                ShowMessage()
            }
        }
    }

}

@Composable
fun ShowMessage() {
    MessageCard(Message().apply {
        date = "2021-09-01"
        sender = "张三"
        text = "你好"
        isMe = false
    })
}
