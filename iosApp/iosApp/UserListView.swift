import SwiftUI
import shared

/**
 * SwiftUI view that displays a list of users from the KMP ViewModel
 * This view bridges SwiftUI with the Kotlin Multiplatform code
 */
struct UserListView: View {
    // KMP ViewModel instance
    let viewModel: UserViewModel
    
    // State to trigger UI updates
    @State private var users: [User] = []
    
    init(viewModel: UserViewModel) {
        self.viewModel = viewModel
    }
    
    var body: some View {
        NavigationView {
            List {
                ForEach(users, id: \.id) { user in
                    UserRow(user: user)
                }
            }
            .navigationTitle("Users")
            .navigationBarTitleDisplayMode(.large)
        }
        .onAppear {
            loadUsers()
        }
    }
    
    private func loadUsers() {
        // Call KMP ViewModel to load users
        viewModel.loadUsers()
        
        // Convert KMP list to Swift array
        let count = Int(viewModel.getUserCount())
        var userArray: [User] = []
        
        for i in 0..<count {
            if let user = viewModel.getUserAt(index: Int32(i)) {
                userArray.append(user)
            }
        }
        
        users = userArray
    }
}

/**
 * Row view for displaying individual user information
 */
struct UserRow: View {
    let user: User
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(user.name)
                .font(.headline)
                .foregroundColor(.primary)
            
            Text(user.email)
                .font(.subheadline)
                .foregroundColor(.secondary)
        }
        .padding(.vertical, 4)
    }
}

// Preview provider for SwiftUI previews
struct UserListView_Previews: PreviewProvider {
    static var previews: some View {
        UserListView(viewModel: UserViewModel(userService: UserService()))
    }
}

