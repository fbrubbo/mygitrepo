package br.datamaio.fly.web;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDateTime;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.datamaio.fly.quartz.Schedule;
import br.datamaio.fly.quartz.SchedulerWrapper;
import br.datamaio.fly.quartz.job.MyJob;

/**
 * Inicia o quartz na inicialização do servidor
 */
@WebListener
public class SchedulerStartupListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("========================================================================");
		System.out.println("============== SchedulerStartupListener:contextInitialized =============");
		System.out.println("========================================================================");
		
		SchedulerWrapper wrapper = SchedulerWrapper.get();
		wrapper.start();
		
		Schedule agen = new Schedule();
		agen.setId(1L);
		agen.setFrequency(Schedule.Frequency.PERIODIC);
		agen.setName("Teste");
		agen.setJobClass(MyJob.class);
		agen.setCron("0 26 23 * * ? *");
		agen.setFrom(LocalDateTime.now());
		agen.setTo(LocalDateTime.now().plus(3, DAYS));
		wrapper.rescheduleJob(agen);		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("========================================================================");
		System.out.println("============== SchedulerStartupListener:contextDestroyed =============");
		System.out.println("========================================================================");
		
		SchedulerWrapper wrapper = SchedulerWrapper.get();
		wrapper.stop();
	}
}
