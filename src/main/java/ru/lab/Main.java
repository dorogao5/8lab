package ru.lab;

import com.opencsv.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        FileReader filereader;
        try {
            filereader = new FileReader("skill.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        CSVReader csvReader = new CSVReader(filereader);
    }
}