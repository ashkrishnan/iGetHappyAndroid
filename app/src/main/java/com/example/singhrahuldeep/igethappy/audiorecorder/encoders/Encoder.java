package com.example.singhrahuldeep.igethappy.audiorecorder.encoders;

public interface Encoder {
    public void encode(short[] buf);

    public void close();
}
