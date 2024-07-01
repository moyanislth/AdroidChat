package com.lth.chat.dao

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

@Database(entities = [User::class, Message::class, Friend::class, GroupChat::class], version = 1 , exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun friendDao(): FriendDao
    abstract fun groupChatDao(): GroupChatDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app"
                ).allowMainThreadQueries() .build()
                INSTANCE = instance
                instance
            }
        }
    }

}

// 用户表
@Entity(tableName = "user")
data class User(
    // 主键id，自增
    @PrimaryKey(autoGenerate = true) val id: Int,
    // 昵称和账号唯一
    @ColumnInfo(name = "account", collate = ColumnInfo.BINARY) val account: String,
    @ColumnInfo(name = "username", collate = ColumnInfo.BINARY) val username: String,
    // 密码
    val password: String,
)

// 消息表
@Entity(tableName = "message", foreignKeys = [
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["senderId"]),
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["receiverId"])
])
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(index = true)
    val senderId: String,

    @ColumnInfo(index = true)
    val receiverId: String,
    val content: String,
    // 时间戳
    val timestamp: Date
)
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}


// 好友表
@Entity(tableName = "friends",foreignKeys = [
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId1"]),
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId2"])
])
data class Friend(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(index = true)
    val userId1: Int,

    @ColumnInfo(index = true)
    val userId2: Int
    // 其他好友属性...
)

// 关联表，表示用户和群聊之间的关系
@Entity(tableName = "group_chat_users")
data class GroupChatUser(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val groupChatId: Int,
    val userId: Int
    // 其他属性...
)

// 群聊表
@Entity(tableName = "group_chats")
data class GroupChat(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val groupName: String,
    // 其他群聊属性...
)

// 数据访问对象示例
@Dao
interface UserDao {
    // 插入用户，这里应该是一个抽象方法，不应该有方法体
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    // 查询全部用户，返回 User 对象列表
    @Query("SELECT * FROM user")
    fun queryAll(): List<User>

    // 查询用户，根据 account 查询用户表，返回 User 对象
    @Query("SELECT * FROM user WHERE account = :account")
    fun queryByAccount(account: String): User?

    // 查询用户，根据 username 查询用户表，返回 User 对象
    @Query("SELECT * FROM user WHERE username = :username")
    fun queryByUsername(username: String): User?

    // 删除用户，根据 account 删除用户表中的用户
    @Query("DELETE FROM user WHERE account = :account")
    fun deleteByAccount(account: String)

    // 更新用户，根据 account 更新用户表中的用户
    @Query("UPDATE user SET username = :username WHERE account = :account")
    fun updateUsername(account: String, username: String)

    // 更改密码
    @Query("UPDATE user SET password = :password WHERE account = :account")
    fun updatePassword(account: String, password: String)

    // 用户相关的数据库操作...
}

@Dao
interface MessageDao {
    // 新增消息
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message)

    // 查询一对一消息
    @Query("SELECT * FROM message WHERE senderId = :senderId AND receiverId = :receiverId ORDER BY timestamp ASC")
    fun queryBySenderAndReceiver(senderId: String, receiverId: String): List<Message>

    // 查询群聊消息
    @Query("SELECT * FROM message WHERE receiverId = :groupId")
    fun queryByGroupChat(groupId: String): List<Message>

    // 删除消息
    @Query("DELETE FROM message WHERE id = :id")
    fun deleteById(id: Int)

    // 消息相关的数据库操作...
}

@Dao
interface FriendDao {
    // 增加好友，如果已存在相同userId1和userId2的记录，则替换
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(friend: Friend)

    // 查询特定用户的好友列表
    @Query("SELECT * FROM friends WHERE userId1 = :userId or userId2 = :userId")
    fun queryByUserId(userId: Int): List<Friend>

    // 删除特定用户的所有的好友关系
    @Query("DELETE FROM friends WHERE userId1 = :userId or userId2 = :userId")
    fun deleteByUserId(userId: Int)

    // 删除特定的好友关系，即两个特定用户之间的好友关系
    @Query("DELETE FROM friends WHERE (userId1 = :userId1 AND userId2 = :userId2)or(userId1 = :userId2 AND userId2 = :userId1)")
    fun deleteFriend(userId1: Int, userId2: Int)

    // 检查两个用户是否已经是好友
    @Query("SELECT EXISTS(SELECT 1 FROM friends WHERE (userId1 = :userId1 AND userId2 = :userId2) OR (userId1 = :userId2 AND userId2 = :userId1))")
    fun areFriends(userId1: Int, userId2: Int): Boolean
    // 好友相关的数据库操作...
}

@Dao
interface GroupChatDao {
    // 增加群聊
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insert(groupChat: GroupChat)

    // 查询全部群聊
    @Query("SELECT * FROM group_chats")
    fun queryAll(): List<GroupChat>

    // 根据用户ID列表查询用户所在的群聊
//    @Query("SELECT * FROM group_chats WHERE id IN (SELECT groupChatId FROM group_chat_users WHERE userId = :userId)")
//    fun queryByUserId(userId: Int): List<GroupChat>

    // 删除群聊
    @Query("DELETE FROM group_chats WHERE id = :id")
    fun deleteById(id: Int)

    // 群聊相关的数据库操作...
}

interface ClearAllTables {
    // 清空所有表
    @Query("DELETE FROM user")
    fun clearUser()

    @Query("DELETE FROM message")
    fun clearMessage()

    @Query("DELETE FROM friends")
   fun clearFriends()

    @Query("DELETE FROM group_chats")
    fun clearGroupChats()
}

