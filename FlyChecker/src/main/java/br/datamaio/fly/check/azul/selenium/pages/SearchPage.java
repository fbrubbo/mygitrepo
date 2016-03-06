package br.datamaio.fly.check.azul.selenium.pages;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchPage {
	private static final Logger LOGGER = Logger.getLogger(SearchPage.class);
	
	private static final DateTimeFormatter DFbr = ofPattern("dd/MM/yyyy");

	@FindBy(id = "Estouem1")
	private WebElement from;
	@FindBy(id = "IndoPara1")
	private WebElement to;
		
	@FindBy(id = "from")
	private WebElement fromDate;
	@FindBy(id = "to")
	private WebElement toDate;
	
	@FindBy(id = "btn-idaevolta")
	private WebElement buy;


	private final WebDriver driver;
	
	public SearchPage(final WebDriver driver) {
		this.driver = driver;
	}

	public SearchPage navigate() {
		driver.get("http://www.voeazul.com.br");
		
		PageFactory.initElements(driver, this);
		try {
			WebElement el = waitUntil(ExpectedConditions.elementToBeClickable(By.className("jq-transform-select-wrapper")));
			el.click();
		}catch(Exception e) {
			System.out.println("IGRNIRE");
		}
		return this;
	}

	public SearchPage from(final String airportOrCity) {
		return from(airportOrCity, 0);
	}
	
	private SearchPage from(final String airportOrCity, int retries) {
		from.clear();
		from.sendKeys(airportOrCity);
//		WebElement el = waitUntil(elementToBeClickable(By.xpath("//div[@class='box-select-aeroportos autocomplete']/div[@class='aeroportos-wrap']/div[@class='aeroportos-tabs']/div[@class='tab-aeroporto']/ul/li")));
		WebElement el = waitUntil(elementToBeClickable(By.xpath("//li[@class='display_box azul highlight hover']")));		
		el.click();
		return this;
	}

	public SearchPage to(final String airportOrCity) {
		return to(airportOrCity, 0);
	}
	
	public SearchPage to(final String airportOrCity, int retries) {
		to.clear();
		to.sendKeys(airportOrCity);
		//WebElement el = waitUntil(elementToBeClickable(By.xpath("//div[@class='box-select-aeroportos aeroportos-destino autocomplete destino']/div[@class='aeroportos-wrap']/div[@class='aeroportos-tabs']/div[@class='tab-aeroporto']/ul/li")));
		WebElement el = waitUntil(elementToBeClickable(By.xpath("//ul/li[@class='display_box azul  hover']")));
		el.click();
		return this;
	}

	public SearchPage departure(final LocalDate date) {
		fromDate.sendKeys("");
		fromDate.sendKeys(date.format(DFbr));
		clickInAnyElementButCurrentOne();
		return this;
	}

	public SearchPage returning(final LocalDate date) {
		toDate.sendKeys("");
		toDate.sendKeys(date.format(DFbr));
		clickInAnyElementButCurrentOne();
		return this;
	}


	public SelectFlyPage buy() {
		buy.click();
		return new SelectFlyPage(driver);
	}
	
	public static void main(String[] args) throws Exception {
		Path f = Paths.get("chromedriver");
        System.setProperty("webdriver.chrome.driver",f.toAbsolutePath().toString());
        ChromeDriver driver = new ChromeDriver();
        
		SearchPage sp = new SearchPage(driver);
		SelectFlyPage select = sp.navigate().from("viracopos").to("caxias").departure(LocalDate.of(2015, 3, 6)).returning(LocalDate.of(2015, 3, 9)).buy();
		System.out.println(select.getDepartureOption());
		System.out.println(select.getReturningOption());
		
		int i = 0;
	}

	// ------------ métodos privados ---------

	public void clickInAnyElementButCurrentOne() {
		WebElement anyDiv = waitUntil(elementToBeClickable(By.xpath("//div[@class='form ida']")));
		new Actions(driver).moveToElement(anyDiv).click().perform();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private WebElement waitUntil(final ExpectedCondition<WebElement> condition) {
		return new WebDriverWait(driver, 30, 50).until(condition);
	}
}
