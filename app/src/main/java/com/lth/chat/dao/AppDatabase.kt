package com.lth.chat.dao

import androidx.room.Dao
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Database(entities = [User::class, Message::class, Friend::class, GroupChat::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun friendDao(): FriendDao
    abstract fun groupChatDao(): GroupChatDao

    // 其他数据库操作...

}

// 用户表
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val username: String,
    // 其他用户属性...
)

// 消息表
@Entity(tableName = "messages", foreignKeys = [
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["senderId"]),
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["receiverId"])
])
data class Message(
    @PrimaryKey val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    // 消息时间戳
    val timestamp: Long,
    // 其他消息属性...
)

// 好友表
@Entity(tableName = "friends")
data class Friend(
    @PrimaryKey val id: String,
    val userId: String,
    val friendId: String,
    // 默认值为 "pending"
    val status: String, // 例如："pending", "accepted", "blocked"
    // 其他好友属性...
)

// 群聊表
@Entity(tableName = "group_chats")
data class GroupChat(
    @PrimaryKey val id: String,
    val groupName: String,
    // 其他群聊属性...
)

// 数据访问对象示例
@Dao
interface UserDao {
    // 用户相关的数据库操作...
}

@Dao
interface MessageDao {
    // 消息相关的数据库操作...
}

@Dao
interface FriendDao {
    // 好友相关的数据库操作...
}

@Dao
interface GroupChatDao {
    // 群聊相关的数据库操作...
}

// 日期转换器
class DateConverters {
    // 将日期转换为字符串和从字符串转换回日期的逻辑...

}