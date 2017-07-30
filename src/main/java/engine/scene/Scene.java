package engine.scene;

import engine.gameEntities.GameEntity;
import engine.mesh.Mesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private Map<Mesh, List<GameEntity>> meshMap;
    private SceneLight sceneLight;

    public Scene()
    {
        meshMap = new HashMap();
    }

    public Map<Mesh, List<GameEntity>> getGameMeshes()
    {
        return meshMap;
    }

    public void setGameItems(ArrayList<GameEntity> gameEntities)
    {
        for(GameEntity gameEntity : gameEntities)
        {
            Mesh mesh = gameEntity.getMesh();

            List<GameEntity> list = meshMap.get(mesh);
            if ( list == null )
            {
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(gameEntity);
        }
    }

    public void cleanup()
    {
        for (Mesh mesh : meshMap.keySet())
        {
            mesh.cleanUp();
        }
    }

    public SceneLight getSceneLight()
    {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight)
    {
        this.sceneLight = sceneLight;
    }

}