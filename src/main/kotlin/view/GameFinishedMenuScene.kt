package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

class GameFinishedMenuScene (private val rootService: RootService) : MenuScene(width = 410 , height = 490, background = ImageVisual("GameOverArtwork.png")), Refreshable {

    private val labelWidth = 125
    private val labelHeight = 50
    private val adjustNamePosX = 100
    private val adjustNamePosY = -60

    private val adjustScorePosX = 100
    private val adjustScorePosY = -20

    private val adjustButtonPosX = 90
    private val adjustButtonPosY = 160

    private val player1NameLabel = Label(
        posX = (410-labelWidth)/2 - adjustNamePosX, // X position from the left edge
        posY = (490-labelHeight)/2 + adjustNamePosY,
        width = labelWidth,
        height = labelHeight,
        text = "abcdefghijklmn"
    ).apply {
        font = Font(size = 25, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL, family = "Copperplate")
    }

    private val player2NameLabel = Label(
        posX = (410-labelWidth)/2 + adjustNamePosX, // X position from the left edge
        posY = (490-labelHeight)/2 + adjustNamePosY,
        width = labelWidth,
        height = labelHeight,
        text = "Player 2"
    ).apply {
        font = Font(size = 25, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL, family = "Copperplate")
    }

    private val p1Score = Label(
        posX = (410-150)/2 - adjustScorePosX,
        posY = (490-30)/2 + adjustScorePosY,
        width = 150,
        height = 30,
        text = "0"
    ).apply {
        font = Font(size = 60, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL, family = "Copperplate")
    }

    private val p2Score = Label(
        posX = (410-150)/2 + adjustScorePosX,
        posY = (490-30)/2 + adjustScorePosY,
        width = 150,
        height = 30,
        text = "0"
    ).apply {
        font = Font(size = 60, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL, family = "Copperplate")
    }

    private val gameResult = Label(
        width = 300,
        height = 35,
        posX = 50,
        posY = 195
     ).apply {
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
            player1NameLabel,
            player2NameLabel,
            p1Score,
            p2Score,
            gameResult,
            newGameButton,
            quitButton)
    }



    override fun refreshAfterGameEnd() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        p1Score.text = game.player1.score.toString()
        p2Score.text = game.player2.score.toString()
    }


}