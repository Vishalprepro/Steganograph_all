package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

public class Steganographyaudio {

    public static void convertM4AToWAV(String inputPath, String outputPath) throws EncoderException {
        File source = new File(inputPath);
        File target = new File(outputPath);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("pcm_s16le");
        audio.setChannels(2);
        audio.setSamplingRate(44100);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("wav");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(source), target, attrs);
    }

    public static void hideAudio(String imagePath, String audioPath, String outputImagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        byte[] audioBytes = FileUtils.readFileToByteArray(new File(audioPath));
        int width = image.getWidth();
        int height = image.getHeight();

        int audioIndex = 0;
        boolean audioFinished = false;

        for (int y = 0; y < height && !audioFinished; y++) {
            for (int x = 0; x < width && !audioFinished; x++) {
                int pixel = image.getRGB(x, y);

                for (int i = 0; i < 3 && audioIndex < audioBytes.length; i++) {
                    int audioByte = (audioBytes[audioIndex++] & 0xFF);
                    pixel = (pixel & ~(0xFF << (8 * i))) | (audioByte << (8 * i));
                }

                image.setRGB(x, y, pixel);
                if (audioIndex >= audioBytes.length) {
                    audioFinished = true;
                }
            }
        }

        ImageIO.write(image, "png", new File(outputImagePath));
    }

    public static byte[] extractAudio(String imagePath, int audioLength) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        int width = image.getWidth();
        int height = image.getHeight();

        byte[] audioBytes = new byte[audioLength];
        int audioIndex = 0;
        boolean audioFinished = false;

        for (int y = 0; y < height && !audioFinished; y++) {
            for (int x = 0; x < width && !audioFinished; x++) {
                int pixel = image.getRGB(x, y);

                for (int i = 0; i < 3 && audioIndex < audioBytes.length; i++) {
                    int audioByte = (pixel >> (8 * i)) & 0xFF;
                    audioBytes[audioIndex++] = (byte) audioByte;
                }

                if (audioIndex >= audioBytes.length) {
                    audioFinished = true;
                }
            }
        }

        return audioBytes;
    }

    public static void saveAudio(byte[] audioBytes, String outputPath) throws IOException {
        FileUtils.writeByteArrayToFile(new File(outputPath), audioBytes);
    }

    public Steganographyaudio() throws Exception {
        System.out.println("This is a Stegano-audio try to inster m4a");
        String imagePath = "D:/encryption of java/demo/src/main/resources/static/aesfull.png";
        String audioPath = "D:/encryption of java/demo/src/main/resources/static/Recording.m4a";
        String wavPath = "D:/encryption of java/demo/src/main/resources/static/audio.wav";
        String outputImagePath = "D:/encryption of java/demo/src/main/resources/static/Stegno1.png";
        String extractedAudioPath = "D:/encryption of java/demo/src/main/resources/static/extracted_audio.wav";

        // Convert .m4a to .wav
        convertM4AToWAV(audioPath, wavPath);

        hideAudio(imagePath, wavPath, outputImagePath);
        System.out.println("Audio hidden successfully!");

        int audioLength = (int) new File(wavPath).length();
        byte[] audioBytes = extractAudio(outputImagePath, audioLength);
        saveAudio(audioBytes, extractedAudioPath);
        System.out.println("Audio extracted successfully!");
    }
}
