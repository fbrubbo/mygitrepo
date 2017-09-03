package br.datamaio.fly.check.azul.selenium.pages;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

	@FindBy(id = "ticket-origin1")
	private WebElement from;
	@FindBy(id = "ticket-destination1")
	private WebElement to;
		
	@FindBy(id = "ticket-departure1")
	private WebElement fromDate;
	@FindBy(id = "ticket-arrival1")
	private WebElement toDate;
	
	@FindBy(xpath = "//*[@id='ticket-detail']/div[5]/div[3]/div/button")
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
//		WebElement el = waitUntil(elementToBeClickable(By.xpath("//li[@class='display_box azul highlight hover']")));
		WebElement el = waitUntil(elementToBeClickable(By.xpath("//ul/li[@class='destination-0001__item destination-0001__highlight']")));
		////html[@class='js svg canvas']/body[@id='home']/div[@id='wrapper']/div[@class='ps-relative']/div[@id='main-menu-content']/div[@class='section__outer skin-0025 skin-0019']/div[@class='section--0007__container']/ul[@class='banner-0001 banner-0001--0001 css3']/li[@class='banner-0001__item banner-0001__loading']/section[@id='ticket']/div[@class='section__outer--0003']/form[@class='form-0001 clear']/div[2]/div/fieldset[@id='ticket-content']/div[@class='grid-g ps-relative']/div[@class='grid-12-24 gutter--t15 gutter--b5 gutter--r30 gutter--lg-r45']/div[@class='ps-relative']/article[@class='destination-0001 destination-0001--0001 skin-0040']/div[@class='tinyscrollbar no-scrollable']/div[@class='viewport']/div[@class='overview']/div[@class='destination-0001__category animate']/ul/li[@class='destination-0001__item destination-0001__highlight']
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
		// WebElement el = waitUntil(elementToBeClickable(By.xpath("//ul/li[@class='display_box azul  hover']")));
		WebElement el = waitUntil(elementToBeClickable(By.xpath("//ul/li[@class='destination-0001__item destination-0001__highlight']")));
		// /html[@class='js svg canvas']/body[@id='home']/div[@id='wrapper']/div[@class='ps-relative']/div[@id='main-menu-content']/div[@class='section__outer skin-0025 skin-0019']/div[@class='section--0007__container']/ul[@class='banner-0001 banner-0001--0001 css3']/li[@class='banner-0001__item banner-0001__loading']/section[@id='ticket']/div[@class='section__outer--0003']/form[@class='form-0001 clear']/div[2]/div/fieldset[@id='ticket-content']/div[@class='grid-g ps-relative']/div[@class='grid-12-24 gutter--t15 gutter--b5 gutter--l30 gutter--lg-l45']/div[@class='ps-relative']/article[@class='destination-0001 destination-0001--0001 skin-0040']/div[@class='tinyscrollbar no-scrollable']/div[@class='viewport']/div[@class='overview']/div[@class='destination-0001__category animate']/ul/li[@class='destination-0001__item destination-0001__highlight']
		el.click();
		return this;
	}

	public SearchPage departure(final LocalDate date) {
		//fromDate.sendKeys("");
		//fromDate.sendKeys(date.format(DFbr));
		
		String js = String.format("document.getElementById('ticket-departure1').value='%s';", date.format(DFbr));
		executeScript(js);
		return this;		
	}

	public SearchPage returning(final LocalDate date) {
//		toDate.sendKeys("");
//		toDate.sendKeys(date.format(DFbr));
		
		String js = String.format("document.getElementById('ticket-arrival1').value='%s';", date.format(DFbr));
		executeScript(js);
		return this;
	}


	public SelectFlyPage buy() {
		clickInAnyElementButCurrentOne();
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
		WebElement anyDiv = waitUntil(elementToBeClickable(By.xpath("//*[@id='ticket']/div")));
		new Actions(driver).moveToElement(anyDiv).click().perform();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void executeScript(final String js) {
		((JavascriptExecutor) driver).executeScript(js);
	}	
	
	private WebElement waitUntil(final ExpectedCondition<WebElement> condition) {
		return new WebDriverWait(driver, 30, 50).until(condition);
	}
}
