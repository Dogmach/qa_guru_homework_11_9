package com.example;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SelenideFileTest {

    @Test
    void selenideDownloadTest() throws Exception {
        Selenide.open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadedFile = Selenide.$("#raw-url").download();
        try (InputStream is = new FileInputStream(downloadedFile)) {
            assertThat(new String(is.readAllBytes(), StandardCharsets.UTF_8))
                    .contains("This repository is the home of the next generation of JUnit");
        }
        String readString = Files.readString(downloadedFile.toPath(), StandardCharsets.UTF_8);
    }

    @Test
    void uploadSelenideTest(){
        Selenide.open("https://the-internet.herokuapp.com/upload");
        Selenide.$("input[type='file']")
//                .uploadFile(new File("C:\\Users\\act1v\\IdeaProjects\\QaGuru\\qa_guru_homework_11_9\\src\\test\\resources\\files\\file.txt")); // bad practice
                .uploadFromClasspath("files/file.txt");
        Selenide.$("#file-submit").click();
        Selenide.$("div.example").shouldHave(Condition.text("File Uploaded!"));
        Selenide.$("#uploaded-files").shouldHave(Condition.text("file.txt"));
    }
}
