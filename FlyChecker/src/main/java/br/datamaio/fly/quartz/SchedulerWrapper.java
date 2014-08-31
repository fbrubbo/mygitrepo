package br.datamaio.fly.quartz;

import static java.time.ZoneOffset.UTC;
import static org.quartz.CronScheduleBuilder.cronSchedule;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

/**
 * Classe utilizada para gerenciar o scheduler do Quartz
 *
 * @author Fernando Rubbo
 */
public class SchedulerWrapper {
    private static final Logger LOGGER = Logger.getLogger(SchedulerWrapper.class);
	private static final String QUARTZ_PROPERTIES = "quartz.properties";

	private static SchedulerWrapper INSTANCE;

	/** Retorna a instância deste objeto */
	public static synchronized SchedulerWrapper get() {
		if (INSTANCE == null) {
			INSTANCE = new SchedulerWrapper();
		}
		return INSTANCE;
	}


	private Scheduler scheduler;

	SchedulerWrapper() {}

	/** Inicia o scheduler do quartz */
	public synchronized void start() {
		try {
			LOGGER.info("Inicializando Servico Singleton do QUARTZ ...");

			// Carrega arquivo de propriedades do Quartz e inicializa schedule
			final Properties quartzProperties = loadQuartzProperties();
			final StdSchedulerFactory factory = new StdSchedulerFactory(quartzProperties);
			scheduler = factory.getScheduler();
			scheduler.start();

			LOGGER.info("Servico Singleton do QUARTZ inicializado.");
		} catch (final Exception e) {
			LOGGER.error("Erro ao inicializar o servico do QUARTZ", e);
		}
	}

	/** Retorna as execu��es que ir�o inicar nos pr�ximos 5 mimutos	 */
	public synchronized List<ProximaExecucao> buscarProximasExecucoes() {

		final Calendar now = Calendar.getInstance();
		final Calendar fiveMinutesFromNow = Calendar.getInstance();
		fiveMinutesFromNow.add(Calendar.MINUTE, 5);

		final List<ProximaExecucao> retorno = new ArrayList<ProximaExecucao>();
		if (isSchedulerStarted()) {
			try {
				final Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals("group-agendamento"));
				for (final TriggerKey triggerKey : triggerKeys) {
					final Trigger trigger = scheduler.getTrigger(triggerKey);
					final Date nextFireTime = trigger.getNextFireTime();
					if (nextFireTime != null) {
						Calendar temp = Calendar.getInstance();
						temp.setTime(nextFireTime);
						final Calendar nextFire = temp;
						if (nextFire.after(now) && nextFire.before(fiveMinutesFromNow)) {
							final JobDetail job = scheduler.getJobDetail(trigger.getJobKey());
						    final JobDataMap dataMap = job.getJobDataMap();
						    final Long id = dataMap.getLong("id");
						    final String nome = dataMap.getString("nome");
						    final ProximaExecucao dto = new ProximaExecucao(id, nome, nextFire);
						    retorno.add(dto);
						}
					}
				}
			} catch (final Exception e) {
				LOGGER.error("Erro ao inicializar o servico do QUARTZ", e);
			}
		}

		return retorno;
	}



	/**
	 * Agenda o job. Se j� existe, remove o velho e recria o agendamento.
	 *
	 * @param sched os dados necess�rios para o agendamento
	 */
	public synchronized void rescheduleJob(final Schedule sched) {
		Objects.requireNonNull(sched.getId());

		if (isSchedulerStarted()) {
			unscheduleJob(sched);
			scheduleJob(sched);
		} else {
			LOGGER.warn(String.format("Agendamento '%d-%s' não foi realizado pois o QUARTZ não foi iniciado!", sched.getId(), sched.getName()));
		}
	}

	/**
	 * Se o job j� existe, deleta ele e as triggers relacionadas
	 *
	 * @param sched o agendamento
	 */
	public synchronized void unscheduleJob(final Schedule sched) {
		Objects.requireNonNull(sched.getId());

		if (isSchedulerStarted()) {
			try {
				final JobDetail job = buildJob(sched);
				final JobKey key = job.getKey();
				if (scheduler.checkExists(key)) {
					scheduler.deleteJob(key);
					LOGGER.debug(String.format("Agendamento '%d-%s' foi cancelado!", sched.getId(), sched.getName()));
				}
			} catch (final SchedulerException e) {
				throw new RuntimeException(String.format("Ocorreu um erro inesperado "
						+ "removendo job '%d-%s' do agendamento!", sched.getId(), sched.getName()), e);
			}
		} else {
			LOGGER.warn(String.format("Cancelamento do agentamento '%d-%s' não "
					+ "foi executado pois o QUARTZ não foi iniciado!", sched.getId(), sched.getName()));
		}
	}
	
	public void stop(){
		try {
			scheduler.shutdown(true);
		} catch (SchedulerException e) {
			LOGGER.warn("ERRO PARANDO A SCHEDULER", e);
		}
	}

	private void scheduleJob(final Schedule sched) {
		try {
			final JobDetail job = buildJob(sched);
			final Trigger trigger = buildTrigger(sched);
			scheduler.scheduleJob(job, trigger);
			LOGGER.debug(String.format("Agendamento '%d-%s' foi registrado!", sched.getId(), sched.getName()));
		} catch (final SchedulerException e) {
			throw new RuntimeException(String.format("Ocorreu um erro inesperado agendando job '%d-%s'!",  sched.getId(), sched.getName()), e);
		}
	}

	private JobDetail buildJob(final Schedule sched) {
		return JobBuilder.newJob(sched.getJobClass())
				.withIdentity(sched.getJobName(), "agendamento")
				.usingJobData("id", sched.getId())
				.usingJobData("nome", sched.getName())
				.build();
	}

	private Trigger buildTrigger(final Schedule sched) {
		final TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger()
				.withIdentity("trigger-" + sched.getJobName(), "agendamento")
				.startNow();

		if (Schedule.Frequency.SINGLE == sched.getFrequency()) {
			builder.startAt(Date.from(sched.getTime().toInstant(UTC)));
		} else {
			builder.startAt(Date.from(sched.getFrom().toInstant(UTC)))
				.endAt(Date.from(sched.getTo().toInstant(UTC)))
				.withSchedule(cronSchedule(sched.getCron())
								.withMisfireHandlingInstructionDoNothing());
		}

		return builder.build();
	}

	/** Indica se o quartz est� ativo */
	public boolean isSchedulerStarted() {
		return scheduler != null;
	}

    /** Busca arquivo de configura��o dos servi�os Quartz 2 */
	private Properties loadQuartzProperties() {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(QUARTZ_PROPERTIES)) {
			Properties properties = new Properties();
			properties.load(is);
			return properties;
		} catch (IOException e) {
			throw new RuntimeException(String.format("Erro na leitura do arquivo %s", QUARTZ_PROPERTIES), e);
		}
	}
}
