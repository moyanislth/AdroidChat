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

@Database(entities = [User::class, Message::class, Friend::class, GroupChat::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun friendDao(): FriendDao
    abstract fun groupChatDao(): GroupChatDao

    companion object {
        // 使用 volatile 确保线程安全
        @Volatile
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
    @PrimaryKey val id: Int,
    val account: String,
    val username: String,
    val password: String,
)

// 消息表
@Entity(tableName = "message", foreignKeys = [
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["senderId"]),
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["receiverId"])
])
data class Message(
    @PrimaryKey val id: Int,

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
        return date?.time?.toLong()
    }
}



// 好友表
@Entity(tableName = "friends",foreignKeys = [
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId1"]),
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId2"])
])
data class Friend(
    @PrimaryKey val id: Int,

    @ColumnInfo(index = true)
    val userId1: Int,

    @ColumnInfo(index = true)
    val userId2: Int
    // 其他好友属性...
)

// 关联表，表示用户和群聊之间的关系
@Entity(tableName = "group_chat_users")
data class GroupChatUser(
    @PrimaryKey val id: Int,
    val groupChatId: String,
    val userId: Int
    // 其他属性...
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
    // 插入用户，这里应该是一个抽象方法，不应该有方法体
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract  fun insert(user: User)

    // 查询全部用户，返回 User 对象列表
    @Query("SELECT * FROM user")
    abstract  fun queryAll(): List<User>

    // 查询用户，根据 account 查询用户表，返回 User 对象
    @Query("SELECT * FROM user WHERE account = :account")
    abstract  fun queryByAccount(account: String): User?

    // 查询用户，根据 username 查询用户表，返回 User 对象
    @Query("SELECT * FROM user WHERE username = :username")
    abstract  fun queryByUsername(username: String): User?

    // 删除用户，根据 account 删除用户表中的用户
    @Query("DELETE FROM user WHERE account = :account")
    abstract  fun deleteByAccount(account: String)

    // 更新用户，根据 account 更新用户表中的用户
    @Query("UPDATE user SET username = :username WHERE account = :account")
    abstract  fun updateUsername(account: String, username: String)

    // 用户相关的数据库操作...
}

@Dao
interface MessageDao {
    // 新增消息
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message)

    // 查询一对一消息
    @Query("SELECT * FROM message WHERE senderId = :senderId AND receiverId = :receiverId")
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
    // 增加好友
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insert(friend: Friend)

    // 查询好友
    @Query("SELECT * FROM friends WHERE userId1 = :userId or userId2= :userId")
    fun queryByUserId(userId: Int): Friend?

    // 删除好友
    @Query("DELETE FROM friends WHERE userId1 = :userId")
    fun deleteByUserId(userId: Int)

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
  //  @Query("SELECT gc.* FROM group_chats gc JOIN group_chat_users gcu ON gc.id = gcu.groupChatId WHERE gcu.userId IN (:userIds)")
   // fun queryGroupChatsByUserIds(userIds: List<Int>): List<GroupChat>

    // 群聊相关的数据库操作...
}

interface clearAllTables {
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

// 时间戳转换
fun Date.toTimestamp(): Long {
    return this.time
}