package view

import entity.Card
import entity.Player
import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.util.Stack
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

class PyramidGameScene (private val rootService: RootService) : BoardGameScene(1920, 1240), Refreshable {

    //Player Names
    private val labelWidth = 550
    private val labelHeight = 50
    private val sideMargin = 60 // Adjust this margin to move labels closer to or further from the sides

    //Player Points
    private val pointsLabelWidth = 150
    private val pointsLabelHeight = 30


    private val player1NameLabel = Label(
        posX = sideMargin, // X position from the left edge
        posY = 50,
        width = labelWidth,
        height = labelHeight,
        text = "Player 1"
    )

    private val player2NameLabel = Label(
        posX = 1920 - labelWidth - sideMargin, // X position from the right edge
        posY = 50,
        width = labelWidth,
        height = labelHeight,
        text = "Player 2"
    )


    private val player1PointsLabel = Label(
        posX = player1NameLabel.posX + (labelWidth - pointsLabelWidth) / 2, // Center under the player name label
        posY = player1NameLabel.posY + labelHeight + 85, // Below the player name label
        width = pointsLabelWidth,
        height = pointsLabelHeight,
        text = "0"
    ).apply {
        font = Font(size = 80, color = Color.BLACK, fontWeight = Font.FontWeight.BOLD)
    }

    private val player2PointsLabel = Label(
        posX = player2NameLabel.posX + (labelWidth - pointsLabelWidth) / 2, // Center under the player name label
        posY = player2NameLabel.posY + labelHeight + 85, // Below the player name label
        width = pointsLabelWidth,
        height = pointsLabelHeight,
        text = "0"
    ).apply {
        font = Font(size = 80, color = Color.BLACK, fontWeight = Font.FontWeight.BOLD)
    }


    private val pyramidLayout: MutableList<MutableList<CardView>> = mutableListOf()

    private val drawPile = LabeledStackView(posX = 270, posY = 500, "Draw Pile").apply {
        onMouseClicked = {
            rootService.currentGame?.let { game ->
                // Determine which player's turn it is and perform the action
                if (game.playerOnesTurn) {
                    rootService.playerActionService.useDrawPile(game.player1)
                } else {
                    rootService.playerActionService.useDrawPile(game.player2)
                }
            }
        }
    }

    private val reservePile = LabeledStackView(posX = 1520, posY = 500, "Reserve Pile")

    // Pass Button
    private val passButton = Button(
        width = 260,
        height = 120,
        posX = 1565,       // Centered horizontally
        posY = 1040,       // Positioned at the bottom
    ).apply {
        visual = ImageVisual("PassButton.png")
        onMouseClicked = {
            // Handle pass action
            refreshAfterPass(currentPlayer())
        }
    }

    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    private var selectedCards: MutableList<CardView> = mutableListOf()



    init {

        background = ColorVisual(57, 70, 59)

        // Customize labels if needed
        player1NameLabel.apply {
            font = Font(size = 67, color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, family = "Copperplate" )
            // More styling options can be set here
        }
        player2NameLabel.apply {
            font = Font(size = 67, color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, family = "Copperplate" )
            // More styling options can be set here
        }

        // Add the player name labels to the scene
        addComponents(
            drawPile,
            reservePile,
            player1NameLabel,
            player2NameLabel,
            player1PointsLabel,
            player2PointsLabel,
            passButton
        )
    }



