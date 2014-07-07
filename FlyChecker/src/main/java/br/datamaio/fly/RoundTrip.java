package br.datamaio.fly;

public class RoundTrip {
	private TripOption departure;
	private TripOption returning;
	public RoundTrip(TripOption departure, TripOption returning) {
		super();
		this.departure = departure;
		this.returning = returning;
	}
	public TripOption getDeparture() {
		return departure;
	}
	public TripOption getReturning() {
		return returning;
	}
}
