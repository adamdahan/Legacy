package com.adamdahan.cibc.legacy

/**
 * Service layer that provides mock data
 * In a real app, this would fetch from a network or database
 */
class UserService {
    
    fun getUsers(): List<User> {
        return listOf(
            User(1, "John Doe", "john.doe@example.com"),
            User(2, "Jane Smith", "jane.smith@example.com"),
            User(3, "Bob Johnson", "bob.johnson@example.com"),
            User(4, "Alice Williams", "alice.williams@example.com"),
            User(5, "Charlie Brown", "charlie.brown@example.com")
        )
    }
    
    fun getUserById(id: Int): User? {
        return getUsers().find { it.id == id }
    }
}

