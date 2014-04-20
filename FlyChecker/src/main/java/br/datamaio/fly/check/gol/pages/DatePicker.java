package br.datamaio.fly.check.gol.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DatePicker {
    @FindBy(id="ui-datepicker-div")
    private WebElement datepicker;

    private final WebDriver driver;

    public DatePicker(final WebDriver driver) {
        this.driver = driver;
    }

    public void open(){
        datepicker.click();
        new WebDriverWait(driver, 30, 10).until(elementToBeClickable(By.id("ui-datepicker-div")));
    }

    @Test
    public void test(){
        Path f = Paths.get("chromedriver.exe");
        System.setProperty("webdriver.chrome.driver",f.toAbsolutePath().toString());
        WebDriver driver = new ChromeDriver();
        driver.get("https://compre2.voegol.com.br/Search.aspx");

        WebElement ida = driver.findElement(By.id("field-ida"));
    }


//    /div[@class='ui-datepicker-group ui-datepicker-group-first']
//        /div[@class='ui-datepicker-header ui-widget-header ui-helper-clearfix ui-corner-left']
//            /a    <-
//        /div[@class='ui-datepicker-title']
//            /span[@class='ui-datepicker-month']
//            /span[@class='ui-datepicker-year']
//        /table[@class='ui-datepicker-calendar']
//            /tbody
//                /td
//                    /span           disabled
//                    /a              enabled
//                        text        dia
//
//
//    /div[@class='ui-datepicker-group ui-datepicker-group-last']
//        /div[@class='ui-datepicker-header ui-widget-header ui-helper-clearfix ui-corner-right']
//            /a    ->
//        /div[@class='ui-datepicker-title']
//            /span[@class='ui-datepicker-month']
//            /span[@class='ui-datepicker-year']

}
