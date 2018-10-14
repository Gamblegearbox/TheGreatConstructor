package audio;

import libraries.AudioLibrary;
import org.lwjgl.openal.*;
import utils.Logger;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class OpenALAudioEngine
{
    private String defaultDeviceName;
    private long device;
    private long context;

    public void init() {
        defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);

        Logger.getInstance().writeln(">> INITIALISING SOUND ENGINE");
        logAudioInfo();

        int[] attributes = {0};
        context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
    }

    public void setListenerData(){
        alListener3f(AL_POSITION, 0,0,0);
        alListener3f(AL_VELOCITY, 0,0,0);
    }

    public void logAudioInfo()
    {
        String info =
                "\tOPENAL DEVICE:             " + defaultDeviceName +
                "\n";

        Logger.getInstance().write(info);
    }

    public void cleanup(){
        Logger.getInstance().writeln(">> CLEANING UP AUDIO ENGINE");

        AudioLibrary.cleanup();

        alcDestroyContext(context);
        alcCloseDevice(device);
    }


}