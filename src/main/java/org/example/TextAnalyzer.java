package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TextAnalyzer {
    private final Path file;
    private final int topN;
    private final int phraseSize;

    public TextAnalyzer(String file, int topN, int phraseSize) {
        this.file = Paths.get(file);
        this.topN = topN;
        this.phraseSize = phraseSize;
    }

    public void analyze() throws IOException {
        String content = new String(Files.readAllBytes(file));
        String[] words = content.toLowerCase().split("\\P{L}+");
        String[] sentences = content.split("[.!?]\\s*");
        Map<String, Integer> phraseFrequency = new HashMap<>();

        for (int i = 0; i <= words.length - phraseSize; i++) {
            StringJoiner joiner = new StringJoiner(" ");
            for (int j = 0; j < phraseSize; j++) {
                joiner.add(words[i + j]);
            }
            String phrase = joiner.toString();
            phraseFrequency.merge(phrase, 1, Integer::sum);
        }

        System.out.println("+-----------------+-------+");
        System.out.printf("| Number of words: | %5d |\n", words.length);
        System.out.println("+-----------------+-------+");
        System.out.printf("| Number of sentences: | %5d |\n", sentences.length);
        System.out.println("+-----------------+-------+");

        printTopPhrases(phraseFrequency);
    }

    private void printTopPhrases(Map<String, Integer> phraseFrequency) {
        System.out.println("+-------------------------+-------+");
        System.out.println("| Phrases                 | Count |");
        System.out.println("+-------------------------+-------+");
        phraseFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(topN)
                .forEach(entry -> System.out.printf("| %-23s | %5d |\n", entry.getKey(), entry.getValue()));
        System.out.println("+-------------------------+-------+");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage: java TextAnalyzer <file_path> <top_n_phrases> <phrase_size>");
            System.exit(1);
        }
        new TextAnalyzer(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2])).analyze();
    }
}
