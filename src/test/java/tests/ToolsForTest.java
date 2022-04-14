package tests;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static tests.OpenZipAndTest.pathResources;
import static tests.OpenZipAndTest.tempDir;

public class ToolsForTest {

    public static void unZip(String nameZipFile) throws Exception {
        String zipFile = pathResources + nameZipFile;
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                System.out.printf("File name: %s \n", name);

                FileOutputStream fout = new FileOutputStream(tempDir + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        }
    }

    public static String creatTempDir() throws Exception {
        String addDirTemp = "build\\temp\\";
        Path str = Paths.get(pathResources);
        String pathAbs = String.valueOf(str.toAbsolutePath());
        String tempDir1 = pathAbs.substring(0, pathAbs.indexOf("src")) + addDirTemp;
        Path pathTempDir = Paths.get(tempDir1);
        if (Files.exists(pathTempDir)) {
            System.out.println(addDirTemp + " - directory  exists");
        } else {
            Path donePath = Files.createDirectory(pathTempDir);
        }
        return tempDir1;
    }
}
