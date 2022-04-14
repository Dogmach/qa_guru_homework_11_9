package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.ToolsForTest.creatTempDir;
import static tests.ToolsForTest.unZip;

@DisplayName("Общий тест по работе с файлами разной структуры")
public class OpenZipAndTest {

    public static String pathResources = "src/test/resources/files/";
    public static String zipFileExecute = "controlfile.zip";
    public static String pdfFile = "quick_start.pdf";
    public static String xlsFile = "qakurs.xlsx";
    public static String csvFile = "qaguru1.csv";
    public static String jsonFile = "testExample.json";
    public static String tempDir;

    @BeforeAll
    static void beforeAll() throws Exception {
        tempDir = creatTempDir();
        unZip(zipFileExecute);
    }

    @Test
    @DisplayName("- Общий Тест по разархивированию zip файла с контролем структуры и достоверности данных в извлеченных файлах")
    void generalTest() throws Exception {
        unZipPdfTest();
        qakurXlsxTest();
        qaguruCsvTest();
        openPDFTest();
        jsonJacksonTest();
    }

    @Test
    @DisplayName("проверка структуры и достоверности содержимого  файла формата Zip с извлечением файла PDF")
    void unZipPdfTest() throws Exception {
        String path = "src/test/resources/files/" + zipFileExecute;
        ZipFile zipFile = new ZipFile(path);
        ZipEntry zipEntry = zipFile.getEntry(pdfFile);
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        PDF pdf = new PDF(inputStream);
        assertThat(pdf.text).contains("Структура HTML-документа");
    }

    @Test
    @DisplayName("проверка структуры и достоверности содержимого  файла формата PDF ")
    void openPDFTest() throws Exception {
        String path = tempDir + pdfFile;
        System.out.println(path);
        File pdfDownload = new File(path);
        PDF pdf = new PDF(pdfDownload);
        assertThat(pdf.text).contains("Структура HTML-документа");
        assertThat(pdf.numberOfPages).isEqualTo(87);
    }

    @Test
    @DisplayName("проверка структуры и достоверности содержимого файла формата Xlsx ")
    void qakurXlsxTest() {
        String path = tempDir + xlsFile;
        File xlsDownload = new File(path);
        XLS xls = new XLS(xlsDownload);
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(1)
                .getCell(1)
                .getStringCellValue()).contains("Работаем с файлами");
    }

    @Test
    @DisplayName("проверка структуры и достоверности содержимого  файла формата CSV ")
    void qaguruCsvTest() throws Exception {
        String path = tempDir + csvFile;
        File is = new File(path);
        CSVReader csv = new CSVReader(new FileReader(path));
        List<String[]> content = csv.readAll();
        assertThat(content.get(1)).contains("Артём Eрошенко",
                "Aлексей Виноградов",
                "Роман Орлов",
                "Станислав Васенков",
                "Дмитрий Тучс",
                "Евгений Данилов");
    }

    @Test
    @DisplayName("Проверка формат и достоверность данных файла JSON ")
    void jsonJacksonTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestExample jsonObject = mapper.readValue(new File(pathResources + jsonFile), TestExample.class);
        assertThat(jsonObject.tread).isEqualTo("QA");
        assertThat(jsonObject.subjects).contains("Selenide", "Jenkins", "Selenoid");
    }
}
