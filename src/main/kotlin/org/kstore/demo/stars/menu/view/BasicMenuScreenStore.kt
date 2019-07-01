package org.kstore.demo.stars.menu.view

import org.kstore.demo.stars.common.*
import react.kstore.BasicStore

abstract class BasicMenuScreenStore(
) : BasicStore<List<String>>(
        initialState = listOf<String>().pad(28, "".leftField(78))
)
