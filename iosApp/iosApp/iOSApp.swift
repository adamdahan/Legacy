import SwiftUI

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
			// Using the Objective-C View Controller that hosts SwiftUI + KMP
			// Flow: ObjC ViewController -> UIHostingController -> SwiftUI -> KMP ViewModel -> KMP Service
			LegacyViewWrapper()
		}
	}
}