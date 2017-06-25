package com.markert.bittrex.common;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 * Created by admin on 6/25/17.
 */
public class AudioPlayer implements LineListener {
    /**
     * this flag indicates whether the playback completes or not.
     */
    private boolean playing;
    private Clip audioClip;

    /**
     * Play a given audio file.
     * @param audioFilePath Path of the audio file.
     */
    public void play(String audioFilePath) {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(audioFilePath);
            InputStream bufferedIn = new BufferedInputStream(inputStream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.addLineListener(this);
            audioClip.open(audioStream);
            audioClip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    /**
     * Listens to the START and STOP events of the audio line.
     */
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
        if (type == LineEvent.Type.START) {
            playing = true;
        } else if (type == LineEvent.Type.STOP) {
            playing = false;
            audioClip.close();
        }
    }

    public boolean isPlaying() {
        return playing;
    }
}
