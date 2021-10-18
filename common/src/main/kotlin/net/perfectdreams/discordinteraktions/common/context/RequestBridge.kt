package net.perfectdreams.discordinteraktions.common.context

import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.utils.Observable
import kotlin.properties.Delegates

/**
 * Bridges code between a interaction handler and the request managers
 *
 * Used to track [state] changes and to allow request managers to switch to other request managers within their own code
 *
 * **Don't forget to initialize the [manager] after creating the bridge!**
 *
 * @param state a [Observable] interaction request state
 */
public class RequestBridge(
    public val state: Observable<InteractionRequestState>
) {
    public var manager: RequestManager by Delegates.notNull()
}
