package br.com.chai;

import java.io.InputStream;

import org.junit.Test;

public class SpreadSheetInputTest {

    @Test
    public void test() throws Exception {
        InputStream in = SpreadSheetInputTest.class.getResourceAsStream("/output_sap.xlsx");
        SpreadSheetInput ssin = new SpreadSheetInput(in);
        ssin.read();
    }
}
