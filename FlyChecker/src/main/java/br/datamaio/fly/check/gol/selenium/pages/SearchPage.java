package br.datamaio.fly.check.gol.selenium.pages;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.datamaio.fly.TripType;

public class SearchPage {
	private static final Logger LOGGER = Logger.getLogger(SearchPage.class);
	
	private static final DateTimeFormatter DFday = ofPattern("dd");
	private static final DateTimeFormatter DFmonth = ofPattern("yyyy-MM");
	private static final DateTimeFormatter DFbr = ofPattern("dd/MM/yyyy");

	@FindBy(id = "ControlGroupSearchView_AvailabilitySearchInputSearchView_RoundTrip")
	private WebElement roundTrip;
	@FindBy(id = "ControlGroupSearchView_AvailabilitySearchInputSearchView_OneWay")
	private WebElement oneWay;
	@FindBy(id = "ControlGroupSearchView_AvailabilitySearchInputSearchView_TextBoxMarketOrigin1")
	private WebElement from;
	@FindBy(id = "ControlGroupSearchView_AvailabilitySearchInputSearchView_TextBoxMarketDestination1")
	private WebElement to;
	@FindBy(xpath = "/html/body[@class='BodySite']/form[@id='SkySales']")
	private WebElement anyDiv;


	private TripType trip;
	private LocalDate departure;
	private LocalDate returning;
	private final WebDriver driver;
	
	public SearchPage(final WebDriver driver) {
		this.driver = driver;
	}

	public SearchPage navigate() {
		driver.get("https://compre2.voegol.com.br/Search.aspx");
		PageFactory.initElements(driver, this);
		selectRoundTrip();
		return this;
	}

	public SearchPage selectRoundTrip() {
		roundTrip.click();
		trip = TripType.ROUND_TRYP;
		return this;
	}

	public SearchPage selectOneWay() {
		oneWay.click();
		trip = TripType.ONE_WAY;
		return this;
	}

	public SearchPage from(final String airportOrCity) {
		return from(airportOrCity, 0);
	}
	
	private SearchPage from(final String airportOrCity, int retries) {
		from.clear();
		from.sendKeys(airportOrCity);
		try{
			WebElement el = waitUntil(elementToBeClickable(By.xpath("//div[@class='line origin']/span/div/div/ul/li")));
			el.click();
			return this;
		} catch (TimeoutException e) {
			if(retries>3)
				throw e;
			
			LOGGER.warn("Retrying FROM because was unable to find its dropdown.. ");			
			clickInAnyElementButCurrentOne();
			return from(airportOrCity, ++retries);
		}
	}

	public SearchPage to(final String airportOrCity) {
		return to(airportOrCity, 0);
	}
	
	public SearchPage to(final String airportOrCity, int retries) {
		to.clear();
		to.sendKeys(airportOrCity);
		try{
			WebElement el = waitUntil(elementToBeClickable(By.xpath("//div[@class='line destination']/span/div/div/ul/li")));
			el.click();
			return this;
		} catch (TimeoutException e) {
			if(retries>3)
				throw e;

			LOGGER.warn("Retrying TO because was unable to find its dropdown.. ");			
			clickInAnyElementButCurrentOne();
			return to(airportOrCity, ++retries);
		}
	}

	public SearchPage departure(final LocalDate date) {
		departure = date;
		String js = String.format("document.getElementById('ControlGroupSearchView_AvailabilitySearchInputSearchView_DropDownListMarketDay1').value='%s';"
				+ "document.getElementById('ControlGroupSearchView_AvailabilitySearchInputSearchView_DropDownListMarketMonth1').value='%s';"
				+ "document.getElementById('dateIda').value='%s';", date.format(DFday), date.format(DFmonth), date.format(DFbr));

		executeScript(js);
		return this;
	}

	public SearchPage returning(final LocalDate date) {
		if (trip == TripType.ONE_WAY) {
			throw new IllegalStateException("não pode chamar o método returning(..) se é oneway trip");
		}

		returning = date;
		String js = String.format("document.getElementById('ControlGroupSearchView_AvailabilitySearchInputSearchView_DropDownListMarketDay2').value='%s';"
				+ "document.getElementById('ControlGroupSearchView_AvailabilitySearchInputSearchView_DropDownListMarketMonth2').value='%s';"
				+ "document.getElementById('dateVolta').value='%s';", date.format(DFday), date.format(DFmonth), date.format(DFbr));
		executeScript(js);
		return this;
	}


	public SelectFlyPage buy() {
		clickInAnyElementButCurrentOne();		

		try {
			// outra gambi porque a m* do site da gol tem dois ids iguais
			java.util.List<WebElement> els = driver.findElements(By.id("ControlGroupSearchView_ButtonSubmit"));
			WebElement buy = els.get(1);
			waitUntil(elementToBeClickable(buy));
			Thread.sleep(700);						// TODO: por algum motivo, o waituntil acima não é suficiente.. verificar porque
			buy.click();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new SelectFlyPage(driver, trip, departure, returning);
	}

	public void clickInAnyElementButCurrentOne() {
		// gambi para o site da gol perder o foco no campo de data
		sleep(100);
		new Actions(driver).moveToElement(anyDiv).click().perform();
	}

	private void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ------------ métodos privados ---------

	private void executeScript(final String js) {
		((JavascriptExecutor) driver).executeScript(js);
	}

	private WebElement waitUntil(final ExpectedCondition<WebElement> condition) {
		return new WebDriverWait(driver, 30, 50).until(condition);
	}
}
