package br.datamaio.fly.check.gol.pages;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import br.datamaio.fly.DayPeriod;
import br.datamaio.fly.Option;
import br.datamaio.fly.Schedule;
import br.datamaio.fly.ScheduleOptions;
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

    public Schedule getBestDepartureSchedule(){
        buildDeparture();
        return departureSchedules.getBestSchedule();
    }

    public Option getBestDeparturegOption(final LocalTime at){
        buildDeparture();
        return departureSchedules.getBestOption(at);
    }

    public Option getBestDepartureOption(final DayPeriod period){
        buildDeparture();
        return departureSchedules.getBestOption(period);
    }

    public List<Schedule> getDepartureSchedules() {
        buildDeparture();
        return departureSchedules.getSchedules();
    }

    // ---- returning methods -----

    public Schedule getBestReturningSchedule(){
        buildReturning();
        return returningSchedules.getBestSchedule();
    }

    public Option getBestReturningOption(final LocalTime at){
        buildReturning();
        return returningSchedules.getBestOption(at);
    }

    public Option getBestReturningOption(final DayPeriod period){
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
            throw new IllegalStateException("Voc� n�o pode chamar o m�todo de retorno de viagem se ecolheu apenas one tryp");
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
            String time = leftHeader.findElement(By.xpath("./div[@class='scale']/div[@class='infoScale']/span[@class='timeGoing']")).getText();
            Schedule s = new Schedule(flyNumber, date, LocalTime.parse(time));
            schedules.add(s);
            List<WebElement> ops = line.findElements(By.xpath("./div[contains(@class,'taxa ')]"));
            ops.forEach(op -> {
                try {
                    String type = op.getAttribute("class").substring(5);
                    WebElement el = op.findElement(By.xpath("./div/strong[@class='fareValue']"));
                    try {
                        Number value = NumberFormat.getCurrencyInstance().parse(el.getText());
                        s.addOption(new Option(type, new BigDecimal(value.toString())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchElementException e) {
                    // ignora a op��o porque ela est� indispon�nvel
                }
            });
        });
        return schedules;
    }

}
