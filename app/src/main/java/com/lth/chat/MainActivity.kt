package com.lth.chat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun QQHomePage(navController: NavHostController) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("主页") }) // 顶部导航栏
            },
            bottomBar = {
//                // 底部导航栏
//                QQBottomNav(navController)
                Text(text = "底部导航栏") // 底部导航栏
            }
        ) {
            // 使用LazyColumn创建可滑动的全屏消息列表
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                // 这里你可以添加消息列表的item
                items(count = 20) { index ->
                    MessageItem("消息内容 $index") // 假设的MessageItem，你需要自定义这个组件
                }
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
            QQHomePage(navController)
        }
    }
}
