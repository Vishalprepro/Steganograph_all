package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Steganographyimage {

    public static void hideMessage(String ci, String input, String out) throws Exception {
        BufferedImage coverImage = ImageIO.read(new File(ci));
        int width = coverImage.getWidth();
        int height = coverImage.getHeight();
        String messageWithDelimiter = input + "#"; // Add delimiter to indicate end of message

        int messageIndex = 0;
        outerLoop: for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (messageIndex < messageWithDelimiter.length() * 8) {
                    int rgb = coverImage.getRGB(x, y);
                    char currentChar = messageWithDelimiter.charAt(messageIndex / 8);
                    int bit = (currentChar >> (7 - (messageIndex % 8))) & 1; // Extract bit from character
                    rgb = (rgb & 0xFFFFFFFE) | bit; // Set the LSB of the blue component
                    coverImage.setRGB(x, y, rgb);
                    messageIndex++;
                } else {
                    break outerLoop;
                }
            }
        }

        ImageIO.write(coverImage, "png", new File(out));
        System.out.println("Message hidden successfully!");
    }

    public static String extractMessage(String stegoImagePath) {
        StringBuilder extractedMessage = new StringBuilder();
        try {
            BufferedImage stegoImage = ImageIO.read(new File(stegoImagePath));
            int width = stegoImage.getWidth();
            int height = stegoImage.getHeight();
            StringBuilder binaryMessage = new StringBuilder();

            outerLoop: for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = stegoImage.getRGB(x, y);
                    int bit = rgb & 1; // Get the LSB of the blue component
                    binaryMessage.append(bit);

                    if (binaryMessage.length() % 8 == 0) {
                        String byteString = binaryMessage.substring(binaryMessage.length() - 8);
                        int asciiValue = Integer.parseInt(byteString, 2);
                        char character = (char) asciiValue;

                        if (character == '#') {
                            break outerLoop;
                        }

                        extractedMessage.append(character);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractedMessage.toString();
    }

    public Steganographyimage() throws Exception {
        System.out.println("This is a Stegano-image try to inster jpg");
        String coverImage = "D:/encryption of java/demo/src/main/resources/static/aesfull.png";
        // String input = "hdkjckd";
        String out = "D:/encryption of java/demo/src/main/resources/static/Stegno12.png";
        Scanner sc = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        System.out.println("enter the text");
        while (true) {
            String line = sc.nextLine();
            if (line.isEmpty()) {
                break;
            }
            input.append(line).append("\n");
        }

        hideMessage(coverImage, input.toString(), out);
        System.out.println("Stego message:");
        String extractedMessage = extractMessage(out);
        System.out.println(extractedMessage);
    }

}
