package interaktions.common.context

import interaktions.common.context.manager.RequestManager
import interaktions.common.util.Observable
import kotlin.properties.Delegates

/**
 * Bridges code between an interaction handler and the request managers
 *
 * Used to track [state] changes and to allow request managers to switch to other request managers within their own code
 *
 * **Don't forget to initialize the [manager] after creating the bridge!**
 *
 * @param state a [Observable] interaction request state
 */
public class RequestBridge(public val state: Observable<InteractionRequestState>) {
    public var manager: RequestManager by Delegates.notNull()
}
