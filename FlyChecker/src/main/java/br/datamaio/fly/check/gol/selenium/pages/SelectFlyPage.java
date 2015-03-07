package br.datamaio.fly.check.gol.selenium.pages;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;


import br.datamaio.fly.DayPeriod;
import br.datamaio.fly.RoundTrip;
import br.datamaio.fly.Schedule;
import br.datamaio.fly.ScheduleOptions;
import br.datamaio.fly.TripOption;
import br.datamaio.fly.TripType;

public class SelectFlyPage {

    private final WebDriver driver;
    private final TripType trip;
    private final LocalDate departure;
    private final LocalDate returning;

    private boolean buildDeparture = true;
    private final ScheduleOptions departureSchedules = new ScheduleOptions();
    private boolean buildReturning = true;
    private final ScheduleOptions returningSchedules = new ScheduleOptions();

    public SelectFlyPage(final WebDriver driver, final TripType trip, final LocalDate departure, final LocalDate returning) {
        this.driver = driver;
        this.trip = trip;
        this.departure = departure;
        this.returning = returning;
        PageFactory.initElements(driver, this);
    }

    // ---- departure methods -----

    public TripOption getBestDepartureOption(){
        buildDeparture();
        return departureSchedules.getBestOption();
    }

    public TripOption getBestDeparturegOption(final LocalTime at){
        buildDeparture();
        return departureSchedules.getBestOption(at);
    }

    public TripOption getBestDepartureOption(final DayPeriod period){
        buildDeparture();
        return departureSchedules.getBestOption(period);
    }

    public List<Schedule> getDepartureSchedules() {
        buildDeparture();
        return departureSchedules.getSchedules();
    }
    
    public RoundTrip getBestRoundTripOption(final DayPeriod pdep, final DayPeriod pret){
        TripOption bestDeparture = getBestDepartureOption(pdep);
        TripOption bestReturn = getBestReturningOption(pret);
        if (bestDeparture == null || bestReturn == null) {
            return null;
        }
        return new RoundTrip(bestDeparture, bestReturn);
    }

    // ---- returning methods -----

    public Schedule getBestReturningSchedule(){
        buildReturning();
        return returningSchedules.getBestSchedule();
    }

    public TripOption getBestReturningOption(final LocalTime at){
        buildReturning();
        return returningSchedules.getBestOption(at);
    }

    public TripOption getBestReturningOption(final DayPeriod period){
        buildReturning();
        return returningSchedules.getBestOption(period);
    }

    public List<Schedule> getReturningSchedules() {
        buildReturning();
        return returningSchedules.getSchedules();
    }

    // ---- private methods ----

    private void buildDeparture() {
        if(buildDeparture) {
            departureSchedules.add(buildSchedules("ida", departure));
            buildDeparture = false;
        }
    }

    private void buildReturning() {
        if(TripType.ONE_WAY.equals(trip)){
            // TODO: o ideal seria construir objetos diferente de acordo com o trip
            throw new IllegalStateException("você não pode chamar o método de retorno de viagem se ecolheu apenas one tryp");
        }

        if(buildReturning) {
            returningSchedules.add(buildSchedules("volta", returning));
            buildReturning = false;
        }
    }

    private List<Schedule> buildSchedules(final String trip, final LocalDate date) {
        final List<Schedule> schedules = new ArrayList<>();
        List<WebElement> lines = driver.findElements(By.xpath(String.format("/html//div[@id='%s']/div[@class='ContentTable']/div[@class='lineTable']", trip)));
        lines.forEach(line -> {
            WebElement leftHeader = line.findElement(By.xpath("./div[contains(@class,'status')]"));
            String flyNumber = leftHeader.findElement(By.xpath("./span[@class='titleAirplane']/div[@class='operatedBy']")).getText();
            String takeofTime = leftHeader.findElement(By.xpath("./div[@class='scale']/div[@class='infoScale']/span[@class='timeGoing']")).getText();
            String landingTime = leftHeader.findElement(By.xpath("./div[@class='scale']/div[@class='infoScale']/span[@class='timeoutGoing']")).getText();
            Schedule s = new Schedule(flyNumber, date, LocalTime.parse(takeofTime), LocalTime.parse(landingTime));
            schedules.add(s);
            List<WebElement> ops = line.findElements(By.xpath("./div[contains(@class,'taxa ')]"));
            ops.forEach(op -> {
                try {
                    String type = op.getAttribute("class").substring(5);
                    WebElement el = op.findElement(By.xpath("./div/strong[@class='fareValue']"));
                    try {
                        Number value = NumberFormat.getCurrencyInstance(new Locale( "pt", "BR" )).parse(el.getText());
                        s.addOption(new TripOption(s, type, new BigDecimal(value.toString())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchElementException e) {
                    // ignora a op��o porque ela está indispon�nvel
                }
            });
        });
        return schedules;
    }

    public static void main(String[] args) throws Exception {
    	Number value = NumberFormat.getCurrencyInstance(new Locale( "pt", "BR" )).parse("R$ 469,90");
		System.out.println(value);
	}


}
