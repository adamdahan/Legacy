//
//  LegacyViewController.m
//  iosApp
//
//  Demonstrates how to integrate KMP into a legacy Objective-C view controller
//

#import "LegacyViewController.h"
@import SwiftUI;
#import "iosApp-Swift.h"  // Import the Swift bridging header
@import shared;            // Import the KMP shared module

@interface LegacyViewController ()

@property (nonatomic, strong) UIViewController *hostingController;
@property (nonatomic, strong) SharedUserViewModel *viewModel;
@property (nonatomic, strong) UserListViewControllerWrapper *wrapper;

@end

@implementation LegacyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Set background color
    self.view.backgroundColor = [UIColor systemBackgroundColor];
    
    // Setup navigation bar appearance (optional)
    self.title = @"Legacy ObjC Demo";
    
    // Initialize the KMP ViewModel
    [self setupViewModel];
    
    // Create and embed the SwiftUI view via UIHostingController
    [self setupSwiftUIHosting];
}

/**
 * Initialize the KMP ViewModel
 * This is the entry point to the Kotlin Multiplatform code
 */
- (void)setupViewModel {
    // Create an instance of the KMP ViewModel
    SharedUserService *service = [[SharedUserService alloc] init];
    self.viewModel = [[SharedUserViewModel alloc] initWithUserService:service];
    
    NSLog(@"✅ KMP ViewModel initialized from Objective-C");
}

/**
 * Create UIHostingController and embed the SwiftUI view
 * This bridges Objective-C with SwiftUI
 */
- (void)setupSwiftUIHosting {
    // Create a wrapper that will create the SwiftUI view with UIHostingController
    self.wrapper = [[UserListViewControllerWrapper alloc] initWithViewModel:self.viewModel];
    
    // Get the view controller from the wrapper
    self.hostingController = [self.wrapper createViewController];
    
    // Add as a child view controller
    [self addChildViewController:self.hostingController];
    
    // Add the hosting controller's view to our view hierarchy
    UIView *hostingView = self.hostingController.view;
    hostingView.translatesAutoresizingMaskIntoConstraints = NO;
    [self.view addSubview:hostingView];
    
    // Setup Auto Layout constraints to fill the parent view
    [NSLayoutConstraint activateConstraints:@[
        [hostingView.topAnchor constraintEqualToAnchor:self.view.topAnchor],
        [hostingView.bottomAnchor constraintEqualToAnchor:self.view.bottomAnchor],
        [hostingView.leadingAnchor constraintEqualToAnchor:self.view.leadingAnchor],
        [hostingView.trailingAnchor constraintEqualToAnchor:self.view.trailingAnchor]
    ]];
    
    // Complete the child view controller addition
    [self.hostingController didMoveToParentViewController:self];
    
    NSLog(@"✅ SwiftUI view hosted in Objective-C UIViewController");
}

- (void)dealloc {
    NSLog(@"🗑️ LegacyViewController deallocated");
}

@end

