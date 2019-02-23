package hud;

import core.EngineOptions;
import rendering.Texture;
import utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

public class FontTexture {


    private static final String IMAGE_FORMAT = "png";
    private Font font;
    private String charsetName;
    private Map<Character, CharInfo> charMap;
    private Texture texture;
    private int width;
    private int height;

    public FontTexture(Font _font, String _charsetName){

        font = _font;
        charsetName = _charsetName;

        charMap = new HashMap<>();

        buildTexture();
    }

    public CharInfo getCharInfo(char _c){
        return charMap.get(_c);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Texture getTexture(){
        return texture;
    }

    private String getAllAvailableCharacters(String _charsetName){

        CharsetEncoder ce = Charset.forName(_charsetName).newEncoder();
        StringBuilder result = new StringBuilder();

        for(char c = 0; c < Character.MAX_VALUE; c++){
            if(ce.canEncode(c)){
                result.append(c);
            }
        }

        return result.toString();
    }

    private void buildTexture(){

        //Get font metrics for each character for the selected font by using image
        BufferedImage img = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = img.createGraphics();

        g2D.setFont(font);
        FontMetrics fontMetrics = g2D.getFontMetrics();

        String allChars = getAllAvailableCharacters(charsetName);

        width = 0;
        height = 0;

        for(char c : allChars.toCharArray()){
            CharInfo charInfo = new CharInfo(width, fontMetrics.charWidth(c));
            charMap.put(c, charInfo);
            width += charInfo.getWidth();
            height = Math.max(height, fontMetrics.getHeight());
        }

        g2D.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        fontMetrics = g2D.getFontMetrics();
        g2D.setColor(Color.WHITE);
        g2D.drawString(allChars, 0, fontMetrics.getAscent());
        g2D.dispose();

        if(EngineOptions.DEBUG_MODE){

            String path = ".\\res\\TestGameContent\\font\\generatedFontTexture.png";
            try {
                ImageIO.write(img, IMAGE_FORMAT, new java.io.File(path));
            } catch (Exception e) {
                Logger.getInstance().writeln("Could not write font texture to " + path);
                e.printStackTrace();
            }
        }

        // put image into a byte buffer
        InputStream inputStream;

        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ImageIO.write(img, IMAGE_FORMAT, out);
            out.flush();
            inputStream = new ByteArrayInputStream(out.toByteArray());
            texture = new Texture(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static class CharInfo {

        private final int startX;
        private final int width;

        public CharInfo(int _startX, int _width){

            startX = _startX;
            width = _width;
        }

        public int getStartX(){
            return startX;
        }

        public int getWidth(){
            return width;
        }

    }


}


