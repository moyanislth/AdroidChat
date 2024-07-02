package com.lth.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lth.chat.dao.AppDatabase
import com.lth.chat.pojo.User
import com.lth.chat.ui.theme.ChatTheme

class SignUpActivity : ComponentActivity() {
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
    private fun MyScreen() {
        val context = this
        val username = remember { mutableStateOf("") }
        val account = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        // 使用 Scaffold 提供的 innerPadding 作为 Column 的内边距
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("注册") })
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

                // 账号输入框
                TextField(
                    value = account.value,
                    onValueChange = { account.value = it },
                    label = { Text("Account") },
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

                // 注册按钮
                Button(
                    onClick = {
                        /*实现注册逻辑*/
                        val db = AppDatabase.getInstance(context)
                        val userDao = db.userDao()

                        com.lth.chat.dao.User(Math.random().toInt(),account.value,username.value,password.value).let {
                            userDao.insert(it)
                        }

                        Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show()

                        // 清空输入框内容
                        username.value = ""
                        account.value = ""
                        password.value = ""

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text("SignUp")
                }
                CustomTextWithUnderline {
                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                }
            }
        }
    }
    @Composable
    fun CustomTextWithUnderline(onClick: () -> Unit) {
        Text(
            text = AnnotatedString("登录"),
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 4.dp), // 根据需要添加内边距
            style = TextStyle(
                color = androidx.compose.ui.graphics.Color.Black, // 文本颜色
                textDecoration = TextDecoration.Underline // 下划线
            )
        )
    }
}
