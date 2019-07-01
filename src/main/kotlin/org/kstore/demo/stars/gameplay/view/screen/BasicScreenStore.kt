package org.kstore.demo.stars.gameplay.view.screen

import org.kstore.demo.stars.common.*
import react.kstore.BasicStore

abstract class BasicScreenStore(
) : BasicStore<List<String>>(
        initialState = listOf<String>().pad(28, "".leftField(78))
)
