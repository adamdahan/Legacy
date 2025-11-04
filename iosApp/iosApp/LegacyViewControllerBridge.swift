import SwiftUI
import UIKit

/**
 * SwiftUI wrapper for the Objective-C LegacyViewController
 * This allows us to present the Objective-C view controller in SwiftUI
 */
struct LegacyViewControllerBridge: UIViewControllerRepresentable {
    
    func makeUIViewController(context: Context) -> LegacyViewController {
        let viewController = LegacyViewController()
        return viewController
    }
    
    func updateUIViewController(_ uiViewController: LegacyViewController, context: Context) {
        // No updates needed for this demo
    }
}

/**
 * Helper view to present the LegacyViewController in a NavigationView
 */
struct LegacyViewWrapper: View {
    var body: some View {
        LegacyViewControllerBridge()
            .edgesIgnoringSafeArea(.all)
    }
}

