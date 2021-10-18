package net.perfectdreams.discordinteraktions.common.components.buttons

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.components.ComponentContext

public interface ButtonClickWithDataExecutor : ButtonClickExecutor {
    public suspend fun onClick(user: User, context: ComponentContext, data: String)
}
