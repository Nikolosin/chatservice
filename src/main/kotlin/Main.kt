package ru.netology

data class User(val id: Int)

data class Message(
    val id: Int,
    val sender: User,
    val recipient: User,
    var content: String,
    var isRead: Boolean = false
)

data class Chat(
    val id: Int,
    val user1: User,
    val user2: User,
    val messages: MutableList<Message>,
    var unreadCount: Int = 0
)

class ChatService {
    val chats: MutableMap<Int, Chat> = mutableMapOf()
    private var nextMessageId = 0
    private var nextChatId = 0

    fun createChat(user1: User, user2: User, text: String): Chat {
        val chatId = ++nextChatId
        val chat = Chat(chatId, user1, user2, mutableListOf())
        chats[chatId] = chat
        sendMessage(chatId, user1, user2, text)
        return chat
    }

    fun getChats(userId: Int): List<Chat> {
        return chats.values.filter { it.user1.id == userId || it.user2.id == userId }
    }

    fun getUnreadChatsCount(userId: Int): Int {
        return getChats(userId).count { it.unreadCount > 0 }
    }

    fun getMessages(chatId: Int, recipientId: Int, count: Int): List<Message> {
        val chat = chats[chatId] ?: return emptyList()
        // Mark messages as read
        chat.messages.filter { !it.isRead && (it.recipient.id == recipientId) }.forEach { it.isRead = true }
        chat.unreadCount = chat.messages.count { !it.isRead && (it.recipient.id == recipientId) }
        return chat.messages.takeLast(count)
    }

    fun getLastMessages(userId: Int): List<String> {
        val lastMessages = mutableListOf<String>()
        for (chat in getChats(userId)) {
            if (chat.messages.isNotEmpty()) {
                lastMessages.add(chat.messages.last().content)
            } else {
                lastMessages.add("нет сообщений")
            }
        }
        return lastMessages
    }

    fun sendMessage(chatId: Int, sender: User, recipient: User, messageContent: String): Message {
        val chat = chats[chatId] ?: throw IllegalArgumentException("чат не найден")
        val message = Message(
            ++nextMessageId,
            sender,
            recipient,
            messageContent
        )
        chat.messages.add(message)
        chat.unreadCount++
        return message
    }

    fun editMessage(messageId: Int, newContent: String): Boolean {
        val chat = chats.values.find { it.messages.any { message -> message.id == messageId } }
        if (chat != null) {
            val message = chat.messages.find { it.id == messageId }
            if (message != null) {
                message.content = newContent
                return true
            }
        }
        return false
    }

    fun deleteMessage(messageId: Int): Boolean {
        val chat = chats.values.find { it.messages.any { message -> message.id == messageId } }
        if (chat != null) {
            chat.messages.removeAll { it.id == messageId }
            return true
        }
        return false
    }

    fun deleteChat(chatId: Int): Boolean {
        return chats.remove(chatId) != null
    }
}

fun main() {
}