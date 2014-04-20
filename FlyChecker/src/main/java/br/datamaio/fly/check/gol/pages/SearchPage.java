package br.datamaio.fly.check.gol.pages;

import static java.time.format.DateTimeFormatter.ofPattern;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.datamaio.fly.TripType;

public class SearchPage {
    private static final DateTimeFormatter DFday = ofPattern("dd");
    private static final DateTimeFormatter DFmonth = ofPattern("yyyy-MM");
    private static final DateTimeFormatter DFus = ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DFbr = ofPattern("dd/MM/yyyy");

    private TripType trip;

    @FindBy(id="bt-ida-volta")
    private WebElement roundTrip;
    @FindBy(id="bt-so-ida")
    private WebElement oneWay;

    @FindBy(id="ctl00_PlaceHolderCentro_fieldde")
    private WebElement from;
    @FindBy(id="ctl00_PlaceHolderCentro_fieldpara")
    private WebElement to;

    @FindBy(id="bt-buy")
    private WebElement buyButton;

    private LocalDate departure;
    private LocalDate returning;

    private final WebDriver driver;

    public SearchPage(final WebDriver driver) {
        this.driver = driver;
    }

    public SearchPage navigate(){
        driver.get("http://www.voegol.com.br/pt-br/Paginas/default.aspx");
        PageFactory.initElements(driver, this);
        selectRoundTrip();
        return this;
    }

    public SearchPage selectRoundTrip(){
        roundTrip.click();
        trip = TripType.ROUND_TRYP;
        return this;
    }

    public SearchPage selectOneWay(){
        oneWay.click();
        trip = TripType.ONE_WAY;
        return this;
    }

    public SearchPage from(final String airportOrCity) {
        from.clear();
        sleep(100);
        from.sendKeys(airportOrCity);
        WebElement el = waitUntil(elementToBeClickable(By.xpath("//div[@id='autocomplete-de']/div/div/div")));
        el.click();
        return this;
    }

    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public SearchPage to(final String airportOrCity){
        to.clear();
        sleep(100);
        to.sendKeys(airportOrCity);
        WebElement el = waitUntil(elementToBeClickable(By.xpath("//div[@id='autocomplete-para']/div/div/div")));
        el.click();
        return this;
    }

    public SearchPage departure(final LocalDate date) {
        departure = date;
        String js = String.format("document.getElementById('hidden-field-ida-day').value='%s';"
                                + "document.getElementById('hidden-field-ida-month').value='%s';"
                                + "document.getElementById('hidden-field-DepartureDate').value='%s';"
                                + "document.getElementById('field-ida').value='%s';",
                                date.format(DFday),
                                date.format(DFmonth),
                                date.format(DFus),
                                date.format(DFbr));
        executeScript(js);
        return this;
    }

    public SearchPage returning(final LocalDate date) {
        if(trip == TripType.ONE_WAY) {
            throw new IllegalStateException("Não pode chamar o método returning(..) se é oneway trip");
        }

        returning = date;
        String js = String.format("document.getElementById('hidden-field-volta-day').value='%s';"
                + "document.getElementById('hidden-field-volta-month').value='%s';"
                + "document.getElementById('hidden-field-ReturnDate').value='%s';"
                + "document.getElementById('field-volta').value='%s';",
                date.format(DFday),
                date.format(DFmonth),
                date.format(DFus),
                date.format(DFbr));
        executeScript(js);
        return this;
    }


    public SelectFlyPage buy(){
        buyButton.click();
        return new SelectFlyPage(driver, trip, departure, returning);
    }

    // ------------ métodos privados ---------

    private void executeScript(final String js) {
        ((JavascriptExecutor) driver).executeScript(js);
    }

    private WebElement waitUntil(final ExpectedCondition<WebElement> condition) {
        return new WebDriverWait(driver, 10, 10).until(condition);
    }
}
