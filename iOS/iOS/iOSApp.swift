//
//  iOSApp.swift
//  iOS
//
//  Created by Alif on 21/10/25.
//

import SwiftUI

@main
struct iOSApp: App {
    @State private var deepLinkHandler = DeeplinkHandler()
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    deepLinkHandler.handle(url: url)
                }
                .alert(item: $deepLinkHandler.alertData) { alert in
                    Alert(
                        title: Text(alert.title),
                        message: Text(alert.message),
                        dismissButton: .default(Text("OK"))
                    )
                }
        }
    }
}
