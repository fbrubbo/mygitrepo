package br.datamaio.fly.quartz.job;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.android.gcm.server.Sender;

import br.datamaio.fly.RoundTrip;
import br.datamaio.fly.Schedule;
import br.datamaio.fly.check.gol.selenium.VoeGolCheck;
import br.datamaio.fly.web.ApiKey;
import br.datamaio.fly.web.SendAllMessagesServlet;

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

	    try {
			LOGGER.info(String.format("Executando Agendamento '%s-%s' ..", id, nome));	

			BigDecimal threshold = new BigDecimal("350");
			VoeGolCheck check = new VoeGolCheck();
			check.setUp(threshold);
			
			List<RoundTrip> trips = check.caxias2congonhas();
			if(trips.size()>0) {
				sendToAndroid(trips, "Caxias -> Congonhas");
			}
			
			trips = check.congonhas2caxias(); 
			if(trips.size()>0) {
				sendToAndroid(trips, "Congonhas -> Caxias");
			}
			
			check.tearDown();
			LOGGER.info(String.format("Finalizado com sucesso Agendamento '%s-%s' ..", id, nome));
		} catch (final Exception cause) {
			throw new JobExecutionException(cause, false);
		}
		
	}

	public void sendToAndroid(List<RoundTrip> trips, String prefix) throws IOException {
		// FIXME: melhorar isto.. agora é so para testar
		Sender sender = new Sender(new ApiKey().getKey());

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
		SendAllMessagesServlet.send(sender, prefix, builder.toString());
	}	
	
}
