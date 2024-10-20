package com.example.demo;

import java.util.Scanner;
public class Centeral {
    public static void main(String args[]) throws Exception{
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Select the steganogrpahy you needed");
        System.out.println("1.stegano-Image   2.stegano-video  3.stegano-audio");
        int n = sc.nextInt();
        switch (n) {
            case 1 ->new Steganographyimage();
            case 2 -> new Steganographyvideo();
            case 3 ->  new Steganographyaudio();
            default -> System.out.println("PLease select the above one.");
            
              
        }
        

    }
    
}
