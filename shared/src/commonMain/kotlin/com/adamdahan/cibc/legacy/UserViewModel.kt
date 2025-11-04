package com.adamdahan.cibc.legacy

/**
 * ViewModel that manages the business logic and state
 * This is exposed to iOS via the shared framework
 */
class UserViewModel(
    private val userService: UserService = UserService()
) {
    
    private var _users: List<User> = emptyList()
    
    val users: List<User>
        get() = _users
    
    /**
     * Load users from the service
     * In a real app, this would be suspend/async
     */
    fun loadUsers() {
        _users = userService.getUsers()
    }
    
    /**
     * Get the count of users
     */
    fun getUserCount(): Int {
        return _users.size
    }
    
    /**
     * Get a user at a specific index
     */
    fun getUserAt(index: Int): User? {
        return if (index >= 0 && index < _users.size) {
            _users[index]
        } else {
            null
        }
    }
}

