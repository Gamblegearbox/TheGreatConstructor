package game;

import engine.core.EngineOptions;
import engine.core.Window;
import engine.gameEntities.GameEntity;
import engine.gameEntities.TextEntity;
import engine.interfaces.IHud;
import engine.mesh.Mesh;
import engine.utils.DebugMeshes;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;


public class Hud implements IHud {

    private static final int FONT_COLS = 16;
    private static final int FONT_ROWS = 16;
    private static final float COMPASS_NEEDLE_SIZE = 30.0f;
    private static final float DEBUG_DETAILS_SIZE = 30.0f;
    private static final float TEXT_SIZE = 30f;

    private static final String FONT_TEXTURE = "/textures/Font_Menlo.png";
    private final GameEntity[] gameEntities;
    private final TextEntity statusTextItem;
    private final GameEntity compassItem;
    private GameEntity debugCircle_maxFrontAxleForce;
    private GameEntity debugCircle_maxRearAxleForce;
    private GameEntity debugLine_FrontForce;
    private GameEntity debugLine_RearForce;

    public Hud(String statusText) throws Exception
    {
        this.statusTextItem = new TextEntity(statusText, FONT_TEXTURE, FONT_COLS, FONT_ROWS);
        this.statusTextItem.getMesh().getMaterial().setColor(new Vector3f(1, 1, 1));
        this.statusTextItem.setScale(TEXT_SIZE);

        // Create compass
        Mesh mesh = DebugMeshes.buildHUDCompassNeedle();
        mesh.setMaterial(Materials.RED);
        compassItem = new GameEntity(mesh);
        compassItem.setScale(COMPASS_NEEDLE_SIZE);

        if(EngineOptions.DEBUG)
        {
            // Create debug meshes
            mesh = DebugMeshes.buildHUDCircle(16);
            mesh.setMaterial(Materials.WHITE);
            debugCircle_maxFrontAxleForce = new GameEntity(mesh);
            debugCircle_maxFrontAxleForce.setScale(DEBUG_DETAILS_SIZE);

            mesh = DebugMeshes.buildHUDCircle(16);
            mesh.setMaterial(Materials.WHITE);
            debugCircle_maxRearAxleForce = new GameEntity(mesh);
            debugCircle_maxRearAxleForce.setScale(DEBUG_DETAILS_SIZE);

            mesh = DebugMeshes.buildHUDline();
            mesh.setMaterial(Materials.GREEN);
            debugLine_FrontForce = new GameEntity(mesh);
            debugLine_FrontForce.setScale(DEBUG_DETAILS_SIZE);

            mesh = DebugMeshes.buildHUDline();
            mesh.setMaterial(Materials.GREEN);
            debugLine_RearForce = new GameEntity(mesh);
            debugLine_RearForce.setScale(DEBUG_DETAILS_SIZE);

            // Create list that holds the items that compose the HUD
            gameEntities = new GameEntity[]{statusTextItem, compassItem, debugCircle_maxFrontAxleForce, debugCircle_maxRearAxleForce, debugLine_RearForce, debugLine_FrontForce};
        }
        else
        {
            gameEntities = new GameEntity[]{statusTextItem, compassItem};
        }

        updateCompass(0);
    }

    public void setStatusText(String statusText)
    {
        this.statusTextItem.setText(statusText);
    }

    public void updateCompass(float angle)
    {
        compassItem.setRotation(0, 0, -angle + 180);
    }

    //@Override
    public GameEntity[] getGameEntities()
    {
        return gameEntities;
    }

    public void updateSize(Window window)
    {
        float margin = 50;
        float finalMargin = margin + COMPASS_NEEDLE_SIZE;
        this.compassItem.setPosition(window.getWidth() - finalMargin, finalMargin, 0);

        finalMargin = margin + TEXT_SIZE;
        this.statusTextItem.setPosition(margin, window.getHeight() - finalMargin, 0);

        if(EngineOptions.DEBUG)
        {
            finalMargin = margin + DEBUG_DETAILS_SIZE;
            float frontForceMeterY = window.getHeight() / 2f - finalMargin;
            float rearForceMeterY = frontForceMeterY + finalMargin * 2;
            debugCircle_maxFrontAxleForce.setPosition(finalMargin, frontForceMeterY, -0.1f);
            debugCircle_maxRearAxleForce.setPosition(finalMargin, rearForceMeterY, -0.1f);
            debugLine_FrontForce.setPosition(finalMargin, frontForceMeterY, 0);
            debugLine_RearForce.setPosition(finalMargin, rearForceMeterY, 0);
        }
    }
}
