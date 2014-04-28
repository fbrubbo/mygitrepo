package br.datamaio.fly.quartz;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.quartz.Job;

import br.datamaio.fly.quartz.validation.Cron;
import br.datamaio.fly.quartz.validation.Periodic;
import br.datamaio.fly.quartz.validation.Single;

/**
 * The persistent class for the agendamento database table.
 */
@Entity
@Table(name = "agendamento")
public class Schedule {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false, length = 64)
	@NotNull(message = "O nome do agendamento é obrigatório!")
	@Size(min = 4, max = 64, message = "Tamanho do nome deve ser maior que 4 e menor que 64 posições!")
	private String name;

	@Column(name = "jobClass")
	@NotNull(message = "A classe do Job é obrigatória!")
	private Class<? extends Job> jobClass;

	@Enumerated(EnumType.STRING)
	@Column(name = "frequency")
	@NotNull(message = "O tipo do agendamento é obrigatório!")
	private Frequency frequency;	
	
	// PERIODICO
	@Column(name = "cron", nullable = true, length = 64)
	@Cron	
	@NotNull(message = "Para agendamento periodico a expressão Cron é obrigatória!", groups = Periodic.class)
	private String cron;

	@Temporal(TemporalType.DATE)
	@Column(name = "from", nullable = true)
	@Future(message = "A data inicial deve estar no futuro!")
	@NotNull(message = "Para agendamento periodico a data inicial é obrigatória!", groups = Periodic.class)
	private LocalDateTime from;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dataFinal", nullable = true)
	@Future(message = "A data final deve estar no futuro!")
	@NotNull(message = "Para agendamento periodico a data final é obrigatória!", groups = Periodic.class)
	private LocalDateTime to;

	// UNICO
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datahora", nullable = true)
	@Future(message = "A data e hora deve estar no futuro!")
	@NotNull(message = "Para agendamento único a data e hora é obrigatória!", groups = Single.class)
	private LocalDateTime time;

	
	public Schedule() {
		this(Frequency.SINGLE);
	}
	public Schedule(final Frequency tipo) {
		this.frequency = tipo;
	}

	public Long getId() {
		return this.id;
	}
	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(final String nome) {
		this.name = nome;
	}

	public Class<? extends Job> getJobClass() {
		return jobClass;
	}
	public void setJobClass(Class<? extends Job> jobClass) {
		this.jobClass = jobClass;
	}

	public Frequency getFrequency() {
		return frequency;
	}
	public void setFrequency(final Frequency tipo) {
		this.frequency = tipo;
	}

	public String getCron() {
		return cron;
	}
	public void setCron(final String cron) {
		this.cron = cron;
	}
	
	public LocalDateTime getFrom() {
		return from;
	}
	public void setFrom(final LocalDateTime dataInicial) {
		this.from = dataInicial;
	}

	public LocalDateTime getTo() {
		return to;
	}
	public void setTo(final LocalDateTime dataFinal) {
		this.to = dataFinal;
	}
	
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(final LocalDateTime dataHora) {
		this.time = dataHora;
	}
	
	
	
	/**
	 * Retorna o nome que será utilizado no Job
	 *
	 * @return o nome do Job
	 */
	@Transient
	public String getJobName() {
		return "job-" + id;
	}

	@Override
	public String toString() {
		final DateTimeFormatter DF = ofPattern("dd/MM/yyyy HH:mm:ss");
		return "AgendamentoPeriodico ["
				+ " id=" + getId()
				+ ", nome=" + getName()
				+ ", inicial=" + (from != null ? from.format(DF) : "null")
				+ ", dataFinal=" + (to != null ? to.format(DF) : "null")
				+ ", cron=" + cron
				+ ", dataHora=" + (time != null ? time.format(DF) : "null") 
				+ "]";
	}

	
	public static enum Frequency {

		PERIODIC(Periodic.class),
		SINGLE(Single.class);

		Class<?> validationGroup;

		Frequency(final Class<?> validationGroup) {
			this.validationGroup = validationGroup;
		}

		public Class<?> getValidationGroup() {
			return validationGroup;
		}
	}
}