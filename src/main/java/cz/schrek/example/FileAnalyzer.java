package cz.schrek.example;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileAnalyzer {

    private final List<Rule> rules;

    /**
     * Analyzer with default rules
     */
    public FileAnalyzer() {
        rules = RuleFactory.getDefaultRules();
    }

    /**
     * Analyzer with custom rules
     *
     * @param rules custom list of rules
     */
    public FileAnalyzer(List<Rule> rules) {
        this.rules = rules;
    }


    /**
     * Method for analyzing text in input file and write histogram to output file
     *
     * @param encoding   encoding of files
     * @param inputFile  path to input file
     * @param outputFile path to output file
     * @throws RuntimeException when IO exception occurred
     */
    public void analyzeFile(String encoding, String inputFile, String outputFile) {
        Charset charset = getCharset(encoding);

        try (
                BufferedReader reader = getReader(inputFile, charset);
                BufferedWriter writer = getWriter(outputFile, charset)
        ) {
            Map<String, Integer> results = new HashMap<>();

            while (reader.ready()) {
                String[] words = reader.readLine().split("\\s+");
                for (String word : words) {
                    if (isMatching(word)) addToMap(word, results);
                }
            }

            List<Map.Entry<String, Integer>> sorted = results.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toList());

            int resultSize = sorted.size();
            int lastElementIndex = resultSize - 1;

            for (int i = 0; i < resultSize; i++) {
                Map.Entry<String, Integer> pair = sorted.get(i);
                writer.write(pair.getKey() + " " + pair.getValue());
                if (i != lastElementIndex) writer.newLine();
            }

            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException("IO problem occurred", e);
        }

    }

    private boolean isMatching(String word) {
        return rules.stream().anyMatch(rule -> rule.isMatching(word));
    }

    private void addToMap(String word, Map<String, Integer> map) {
        int count = map.getOrDefault(word, 0) + 1;
        map.put(word, count);
    }

    private Charset getCharset(String encodingName) throws RuntimeException {
        try {
            return Charset.forName(encodingName);
        } catch (Exception e) {
            throw new RuntimeException("Bad charset", e);
        }
    }

    private BufferedReader getReader(String filePath, Charset charset) throws RuntimeException {
        try {
            return new BufferedReader(new FileReader(new File(filePath), charset));
        } catch (Exception e) {
            throw new RuntimeException("Bad input file", e);
        }
    }

    private BufferedWriter getWriter(String filePath, Charset charset) throws RuntimeException {
        try {
            File file = new File(filePath);
            file.mkdirs();
            file.createNewFile();
            return new BufferedWriter(new FileWriter(file, charset, false));
        } catch (Exception e) {
            throw new RuntimeException("Bad output file", e);
        }
    }
}
