package interaktions.common.component.selects

import interaktions.api.entity.User
import interaktions.common.context.components.ComponentContext

public interface SelectMenuWithDataExecutor : SelectMenuExecutor {
    public suspend fun onSelect(user: User, context: ComponentContext, data: String, values: List<String>)
}
