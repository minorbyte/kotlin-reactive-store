package org.kstore.demo.stars.gameplay.view.screen.menu

import org.kstore.demo.stars.ExitToMenu
import org.kstore.demo.stars.common.menu.BasicMenuSelectedItemStore
import org.kstore.demo.stars.gameplay.save.SaveGameAction
import org.kstore.demo.stars.gameplay.view.screen.*
import org.springframework.stereotype.Service
import react.kstore.selection.DeselectListItemAction

class DeselectGameMenuAction : DeselectListItemAction

@Service
class GameMenuItemStore : BasicMenuSelectedItemStore<DeselectGameMenuAction>(
        items = mapOf(
                "    SAVE:   " to { },
                "     SLOT 1 " to { SaveGameAction(1) },
                "     SLOT 2 " to { SaveGameAction(2) },
                "     SLOT 3 " to { SaveGameAction(3) },
                "     SLOT 4 " to { SaveGameAction(4) },
                "     SLOT 5 " to { SaveGameAction(5) },
                "     SLOT 6 " to { SaveGameAction(6) },
                "     SLOT 7 " to { SaveGameAction(7) },
                "     SLOT 8 " to { SaveGameAction(8) },
                "     SLOT 9 " to { SaveGameAction(9) },
                "    EXIT    " to { ExitToMenu() },
                "------------" to { },
                "BACK TO GAME" to { ShowMainMapAction() }
        ),
        nextItemClass = MenuItemDownAction::class,
        prevItemClass = MenuItemUpAction::class,
        runItem = MenuItemPressAction::class,
        showMenuClass = ShowMenuAction::class,
        deselectMenuActionClass = DeselectGameMenuAction::class,
        deselectMenuActionBuilder = { DeselectGameMenuAction() }
)
