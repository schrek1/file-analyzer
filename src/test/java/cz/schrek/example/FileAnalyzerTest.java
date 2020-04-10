package cz.schrek.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class FileAnalyzerTest {

    private static File SOURCE_FILE;
    private static File RESULT_FILE;

    private File outputFile;
    private FileAnalyzer fileAnalyzer;

    @BeforeAll
    static void beforeAll() {
        SOURCE_FILE = new File(FileAnalyzerTest.class.getResource("/test_data.txt").getPath());
        RESULT_FILE = new File(FileAnalyzerTest.class.getResource("/test_result.txt").getPath());
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        fileAnalyzer = new FileAnalyzer();
        outputFile = File.createTempFile("output", ".txt");
    }

    @Test
    public void testSuccessPath() {
        Charset charset = StandardCharsets.UTF_8;
        String sourceFilePath = SOURCE_FILE.getAbsolutePath();
        String outputFilePath = outputFile.getAbsolutePath();

        fileAnalyzer.analyzeFile(charset.displayName(), sourceFilePath, outputFilePath);

        assertThat(outputFile).exists();
        assertThat(outputFile).hasSameTextualContentAs(RESULT_FILE, charset);
    }

    @Test
    public void testBadCharset() {
        String sourceFilePath = SOURCE_FILE.getAbsolutePath();
        String outputFilePath = outputFile.getAbsolutePath();

        assertThatThrownBy(() -> fileAnalyzer.analyzeFile(null, sourceFilePath, outputFilePath))
                .isInstanceOf(RuntimeException.class).hasMessage("Bad charset");

        assertThatThrownBy(() -> fileAnalyzer.analyzeFile("bad_charset", sourceFilePath, outputFilePath))
                .isInstanceOf(RuntimeException.class).hasMessage("Bad charset");
    }

    @Test
    public void testBadInputFile() {
        String charset = StandardCharsets.UTF_8.displayName();
        String outputFilePath = outputFile.getAbsolutePath();

        assertThatThrownBy(() -> fileAnalyzer.analyzeFile(charset, null, outputFilePath))
                .isInstanceOf(RuntimeException.class).hasMessage("Bad input file");

        assertThatThrownBy(() -> fileAnalyzer.analyzeFile(charset, "not_exist.txt", outputFilePath))
                .isInstanceOf(RuntimeException.class).hasMessage("Bad input file");
    }

    @Test
    public void testBadOutputFile() {
        String charset = StandardCharsets.UTF_8.displayName();
        String sourceFilePath = SOURCE_FILE.getAbsolutePath();

        assertThatThrownBy(() -> fileAnalyzer.analyzeFile(charset, sourceFilePath, null))
                .isInstanceOf(RuntimeException.class).hasMessage("Bad output file");

        assertThatThrownBy(() -> fileAnalyzer.analyzeFile(charset, sourceFilePath, "not_exist.txt"))
                .isInstanceOf(RuntimeException.class).hasMessage("Bad output file");
    }

    @AfterEach
    void tearDown() {
        if (outputFile != null && outputFile.exists()) outputFile.delete();
    }

}
