import SwiftUI
import UIKit
import shared

/**
 * Swift wrapper that creates and manages a UIHostingController with UserListView
 * This class is @objc compatible and can be used from Objective-C
 */
@objc class UserListViewControllerWrapper: NSObject {
    
    private let viewModel: UserViewModel
    private var hostingController: UIViewController?
    
    @objc init(viewModel: UserViewModel) {
        self.viewModel = viewModel
        super.init()
    }
    
    /**
     * Creates and returns a UIViewController that hosts the SwiftUI UserListView
     * This method is callable from Objective-C
     */
    @objc func createViewController() -> UIViewController {
        let swiftUIView = UserListView(viewModel: viewModel)
        let hosting = UIHostingController(rootView: swiftUIView)
        self.hostingController = hosting
        return hosting
    }
}