    private fun currentPlayer(): Player{
        val game = rootService.currentGame
        checkNotNull(game)
        return if(game.playerOnesTurn) game.player1 else game.player2
    }
    private fun updatePlayerPoints() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        // Update the labels with the current points from the game state
        player1PointsLabel.text = "${game.player1.score}"
        player2PointsLabel.text = "${game.player2.score}"
    }




    private fun highlightCurrentPlayer() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        // Reset styles for both player labels to default
        player1NameLabel.font = Font(size = 67, color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, family = "Copperplate" )
        player2NameLabel.font = Font(size = 67, color = Color.BLACK, fontWeight = Font.FontWeight.BOLD, family = "Copperplate" )

        // Highlight the label of the current player
        if (game.playerOnesTurn) {
            player1NameLabel.font = Font(size = 67, color = Color(208, 0, 0), fontWeight = Font.FontWeight.BOLD, family = "Copperplate" )
            // Optionally add a border or other visual indicator here
        } else {
            player2NameLabel.font = Font(size = 67, color = Color(208, 0, 0), fontWeight = Font.FontWeight.BOLD, family = "Copperplate" )
            // Optionally add a border or other visual indicator here
        }
    }
    private fun initializePyramid(pyramidCards: List<Card>) {
        val cardWidth = 100
        val cardHeight = 150
        val cardSpacing = 167 // Spacing between cards

        // Horizontal and vertical overlap should be less than card width and height respectively for spacing
        val hOverlap = (cardWidth / 2) - (cardSpacing / 2)
        val vOverlap = (cardHeight / 2) - (cardSpacing / 2)
        val sceneWidth = 1920 // Scene width from BoardGameScene(1920, 1080)

        // Clear any existing layout
        pyramidLayout.clear()

        // Starting Y position at the top of the scene
        var currentY = 68 // Increase this value to move the pyramid down

        var cardIndex = 0

        // Loop through 7 rows
        for (row in 1..7) {
            val rowCards = mutableListOf<CardView>()
            // Calculate starting x position for the current row to center the cards
            val offsetX = (cardWidth - hOverlap)
            val rowWidth = (row * cardWidth) - (row - 1) * (offsetX - cardSpacing)
            var currentX = (sceneWidth - rowWidth) / 2

            // Loop through cards in the row
            for (col in 1..row) {
                val card = pyramidCards[cardIndex++]
                val isEdgeCard = col == 1 || col == row // Edge card if it's the first or last in the row
                val cardView = initializeCardView(card, isEdgeCard)

                // Set position and add to the row
                cardView.posX = currentX.toDouble()
                cardView.posY = currentY.toDouble()
                addComponents(cardView)
                rowCards.add(cardView)

                // Adjust position for the next card in the row
                currentX += offsetX
            }

            pyramidLayout.add(rowCards)
            // Adjust position for the next row
            currentY += (cardHeight - vOverlap)
        }
    }
    private fun clearPyramidLayout() {
        for (row in pyramidLayout) {
            for (cardView in row) {
                removeComponents(cardView)
            }
        }
        pyramidLayout.clear()
    }
    private fun isEdgeCard(cardView: CardView): Boolean {
        for (row in pyramidLayout) {
            if (row.contains(cardView)) {
                // Check if the card is the first or the last in its row
                return row.indexOf(cardView) == 0 || row.indexOf(cardView) == row.size - 1
            }
        }
        return false
    }
    private fun isCardOnReservePile(card: CardView): Boolean {
        val game = rootService.currentGame ?: return false

        // Check if the reserve pile is not empty and the top card matches the given card
        return game.storagePile.isNotEmpty() && cardMap.forward(game.storagePile.peek()) == card
    }




    private fun toggleCardSelection(cardView: CardView) {

        val game = rootService.currentGame
        checkNotNull(game)

        // Check if the card is already selected
        if (selectedCards.contains(cardView)) {

            // Deselect the card
            cardView.posY += 30 // Adjust this value to change the elevation effect
            selectedCards.remove(cardView)

        } else if(selectedCards.size == 0) {

            cardView.posY -= 30 // Adjust this value to change the elevation effect
            selectedCards.add(cardView)

        } else if (selectedCards.size == 1) {

            cardView.posY -= 30 // Adjust this value to change the elevation effect
            selectedCards.add(cardView)

            //if((isEdgeCard(selectedCards[0]) && isEdgeCard(selectedCards[1]))           ||
            //  ((isEdgeCard(selectedCards[0]) && isCardOnReservePile(selectedCards[1]))) ||
            //  ((isEdgeCard(selectedCards[1]) && isCardOnReservePile(selectedCards[0])))) {
            //
            //    val list = listOf(cardMap.backward(selectedCards[0]), cardMap.backward(selectedCards[1]))
            //
            //    refreshAfterRemovePair(currentPlayer(), list)
            //}

        } else if (selectedCards.size == 2) {

            selectedCards.forEach{card -> card.posY += 30 }
            selectedCards.clear()
        }

    }
    private fun initializeStackView(stack: Stack<Card>, stackView: LabeledStackView, cardImageLoader: CardImageLoader){
        stackView.clear()
        stack.peekAll().forEach(){ card ->
            val cardView = CardView(
                height = 200 / 6 * 5,
                width = 130 / 6 * 5,
                front = ImageVisual(cardImageLoader.frontImageFor(card.suit, card.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            stackView.add(cardView)
            cardMap.add(card to cardView)
        }
    }
    private fun initializeCardView(card: Card, isEdgeCard: Boolean): CardView {
        val cardImageLoader = CardImageLoader()

        val cardView = CardView(
            width = 100,
            height = 150,
            front = ImageVisual(cardImageLoader.frontImageFor(card.suit, card.value)),
            back = ImageVisual(cardImageLoader.backImage)
        )

        // Show the front for edge cards, back for non-edge cards
        if (isEdgeCard) {
            cardView.showFront()
        } else {
            cardView.showBack()
        }

        cardView.onMouseClicked = {
            toggleCardSelection(cardView)
            println(card.toString())
        }

        return cardView
    }


    override fun refreshAfterStartNewGame() {

        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        selectedCards.clear()
        // Clear existing pyramid layout
        clearPyramidLayout()

        player1NameLabel.text = game.player1.name
        player2NameLabel.text = game.player2.name
        highlightCurrentPlayer()

        // Initialize points labels with zero points
        player1PointsLabel.text = "0"
        player2PointsLabel.text = "0"

        selectedCards.clear()
        // Clear existing pyramid layout
        clearPyramidLayout()
        cardMap.clear()
        val cardImageLoader = CardImageLoader()


        initializeStackView(game.drawPile, drawPile, cardImageLoader)
        initializeStackView(game.storagePile, reservePile, cardImageLoader)


        val pyramidCards = game.pyramid.flatten()
        initializePyramid(pyramidCards)
    }
    override fun refreshAfterRemovePair(player: Player, removedCards: List<Card>){
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        val card1 = removedCards[0]
        val card2 = removedCards[1]

        rootService.playerActionService.selectPair(player, card1, card2)
        updatePlayerPoints()

        game.playerOnesTurn = !game.playerOnesTurn
        highlightCurrentPlayer()

    }
    override fun refreshAfterPass(player: Player) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        // Implement the logic for passing the turn
        // For example, toggle the player's turn and update the game state
        game.passCounter++

        if(game.passCounter == 2){
            rootService.gameService.endGame()
        }

        game.playerOnesTurn = !game.playerOnesTurn
        highlightCurrentPlayer()

        // You might also want to update other parts of the UI or game state as needed
    }
    override fun refreshAfterRevealCard(player: Player, revealedCard: Card) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        highlightCurrentPlayer()

        when (player) {
            game.player1 -> moveCardView(cardMap.forward(game.storagePile.peek()),reservePile, true)
            game.player2 -> moveCardView(cardMap.forward(game.storagePile.peek()),reservePile, true)
        }
        if(game.drawPile.isEmpty()){
            drawPile.clear()
            drawPile.onMouseClicked = null
        }
    }



    /**
     * moves a [cardView] from current container on top of [toStack].
     *
     * @param flip if true, the view will be flipped from [CardView.CardSide.FRONT] to
     * [CardView.CardSide.BACK] and vice versa.
     */
    private fun moveCardView(cardView: CardView, toStack: LabeledStackView, flip: Boolean = false) {
        if (flip) {
            when (cardView.currentSide) {
                CardView.CardSide.BACK -> cardView.showFront()
                CardView.CardSide.FRONT -> cardView.showBack()
            }
        }
        cardView.removeFromParent()
        toStack.add(cardView)
    }

}
