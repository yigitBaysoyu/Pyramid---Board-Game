package entity

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