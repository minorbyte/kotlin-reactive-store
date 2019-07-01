package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.gameplay.model.player.colony.ColonyStore
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.optional.present
import java.util.*

@Service
class SelectedColonySummaryViewStore(
        selectedColonyIdViewStore: SelectedColonyIdViewStore,
        colonyStore: ColonyStore
) : BasicStore<Optional<ColonySummaryView>>(
        initialState = Optional.empty(),
        dependsOn = {
            stores(
                    selectedColonyIdViewStore,
                    colonyStore
            ) {
                rewrite { maybeColonyId, colonies ->
                    present(Optional.empty(), maybeColonyId) { colonyId ->
                        val colony = colonies.firstOrNull { it.id == colonyId } ?: return@rewrite Optional.empty()

                        Optional.of(
                                ColonySummaryView(
                                        name = colony.name,
                                        playerId = colony.playerId
                                )
                        )
                    }
                }
            }
        }
)
