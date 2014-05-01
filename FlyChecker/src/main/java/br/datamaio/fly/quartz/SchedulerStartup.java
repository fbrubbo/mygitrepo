package br.datamaio.fly.quartz;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDateTime;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.datamaio.fly.quartz.job.MyJob;

/**
 * Inicia o quartz na inicialização do servidor
 */
@WebListener
public class SchedulerStartup implements ServletContextListener {


	@Override
	public void contextInitialized(ServletContextEvent sce) {
		SchedulerWrapper wrapper = SchedulerWrapper.get();
		wrapper.start();
		
		Schedule agen = new Schedule();
		agen.setId(1L);
		agen.setFrequency(Schedule.Frequency.PERIODIC);
		agen.setName("Teste");
		agen.setJobClass(MyJob.class);
		agen.setCron("0 0 9,22 * * ? *");
		agen.setFrom(LocalDateTime.now());
		agen.setTo(LocalDateTime.now().plus(3, DAYS));
		wrapper.rescheduleJob(agen);		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		SchedulerWrapper wrapper = SchedulerWrapper.get();
		wrapper.stop();
	}
}
