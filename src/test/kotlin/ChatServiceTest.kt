import org.junit.Assert.*
import ru.netology.ChatService
import ru.netology.User

class ChatServiceTest {

    @org.junit.Test
    fun createChat() {
        val chatService = ChatService()
        val countChats = chatService.chats.size
        val user1 = User(1)
        val user2 = User(2)
        chatService.createChat(user1, user2, "hello")
        assertEquals(countChats + 1, chatService.chats.size)


    }

    @org.junit.Test
    fun getChats() {
        val chatService = ChatService()
        val user1 = User(1)
        val user2 = User(2)
        val user3 = User(3)
        chatService.createChat(user1, user2, "hello")
        chatService.createChat(user2, user3, "hello")
        chatService.createChat(user1, user3, "hello")
        val chats = chatService.getChats(2)
        assertEquals(2, chats.size)
    }

    @org.junit.Test
    fun getUnreadChatsCount() {
        val chatService = ChatService()
        val user1 = User(1)
        val user2 = User(2)
        chatService.createChat(user1, user2, "hello")
        assertEquals(1, chatService.chats[1]?.unreadCount)
    }

    @org.junit.Test
    fun getUnreadChatsCountNull() {
        val chatService = ChatService()
        val user1 = User(1)
        val user2 = User(2)
        //chatService.createChat(user1, user2, "hello" )
        assertEquals(null, chatService.chats[1]?.unreadCount)
    }

    @org.junit.Test
    fun getMessages() {
        val chatService = ChatService()
        val user1 = User(1)
        val user2 = User(2)
        val chat = chatService.createChat(user1, user2, "Message 1")
        chatService.sendMessage(chat.id, user1, user2, "Message 2")
        chatService.sendMessage(chat.id, user2, user1, "Message 3")
        val messages = chatService.getMessages(chat.id, user2.id, 2)
        assertEquals(2, messages.size)
    }

    @org.junit.Test
    fun getLastMessages() {
        val chatService = ChatService()
        val user1 = User(1)
        val user2 = User(2)
        val chat = chatService.createChat(user1, user2, "Message 1")
        chatService.sendMessage(chat.id, user1, user2, "Message 2")
        chatService.sendMessage(chat.id, user2, user1, "Message 3")
        val messages = chatService.getMessages(chat.id, user2.id, 2)
        assertEquals(2, messages.size)
    }

    @org.junit.Test
    fun sendMessage() {
        val chatService = ChatService()
        val user1 = User(1)
        val user2 = User(2)
        val chat = chatService.createChat(user1, user2, "Hello")
        val message = chatService.sendMessage(chat.id, user1, user2, "Hi")
        assertEquals("Hi", message.content)
    }

    @org.junit.Test
    fun editMessage() {
        val chatService = ChatService()
        val user1 = User(1)
        val user2 = User(2)
        val chat = chatService.createChat(user1, user2, "Hello")
        val message = chatService.sendMessage(chat.id, user1, user2, "Original message")
        assertTrue(chatService.editMessage(message.id, "Edited message"))
    }

    @org.junit.Test
    fun deleteMessage() {
        val chatService = ChatService()
        val user1 = User(1)
        val user2 = User(2)
        val chat = chatService.createChat(user1, user2, "Hello")
        val message = chatService.sendMessage(chat.id, user1, user2, "Message to delete")
        assertTrue(chatService.deleteMessage(message.id))
        assertEquals(1, chat.messages.size)
    }

    @org.junit.Test
    fun deleteChat() {
        val chatService = ChatService()
        val user1 = User(1)
        val user2 = User(2)
        val chat = chatService.createChat(user1, user2, "Hello")
        assertTrue(chatService.deleteChat(chat.id))
        assertFalse(chatService.chats.containsKey(chat.id))
    }

}