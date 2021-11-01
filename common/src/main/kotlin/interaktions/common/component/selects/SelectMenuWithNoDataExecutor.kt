package interaktions.common.component.selects

import interaktions.api.entity.User
import interaktions.common.context.components.ComponentContext

public interface SelectMenuWithNoDataExecutor : SelectMenuExecutor {
    public suspend fun onSelect(user: User, context: ComponentContext, values: List<String>)
}
