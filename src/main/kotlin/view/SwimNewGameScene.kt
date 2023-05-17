package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * [MenuScene] that is used for starting a new game. It is displayed directly at program start or reached
 * when "new game" is clicked in [GameFinishedMenuScene]. After providing the names of both players,
 * [startButton] can be pressed. There is also a [quitButton] to end the program.
 */
class SwimNewGameScene(private val rootService: RootService) : MenuScene(1920,1080),Refreshable {
    private var numberOfPlayer = 4
    private val buttonColor1 = ColorVisual(222,	49,	99)
    private val buttonColor2 = ColorVisual(	255,	255,	49)
    private val labelFont = Font(size = 40, color = Color.WHITE)
    private val headlineLabel = Label(
        width = 900, height = 70,
        posX = 550, posY = 70,
        text = "Game Swim",
        font = Font(size = 70, color = Color.WHITE)
    )

    private val p1Label = Label(
        width = 250, height = 70,
        posX = 405, posY = 350,
        text = "Player 1:",
        font = labelFont,
    )
    private val p1Input: TextField = TextField(
        width = 250, height = 70,
        posX = 625, posY = 350,
        font = Font(size = 30)
    )
    private val p2Label = Label(
        width = 250, height = 70,
        posX = 1020, posY = 350,
        text = "Player 2:",
        font = labelFont,
    )
    private val p2Input: TextField = TextField(
        width = 250, height = 70,
        posX = 1240, posY = 350,
        font = Font(size = 30)
    )
    private val p3Label = Label(
        width = 250, height = 70,
        posX = 405, posY = 600,
        text = "Player 3:",
        font = labelFont,
    )
    private val p3Input: TextField = TextField(
        width = 250, height = 70,
        posX = 625, posY = 600,
        font = Font(size = 30)
    )
    private val p4Label = Label(
        width = 250, height = 70,
        posX = 1020, posY = 600,
        text = "Player 4:",
        font = labelFont,
    )
    private val p4Input: TextField = TextField(
        width = 250, height = 70,
        posX = 1240, posY = 600,
        font = Font(size = 30)
    )

    val quitButton = Button(
        width = 250, height = 70,
        posX = 670, posY = 800,
        text = "Exit",
        visual = buttonColor1,
        font = Font(size = 30)
    )
    private val startButton = Button(
        width = 250, height = 70,
        posX = 1070, posY = 800,
        text = "Start",
        visual = buttonColor2,
        font = Font(size = 30)
    ).apply {
        onMouseClicked ={
            val playerList = mutableListOf<String>()
            if (p1Input.text.trim().isNotEmpty())
                playerList.add(p1Input.text.trim())
            if (p2Input.text.trim().isNotEmpty())
                playerList.add(p2Input.text.trim())
            if (p3Input.text.trim().isNotEmpty())
                playerList.add(p3Input.text.trim())
            if (p4Input.text.trim().isNotEmpty())
                playerList.add(p4Input.text.trim())
            rootService.gameService.startGame(
                playerList
            )
            println(playerList)
        }
    }
    /**
     * reset everything
     */
    override fun refreshAfterEndGame() {
        p1Input.text = ""
        p2Input.text = ""
        p3Input.text = ""
        p4Input.text = ""
        numberOfPlayer = 4
    }
    init {
        opacity = .5
        background = ImageVisual("OldTrafford.png")
        addComponents(
            headlineLabel,
            p1Label, p1Input,
            p2Label, p2Input,
            p3Label, p3Input,
            p4Label, p4Input,
            startButton, quitButton
        )
    }
}

