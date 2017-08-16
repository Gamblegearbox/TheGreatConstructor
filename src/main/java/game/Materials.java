package game;

import engine.shading.Material;
import org.joml.Vector3f;

public class Materials {
    public static final Material CAP_MAT = new Material(new Vector3f(0.87f, 0.86f, 0.84f), 0f);
    public static final Material WHITE = new Material(new Vector3f(0.87f, 0.86f, 0.84f), 0f);
    public static final Material RED = new Material(new Vector3f(0.6f, 0.2f, 0.2f), 0.75f);
    public static final Material GREEN = new Material(new Vector3f(0.2f, 0.6f, 0.2f), 0.75f);
    public static final Material BLUE = new Material(new Vector3f(0.2f, 0.2f, 0.6f), 0.75f);
}
