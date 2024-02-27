package io.github.trueangle.blackbox.sample.movie.shared

import io.github.trueangle.blackbox.sample.movie.shared.ui.AppConfig
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import moe.tlaster.precompose.PreComposeAppController
import platform.Foundation.NSClassFromString
import platform.Foundation.NSLog
import platform.Foundation.NSUserActivity
import platform.UIKit.UIOpenURLContext
import platform.UIKit.UIResponder
import platform.UIKit.UIResponderMeta
import platform.UIKit.UIScene
import platform.UIKit.UISceneConnectionOptions
import platform.UIKit.UISceneSession
import platform.UIKit.UIScreen
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.UIWindowSceneDelegateProtocol
import platform.UIKit.UIWindowSceneDelegateProtocolMeta
import platform.UIKit.userActivity

/**
 * Kotlin implementation of Scene delegate class that supports
 * navigation state preservation via NSUserActivity.
 *
 * Example of usage the class in swift code:
 * @UIApplicationMain
 * class AppDelegate: UIResponder, UIApplicationDelegate {
 *
 *     var window: UIWindow?
 *
 *     func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
 *
 *         if connectingSceneSession.role == UISceneSession.Role.windowApplication {
 *              let config = UISceneConfiguration(name: nil, sessionRole: connectingSceneSession.role)
 *              config.delegateClass = MovieAppSceneDelegateKt.Self()
 *              return config
 *          }
 *
 *         fatalError("Unhandled scene role \(connectingSceneSession.role)")
 *     }
 * }
 *
 */
@OptIn(ExperimentalForeignApi::class)
@BetaInteropApi
@ExportObjCClass
class MovieAppSceneDelegate : UIResponder, UIWindowSceneDelegateProtocol {
    @OverrideInit
    constructor() : super()

    private var _window: UIWindow? = null
    override fun window() = _window
    override fun setWindow(window: UIWindow?) {
        _window = window
    }

    override fun scene(
        scene: UIScene,
        willConnectToSession: UISceneSession,
        options: UISceneConnectionOptions
    ) {
        val windowScene = (scene as? UIWindowScene) ?: return
        val activity = (options.userActivities.firstOrNull()
            ?: scene.session.stateRestorationActivity) as? NSUserActivity

        val urlContexts = options.URLContexts as? Set<UIOpenURLContext>
        val deeplink = urlContexts?.firstOrNull()?.URL?.absoluteString

        val appController = createMovieAppController(
            nsUserActivity = activity,
            config = AppConfig(deeplink = deeplink)
        )

        if (appController.isRestoredFromState) {
            windowScene.userActivity = activity
            windowScene.title = activity?.title.orEmpty()
        } else {
            NSLog("The provided activity type is null or did not recognised")
        }

        window = UIWindow(frame = UIScreen.mainScreen.bounds)
        window?.windowScene = windowScene
        window?.rootViewController = appController
        window?.makeKeyAndVisible()
    }

    override fun scene(scene: UIScene, openURLContexts: Set<*>) {
        val urlContexts = openURLContexts as? Set<UIOpenURLContext>
        val deeplink = urlContexts?.firstOrNull()?.URL?.absoluteString

        window?.rootViewController = createMovieAppController(
            nsUserActivity = null,
            config = AppConfig(deeplink = deeplink)
        )
    }

    override fun sceneWillResignActive(scene: UIScene) {
        val controller = (window?.rootViewController as? PreComposeAppController)
        scene.userActivity = controller?.willBecomeInactive()
    }

    override fun stateRestorationActivityForScene(scene: UIScene): NSUserActivity? =
        scene.userActivity

    companion object : UIResponderMeta(), UIWindowSceneDelegateProtocolMeta
}

fun Self() = NSClassFromString("MovieAppSceneDelegate")
