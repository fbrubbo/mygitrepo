package br.datamaio.fly.check.azul.selenium;

import static br.datamaio.fly.DayPeriod.AFTERNOON;
import static br.datamaio.fly.DayPeriod.ANY;
import static br.datamaio.fly.DayPeriod.MORNING;
import static br.datamaio.fly.DayPeriod.NIGHT;
import static java.time.LocalDate.of;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.TemporalAdjusters.next;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import br.datamaio.fly.DayPeriod;
import br.datamaio.fly.RoundTrip;
import br.datamaio.fly.Schedule;
import br.datamaio.fly.TripOption;
import br.datamaio.fly.check.azul.selenium.pages.SearchPage;
import br.datamaio.fly.check.azul.selenium.pages.SelectFlyPage;

public class VoeAzulCheck {
	private static final Logger LOGGER = Logger.getLogger(VoeAzulCheck.class);
	
	private static final String VIRACOPOS = "Viracopos";
	private static final String CAXIAS = "Caxias do Sul";
	private static final String POA = "Porto Alegre";
	private static final DateTimeFormatter DATE = ofPattern("dd/MM/yyyy");
	private static final NumberFormat REAIS = DecimalFormat.getCurrencyInstance();
	
	protected BigDecimal threshold;
	protected LocalDate startDate;
	protected LocalDate endDate;
	
	private static WebDriver driver;
	
    public void setUp(BigDecimal threshold, LocalDate startDate, Period period){
    	this.threshold = threshold;
        this.startDate = startDate;
		this.endDate = startDate.plus(period);
		
		//TODO: tirar o chrome driver do bin do wildfly e colocar dentro do jar. depois jogar em um temp na hora de executar
    	Path f = Paths.get("chromedriver");
        System.setProperty("webdriver.chrome.driver",f.toAbsolutePath().toString());
        driver = new ChromeDriver();
    }

    public void tearDown(){
    }
    
 
	public List<RoundTrip> weekendCheckViracopos2Caxias() throws Exception {
		List<RoundTrip> tripsWithGoodPrice = new ArrayList<>();

		Path logFile = buildLogFile("viracopos2caxias_");
		try (BufferedWriter report = Files.newBufferedWriter(logFile)) {

			write(report, String.format("Searching Flyies from '%s' to '%s' ", startDate.format(DATE), endDate.format(DATE)));

			LocalDate next = startDate;
			do {
				LocalDate friday = next.with(next(DayOfWeek.FRIDAY));
				LocalDate sunday = friday.with(next(DayOfWeek.SUNDAY));
				LocalDate monday = friday.with(next(DayOfWeek.MONDAY));
				LocalDate tuesday = friday.with(next(DayOfWeek.TUESDAY));
				
//				LocalDate lastSaturday = LocalDate.now().with(previous(SATURDAY));

				tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, friday, ANY, sunday, ANY));
				tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, friday, ANY, tuesday, ANY));
