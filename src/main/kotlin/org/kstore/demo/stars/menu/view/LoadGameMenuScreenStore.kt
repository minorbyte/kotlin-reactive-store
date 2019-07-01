package org.kstore.demo.stars.menu.view

import org.kstore.demo.stars.common.menu.BasicMenuSelectedItemStore
import org.kstore.demo.stars.menu.model.MenuLoadGame
import org.kstore.demo.stars.menu.view.MenuScreen.LOAD_GAME_MENU
import org.springframework.stereotype.*
import react.kstore.action.asyncAction
import react.kstore.dependency.dependsOn
import react.kstore.selection.*
import java.io.File
import java.util.*

class LoadMenuItemUpAction : SelectPreviousListItemAction
class LoadMenuItemDownAction : SelectNextListItemAction
class LoadMenuItemPressAction

class ShowLoadMenuAction

class DeselectSaveMenuItemAction : DeselectListItemAction

@Service
class LoadGameMenuScreenStore : BasicMenuScreenStore() {
    private val gameMenuItemStore = BasicMenuSelectedItemStore(
            items = (1..9).fold(LinkedHashMap<String, () -> Any>()) { map, index ->
                if (File("game$index.sav").exists()) {
                    map["    SLOT $index  "] = { MenuLoadGame(index) } as () -> Any
                }
                map
            }.plus("    BACK    " to { ShowMainMenuAction() }),
            nextItemClass = LoadMenuItemDownAction::class,
            prevItemClass = LoadMenuItemUpAction::class,
            runItem = LoadMenuItemPressAction::class,
            showMenuClass = ShowLoadMenuAction::class,
            deselectMenuActionClass = DeselectSaveMenuItemAction::class,
            deselectMenuActionBuilder = {DeselectSaveMenuItemAction()}

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
class LoadMenuReaction(
        activeScreenStore: ActiveMenuScreenStore
) : BaseMenuScreenReaction(activeScreenStore, LOAD_GAME_MENU) {
    override fun processKey(char: Char) {
        when (char) {
            'w' -> up()
            's' -> down()
            'b' -> press()
        }
    }

    private fun down() = asyncAction(LoadMenuItemDownAction())
    private fun up() = asyncAction(LoadMenuItemUpAction())
    private fun press() = asyncAction(LoadMenuItemPressAction())

}
