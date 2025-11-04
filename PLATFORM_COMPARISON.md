# iOS vs Android: KMP Integration Comparison

This document provides a side-by-side comparison of how KMP is integrated into legacy applications on both platforms.

## 📊 Side-by-Side Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          PLATFORM COMPARISON                             │
├──────────────────────────────────┬──────────────────────────────────────┤
│              iOS                 │            ANDROID                    │
├──────────────────────────────────┼──────────────────────────────────────┤
│                                                                           │
│  Objective-C ViewController      │     XML Layout Activity              │
│  (LegacyViewController.m)        │     (activity_legacy.xml)            │
│          ↓                       │            ↓                          │
│                                  │                                       │
│  Swift Wrapper                   │     ComposeView                       │
│  (UserListViewControllerWrapper) │     (in XML layout)                   │
│          ↓                       │            ↓                          │
│                                  │                                       │
│  UIHostingController             │     Jetpack Compose                   │
│  (Apple Framework)               │     (setContent)                      │
│          ↓                       │            ↓                          │
│                                  │                                       │
│  SwiftUI View                    │     Composable Function               │
│  (UserListView.swift)            │     (UserListComposable.kt)           │
│          ↓                       │            ↓                          │
│                                  │                                       │
│ ┌──────────────────────────────────────────────────────────────────┐   │
│ │              SHARED KOTLIN MULTIPLATFORM LAYER                   │   │
│ │                                                                   │   │
│ │  UserViewModel.kt  → UserService.kt  → User.kt                   │   │
│ │  (Business Logic)    (Data Layer)      (Model)                   │   │
│ └──────────────────────────────────────────────────────────────────┘   │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘
```

## 🔍 Detailed Comparison

### 1. Legacy Entry Point

#### iOS: Objective-C ViewController
```objc
// LegacyViewController.m
@implementation LegacyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Initialize KMP ViewModel
    SharedUserService *service = [[SharedUserService alloc] init];
    self.viewModel = [[SharedUserViewModel alloc] initWithUserService:service];
    
    // Create Swift wrapper for SwiftUI
    self.wrapper = [[UserListViewControllerWrapper alloc] 
                    initWithViewModel:self.viewModel];
    
    // Get UIViewController from wrapper
    self.hostingController = [self.wrapper createViewController];
    
    // Add as child view controller
    [self addChildViewController:self.hostingController];
}

@end
```

#### Android: XML Layout Activity
```kotlin
// LegacyActivity.kt
class LegacyActivity : ComponentActivity() {
    
    private lateinit var viewModel: UserViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set XML layout
        setContentView(R.layout.activity_legacy)
        
        // Initialize KMP ViewModel
        val userService = UserService()
        viewModel = UserViewModel(userService)
        
        // Setup Compose in ComposeView
        val composeView = findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            MyApplicationTheme {
                UserListScreen(viewModel = viewModel)
            }
        }
    }
}
```

### 2. XML/Layout Files

#### iOS: No XML (programmatic)
```objc
// All done in code, no XIB or Storyboard needed
```

#### Android: XML Layout
```xml
<!-- activity_legacy.xml -->
<LinearLayout>
    <TextView
        android:text="Legacy XML Activity"
        android:background="#6200EE" />
    
    <androidx.compose.ui.platform.ComposeView
        android:id="@+id/composeView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

### 3. Bridge Layer

#### iOS: Swift Wrapper Class
```swift
// UserListViewControllerWrapper.swift
@objc class UserListViewControllerWrapper: NSObject {
    
    private let viewModel: UserViewModel
    
    @objc init(viewModel: UserViewModel) {
        self.viewModel = viewModel
    }
    
    @objc func createViewController() -> UIViewController {
        let swiftUIView = UserListView(viewModel: viewModel)
        return UIHostingController(rootView: swiftUIView)
    }
}
```

**Why needed?** Objective-C cannot directly instantiate Swift generic types like `UIHostingController<Content>`.

#### Android: Direct Integration
```kotlin
// No wrapper needed!
composeView.setContent {
    UserListScreen(viewModel = viewModel)
}
```

**Why simpler?** Both the Activity and Compose are in Kotlin, no language boundary.

### 4. Modern UI Layer

#### iOS: SwiftUI
```swift
// UserListView.swift
struct UserListView: View {
    let viewModel: UserViewModel
    @State private var users: [User] = []
    
    var body: some View {
        NavigationView {
            List {
                ForEach(users, id: \.id) { user in
                    UserRow(user: user)
                }
            }
            .navigationTitle("Users")
        }
        .onAppear {
            loadUsers()
        }
    }
}
```

