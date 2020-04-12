package interfaces;
import game.MainGame;
import hud.TextItem;
import java.util.Map;

public interface IF_HudState {

    void init();
    void update(float _deltaTime);
    void cleanup();

    Map<String, TextItem> getHudItems();
    TextItem getItemByTag(String _tag);
}
