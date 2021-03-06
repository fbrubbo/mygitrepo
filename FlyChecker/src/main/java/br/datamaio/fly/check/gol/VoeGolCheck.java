package br.datamaio.fly.check.gol;

import static br.datamaio.fly.DayPeriod.AFTERNOON;
import static br.datamaio.fly.DayPeriod.AFTERNOON_OR_NIGHT;
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

import br.datamaio.fly.DayPeriod;
import br.datamaio.fly.RoundTrip;
import br.datamaio.fly.Schedule;
import br.datamaio.fly.TripOption;

public abstract class VoeGolCheck {
	private static final Logger LOGGER = Logger.getLogger(VoeGolCheck.class);
	
	private static final String CONGONHAS = "Congonhas";
	private static final String CAXIAS = "Caxias do Sul";
	private static final String POA = "Porto Alegre";
	private static final DateTimeFormatter DATE = ofPattern("dd/MM/yyyy");
	private static final NumberFormat REAIS = DecimalFormat.getCurrencyInstance();
	private static final String PROMO_CODE = "";
	
	protected BigDecimal threshold;
	protected LocalDate startDate;
	protected LocalDate endDate;
	
    public void setUp(BigDecimal threshold, LocalDate startDate, Period period){
    	this.threshold = threshold;
        this.startDate = startDate;
		this.endDate = startDate.plus(period);
    }

    public void tearDown(){
    }
    
    public List<RoundTrip> checkCaxiasCongonhas() throws Exception {
    	List<RoundTrip> tripsWithGoodPrice = new ArrayList<>();
				
		Path logFile = buildLogFile("caxias2congonhas");
		try (BufferedWriter report = Files.newBufferedWriter(logFile)) {
			write(report, "--------- Searching Flyies caxias 2 congonhas ---------");
			
			LocalDate dpart = of(2016, 11, 20);
			LocalDate dret = of(2016, 11, 24);
			tripsWithGoodPrice.add(check(report, CAXIAS, CONGONHAS, dpart, ANY, dret, ANY, PROMO_CODE));

			dpart = of(2015, 11, 21);
			dret = of(2015, 11, 24);
			tripsWithGoodPrice.add(check(report, CAXIAS, CONGONHAS, dpart, ANY, dret, ANY, PROMO_CODE));

			
			write(report, "Finalizado com sucesso..");
		} 
		return tripsWithGoodPrice.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}
    
    public List<RoundTrip> checkNatal() throws Exception {
    	List<RoundTrip> tripsWithGoodPrice = new ArrayList<>();
				
		Path logFile = buildLogFile("NATAL_congonhas2caxias_");
		try (BufferedWriter report = Files.newBufferedWriter(logFile)) {
			write(report, "--------- Searching Flyies PARA NATAL ---------");
			
			LocalDate dret = of(2016, 1, 2);
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 17), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 18), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 19), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 20), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 21), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 22), ANY, dret, ANY, PROMO_CODE));

			
			dret = of(2016, 1, 3);
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 17), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 18), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 19), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 20), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 21), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 22), ANY, dret, ANY, PROMO_CODE));
			
			dret = of(2016, 1, 4);
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 17), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 18), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 19), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 20), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 21), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 22), ANY, dret, ANY, PROMO_CODE));
			
			dret = of(2016, 1, 5);
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 17), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 18), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 19), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 20), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 21), ANY, dret, ANY, PROMO_CODE));
			tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, of(2015, 12, 22), ANY, dret, ANY, PROMO_CODE));
			
			write(report, "Finalizado com sucesso Agendamento para NATAL..");
		} 
		return tripsWithGoodPrice.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	public List<RoundTrip> weekendCheckCongonhas2Caxias() throws Exception {
		List<RoundTrip> tripsWithGoodPrice = new ArrayList<>();

		Path logFile = buildLogFile("congonhas2caxias_");
		try (BufferedWriter report = Files.newBufferedWriter(logFile)) {

			write(report, String.format("Searching Flyies from '%s' to '%s' ", startDate.format(DATE), endDate.format(DATE)));

			LocalDate next = startDate;
			do {
				LocalDate friday = next.with(next(DayOfWeek.FRIDAY));
				LocalDate saturday = friday.with(next(DayOfWeek.SATURDAY));
				LocalDate sunday = friday.with(next(DayOfWeek.SUNDAY));
				LocalDate monday = friday.with(next(DayOfWeek.MONDAY));

				tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, friday, ANY, sunday, NIGHT, PROMO_CODE));
//				tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, friday, AFTERNOON, monday, MORNING, PROMO_CODE));
//				tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, saturday, MORNING, sunday, NIGHT, PROMO_CODE));
				tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, saturday, ANY, monday, ANY, PROMO_CODE));
				
