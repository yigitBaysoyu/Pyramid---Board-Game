package entity

import tools.aqua.bgw.util.Stack


/**
 * Entity to represent the state and functionalities of the card game "Pyramid".
 *
 * The game is characterized by its main components:
 * - [pyramid]: The main layout of the game consisting of rows of cards, with the topmost row containing
 *              a single card and each row below it containing one more card than the row above.
 * - [drawPile]: The deck from which players draw cards.
 * - [collectedStoragePile]: Pile to store the collected pairs of cards.
 * - [storagePile]: A pile where cards can temporarily be placed for strategic purposes.
 * - [player1]: Instance of the first player.
 * - [player2]: Instance of the second player.
 *
 * Game properties:
 * - [playerOnesTurn]: Indicates whose turn it is. `true` for player 1 and `false` for player 2.
 * - [flippedCardCount]: Counts the number of cards that have been flipped over in the current turn.
 * - [passCounter]: Counts the number of consecutive passes by the players without making a move.
 *
 * @param player1Name The name of the first player.
 * @param player2Name The name of the second player.
 */

class PyramidGame(player1Name: String, player2Name: String) {
    var playerOnesTurn: Boolean = true
    var flippedCardCount: Int = 0
    var passCounter: Int = 0

    var pyramid: MutableList<MutableList<Card>> = mutableListOf()

    var drawPile: Stack<Card> = Stack()
    val collectedStoragePile: Stack<Card> = Stack()
    val storagePile: Stack<Card> = Stack()

    val player1: Player = Player(name = player1Name, isFirstPlayer = true)
    val player2: Player = Player(name = player2Name, isFirstPlayer = false)

}