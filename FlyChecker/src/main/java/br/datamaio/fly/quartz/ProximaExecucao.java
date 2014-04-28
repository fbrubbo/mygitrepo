package br.datamaio.fly.quartz;

import java.util.Calendar;

/**
 * DTO que contem a pr�xima execu��o
 */
public class ProximaExecucao {

	private final Long id;
	private String nome;
	private Calendar dataHora;

	/**
	 * Construtor padr�o
	 */
	public ProximaExecucao() {
		this(null, null, null);
	}

	/**
	 * Construtor
	 *
	 * @param id do Agendamento
	 * @param nome do Job
	 * @param dataHora data e hora da pr�xima execu��o
	 */
	public ProximaExecucao(final Long id, final String nome,
			final Calendar dataHora) {
		this.id = id;
		this.nome = nome;
		this.dataHora = dataHora;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public Calendar getDataHora() {
		return dataHora;
	}

	public void setDataHora(final Calendar dataHora) {
		this.dataHora = dataHora;
	}

	public Long getId() {
		return id;
	}
}
