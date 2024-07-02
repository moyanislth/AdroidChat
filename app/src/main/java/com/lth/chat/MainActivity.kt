package com.lth.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lth.chat.ui.theme.ChatTheme


class MainActivity : AppCompatActivity() {

    companion object {
        var isLoginActivityStarted = false
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 登录起手、一无所有
        if (!isLoginActivityStarted) {
            startActivity(Intent(this, LoginActivity::class.java))
            isLoginActivityStarted = true
        }

        setContent {
            ChatTheme {
                MainCompose()
            }
        }
    }

    // 底部导航栏
    @Composable
    private fun BottomBar() {
        TODO("Not yet implemented")

    }

    // 中间信息展示列表
    @Composable
    private fun BloomRowBanner() {
        TODO("Not yet implemented")

    }

    // 中间信息展示列表
    @Composable
    private fun BloomInfoList() {
        TODO("Not yet implemented")

    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun HomePage(navController: NavHostController) {
        Scaffold(
            topBar = {
                // 顶部标题栏
                Text("Chat", modifier = Modifier.padding(16.dp))
            }
        ) {
            // 列表
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // 消息列表
                MessageItem("Hello, World!")
            }
        }
    }

    @Composable
    fun MessageItem(text: String) {
        // 消息列表项的布局，这里只是一个简单的文本展示
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text)
        }
    }

    // 启动Compose界面
    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun MainCompose() {
        val navController = rememberNavController()
        // 启动Compose界面
        setContent {
            HomePage(navController)
        }
    }
}
