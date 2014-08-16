package br.datamaio.fly.check.gol.urlconn;

import java.time.LocalDate;

import br.datamaio.fly.DayPeriod;
import br.datamaio.fly.RoundTrip;
import br.datamaio.fly.check.gol.VoeGolCheck;

public class UrlConnVoeGolCheck extends VoeGolCheck {

	public RoundTrip getBestRoundTripOption(final String from, final String to, final LocalDate ddep,
			final DayPeriod pdep, final LocalDate dret, final DayPeriod pret) {
		return new SearchFly(from, to, ddep, dret).getBestRoundTripOption(pdep, pret);
	}

}
