package org.kstore.action

import org.junit.jupiter.api.extension.*

class CommonDispatcherActionSinkExtension : ParameterResolver {

    override fun supportsParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Boolean {
        return parameterContext?.parameter?.run {
            isAnnotationPresent(SubscribeTo::class.java) && type == EmittedActions::class.java
        } ?: false
    }

    override fun resolveParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Any {
        return EmittedActions()
    }

}
