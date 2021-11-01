package interaktions.common.component.buttons

import interaktions.api.entity.User
import interaktions.common.context.components.ComponentContext

public interface ButtonClickWithNoDataExecutor : ButtonClickExecutor {
    public suspend fun onClick(user: User, context: ComponentContext)
}
