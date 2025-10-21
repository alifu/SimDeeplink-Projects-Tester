//
//  DeeplinkHandler.swift
//  iOS
//
//  Created by Alif on 21/10/25.
//

import Foundation
import Observation

struct AlertData: Identifiable {
    let id = UUID()
    let title: String
    let message: String
}

@Observable
class DeeplinkHandler {
    var alertData: AlertData?

    func handle(url: URL) {
        guard url.scheme == "sdlmobile", url.host == "show_alert" else { return }

        let components = URLComponents(url: url, resolvingAgainstBaseURL: false)
        let queryItems = components?.queryItems

        let title = queryItems?.first(where: { $0.name == "title" })?.value ?? "Alert"
        let message = queryItems?.first(where: { $0.name == "message" })?.value ?? "Default message from deep link."

        alertData = AlertData(title: title, message: message)
    }
}

