package br.datamaio.fly.check.gol.selenium;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import br.datamaio.fly.DayPeriod;
import br.datamaio.fly.RoundTrip;
import br.datamaio.fly.Schedule;
import br.datamaio.fly.check.gol.VoeGolCheck;
import br.datamaio.fly.check.gol.selenium.pages.SearchPage;
import br.datamaio.fly.check.gol.selenium.pages.SelectFlyPage;
import br.datamaio.fly.check.gol.urlconn.UrlConnVoeGolCheck;

public class SeleniumVoeGolCheck extends VoeGolCheck {

    private static WebDriver driver;

    public void setUp(BigDecimal threshold, LocalDate startDate, Period period){
    	super.setUp(threshold, startDate, period);
        
    	//TODO: tirar o chrome driver do bin do wildfly e colocar dentro do jar. depois jogar em um temp na hora de executar
    	Path f = Paths.get("chromedriver");
        System.setProperty("webdriver.chrome.driver",f.toAbsolutePath().toString());
        driver = new ChromeDriver();
    }

    public void tearDown(){
        driver.quit();
    }
    
	public RoundTrip getBestRoundTripOption(final String from, final String to, final LocalDate ddep,
			final DayPeriod pdep, final LocalDate dret, final DayPeriod pret) {

		SearchPage search = new SearchPage(driver).navigate()
				.selectRoundTrip().from(from).to(to)
				.departure(ddep).returning(dret);
		
		SelectFlyPage selectFly = search.buy();
		return selectFly.getBestRoundTripOption(pdep, pret);
	}
	
	public static void main(String[] args) throws Exception {
		System.setProperty("-Dlog4j.configuration", "log4j.properties");
		BigDecimal threshold = new BigDecimal("6000");
		LocalDate startDate = LocalDate.now().plusDays(3);
		Period period = Period.ofMonths(4);
		
		VoeGolCheck check = new SeleniumVoeGolCheck();  
		check.setUp(threshold, startDate, period);
//		List<RoundTrip> trips = check.weekendCheckCongonhas2Caxias();
		List<RoundTrip> trips = check.checkCaxiasCongonhas();		
		print(trips);
		
//		trips = check.checkNatal();
		print(trips);
		
		check.tearDown();
	}

	static void print(List<RoundTrip> trips) {
		DateTimeFormatter DATE = ofPattern("dd/MM/yyyy");
		NumberFormat REAIS = DecimalFormat.getCurrencyInstance();
		StringBuilder builder = new StringBuilder();
		for (RoundTrip t : trips) {
		    Schedule sd = t.getDeparture().getSchedule();
		    Schedule sr = t.getReturning().getSchedule();
		    
		    LocalDate dDate = sd.getDate();
			LocalDate rDate = sr.getDate();
			BigDecimal totalValue = t.getDeparture().getValue().add(t.getReturning().getValue());
			builder.append(builder.length()>0 ? "\n" : "")
					.append(String.format("- Dia %s (%s - %s): %s", 
						dDate.format(DATE), 
						dDate.getDayOfWeek().toString().substring(0, 2), 
						rDate.getDayOfWeek().toString().substring(0, 2), 
						REAIS.format(totalValue))); 
		}
		System.out.println(builder.toString());
	}
	
    
//    private static final String CONGONHAS = "Congonhas";
//    private static final String CAXIAS = "Caxias do Sul";
//    private static final DateTimeFormatter DATE = ofPattern("dd/MM/yyyy");
//    private static final NumberFormat REAIS = DecimalFormat.getCurrencyInstance();
//
//    public void caxias2congonhas_apenasida() throws Exception {
//        Period period = Period.ofDays(8);
//        LocalDate fromDate = LocalDate.of(2014,06,01);
//        LocalDate untilDate = fromDate.plus(period);
//
//        Path logFile = buildLogFile("caxias2congonhas_apenasida_");
//        try (BufferedWriter writter = Files.newBufferedWriter(logFile)) {
//
//            write(writter, String.format("Searching Flyies from '%s' to '%s' ", fromDate.format(DATE), untilDate.format(DATE)));
//
//            LocalDate next = fromDate;
//            do {
//                SearchPage search = new SearchPage(driver).navigate()
//                        .selectOneWay().from(CAXIAS).to(CONGONHAS)
//                        .departure(next);
//                SelectFlyPage selectFly = search.buy();
//                TripOption o = selectFly.getBestDepartureOption();
//
//                if (o != null ) {
//                    write(writter, "==============================================================================================");
//                    write(writter, String.format("%s -> %s", CAXIAS, CONGONHAS));
//                    Schedule s = o.getSchedule();
//                    write(writter, String.format("\tIDA     : %s dia %s (%s - %s): %s", next.getDayOfWeek(), next.format(DATE), s.getTakeoffTime(), s.getLandingTime(), o));
//                    write(writter, String.format("\t** TOTAL ** : %s", REAIS.format(o.getValue())));
//                }
//
//                next = next.plus(1, DAYS);
//            } while(next.compareTo(untilDate)<0);
//        }
//    }

}
