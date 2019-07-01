package org.kstore.demo.stars.menu.view

import org.kstore.demo.stars.*
import org.kstore.demo.stars.common.menu.BasicMenuSelectedItemStore
import org.kstore.demo.stars.menu.view.MenuScreen.NEW_GAME_MENU
import org.springframework.stereotype.*
import react.kstore.action.asyncAction
import react.kstore.dependency.dependsOn
import react.kstore.selection.*
import java.util.*

class NewGameMenuItemUpAction : SelectPreviousListItemAction
class NewGameMenuItemDownAction : SelectNextListItemAction
class NewGameMenuItemPressAction

class ShowNewGameMenuAction

class DeselectNewMenuItemAction : DeselectListItemAction

@Service
class NewGameGameMenuScreenStore(
        gameDescriptions: List<GameDescription>
) : BasicMenuScreenStore() {
    private val gameMenuItemStore = BasicMenuSelectedItemStore(
            items = gameDescriptions
                    .associate { it.name to { asyncAction(StartGame(it)) } as () -> Any }
                    .plus("    BACK    " to { ShowMainMenuAction() }),
            nextItemClass = NewGameMenuItemDownAction::class,
            prevItemClass = NewGameMenuItemUpAction::class,
            runItem = NewGameMenuItemPressAction::class,
            showMenuClass = ShowNewGameMenuAction::class,
            deselectMenuActionClass = DeselectNewMenuItemAction::class,
            deselectMenuActionBuilder = {DeselectNewMenuItemAction()}

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
class NewGameMenuReaction(
        activeScreenStore: ActiveMenuScreenStore
) : BaseMenuScreenReaction(activeScreenStore, NEW_GAME_MENU) {
    override fun processKey(char: Char) {
        when (char) {
            'w' -> up()
            's' -> down()
            'b' -> press()
        }
    }

    private fun down() = asyncAction(NewGameMenuItemDownAction())
    private fun up() = asyncAction(NewGameMenuItemUpAction())
    private fun press() = asyncAction(NewGameMenuItemPressAction())

}
