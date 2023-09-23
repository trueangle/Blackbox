//
//  iosMovieAppApp.swift
//  Shared
//
//  Created by Viacheslav Ivanovichev on 05.09.2023.
//

import UIKit
import sharedApp

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        
        if connectingSceneSession.role == UISceneSession.Role.windowApplication {
             let config = UISceneConfiguration(name: nil, sessionRole: connectingSceneSession.role)

             config.delegateClass = MovieAppSceneDelegateKt.Self()
        
             return config
         }
        
        fatalError("Unhandled scene role \(connectingSceneSession.role)")
    }
}
