package br.datamaio.fly.quartz.job;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.android.gcm.server.Sender;

import br.datamaio.fly.check.gol.VoeGolCheck;
import br.datamaio.fly.web.ApiKeyInitializer;
import br.datamaio.fly.web.SendAllMessagesServlet;

@DisallowConcurrentExecution
public class MyJob implements Job {

	private static final Logger LOGGER = Logger.getLogger(MyJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		final JobDataMap dataMap = context.getJobDetail().getJobDataMap();
	    final Long id = dataMap.getLong("id");
	    final String nome = dataMap.getString("nome");

	    try {
			LOGGER.info(String.format("Executando Agendamento '%s-%s' ..", id, nome));
			
			// FIXME: melhorar isto.. agora é so para testar
			Sender sender = new Sender(new ApiKeyInitializer().getKey());
			SendAllMessagesServlet.send(sender);
			
			VoeGolCheck check = new VoeGolCheck();
			check.setUp();
			check.caxias2congonhas_apenasida();
			check.tearDown();
			LOGGER.info(String.format("Finalizado com sucesso Agendamento '%s-%s' ..", id, nome));
		} catch (final Exception cause) {
			throw new JobExecutionException(cause, false);
		}
		
	}	
	
}
