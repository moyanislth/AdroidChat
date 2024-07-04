package com.lth.chat.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lth.chat.R
import com.lth.chat.dao.AppDatabase
import com.lth.chat.dao.Message
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Composable
fun MessageCard(message: Message, isSender: Boolean,context: android.content.Context) {
    val db = AppDatabase.getInstance(context)

    Box(
        modifier = Modifier
            .background(Color.White) // 示例背景色
            .padding(0.dp) // 示例内边距
            .longClick{
                Log.d("MessageCard", "长按")
                deleteMessage(db,message)
            }
    ) {
        if (!isSender){
            ReceiveMessageCard(message)
        }else{
            SendMessageCard(message)
        }
    }

}

@OptIn(DelicateCoroutinesApi::class)
fun deleteMessage(db: AppDatabase, message: Message) {
    GlobalScope.launch {
        db.messageDao().deleteById(message.id)
    }
}


/**
 * 长按事件
 */
fun Modifier.longClick(onLongClick: (Offset) -> Unit): Modifier =
    pointerInput(this) {
        detectTapGestures(
            onLongPress = onLongClick
        )
    }

@Composable
fun SendMessageCard(message: Message) {
    Surface(
        modifier = Modifier
            .padding(0.dp, 0.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){

            Column {
                // 昵称
                Text(text = message.senderId, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(0.dp, 8.dp, 4.dp, 6.dp)
                        .align(Alignment.End)
                )
                // 聊天框
                Box (
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 14.dp, 0.dp)
                        .clip(RoundedCornerShape(10.dp))
//                        .background(Color.Green)
                ){
                    Text(
                        text = message.content,
                        fontSize = 15.sp,
                        style = TextStyle(
                            color = Color.Black, // 文本颜色
                        ),
                        modifier = Modifier
                            .background(Color.Green)
                            .padding(start = 12.dp, top = 5.dp, end = 12.dp, bottom = 8.dp) // 增加内边距
                            .wrapContentSize(Alignment.TopStart) // 文本左上角对齐
                    )
                }
            }
            // 头像
            Image(
                painterResource(id = R.drawable.tx2),
                contentDescription = null,
                modifier = Modifier
                    .padding(1.dp, 12.dp)
                    .size(44.dp)
                    .clip(CircleShape),
            )
        }
    }
}

@Composable
fun ReceiveMessageCard(message: Message) {
    Surface(
        modifier = Modifier
            .padding(0.dp, 0.dp)
    ) {
        Row {
            // 头像
            Image(
                painterResource(id = R.drawable.tx1),
                contentDescription = null,
                modifier = Modifier
                    .padding(1.dp, 12.dp)
                    .size(44.dp)
                    .clip(CircleShape),
            )
            Column {
                // 昵称
                Text(text = message.senderId, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(4.dp, 8.dp, 4.dp, 6.dp)
                )
                // 聊天框
                Box (
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 14.dp, 0.dp)
                        .clip(RoundedCornerShape(10.dp))
//                        .background(Color.Green)
                ){
                    Text(
                        text = message.content,
                        fontSize = 15.sp,
                        style = TextStyle(
                            color = Color.Black, // 文本颜色
                        ),
                        modifier = Modifier
                            .background(Color.Green)
                            .padding(start = 12.dp, top = 5.dp, end = 12.dp, bottom = 8.dp) // 增加内边距
                            .wrapContentSize(Alignment.TopStart) // 文本左上角对齐
                    )
                }
            }
        }
    }
}
