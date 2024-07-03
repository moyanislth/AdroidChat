package com.lth.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lth.chat.dao.AppDatabase
import com.lth.chat.dao.Message
import com.lth.chat.ui.theme.ChatTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatActivity : ComponentActivity() {
    private val context = this
    private val senderId by lazy { intent.getIntExtra("UserSession", 0) }
    private val receiverId by lazy { intent.getIntExtra("friendId", 0) }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatTheme {
                ChatView(context)
            }
        }
    }

    // 启用全屏（无状态栏和导航栏）
    private fun enableEdgeToEdge() {
        // 这里可以添加代码来启用全屏模式，例如使用 WindowManager
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ChatView(context: Context) {
        // 使用 Scaffold 创建聊天界面的基础结构
        val db = AppDatabase.getInstance(LocalContext.current)
        db.userDao().queryById(senderId)
        val receiver = db.userDao().queryById(receiverId)

        Scaffold(
            modifier = Modifier
                .fillMaxSize(), // 填充整个屏幕
            topBar = {
                TopAppBar(
                    title = { receiver?.let { Text(text = it.username) } }, // 显示好友昵称
                    navigationIcon = {
                        // 返回按钮
                        IconButton(onClick = {
                            // 返回上一个界面
                            context.startActivity(Intent(context, MainActivity::class.java))
                        }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                        }
                    }
                )
            },
            bottomBar = {
                // 输入区域，包括文本输入框和发送按钮
                SendMessageBar()
            }
        ) { paddingValues ->
            // 使用 Column 来垂直排列聊天消息和输入区域
            Column(modifier = Modifier.padding(paddingValues)) {
                // 消息列表区域
                ChatMessageList() // 假设你有一个 ChatMessageList 组件
            }
        }
    }


    // 假设的 ChatMessageList 组件，用于展示消息列表
    @Composable
    fun ChatMessageList() {
        // 这里将展示消息列表，可以使用 LazyColumn 等组件来实现滚动列表
        LazyColumn {
            // 这里可以添加消息列表的内容

        }
    }

    // 发送消息的输入区域
    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun SendMessageBar() {
        var messageText by remember { mutableStateOf("") }
        val db = AppDatabase.getInstance(LocalContext.current)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // 文本输入框
            TextField(
                value = "", // 初始为空
                onValueChange = {
                        newValue -> messageText = newValue
                },
                placeholder = { Text("输入消息内容...") },
                modifier = Modifier
                    .weight(1f) // 占据大部分空间
                    .padding(end = 8.dp), // 右侧间距
                textStyle = MaterialTheme.typography.bodyLarge,

                )

            // 发送按钮
            Button(
                onClick = {
                    // 在协程中执行数据库插入操作
                    GlobalScope.launch(Dispatchers.IO) {
                        val message = Message(
                            id = 0, // ID 通常是自增主键，这里假设数据库会自动处理
                            senderId = senderId.toString(),
                            receiverId = receiverId.toString(),
                            content = messageText,
                            timestamp = java.util.Date() // 使用 java.time.Instant 替代 java.util.Date
                        )

                        val messageDao = db.messageDao()
                        messageDao.insert(message)

                        // 操作完成后回到主线程更新 UI
                        withContext(Dispatchers.Main) {
                            // 清空文本框
                            messageText = ""
                            // 其他 UI 更新逻辑...
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "发送")
            }
        }
    }

}