//				tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, friday, AFTERNOON, monday, MORNING));

				write(report, "\n");

				next = monday;
			} while (next.compareTo(endDate) < 0);
		}

		return tripsWithGoodPrice.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}
	
    public List<RoundTrip> checkNatal() throws Exception {
    	List<RoundTrip> tripsWithGoodPrice = new ArrayList<>();
				
		Path logFile = buildLogFile("NATAL_viracopos2caxias_");
		try (BufferedWriter report = Files.newBufferedWriter(logFile)) {
			write(report, "--------- Searching Flyies PARA NATAL ---------");
			
			LocalDate dret = of(2016, 1, 2);
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 17), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 18), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 19), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 20), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 21), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 22), ANY, dret, ANY));
			
			dret = of(2016, 1, 3);
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 17), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 18), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 19), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 20), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 21), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 22), ANY, dret, ANY));
			
			dret = of(2016, 1, 4);
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 17), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 18), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 19), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 20), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 21), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 22), ANY, dret, ANY));
			
			dret = of(2016, 1, 5);
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 17), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 18), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 19), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 20), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 21), ANY, dret, ANY));
			tripsWithGoodPrice.add(check(report, VIRACOPOS, CAXIAS, of(2015, 12, 22), ANY, dret, ANY));
			
			write(report, "Finalizado com sucesso Agendamento para NATAL..");
		} 
		return tripsWithGoodPrice.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}	
	
	public List<RoundTrip> weekendCheckViracopos2poa() throws Exception {
		List<RoundTrip> tripsWithGoodPrice = new ArrayList<>();

		Path logFile = buildLogFile("viracopos2poa_");
		try (BufferedWriter report = Files.newBufferedWriter(logFile)) {

			write(report, String.format("Searching Flyies from '%s' to '%s' ", startDate.format(DATE), endDate.format(DATE)));

			LocalDate next = startDate;
			do {
				LocalDate friday = next.with(next(DayOfWeek.FRIDAY));
				LocalDate sunday = friday.with(next(DayOfWeek.SUNDAY));
				LocalDate monday = friday.with(next(DayOfWeek.MONDAY));
				
//				LocalDate lastSaturday = LocalDate.now().with(previous(SATURDAY));

				tripsWithGoodPrice.add(check(report, VIRACOPOS, POA, friday, ANY, sunday, NIGHT));
				tripsWithGoodPrice.add(check(report, VIRACOPOS, POA, friday, ANY, monday, ANY));

				write(report, "\n");

				next = monday;
			} while (next.compareTo(endDate) < 0);
		}

		return tripsWithGoodPrice.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}
	
    public List<RoundTrip> checkCaxiasViracopos() throws Exception {
    	List<RoundTrip> tripsWithGoodPrice = new ArrayList<>();
				
		Path logFile = buildLogFile("caxias2congonhas");
		try (BufferedWriter report = Files.newBufferedWriter(logFile)) {
			write(report, "--------- Searching Flyies caxias 2 congonhas ---------");
			
			LocalDate dpart = of(2015, 9, 11);
			LocalDate dret = of(2015, 9, 13);
			tripsWithGoodPrice.add(check(report, CAXIAS, VIRACOPOS, dpart, ANY, dret, ANY));	
			
			write(report, "Finalizado com sucesso..");
		} 
		return tripsWithGoodPrice.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}
	
    protected void write(final BufferedWriter writter, final String msg) throws IOException {
    	LOGGER.info(msg);
        writter.write(msg);
        writter.newLine();
        writter.flush();
    }
    
    public RoundTrip check(final BufferedWriter writter,  
    		final String from, final String to,
            final LocalDate ddep, final DayPeriod pdep,
            final LocalDate dret, final DayPeriod pret) {
        try {
	        RoundTrip bestTrip = getRoundTrip(from, to, ddep, dret);
	        if (bestTrip==null) {
	            return null;
	        }
	
	        TripOption departure = bestTrip.getDeparture();
	        TripOption returning = bestTrip.getReturning();
	        write(writter, "==============================================================================================");
	        write(writter, String.format("%s -> %s", from, to));
	        write(writter, String.format("\tIDA     : %s dia %s: %s", ddep.getDayOfWeek(), ddep.format(DATE), departure));
	        write(writter, String.format("\tVOLTA   : %s dia %s: %s", dret.getDayOfWeek(), dret.format(DATE), returning));
	        BigDecimal totalValue = departure.getValue().add(returning.getValue());
			write(writter, String.format("\t** TOTAL ** : %s", REAIS.format(totalValue) ));
			
			return totalValue.compareTo(threshold) <= 0 ? bestTrip : null;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
    public RoundTrip getRoundTrip(String from, String to, LocalDate ddep, LocalDate dret) {
		SearchPage search = new SearchPage(driver).navigate()
				.from(from).to(to)
				.departure(ddep).returning(dret);
		
		SelectFlyPage selectFly = search.buy();
		return selectFly.getRoundTrip();
    }
    
	public Path buildLogFile(final String filePrefix) throws IOException {
		LocalDateTime now = LocalDateTime.now();
		Path dir = Paths.get("reports", now.format(ofPattern("yyyyMMdd")));
		Files.createDirectories(dir);
		return Paths.get(dir.toString(), filePrefix + now.format(ofPattern("HHmm")) + ".txt");
	}
	
	public static void main(String[] args) throws Exception {
		System.setProperty("-Dlog4j.configuration", "log4j.properties");
		BigDecimal threshold = new BigDecimal("600");
		LocalDate startDate = LocalDate.now().plusDays(3);
		Period period = Period.ofMonths(5);
		

		
		VoeAzulCheck check = new VoeAzulCheck();  
		check.setUp(threshold, startDate, period);
		List<RoundTrip> trips = check.weekendCheckViracopos2Caxias();
//		List<RoundTrip> trips = check.weekendCheckViracopos2poa();
//		List<RoundTrip> trips = check.checkViracoposCongonhas();
		print(trips);
		
//		trips = check.checkNatal();
//		print(trips);
		
		check.tearDown();
		driver.close();
	}

	static void print(List<RoundTrip> trips) {
		DateTimeFormatter DATE = ofPattern("dd/MM/yyyy");
		NumberFormat REAIS = DecimalFormat.getCurrencyInstance();
		StringBuilder builder = new StringBuilder();
		for (RoundTrip t : trips) {
			try {
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
			} catch (Exception e) {
				// here we may have a null pointer in the case we do not have promo info
			}
		}
		System.out.println(builder.toString());
	}
	

}
