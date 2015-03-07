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

	@FindBy(className = "lowestFarePriceLabelActive")
	private  List<WebElement> options;
	 WebDriver driver;

    public SelectFlyPage(final WebDriver driver) {
    	this.driver = driver;
    	waitUntil(elementToBeClickable(By.xpath("//div[@class='lowestFarePriceLabelActive']")));
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
    	DecimalFormat df = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols ( new Locale ("pt", "BR")));
		try {
			Number n = df.parse(options.get(0).getText());
	    	return new TripOption(new BigDecimal(n.toString()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
    }

    public TripOption getReturningOption(){
    	DecimalFormat df = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols ( new Locale ("pt", "BR")));
		try {
			Number n = df.parse(options.get(1).getText());
	    	return new TripOption(new BigDecimal(n.toString()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
    }


}
