package rendering;

import de.matthiasmann.twl.utils.PNGDecoder;
import utils.Logger;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private final int textureID;
    public static final int DIFFUSE = 0;
    public static final int NORMALS = 1;
    public static final int GLOSS = 2;
    public static final int ILLUMINATION = 3;
    public static final int GRADIENT_SHADING = 4;
    public static final int GRADIENT_LIGHT_COLOR = 5;


    public Texture(String _filePath)
    {
        ByteBuffer buffer;

        int width = 10; //initial width
        int height = 10; //initial height


        try {
            PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(_filePath));
            width = decoder.getWidth();
            height = decoder.getHeight();

            buffer = ByteBuffer.allocateDirect(4 * height * width);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
        } catch (Exception e){

            Logger.getInstance().writeln("Could not load:" + _filePath + " --> generating default texture");

            int bufferSize = 4 * height * width;
            buffer = ByteBuffer.allocateDirect(bufferSize);

            //Fill buffer with fake data
            byte r = -24;
            byte g = 43;
            byte b = -76;
            byte a = -1;

            for(int i = 0; i < bufferSize; i+=4) {
                buffer.put(i, r);
                buffer.put(i+1, g);
                buffer.put(i+2, b);
                buffer.put(i+3, a);
            }

        }
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST_MIPMAP_NEAREST);
    }

    public int getID()
    {
        return textureID;
    }
}
