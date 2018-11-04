package libraries;


import utils.Logger;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class AudioLibrary {

    public static final Map<String, Integer> audioBufferIdMap = new HashMap<>();


    public static void loadAudioFiles(String _filePath) throws Exception{

        List<String> fileContent = utils.Utils.readAllLines(_filePath);
        List<String> audioDataFromFile = new ArrayList<>();


        //PARSE FILE
        for(String line : fileContent){

            if(line.startsWith("#")){
                continue;
            }

            if(line.startsWith("SOUND")){
                audioDataFromFile.add(line);
            }

            if(line.startsWith("MUSIC")){
                audioDataFromFile.add(line);
            }
        }

        Logger.getInstance().writeln(">>> LOADING AUDIO FILES...");

        for(int i = 0; i < audioDataFromFile.size(); i++){

            String[] line = audioDataFromFile.get(i).split("=");
            String objectData = line[line.length - 1];

            String[] objectDataTokens = objectData.split(";");
            String tag = objectDataTokens[0].trim().replaceAll("\"", "");
            String path = objectDataTokens[1].trim().replaceAll("\"", "");


            loadAudioFile(tag, path);
        }

    }

    private static void loadAudioFile(String _tag, String _path){

        //Allocate space to store return information from the function
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);
        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(_path, channelsBuffer, sampleRateBuffer);

        //Retreive the extra information that was stored in the buffers by the function
        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        //Free the space we allocated earlier
        stackPop();
        stackPop();

        //Find the correct OpenAL format
        int format = -1;
        if (channels == 1) {
            format = AL_FORMAT_MONO16;
        } else if (channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        //Request space for the buffer
        int bufferId = alGenBuffers();

        //Save ID to BufferList
        audioBufferIdMap.put(_tag, bufferId);

        //Send the data to OpenAL
        alBufferData(bufferId, format, rawAudioBuffer, sampleRate);

        //Free the memory allocated by STB
        free(rawAudioBuffer);
    }

    public static void cleanup(){

        for(int bufferId : audioBufferIdMap.values()){
            alDeleteBuffers(bufferId);
        }

    }

}
