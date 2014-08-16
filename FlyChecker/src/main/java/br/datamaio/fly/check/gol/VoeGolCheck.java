package br.datamaio.fly.check.gol;

import static br.datamaio.fly.DayPeriod.AFTERNOON;
import static br.datamaio.fly.DayPeriod.AFTERNOON_OR_NIGHT;
import static br.datamaio.fly.DayPeriod.MORNING;
import static br.datamaio.fly.DayPeriod.NIGHT;
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
	private static final DateTimeFormatter DATE = ofPattern("dd/MM/yyyy");

	private static final NumberFormat REAIS = DecimalFormat.getCurrencyInstance();
	private static final int PERIOD_IN_MONTH = 4;
	protected LocalDate startDate;
	protected BigDecimal threshold;
	
    public void setUp(BigDecimal threshold){
    	this.threshold = threshold;
        startDate = LocalDate.of(2014, 11, 1);
    }

    public void tearDown(){
    }

	public List<RoundTrip> congonhas2caxias() throws Exception {
		List<RoundTrip> tripsWithGoodPrice = new ArrayList<>();

		Period period = Period.ofMonths(PERIOD_IN_MONTH);
		LocalDate fromDate = startDate;
		LocalDate untilDate = fromDate.plus(period);

		Path logFile = buildLogFile("congonhas2caxias_");
		try (BufferedWriter report = Files.newBufferedWriter(logFile)) {

			write(report,
					String.format("Searching Flyies from '%s' to '%s' ", fromDate.format(DATE), untilDate.format(DATE)));

			LocalDate next = fromDate;
			do {
				LocalDate friday = next.with(next(DayOfWeek.FRIDAY));
				LocalDate saturday = friday.with(next(DayOfWeek.SATURDAY));
				LocalDate sunday = friday.with(next(DayOfWeek.SUNDAY));
				LocalDate monday = friday.with(next(DayOfWeek.MONDAY));

				tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, friday, AFTERNOON, sunday, NIGHT));
				tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, friday, AFTERNOON, monday, MORNING));

				tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, saturday, MORNING, sunday, NIGHT));
				tripsWithGoodPrice.add(check(report, CONGONHAS, CAXIAS, saturday, MORNING, monday, MORNING));

				write(report, "");
				write(report, "");

				next = monday;
			} while (next.compareTo(untilDate) < 0);
		}

		return tripsWithGoodPrice.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}
	
    public List<RoundTrip> caxias2congonhas() throws Exception {
    	List<RoundTrip> tripsWithGoodPrice = new ArrayList<>();
    	
        Period period = Period.ofMonths(PERIOD_IN_MONTH);
        LocalDate fromDate = startDate;
        LocalDate untilDate = fromDate.plus(period);

        Path logFile = buildLogFile("caxias2congonhas_");
        try (BufferedWriter writter = Files.newBufferedWriter(logFile)) {

            write(writter, String.format("Searching Flyies from '%s' to '%s' ", fromDate.format(DATE), untilDate.format(DATE)));

            LocalDate next = fromDate;
            do {
                LocalDate friday    = next.with(next(DayOfWeek.FRIDAY));
                LocalDate saturday  = friday.with(next(DayOfWeek.SATURDAY));
                LocalDate sunday    = friday.with(next(DayOfWeek.SUNDAY));
                LocalDate monday    = friday.with(next(DayOfWeek.MONDAY));

                tripsWithGoodPrice.add(check(writter, CAXIAS, CONGONHAS, friday, NIGHT, sunday, AFTERNOON_OR_NIGHT));
                tripsWithGoodPrice.add(check(writter, CAXIAS, CONGONHAS, friday, NIGHT, monday, MORNING));

                tripsWithGoodPrice.add(check(writter, CAXIAS, CONGONHAS, saturday, MORNING, sunday, AFTERNOON_OR_NIGHT));
                tripsWithGoodPrice.add(check(writter, CAXIAS, CONGONHAS, saturday, MORNING, monday, MORNING));

                write(writter, "");
                write(writter, "");

                next = monday;
            } while(next.compareTo(untilDate)<0);
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
            final LocalDate dret, final DayPeriod pret) throws Exception {
        
        RoundTrip bestTrip = getBestRoundTripOption(from, to, ddep, pdep, dret, pret);
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
    }
	
    public abstract RoundTrip getBestRoundTripOption(String from, String to, LocalDate ddep, DayPeriod pdep, LocalDate dret, DayPeriod pret);
    
	public Path buildLogFile(final String filePrefix) throws IOException {
		LocalDateTime now = LocalDateTime.now();
		Path dir = Paths.get("reports", now.format(ofPattern("yyyyMMdd")));
		Files.createDirectories(dir);
		return Paths.get(dir.toString(), filePrefix + now.format(ofPattern("HHmm")) + ".txt");
	}

}