#### Android: Jetpack Compose
```kotlin
// UserListComposable.kt
@Composable
fun UserListScreen(viewModel: UserViewModel) {
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
        users = (0 until viewModel.getUserCount()).mapNotNull {
            viewModel.getUserAt(it)
        }
    }
    
    Scaffold(
        topBar = { TopAppBar(title = { Text("Users") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(users) { user ->
                UserListItem(user = user)
            }
        }
    }
}
```

### 5. KMP Access

#### iOS: Shared Prefix in Objective-C
```objc
// Objective-C code
SharedUserService *service = [[SharedUserService alloc] init];
SharedUserViewModel *viewModel = [[SharedUserViewModel alloc] 
                                   initWithUserService:service];
```

```swift
// Swift code (no prefix)
let service = UserService()
let viewModel = UserViewModel(userService: service)
```

#### Android: Direct Access
```kotlin
// Kotlin code (same module, no prefix)
val service = UserService()
val viewModel = UserViewModel(service)
```

## 📋 Feature Comparison Table

| Feature | iOS | Android |
|---------|-----|---------|
| **Language Interop** | Objective-C ↔ Swift | Kotlin ↔ Kotlin |
| **Bridge Complexity** | High (needs wrapper) | Low (direct) |
| **Layout System** | Programmatic | XML + Code |
| **KMP Naming** | `Shared` prefix in ObjC | Direct access |
| **Build System** | CocoaPods + Xcode | Gradle |
| **Modern UI Framework** | SwiftUI | Jetpack Compose |
| **State Management** | @State, @ObservedObject | remember, mutableStateOf |
| **Navigation** | NavigationView | Scaffold + NavController |
| **Bridging Header** | Required | Not needed |

## 🔧 Configuration Comparison

### iOS

#### Podfile
```ruby
target 'iosApp' do
  use_frameworks!
  platform :ios, '16.0'
  
  pod 'shared', :path => '../shared'
end
```

#### Bridging Header
```objc
// iosApp-Bridging-Header.h
#import "LegacyViewController.h"
```

#### Xcode Build Settings
- **Objective-C Bridging Header**: `iosApp/iosApp-Bridging-Header.h`
- **Enable Modules**: Yes
- **Swift Version**: 5.0

### Android

#### build.gradle.kts
```kotlin
android {
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
}
```

#### AndroidManifest.xml
```xml
<application>
    <activity
        android:name=".LegacyActivity"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
</application>
```

## 🎯 Complexity Analysis

### iOS: Higher Initial Complexity
- **Why**: Language boundary (Objective-C ↔ Swift)
- **Solution**: Swift wrapper classes
- **Trade-off**: More boilerplate, but clearer separation

### Android: Lower Initial Complexity
- **Why**: Single language (Kotlin)
- **Solution**: Direct integration
- **Trade-off**: Simpler, but less explicit about boundaries

## 💡 Key Takeaways

### iOS
✅ **Pros:**
- Clear separation of concerns
- Works with legacy Objective-C
- Type-safe bridge with @objc

❌ **Cons:**
- Requires wrapper classes
- Bridging header configuration
- `Shared` prefix in Objective-C

### Android
✅ **Pros:**
- Simpler integration
- No language boundary
- Direct KMP access
- Native Kotlin everywhere

❌ **Cons:**
- Less explicit about layers
- XML + Kotlin mixing

## 🚀 Performance Considerations

### iOS
- **Bridge Overhead**: Minimal (one-time wrapper creation)
- **SwiftUI Performance**: Native Apple framework
- **KMP Access**: JNI bridge (negligible overhead)

### Android
- **Bridge Overhead**: None (same runtime)
- **Compose Performance**: Native Jetpack framework
- **KMP Access**: Direct (zero overhead)

## 📈 Migration Path

### iOS: Gradual Migration
```
1. Objective-C ViewController (Legacy)
2. Add ComposeView for one screen
3. Gradually replace more screens
4. Eventually: Pure SwiftUI app
```

### Android: Gradual Migration
```
1. XML Layout Activity (Legacy)
2. Add ComposeView to XML
3. Gradually replace more screens
4. Eventually: Pure Compose app
```

## 🎓 Learning Curve

### iOS
- **Easy**: If you know Objective-C + Swift
- **Medium**: Understanding bridging and generics
- **Hard**: UIHostingController lifecycle management

### Android
- **Easy**: If you know Kotlin
- **Easy**: ComposeView integration
- **Easy**: Direct KMP access

---

## ✨ Summary

Both platforms successfully demonstrate:
- ✅ Legacy code integration
- ✅ Modern UI frameworks
- ✅ Shared business logic
- ✅ Production-ready patterns

**Choose iOS approach for**: Clear separation, Objective-C codebases  
**Choose Android approach for**: Simpler integration, Kotlin codebases

**Both work excellently!** 🎉

