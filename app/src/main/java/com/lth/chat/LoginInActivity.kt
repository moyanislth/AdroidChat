package com.lth.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.lth.chat.dao.AppDatabase
import com.lth.chat.ui.theme.ChatTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatTheme {
                MyScreen()
            }
        }
    }



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyScreen() {
        val context = this
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        // 使用 Scaffold 提供的 innerPadding 作为 Column 的内边距
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("登录") })
            }
        ) { innerPadding ->

            // 使用 innerPadding 作为 Column 的内边距
            Column(
                modifier = Modifier
                    .padding(innerPadding) // 应用内边距
                    .fillMaxWidth() // 填满宽度
            ) {
                // 用户名输入框
                TextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // 额外的内边距
                )

                // 密码输入框
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    visualTransformation = PasswordVisualTransformation()
                )

                // 登录按钮
                Button(
                    onClick = {
                        /*实现登录逻辑*/
                        val userDao = AppDatabase.getInstance(context).userDao()
                        val user = userDao.queryByUsername(username.value)


                        if (user !=null && user.password == password.value) {
                            Log.v("LoginActivity", "success to login")
                            // 登录成功
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Log.v("LoginActivity", "fail to login")
                            // 登录失败
                            // 提示用户
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text("Login")
                }
            }
        }
    }
}