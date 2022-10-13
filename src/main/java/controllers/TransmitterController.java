package controllers;

import protocols.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TransmitterController {
    private final RdtTransceiver rdtTransceiver;
    private final SlidingWindowTransceiver gbnTransceiver;
    private final SlidingWindowTransceiver srTransceiver;

    private final int atPort;
    private final int toPort;

    private List<byte[]> getPacketData(String s, int packetSize) {
        List<byte[]> packetData = new ArrayList<>();
        int packetCount = ((s.length() - 1) / packetSize + 1);
        for (int i = 0; i < packetCount-1; i++) {
            byte[] bytes = new byte[packetSize];
            for (int j = 0; j < packetSize; j++) {
                bytes[j] = (byte) s.charAt(i * packetSize + j);
            }
            packetData.add(bytes);
        }

        byte[] lastBytes = new byte[s.length()%packetSize];
        for(int i=0;i<s.length()%packetSize;i++){
            lastBytes[i]=(byte)s.charAt((packetCount-1)*packetSize+i);
        }
        packetData.add(lastBytes);

        return packetData;
    }

    private List<byte[]> getPacketData(byte[] allBytes, int packetSize) {
        List<byte[]> packetData = new ArrayList<>();
        int packetCount = ((allBytes.length - 1) / packetSize + 1);
        for (int i = 0; i < packetCount-1; i++) {
            byte[] bytes = new byte[packetSize];
            System.arraycopy(allBytes,i*packetSize,bytes,0,packetSize);
            packetData.add(bytes);
        }

        byte[] lastBytes = new byte[allBytes.length%packetSize];
        System.arraycopy(allBytes,
                (packetCount-1)*packetSize,
                lastBytes,
                0,
                allBytes.length%packetSize);
        packetData.add(lastBytes);

        return packetData;
    }

    public TransmitterController(int atPort, int toPort) {
        this.atPort = atPort;
        this.toPort = toPort;
        this.rdtTransceiver = new RdtTransceiver();
        this.gbnTransceiver = new GbnTransceiver();
        this.srTransceiver = new SrTransceiver();
    }

    public void sendWithRdt(String data,
                            double lossProbability,
                            int owtMs,
                            int maxDelayMs) {
        var packetData = getPacketData(data, 1);
        rdtTransceiver.sendPackets(packetData, lossProbability, atPort, toPort, owtMs, maxDelayMs);
    }

    public void sendWithGbn(String data,
                            double lossProbability,
                            int windowSize,
                            int owtMs,
                            int maxDelayMs) {
        var packetData = getPacketData(data, 1);
        gbnTransceiver.sendPackets(
                packetData, lossProbability, atPort, toPort, windowSize, owtMs, maxDelayMs);
        System.gc();
    }

    public void sendWithSr(String data,
                           double lossProbability,
                           int windowSize,
                           int owtMs,
                           int maxDelayMs) {
        var packetData = getPacketData(data, 1);
        srTransceiver.sendPackets(
                packetData, lossProbability, atPort, toPort, windowSize, owtMs, maxDelayMs);
        System.gc();
    }

    public void sendFileWithSr(String fileName,
                               int windowSize,
                               int owtMs,
                               int maxDelayMs) {
        byte[] fileBytes;

        try {
            fileBytes = Files.readAllBytes(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        var packetData = getPacketData(fileBytes, Constants.DATA_BUFFER_SIZE - 10);

        srTransceiver.sendPackets(
                packetData, 0, atPort, toPort, windowSize, owtMs, maxDelayMs);
        System.gc();
    }
}
