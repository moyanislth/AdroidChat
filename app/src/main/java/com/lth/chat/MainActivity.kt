package com.lth.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.lth.chat.ui.theme.ChatTheme


class MainActivity : AppCompatActivity() {

    companion object {
        var isLoginActivityStarted = false
//        val db = AppDatabase.getInstance()
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
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun HomePage() {
        // 记录当前选中的导航栏项
        var selectedItem by rememberSaveable { mutableIntStateOf(0) } // 0: 消息, 1: 联系人, 2: 我的

        // 主页的布局
        Scaffold(
            topBar = {
                // 顶部工具栏
                TopAppBar(
                    // 设置背景颜色
                    title = { Text("Chat") },
                )
            },
            content = {
                Log.v("HomePage", "selectedItem: $selectedItem")
                // 根据当前选中项来决定展示哪个内容
                when (selectedItem) {
                    0 -> MessageContent() // 消息内容
                    1 -> ContactsContent() // 联系人内容
                    2 -> MyContent() // 我的内容
                }
            }
            ,
            bottomBar = {
                // 底部导航栏
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedItem == 0,
                        onClick = { selectedItem = 0 },
                        { Text(text = "消息") }
                    )
                    NavigationBarItem(
                        selected = selectedItem == 1,
                        onClick = { selectedItem = 1 },
                        { Text(text = "联系人") }
                    )
                    NavigationBarItem(
                        selected = selectedItem == 2,
                        onClick = { selectedItem = 2 },
                        { Text(text = "我的") }
                    )
                }
            }

        )
    }

    // 未完成，要求显示主页我的界面
    @Composable
    private fun MyContent() {
        Log.v("MyContent", "MyContent")
        Text(text = "我的")
    }

    // 未完成，要求显示主页联系人界面
    @Composable
    private fun ContactsContent() {
        Text(text = "联系人")
    }

    // 未完成，要求显示主页消息界面
    @Composable
    private fun MessageContent() {
        Text("消息页面内容", color = Color.Black, fontSize = 24.sp)
    }


    // 启动Compose界面
    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun MainCompose() {
        rememberNavController()
        // 启动Compose界面
        HomePage()
    }
}
