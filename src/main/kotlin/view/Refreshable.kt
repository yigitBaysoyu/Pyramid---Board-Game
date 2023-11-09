package view

import entity.Card
import entity.Player

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the view classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * UI classes only need to react to events relevant to them.
 *
 * @see AbstractRefreshingService
 *
 */
interface Refreshable {

    /**
     * perform refreshes that are necessary after a new game started
     */
    fun refreshAfterStartNewGame() {}

    /**
     * perform refreshes that are necessary after a pair of cards have been removed from the Pyramid
     * by a player
     *
     * @param player the current player that removes a pair of cards from the Pyramid
     * @param removedCards the cards that the player removed onto the collected cards stack
     */
    fun refreshAfterRemovePair(player: Player, removedCards: List<Card>) {}

    /**
     * perform refreshes that are necessary after a player has drawn from the draw pile and places
     * it face up on top of the reserve pile
     *
     * @param player the player that drawn from the draw pile
     * @param revealedCard the card that has been revealed
     */
    fun refreshAfterRevealCard(player: Player, revealedCard: Card) {}

    /**
     * perform refreshes that are necessary after a player passes his turn.
     *
     * @param player the player that passed his turn
     */
    fun refreshAfterPass(player: Player) {}

    /**
     * perform refreshes that are necessary after the last round was played
     */
    fun refreshAfterGameEnd() {}

}