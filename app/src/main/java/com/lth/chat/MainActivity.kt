package com.lth.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.lth.chat.dao.AppDatabase
import com.lth.chat.dao.Friend
import com.lth.chat.dao.User
import com.lth.chat.ui.theme.ChatTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    companion object {
        var isLoginActivityStarted = false
    }
    object UserSession {
        var userId: Int? = null
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
                TopAppBar(
                    title = { Text("Chat") },
                    modifier = Modifier
                        .background(Color.Gray)
                        .fillMaxWidth()
                        .height(40.dp) ,// 设置背景色
                )
            },
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
            },
            content = {
                // 主要内容区域
                // 根据当前选中项来决定展示哪个内容
                Column (
                    Modifier
                        .fillMaxSize()
                        .padding(1.dp)
//                        .background(Color.Gray)
                ){
                    Spacer(modifier = Modifier.height(40.dp))
                    when (selectedItem) {
                        0 -> MessageContent() // 消息内容
                        1 -> ContactsContent() // 联系人内容
                        2 -> MyContent() // 我的内容
                    }
                }

            }
        )
    }

    // 未完成，要求显示主页我的界面
    @Composable
    private fun MyContent() {
        Log.v("MyContent", "MyContent")

        val context = this
        val db = AppDatabase.getInstance(this)
        val user = db.userDao().queryByAccount(UserSession.userId.toString())

        Column {
            Text("我的信息")

            Spacer(modifier = Modifier.height(20.dp))

            if (user != null) {
                Text(text = "Id信息:" + user.id, fontSize = 24.sp)
            }
            if (user != null) {
                Text(text = "账号信息:" + user.account, fontSize = 24.sp)
            }
            if (user != null) {
                Text(text = "用户名" + user.username, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.height(200.dp))

            Button(onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }) {
                Text("退出登录")
            }
        }
    }

    // 未完成，要求显示主页联系人界面
    @Composable
    private fun ContactsContent() {
        // 从数据库中获取用户信息
        val db = AppDatabase.getInstance(this)

        Column {
            SearchBar { search ->
                // 搜索好友
                Log.v("SearchFriendBar", search)
            }

            for (f in db.friendDao().queryByUserId(UserSession.userId!!)) {
                var friendId = if(UserSession.userId == f.userId1) f.userId2 else f.userId1

                ContactsItem(friendId)

            }
        }
        
    }
    @Composable
    fun ContactsItem(friendId: Int) {
        val db = AppDatabase.getInstance(this)
        val userDao = db.userDao()
        val friendDao = db.friendDao()
        val context = this

        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface) // 使用主题颜色
                .clickable {
                    friendDao.deleteFriend(friendId.toInt(), UserSession.userId!!)
                }
                .padding(horizontal = 16.dp, vertical = 8.dp), // 内边距
        ) {
            // 联系人信息
            Column(
                modifier = Modifier
                    .weight(0.8f) // 占据剩余空间
                    .padding(start = 16.dp) // 左侧间距
            ) {
                Text(
                    text = "${userDao.queryById(friendId.toInt())?.username}", // 这里应使用实际的联系人名称
                    style = MaterialTheme.typography.bodyLarge, // 使用主题中的字体样式
                    color = MaterialTheme.colorScheme.onSurface // 使用主题中的文本颜色
                )
            }
            // 聊天按钮
            Button(
                onClick = {
                    // 跳转到聊天界面
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra("friendId", friendId)
                    intent.putExtra("UserSession", UserSession.userId)
                    startActivity(intent)
                },
                shape = MaterialTheme.shapes.small, // 使用主题中定义的形状
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.primary // 使用主题中的主要颜色
                )
            ) {
                Text(text = "聊天")
            }
        }
    }


    // 未完成，要求显示主页消息界面
    @Composable
    private fun MessageContent() {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(text = "消息")
            ChatItem(friendId = "2") {

            }
        }
    }
    @Composable
    fun ChatItem(friendId: String, onChatClick: () -> Unit) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface) // 使用主题颜色
                .clickable { onChatClick() } // 点击事件
                .padding(horizontal = 16.dp, vertical = 8.dp), // 内边距
        ) {
            // 联系人信息
            Column(
                modifier = Modifier
                    .weight(1f) // 占据剩余空间
                    .padding(start = 16.dp) // 左侧间距
            ) {
                Text(
                    text = "联系人名称", // 这里应使用实际的联系人名称
                    style = MaterialTheme.typography.bodyLarge, // 使用主题中的字体样式
                    color = MaterialTheme.colorScheme.onSurface // 使用主题中的文本颜色
                )
                Text(
                    text = "最近的消息或状态...", // 显示最近的消息或状态
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // 稍微淡一些的文本颜色
                )
            }

            // 聊天按钮
            Button(
                onClick = { /*TODO*/ },
                shape = MaterialTheme.shapes.small, // 使用主题中定义的形状
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.primary // 使用主题中的主要颜色
                )
            ) {
                Text(text = "聊天")
            }
        }
    }

    // 启动Compose界面
    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun MainCompose() {
        rememberNavController()
        // 启动Compose界面
        HomePage()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchBar(onSearch: (String) -> Unit) {
        var text by remember { mutableStateOf("") } // 使用remember和mutableStateOf来存储文本值
        val searchResults = remember {
            mutableStateListOf<User>()
        }
        val scope = rememberCoroutineScope() // 记住协程作用域
        val context = this

        Box(modifier = Modifier
            .fillMaxWidth()
        ) {

            TextField(
                value = text, // 初始值，通常应该是空字符串
                onValueChange = { newValue ->
                    text = newValue // 更新text变量
                    onSearch(newValue) // 传递新的搜索值到回调函数
                    scope.launch {
                        performSearch(newValue, searchResults) // 执行搜索操作
                    }
                },
                modifier = Modifier // 给TextField添加修饰符
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp) // 水平内边距
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        RoundedCornerShape(1.dp)
                    ) // 边框
                    .padding(vertical = 1.dp), // 垂直内边距
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Red, // 聚焦时不显示指示器
                    unfocusedIndicatorColor = Color.Transparent // 未聚焦时不显示指示器
                ),
                placeholder = { Text("Search for a friend...") }, // 占位符文本
                singleLine = true // 单行
            )


            DropdownMenu(
                // 是否展开
                expanded = searchResults.isNotEmpty(),
                onDismissRequest = {
                    /* 处理下拉菜单的关闭逻辑 */
                    // 清空搜索文本框
                    text = ""
                    // 清空搜索结果列表
                    searchResults.clear()
                }
            ) {
                searchResults.forEach { result ->
                    DropdownMenuItem(
                        text = {
                            Text("添加联系人："+result.username)
                        },
                        onClick = {
                            val friendDao = AppDatabase.getInstance(context).friendDao()
                            // 处理选择结果的逻辑
                            if (!friendDao.areFriends(UserSession.userId!!, result.id)){
                                onSearchResultSelected(result)
                            }
                            // 清空搜索文本和结果
                            text = ""
                            searchResults.clear()
                        }
                    )
                }
            }
        }


    }

    private fun onSearchResultSelected(result: User) {
        // 处理选择结果的逻辑
        val db = AppDatabase.getInstance(this)
        val friendDao = db.friendDao()

        var friend = Friend(Math.random().toInt(), UserSession.userId!!, result.id)

        friendDao.insert(friend)
    }
    // 查询操作
    suspend fun performSearch(query: String, results: SnapshotStateList<User>) {
        // 异步执行数据库查询
        val db = AppDatabase.getInstance(this) // 确保这是在合适的线程调用
        val users = db.userDao().likelyQueryByAccount(query)// 模糊查询
        // 更新搜索结果状态
        withContext(Dispatchers.Main) {
            results.clear()
            results.addAll(users)
        }
    }

}
