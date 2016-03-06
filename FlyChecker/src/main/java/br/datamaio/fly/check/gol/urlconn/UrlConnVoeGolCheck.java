package br.datamaio.fly.check.gol.urlconn;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.datamaio.fly.DayPeriod;
import br.datamaio.fly.RoundTrip;
import br.datamaio.fly.Schedule;
import br.datamaio.fly.check.gol.VoeGolCheck;

public class UrlConnVoeGolCheck extends VoeGolCheck {

	public RoundTrip getBestRoundTripOption(final String from, final String to, final LocalDate ddep,
			final DayPeriod pdep, final LocalDate dret, final DayPeriod pret, String promoCode) {
		// TODO: implement promo code
		return new SearchFly(from, to, ddep, dret).getBestRoundTripOption(pdep, pret);
	}

	public static void main(String[] args) throws Exception {
		BigDecimal threshold = new BigDecimal("300");
		LocalDate startDate = LocalDate.of(2015, 1, 10);
		Period period = Period.ofMonths(3);
		
		VoeGolCheck check = new UrlConnVoeGolCheck();  
		check.setUp(threshold, startDate, period);
		List<RoundTrip> trips = check.weekendCheckCongonhas2Caxias();
//		List<RoundTrip> trips = check.checkDigo();
		print(trips);
		
		trips = check.weekendCheckCongonhas2Caxias();
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
	
}
