package audio;

import static org.lwjgl.openal.AL10.*;

public class OpenALAudioSource {

    private final int sourceId;

    public OpenALAudioSource() {

        //Request a source
        sourceId = alGenSources();
        alSourcef(sourceId, AL_GAIN, 1f);
        alSourcef(sourceId, AL_PITCH, 1f);
        alSourcef(sourceId, AL_VELOCITY, 1f);
        alSource3f(sourceId, AL_POSITION, 0, 0, 0);

    }

    public void play(int _bufferId){
        //Assign the audio we just loaded to the source
        alSourcei(sourceId, AL_BUFFER, _bufferId);

        //Play the audio
        alSourcePlay(sourceId);
    }

    public void stop(){
        alSourceStop(sourceId);
    }

    public void setPosition(float _x, float _y, float _z){
        alSource3f(sourceId, AL_POSITION, _x, _y, _z);
    }

    public void cleanup(){
        alDeleteSources(sourceId);
    }

}
