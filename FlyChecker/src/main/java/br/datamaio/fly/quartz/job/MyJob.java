package br.datamaio.fly.quartz.job;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.datamaio.fly.RoundTrip;
import br.datamaio.fly.Schedule;
import br.datamaio.fly.check.gol.VoeGolCheck;
import br.datamaio.fly.check.gol.selenium.SeleniumVoeGolCheck;
import br.datamaio.fly.web.ApiKey;
import br.datamaio.fly.web.SendAllMessagesServlet;

import com.google.android.gcm.server.Sender;

@DisallowConcurrentExecution
public class MyJob implements Job {
    private static final DateTimeFormatter DATE = ofPattern("dd/MM/yyyy");
	private static final NumberFormat REAIS = DecimalFormat.getCurrencyInstance();
	private static final Logger LOGGER = Logger.getLogger(MyJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		final JobDataMap dataMap = context.getJobDetail().getJobDataMap();
	    final Long id = dataMap.getLong("id");
	    final String nome = dataMap.getString("nome");

		VoeGolCheck check = new SeleniumVoeGolCheck();  // new UrlConnVoeGolCheck();
	    try {
			LOGGER.info(String.format("Executando Agendamento '%s-%s' ..", id, nome));	
						
			LocalDate startDate = LocalDate.of(2014, 12, 11);
			Period period = Period.ofMonths(3);			

			
			// --- check digo ---
			BigDecimal threshold = new BigDecimal("300");
			check.setUp(threshold, startDate, period);
			
			List<RoundTrip> trips = null;			
			trips = check.checkDigo();
			if(trips.size()>0) {
				sendToAndroid(trips, "DIGO Caxias->Congonhas");
			}
			
			
			// --- check regular ---
			threshold = new BigDecimal("330");
			check.setUp(threshold, startDate, period);
			
			trips = check.weekendCheckCaxias2Congonhas();
			if(trips.size()>0) {
				sendToAndroid(trips, "Caxias -> Congonhas");
			}
			
			trips = check.weekendCheckCongonhas2Caxias(); 
			if(trips.size()>0) {
				sendToAndroid(trips, "Congonhas -> Caxias");
			}
			
			check.tearDown();
			LOGGER.info(String.format("Finalizado com sucesso Agendamento '%s-%s' ..", id, nome));
		} catch (final Exception cause) {
			sendToAndroid(cause);
			throw new JobExecutionException(cause, false);
		} finally {
			check.tearDown();
		}
		
	}
	
	public void sendToAndroid(Exception e)  {
		StringWriter w = new StringWriter();
		PrintWriter pw = new PrintWriter(w);
		e.printStackTrace(pw);
		try {
			SendAllMessagesServlet.send(getSender(), "Error Checking Tip: ", w.getBuffer().toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void sendToAndroid(List<RoundTrip> trips, String prefix) throws IOException {
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
		LOGGER.info("Sending to androids: " + builder.toString());
		SendAllMessagesServlet.send(getSender(), prefix, builder.toString());
	}	
	
	public void sendToAndroidFullDate(List<RoundTrip> trips, String prefix) throws IOException {
		StringBuilder builder = new StringBuilder();
		for (RoundTrip t : trips) {
		    Schedule sd = t.getDeparture().getSchedule();
		    Schedule sr = t.getReturning().getSchedule();
		    
		    LocalDate dDate = sd.getDate();
			LocalDate rDate = sr.getDate();
			BigDecimal totalValue = t.getDeparture().getValue().add(t.getReturning().getValue());
			builder.append(builder.length()>0 ? "\n" : "")
					.append(String.format("- Dia %s - %s (%s - %s): %s", 
						dDate.format(DATE),
						rDate.format(DATE), 
						dDate.getDayOfWeek().toString().substring(0, 2), 
						rDate.getDayOfWeek().toString().substring(0, 2), 
						REAIS.format(totalValue))); 
		}
		LOGGER.info("Sending to androids: " + builder.toString());
		SendAllMessagesServlet.send(getSender(), prefix, builder.toString());
	}	
	
	private Sender getSender() {
		// FIXME: melhorar isto.. agora é so para testar
		return new Sender(new ApiKey().getKey());
	}	
	
}
