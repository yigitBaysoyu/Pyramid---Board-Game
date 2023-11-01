package entity

/**
 * Entity to represent the state and functionalities of the card game "Pyramid".
 *
 * The game is characterized by its main components:
 * - [drawPile]: The deck from which players draw cards.
 * - [pyramid]: The main layout of the game consisting of rows of cards, with the topmost row containing
 *              a single card and each row below it containing one more card than the row above.
 * - [collectedStoragePile]: Pile to store the collected pairs of cards.
 * - [storagePile]: A pile where cards can temporarily be placed for strategic purposes.
 * - [players]: List containing the two players participating in the game.
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

    val drawPile: MutableList<Card> = mutableListOf()
    val pyramid: MutableList<MutableList<Card>> = mutableListOf()
    val collectedStoragePile: MutableList<Card> = mutableListOf()
    val storagePile: MutableList<Card> = mutableListOf()

    private val players: List<Player> = listOf(
        Player(name = player1Name, isFirstPlayer = true),
        Player(name = player2Name, isFirstPlayer = false)
    )

    val player1: Player
        get() = players[0]

    val player2: Player
        get() = players[1]

}