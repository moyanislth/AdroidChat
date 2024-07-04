package com.lth.chat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lth.chat.R
import com.lth.chat.dao.Message
import java.util.Date

@Composable
fun MessageCard(message: Message, isSender: Boolean) {
    if (!isSender){
        ReceiveMessageCard(message)
    }else{
        SendMessageCard(message)
    }
}

@Composable
fun SendMessageCard(message: Message) {
    Surface(
        modifier = Modifier
            .padding(4.dp, 1.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){

            Column {
                // 昵称
                Text(text = message.receiverId, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(0.dp, 10.dp, 10.dp, 6.dp)
                        .align(Alignment.End)
                )
                // 聊天框
                Box (
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 10.dp, 10.dp)
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
                    .padding(1.dp, 10.dp)
                    .size(46.dp)
                    .clip(CircleShape),
            )
        }
    }
}

@Composable
fun ReceiveMessageCard(message: Message) {
    Surface(
        modifier = Modifier
            .padding(4.dp, 1.dp)
    ) {
        Row {
            // 头像
            Image(
                painterResource(id = R.drawable.tx1),
                contentDescription = null,
                modifier = Modifier
                    .padding(1.dp, 10.dp)
                    .size(46.dp)
                    .clip(CircleShape),
            )
            Column {
                // 昵称
                Text(text = message.receiverId, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(0.dp, 10.dp, 10.dp, 6.dp)
                )
                // 聊天框
                Box (
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 10.dp, 10.dp)
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
