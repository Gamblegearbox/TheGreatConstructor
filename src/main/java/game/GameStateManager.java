package game;

import utils.Logger;

public class GameStateManager {

    private static GameStateManager gameStateManager = null;
    private final GameBase game;
    private boolean menuActive;
    private float timeModifier; //USED TO CONTROL TIME IN GAME SCENE

    public GameStateManager(GameBase _game){
        Logger.getInstance().writeln(">> CREATING GAME STATE MANAGER [WANNA BE SINGLETON]");
        game = _game;
        gameStateManager = this;
        menuActive = false;
        timeModifier = 1.0f;
    }

    public void leaveMenu(int _targetHud){
        menuActive = false;
        timeModifier = 1.0f;
        changeHudState(_targetHud);
    }
    public void enterMenu(){
        menuActive = true;
        timeModifier = 0.0f;
        changeHudState(2);
    }

    public boolean isMenuActive(){
        return menuActive;
    }

    public static GameStateManager getInstance(){
        return gameStateManager;
    }

    public float getTimeModifier(){
        return timeModifier;
    }

    public void changeGameState(int _index){
        game.changeGameState(_index);
    }
    public void changeHudState(int _index){
        game.changeHudState(_index);
    }
    public void quitGame(){
        game.quit();
    }
    public void resetGame() {game.reset();}

    public void saveTime(float _time){
        //TODO: save time to file

    }


}
