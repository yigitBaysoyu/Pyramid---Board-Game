package service

import entity.*
import tools.aqua.bgw.util.Stack

/**
 * The GameService class is responsible for managing the state and logic of the Pyramid game.
 * It interacts with the game entities and orchestrates the flow of the game.
 *
 * @property rootService An instance of RootService used to access and modify the root state of the game.
 * @constructor Creates a GameService with the specified RootService.
 */
class GameService(private val rootService: RootService): AbstractRefreshingService() {

    /**
     * Starts a new Pyramid game with two players. It initializes the game state and notifies all refreshables.
     *
     * @param player1Name The name of the first player.
     * @param player2Name The name of the second player.
     */
    fun startNewGame(player1Name: String, player2Name: String) : Unit
    {
        val game = PyramidGame(player1Name, player2Name)

        var standardDeck = createStandardDeck()
        standardDeck.shuffle()

        val pyramidDeck = Stack(standardDeck.popAll(28))
        game.drawPile = standardDeck

        rootService.currentGame = game

        createPyramid(pyramidDeck.clear())

        onAllRefreshables { refreshAfterStartNewGame() }
    }

    private fun createStandardDeck() : Stack<Card>{

        val deck = Stack<Card>()
        for(suit in CardSuit.values()){
            for(value in CardValue.values()){
                var card = Card(suit, value)
                deck.push(card)
            }
        }

        return deck
    }




    /**
     * Creates the pyramid structure of cards required at the beginning of the game.
     * It ensures that the correct number of cards is used to form the pyramid.
     *
     * @param allCards A list of cards to be arranged into the pyramid.
     * @throws IllegalArgumentException If the number of cards provided is not 28.
     */
    fun createPyramid(allCards: List<Card>) : Unit
    {
        require(allCards.size == 28) {
            "Provided list of cards' length must be a total of 28, but ${allCards.size} isn't."
        }
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}

        var currentCardIndex = 0
        for(level in 1..7) {
            val levelList = mutableListOf<Card>()
            for(cardIndex in 1..level){
                if(currentCardIndex < allCards.size){
                    val card = allCards[currentCardIndex++]
                    // Flip the outermost cards
                    if (cardIndex == 1 || cardIndex == level) {
                        levelList.add(card.copy(flipped = true))
                    } else {
                        levelList.add(card)
                    }
                }
            }
            game.pyramid.add(levelList)
        }
        rootService.currentGame = game
    }

    /**
     * Ends the current game. It checks whether the pyramid is empty or if both players have passed,
     * and then triggers the end game refreshables.
     * @throws IllegalStateException If no game is currently running.
     */
    fun endGame() : Unit
    {
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}

        val pyramidIsEmpty = game.pyramid.all { level -> level.isEmpty() }

        if(pyramidIsEmpty || game.passCounter == 2){
            onAllRefreshables { refreshAfterGameEnd() }
        }
    }

    /**
     * Switches the current player's turn in the game.
     */
    fun switchPlayer() : Unit
    {
        val playerOnesTurn = rootService.currentGame?.playerOnesTurn
        if(playerOnesTurn == true){
            rootService.currentGame?.playerOnesTurn = false
        }
        else{
            rootService.currentGame?.playerOnesTurn = true
        }
    }

    /**
     * Checks if a pair of cards can be removed from the pyramid according to the game rules.
     * A pair is valid if the sum of their values is 15, and neither card is an Ace, as two Aces cannot form a pair.
     *
     * @param card1 The first card in the pair.
     * @param card2 The second card in the pair.
     * @return Boolean indicating whether the pair is valid.
     */
    fun checkPair(card1: Card, card2: Card) : Boolean{
        // Check if both cards are Aces, which cannot form a valid pair
        if (card1.getIntValue() == 1 && card2.getIntValue() == 1) {
            return false
        }

        // If only one card is an Ace, the pair is valid if the other card is not an Ace
        // since the Ace can take on the value necessary to make the sum to 15
        if (card1.getIntValue() == 1 || card2.getIntValue() == 1) {
            return true
        }

        // If neither card is an Ace, simply check if their integer values add up to 15
        return card1.getIntValue() + card2.getIntValue() == 15
    }

    /**
     * Removes a valid pair of cards from the pyramid and adds them to the collected storage pile.
     * It also checks if the cards are present in the pyramid before attempting to remove them.
     *
     * @param card1 The first card in the pair to be removed.
     * @param card2 The second card in the pair to be removed.
     * @throws IllegalStateException If no game is currently running.
     */
    fun removePair(card1: Card, card2: Card) : Unit{
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}

        for(level in game.pyramid){
            if(level.remove(card1)){
                game.collectedStoragePile.push(card1)
            }
            if(level.remove(card2)){
                game.collectedStoragePile.push(card2)
            }
        }

        // Check if storagePile is not empty and the top card matches either card1 or card2
        with(game.storagePile) {
            if (isNotEmpty()) {
                if (peek() == card1 || peek() == card2) {
                    println("Removed from reserve pile: ${game.storagePile.peek().toString()}")
                    game.collectedStoragePile.push(pop())
                }
            }
        }

        println("Remaining Cards: ${game.pyramid.flatten().size}")
        println(game.pyramid.flatten())

    }



    /**
     * Adds the score for a collected pair to the game's score.
     * If either of the cards is an Ace, the score for the pair is 1.
     * For all other pairs, the score is 2.
     *
     * @param card1 The first card in the collected pair.
     * @param card2 The second card in the collected pair.
     * @return The score added for the collected pair.
     */
    fun addScore(card1: Card, card2: Card) : Int{
        return if(card1.getIntValue() == 1 || card2.getIntValue() == 1){
            1
        }else{
            2
        }
    }

}