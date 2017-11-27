package engine;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private final int textureID;
    public static final int DIFFUSE = 0;
    public static final int NORMALS = 1;
    public static final int GLOSS = 2;
    public static final int ILLUMINATION = 3;


    public Texture(String _filePath) throws Exception
    {
        PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(_filePath));
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getHeight() * decoder.getWidth());
        decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buffer.flip();

        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    }

    public int getID()
    {
        return textureID;
    }
}