//				tripsWithGoodPrice.add(check(report, CONGONHAS, POA, friday, AFTERNOON, sunday, NIGHT, PROMO_CODE));
//				tripsWithGoodPrice.add(check(report, CONGONHAS, POA, saturday, MORNING, monday, MORNING, PROMO_CODE));				

				write(report, "\n");

				next = monday;
			} while (next.compareTo(endDate) < 0);
		}

		return tripsWithGoodPrice.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}
	
    public List<RoundTrip> weekendCheckCaxias2Congonhas() throws Exception {
    	List<RoundTrip> tripsWithGoodPrice = new ArrayList<>();
    	
        Path logFile = buildLogFile("caxias2congonhas_");
        try (BufferedWriter writter = Files.newBufferedWriter(logFile)) {

            write(writter, String.format("Searching Flyies from '%s' to '%s' ", startDate.format(DATE), endDate.format(DATE)));

            LocalDate next = startDate;
            do {
                LocalDate friday    = next.with(next(DayOfWeek.FRIDAY));
                LocalDate saturday  = friday.with(next(DayOfWeek.SATURDAY));
                LocalDate sunday    = friday.with(next(DayOfWeek.SUNDAY));
                LocalDate monday    = friday.with(next(DayOfWeek.MONDAY));

                tripsWithGoodPrice.add(check(writter, CAXIAS, CONGONHAS, friday, ANY, sunday, ANY, PROMO_CODE));
//                tripsWithGoodPrice.add(check(writter, CAXIAS, CONGONHAS, friday, NIGHT, monday, MORNING, PROMO_CODE));
                tripsWithGoodPrice.add(check(writter, CAXIAS, CONGONHAS, saturday, ANY, sunday, ANY, PROMO_CODE));
//                tripsWithGoodPrice.add(check(writter, CAXIAS, CONGONHAS, saturday, MORNING, monday, MORNING, PROMO_CODE));

                write(writter, "\n");

                next = monday;
            } while(next.compareTo(endDate)<0);
        }
        
        return tripsWithGoodPrice.stream()
    			.filter(Objects::nonNull)
    			.collect(Collectors.toList());
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
            final LocalDate dret, final DayPeriod pret,
            final String promoCode) {
        try {
	        RoundTrip bestTrip = getBestRoundTripOption(from, to, ddep, pdep, dret, pret, promoCode);
	        if (bestTrip==null) {
	            return null;
	        }
	
	        TripOption departure = bestTrip.getDeparture();
	        TripOption returning = bestTrip.getReturning();
	        write(writter, "==============================================================================================");
	        write(writter, String.format("%s -> %s", from, to));
	        Schedule sd = departure.getSchedule();
	        Schedule sr = returning.getSchedule();
	        write(writter, String.format("\tIDA     : %s dia %s (%s - %s): %s", ddep.getDayOfWeek(), ddep.format(DATE), sd.getTakeoffTime(), sd.getLandingTime(), departure));
	        write(writter, String.format("\tVOLTA   : %s dia %s (%s - %s): %s", dret.getDayOfWeek(), dret.format(DATE), sr.getTakeoffTime(), sr.getLandingTime(), returning));
	        BigDecimal totalValue = departure.getValue().add(returning.getValue());
			write(writter, String.format("\t** TOTAL ** : %s", REAIS.format(totalValue) ));
			
			return totalValue.compareTo(threshold) <= 0 ? bestTrip : null;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
    public abstract RoundTrip getBestRoundTripOption(String from, String to, LocalDate ddep, DayPeriod pdep, LocalDate dret, DayPeriod pret, String promoCode);
    
	public Path buildLogFile(final String filePrefix) throws IOException {
		LocalDateTime now = LocalDateTime.now();
		Path dir = Paths.get("reports", now.format(ofPattern("yyyyMMdd")));
		Files.createDirectories(dir);
		return Paths.get(dir.toString(), filePrefix + now.format(ofPattern("HHmm")) + ".txt");
	}

}
