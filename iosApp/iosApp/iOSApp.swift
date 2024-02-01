import SwiftUI
import shared

@main
struct iOSApp: App {

    init() {
        DIKt.doInitDI()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}