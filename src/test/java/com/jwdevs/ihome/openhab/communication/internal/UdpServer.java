package com.jwdevs.ihome.openhab.communication.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.URL;
import java.sql.Timestamp;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class UdpServer {
    UdpServer server;

    public void runServer() throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(2222);
        byte[] receiveData = new byte[1024];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            System.out.println("RECEIVED: " + sentence);
            playDoorBellSound();
        }
    }

    private void playDoorBellSound() {
        try {
            // Open an audio input stream.
            URL url = this.getClass().getClassLoader().getResource("doorbell.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        // UdpServer server = new UdpServer();
        // server.runServer();

        Timestamp startTime = Timestamp.valueOf("2010-11-06 08:00:00");
        System.out.println(startTime.getTime());

    } // - See more at: https://systembash.com/a-simple-java-udp-server-and-udp-client/#sthash.SQ2LxGGt.dpuf

}
