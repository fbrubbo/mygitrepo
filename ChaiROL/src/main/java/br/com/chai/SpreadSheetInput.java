package br.com.chai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SpreadSheetInput {
    private InputStream is = null;



    public SpreadSheetInput(final File file) {
        try {
            this.is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Arquivo " + file.getAbsolutePath() + " não encontrado!", e);
        }
    }
    public SpreadSheetInput(final InputStream is) {
        this.is = is;
    }


    public void read() throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(1);

        for (Row row : sheet) {
            for (Cell cell : row) {
                System.out.print(cell.toString() + "\t" );
            }
            System.out.println();
        }

    }



}
