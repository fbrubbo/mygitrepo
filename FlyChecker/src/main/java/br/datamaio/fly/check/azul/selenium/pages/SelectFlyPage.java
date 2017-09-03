package br.datamaio.fly.check.azul.selenium.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.datamaio.fly.RoundTrip;
import br.datamaio.fly.TripOption;

public class SelectFlyPage {

	@FindBy(xpath="//*[@id='box-depart-flights']/table/tbody/tr/td/div[1]/div[1]/span[@class='fare-price']")	
	private  List<WebElement> options_depart;
	@FindBy(xpath="//*[@id='box-return-flights']/table/tbody/tr/td/div[1]/div[1]/span[@class='fare-price']")	
	private  List<WebElement> options_return;
	WebDriver driver;

    public SelectFlyPage(final WebDriver driver) {
    	this.driver = driver;
    	try {
    		waitUntil(elementToBeClickable(By.xpath("//*[@id='box-depart-flights']/div[1]/div[1]/span")));
    	} catch (org.openqa.selenium.TimeoutException e) {
    		System.out.println("No promo prices found!");
    		// ignore
    	}
        PageFactory.initElements(driver, this);
    }
    
	private WebElement waitUntil(final ExpectedCondition<WebElement> condition) {
		return new WebDriverWait(driver, 30, 50).until(condition);
	}
    
    public RoundTrip getRoundTrip(){
        TripOption bestDeparture = getDepartureOption();
        TripOption bestReturn = getReturningOption();
        if (bestDeparture == null || bestReturn == null) {
            return null;
        }
        return new RoundTrip(bestDeparture, bestReturn);
    }
    
    public TripOption getDepartureOption(){
		List<WebElement> options = options_depart;    	
		return getCheaperOption(options);
    }
    
    public TripOption getReturningOption(){   	
		return getCheaperOption(options_return);
    }

	private TripOption getCheaperOption(List<WebElement> options) {
		DecimalFormat df = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols ( new Locale ("pt", "BR")));
		try {
			BigDecimal minor = new BigDecimal("999999.99");
			for(int i=0; i<options.size(); i++){
				BigDecimal n = new BigDecimal(df.parse(options.get(i).getText()).doubleValue());
				if (n.compareTo(minor)<0) {
					minor = n;
				}
			}
	    	return new TripOption(minor);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
