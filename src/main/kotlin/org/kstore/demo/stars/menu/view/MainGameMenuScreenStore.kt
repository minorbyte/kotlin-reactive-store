package org.kstore.demo.stars.menu.view

import org.kstore.demo.stars.common.menu.BasicMenuSelectedItemStore
import org.kstore.demo.stars.menu.model.MenuExit
import org.kstore.demo.stars.menu.view.MenuScreen.MAIN_MENU
import org.springframework.stereotype.*
import react.kstore.action.asyncAction
import react.kstore.dependency.dependsOn
import react.kstore.selection.*
import java.util.*

class MainMenuItemUpAction : SelectPreviousListItemAction
class MainMenuItemDownAction : SelectNextListItemAction
class MainMenuItemPressAction

class ShowMainMenuAction
class DeselectMainGameMenuAction : DeselectListItemAction

@Service
class MainGameMenuScreenStore : BasicMenuScreenStore() {
    private val gameMenuItemStore = BasicMenuSelectedItemStore(
            items =  mapOf(
                    "  NEW GAME  " to { ShowNewGameMenuAction() },
                    "    LOAD    " to { ShowLoadMenuAction() },
                    "    EXIT    " to { MenuExit() }
                    ),
            nextItemClass = MainMenuItemDownAction::class,
            prevItemClass = MainMenuItemUpAction::class,
            runItem = MainMenuItemPressAction::class,
            showMenuClass = ShowMainMenuAction::class,
            deselectMenuActionClass = DeselectMainGameMenuAction::class,
            deselectMenuActionBuilder = {DeselectMainGameMenuAction()}

    )

    init {
        val itemsList = gameMenuItemStore.items.keys.toList()

        dependsOn {
            stores(
                    gameMenuItemStore
            ) {
                rewrite { selectedItem: Optional<String> ->
                    printMenu(itemsList, selectedItem)
                }
            }
        }
    }
}

@Component
class MainMenuReaction(
        activeScreenStore: ActiveMenuScreenStore
) : BaseMenuScreenReaction(activeScreenStore, MAIN_MENU) {
    override fun processKey(char: Char) {
        when (char) {
            'w' -> up()
            's' -> down()
            'b' -> press()
        }
    }

    private fun down() = asyncAction(MainMenuItemDownAction())
    private fun up() = asyncAction(MainMenuItemUpAction())
    private fun press() = asyncAction(MainMenuItemPressAction())

}
