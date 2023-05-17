package view

import entity.Player
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * [MenuScene] that is displayed when the game is finished. It shows the final result of the game
 * as well as the score. Also, there are two buttons: one for starting a new game and one for
 * quitting the program.
 */
class SwimGameOverScene(private val rootService: RootService) : MenuScene(1920,600),Refreshable {
    private val labelFont = Font(size = 45, color = Color.WHITE)
    private val buttonColor = ColorVisual(255,255,49)

    private val headlineLabel = Label(
        width = 400, height = 50,
        posX = 750, posY = 30,
        text = "Scoreboard",
        font = Font(size = 60, color = Color.YELLOW)
    )
    private val p1Show: Label = Label(
        width = 1000, height = 50,
        posX = 500, posY = 100,
        font = labelFont
    )
    private val p2Show: Label = Label(
        width = 1000, height = 50,
        posX = 500, posY = 150,
        font = labelFont
    )
    private val p3Show: Label = Label(
        width = 1000, height = 50,
        posX = 500, posY = 200,
        font = labelFont,
    )
    private val p4Show = Label(
        width = 1000, height = 50,
        posX = 500, posY = 250,
        font = labelFont
    )
    private val winnerShow = Label(
        width = 1000, height = 50,
        posX = 500, posY = 330,
        font = Font(45, Color.red))
    val quitButton = Button(
        width = 300, height = 70,
        posX = 350, posY = 450,
        text = "Quit", visual = buttonColor,
        font = Font(40))
    val newGameButton = Button(
        width = 300, height = 70,
        posX = 1200, posY = 450,
        text = "New Game", visual = buttonColor,
        font = Font(40))

    init {
        opacity = 1.0
        background = ImageVisual("MuLogo.png")
        addComponents(headlineLabel, p1Show, p2Show,p3Show,p4Show, winnerShow, newGameButton, quitButton)
    }
    private fun Player.scoreString(): String = "${this.name} scored "

    override fun refreshAfterEndGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        val playerList = game.players
        val scoreList = mutableListOf<Double>()
        for (player in game.players){
            scoreList.add(rootService.playerService.calculateScore(player))
        }
        var winnerPlayer = playerList[0].name
        var winnerScore = scoreList[0]
        p1Show.text = playerList[0].scoreString() + "${scoreList[0]}" + " points."
        p2Show.text = playerList[1].scoreString() + "${scoreList[1]}" + " points."
        if (scoreList[1] > winnerScore) {
            winnerPlayer = playerList[1].name
            winnerScore = scoreList[1]
        }
        println(playerList)
        if (scoreList.size > 2) {
            p3Show.text = playerList[2].scoreString() + "${scoreList[2]}" + " points."
            if (scoreList[2] > winnerScore) {
                winnerPlayer = playerList[2].name
                winnerScore = scoreList[2]
            }
            if (scoreList.size > 3) {
                p4Show.text = playerList[3].scoreString() + "${scoreList[3]}" + " points."
                if (scoreList[3] > winnerScore) {
                    winnerPlayer = playerList[3].name
                    winnerScore = scoreList[3]
                }
            }
        }
        var count = 0
        for(i in 0..scoreList.size-1) {
            if(winnerScore == scoreList[i]) {
                count++;
            }
        }
        if(count >= 2) {
            winnerShow.text = " nobody has won the game with the score "
        } else {
        winnerShow.text = winnerPlayer + " has won the game with the score " + "$winnerScore"
        }
    }

    override fun refreshAfterStartGame() {
        p3Show.text = ""
        p4Show.text = ""
    }
}