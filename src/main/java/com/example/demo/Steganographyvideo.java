package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class Steganographyvideo {

    public static void hideVideo(String imagePath, String videoPath, String outputImagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        byte[] videoBytes = FileUtils.readFileToByteArray(new File(videoPath));
        int width = image.getWidth();
        int height = image.getHeight();

        int videoIndex = 0;
        boolean videoFinished = false;

        for (int y = 0; y < height && !videoFinished; y++) {
            for (int x = 0; x < width && !videoFinished; x++) {
                int pixel = image.getRGB(x, y);

                for (int i = 0; i < 3 && videoIndex < videoBytes.length; i++) {
                    int videoByte = (videoBytes[videoIndex++] & 0xFF);
                    pixel = (pixel & ~(0xFF << (8 * i))) | (videoByte << (8 * i));
                }

                image.setRGB(x, y, pixel);
                if (videoIndex >= videoBytes.length) {
                    videoFinished = true;
                }
            }
        }

        ImageIO.write(image, "png", new File(outputImagePath));
    }

    public static byte[] extractVideo(String imagePath, int videoLength) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        int width = image.getWidth();
        int height = image.getHeight();

        byte[] videoBytes = new byte[videoLength];
        int videoIndex = 0;
        boolean videoFinished = false;

        for (int y = 0; y < height && !videoFinished; y++) {
            for (int x = 0; x < width && !videoFinished; x++) {
                int pixel = image.getRGB(x, y);

                for (int i = 0; i < 3 && videoIndex < videoBytes.length; i++) {
                    int videoByte = (pixel >> (8 * i)) & 0xFF;
                    videoBytes[videoIndex++] = (byte) videoByte;
                }

                if (videoIndex >= videoBytes.length) {
                    videoFinished = true;
                }
            }
        }

        return videoBytes;
    }

    public static void saveVideo(byte[] videoBytes, String outputPath) throws IOException {
        FileUtils.writeByteArrayToFile(new File(outputPath), videoBytes);
    }

    public Steganographyvideo() throws IOException {
        System.out.println("This is a Stegano-video  try to inster mp4");
        String imagePath = "D:/encryption of java/demo/src/main/resources/static/aesfull.png";
        String videoPath = "D:/encryption of java/demo/src/main/resources/static/movieapp-video.mp4";
        String outputImagePath = "D:/encryption of java/demo/src/main/resources/static/aesfull.pngStegnoVideo.png";
        String extractedVideoPath = "D:/encryption of java/demo/src/main/resources/static/aesfull.pngextracted_video.mp4";

        hideVideo(imagePath, videoPath, outputImagePath);
        System.out.println("Video hidden successfully!");

        int videoLength = (int) new File(videoPath).length();
        byte[] videoBytes = extractVideo(outputImagePath, videoLength);
        saveVideo(videoBytes, extractedVideoPath);
        System.out.println("Video extracted successfully!");
    }
}
