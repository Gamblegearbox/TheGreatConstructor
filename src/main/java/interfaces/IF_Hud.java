package interfaces;

import hud.TextItem;

import java.util.Map;

public interface IF_Hud {

    Map<String, TextItem> getHudItems();

    void cleanup();

}
