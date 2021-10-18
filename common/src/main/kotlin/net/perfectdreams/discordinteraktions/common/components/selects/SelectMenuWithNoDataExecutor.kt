package net.perfectdreams.discordinteraktions.common.components.selects

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.components.ComponentContext

public interface SelectMenuWithNoDataExecutor : SelectMenuExecutor {
    public suspend fun onSelect(user: User, context: ComponentContext, values: List<String>)
}
