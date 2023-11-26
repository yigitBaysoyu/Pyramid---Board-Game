package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * This scene is displayed at the end of the Pyramid card game. It shows the results of the game, including
 * the names and scores of both players, and the game outcome (winner, loser, or draw). Players can choose
 * to start a new game or quit from this scene.
 */
class GameFinishedMenuScene (private val rootService: RootService) :
    MenuScene(width = 410 , height = 490, background = ImageVisual("GameOverArtwork.png")), Refreshable {

    private val labelWidth = 125
    private val labelHeight = 50
    private val adjustNamePosX = 96
    private val adjustNamePosY = -60

    private val adjustScorePosX = 96
    private val adjustScorePosY = -20

    private val adjustButtonPosX = 90
    private val adjustButtonPosY = 160

    private val p1NameLabel = Label(
        posX = (410-labelWidth)/2 - adjustNamePosX, // X position from the left edge
        posY = (490-labelHeight)/2 + adjustNamePosY,
        width = labelWidth,
        height = labelHeight,
    ).apply {
        font = Font(size = 26, color = Color.BLACK, family = "Copperplate")
    }

    private val p2NameLabel = Label(
        posX = (410-labelWidth)/2 + adjustNamePosX, // X position from the left edge
        posY = (490-labelHeight)/2 + adjustNamePosY,
        width = labelWidth,
        height = labelHeight,
    ).apply {
        font = Font(size = 26, color = Color.BLACK, family = "Copperplate")
    }

    private val p1Score = Label(
        posX = (410-150)/2 - adjustScorePosX,
        posY = (490-30)/2 + adjustScorePosY,
        width = 150,
        height = 30,
        text = "0"
    ).apply {
        font = Font(size = 60, color = Color.BLACK, family = "Copperplate")
    }

    private val p2Score = Label(
        posX = (410-150)/2 + adjustScorePosX,
        posY = (490-30)/2 + adjustScorePosY,
        width = 150,
        height = 30,
        text = "0"
    ).apply {
        font = Font(size = 60, color = Color.BLACK, family = "Copperplate")
    }

    private val gameResultP1 = Label(
        posX = (410-labelWidth)/2 - adjustNamePosX, // X position from the left edge
        posY = (490-labelHeight)/2 + 50,
        width = labelWidth,
        height = labelHeight,
        text = "Winner!"
    ).apply {
        font = Font(size = 26, color = Color.BLACK, family = "Copperplate")
    }

    private val gameResultP2 = Label(
        posX = (410-labelWidth)/2 + adjustNamePosX, // X position from the left edge
        posY = (490-labelHeight)/2 + 50,
        width = labelWidth,
        height = labelHeight,
        text = "Winner!"
    ).apply {
        font = Font(size = 26, color = Color.BLACK, family = "Copperplate")
    }

    private val gameResultD = Label(
        posX = (410-labelWidth)/2 , // X position from the left edge
        posY = (490-labelHeight)/2 + 50,
        width = labelWidth,
        height = labelHeight,
        text = "Draw..."
    ).apply {
        font = Font(size = 26, color = Color.BLACK, family = "Copperplate")
    }


    val quitButton = Button(
        width = 125,
        height = 50,
        posX = (410-120)/2 - adjustButtonPosX,
        posY = (490-50)/2 + adjustButtonPosY,
    ).apply{
        visual = ImageVisual("QuitButton.png")
    }


    val newGameButton = Button(
        width = 120,
        height = 50,
        posX = (410-120)/2 + adjustButtonPosX,
        posY = (490-50)/2 + adjustButtonPosY,
    ).apply {
        visual = ImageVisual("StartButton.png")
    }

    init {
        opacity = 1.0
        addComponents(
            p1NameLabel,
            p2NameLabel,
            p1Score,
            p2Score,
            newGameButton,
            quitButton)
    }

    /**
     * Determines the game result and displays the appropriate label based on the final scores of the players.
     * Adds the winner or draw label to the scene.
     *
     * @throws IllegalStateException if no game is currently running.
     */
    private fun gameResult(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }

        val p1Score = game.player1.score
        val p2Score = game.player2.score

        if(p1Score > p2Score){
            addComponents(gameResultP1)
        }else if(p1Score < p2Score){
            addComponents(gameResultP2)
        }else{
            addComponents(gameResultD)
        }

    }

    /**
     * Refreshes the scene after the game ends. It updates the player names and scores, and displays the
     * game result (winner, loser, or draw). It also manages the removal and addition of result labels
     * based on the game outcome.
     *
     * @throws IllegalStateException if no game is currently running.
     */
    override fun refreshAfterGameEnd() {

        removeComponents(
            gameResultP1,
            gameResultP2,
            gameResultD
        )

        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }

        p1NameLabel.text = game.player1.name
        p2NameLabel.text = game.player2.name

        p1Score.text = game.player1.score.toString()
        p2Score.text = game.player2.score.toString()

        gameResult()
    }


}