//
//  LegacyViewController.h
//  iosApp
//
//  Demonstrates how to integrate KMP into a legacy Objective-C view controller
//  Flow: Objective-C View Controller -> UIHostingController -> SwiftUI -> KMP ViewModel -> KMP Service
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

/**
 * Legacy Objective-C View Controller that hosts a SwiftUI view
 * This demonstrates the bridge between old Objective-C code and modern KMP architecture
 */
@interface LegacyViewController : UIViewController

@end

NS_ASSUME_NONNULL_END

