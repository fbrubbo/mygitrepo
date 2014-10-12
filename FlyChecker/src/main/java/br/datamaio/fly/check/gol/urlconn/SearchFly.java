package br.datamaio.fly.check.gol.urlconn;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.datamaio.fly.DayPeriod;
import br.datamaio.fly.RoundTrip;
import br.datamaio.fly.Schedule;
import br.datamaio.fly.ScheduleOptions;
import br.datamaio.fly.TripOption;

public class SearchFly {
	private static final DateTimeFormatter BR_DATE = ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter US_DATE = ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter US_MONTH = ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter DAY = ofPattern("dd");
	
	private static final Map<String, String> SHORT_PLACE_NAME = new HashMap<>();
	private static final Map<String, String> PLACE_NAME = new HashMap<>();
	static {
		SHORT_PLACE_NAME.put("Caxias do Sul", "CXJ");
		SHORT_PLACE_NAME.put("Congonhas", "CGH");
		PLACE_NAME.put("Caxias do Sul", "Caxias do Sul (CXJ)");
		PLACE_NAME.put("Congonhas", "São Paulo - Congonhas (CGH)");
	}
	
	private String from;
	private String to;
	private LocalDate ddep; 
	private LocalDate dret;
	
	public SearchFly(final String from, final String to, final LocalDate ddep, final LocalDate dret){
		this.from = from;
		this.to = to;
		this.ddep = ddep;
		this.dret = dret;
	}
	
	public RoundTrip getBestRoundTripOption(final DayPeriod pdep, final DayPeriod pret) {

		try {
			CloseableHttpClient client = buildHttpClient();	
			search(client);
			Document doc = getPageContent(client);
			
			Thread.sleep(6000);
			
			ScheduleOptions departureSchedules = new ScheduleOptions();
			departureSchedules.add(buildSchedules(doc, "ida", ddep));
			TripOption bestDeparture = departureSchedules.getBestOption(pdep);
			
			ScheduleOptions returningSchedules = new ScheduleOptions();
			returningSchedules.add(buildSchedules(doc, "volta", dret));
			TripOption bestReturn = returningSchedules.getBestOption(pret);
			
			if (bestDeparture == null || bestReturn == null) {
			    return null;
			}
			return new RoundTrip(bestDeparture, bestReturn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private CloseableHttpClient buildHttpClient() {
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH).build();
		CookieStore cookieStore = new BasicCookieStore();
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		return HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();
	}
	
	private void search(CloseableHttpClient client) throws Exception {
		HttpPost post = new HttpPost("http://compre2.voegol.com.br/CSearch.aspx?culture=pt-br");
		addHeaders(post);
		addParameters(post);
		client.execute(post); // faz esta requisição para setar os parâmetros na sessão
	}
	
	private Document getPageContent(CloseableHttpClient client) throws Exception {
		HttpResponse r = client.execute(new HttpGet("http://compre2.voegol.com.br/Select2.aspx"));
		return html2Dom(r.getEntity().getContent());
	}

	private void addParameters(HttpPost post) throws UnsupportedEncodingException {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				
		urlParameters.add(new BasicNameValuePair("DepartureDate", US_DATE.format(ddep))); 
		urlParameters.add(new BasicNameValuePair("data_ida", BR_DATE.format(ddep)));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay1", DAY.format(ddep)));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth1", US_MONTH.format(ddep)));

		urlParameters.add(new BasicNameValuePair("ReturnDate", US_DATE.format(dret))); 
		urlParameters.add(new BasicNameValuePair("data_volta", BR_DATE.format(dret)));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay2", DAY.format(dret)));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth2", US_MONTH.format(dret)));
		
		urlParameters.add(new BasicNameValuePair("DepartureStation", SHORT_PLACE_NAME.get(from)));
		urlParameters.add(new BasicNameValuePair("ctl00$PlaceHolderCentro$fieldde", PLACE_NAME.get(from))); 
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketOrigin1", SHORT_PLACE_NAME.get(from)));
		urlParameters.add(new BasicNameValuePair("ctl00$PlaceHolderCentro$fieldpara", PLACE_NAME.get(to)));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketDestination1", SHORT_PLACE_NAME.get(to)));
		
		urlParameters.add(new BasicNameValuePair("MarketStructure", "RoundTrip"));
		
		urlParameters.add(new BasicNameValuePair("AdultPaxCount", "1"));
		urlParameters.add(new BasicNameValuePair("ChildrenPaxCount", "0"));
		urlParameters.add(new BasicNameValuePair("MaxPrice", ""));
		urlParameters.add(new BasicNameValuePair("bttrechos", "on"));
		urlParameters.add(new BasicNameValuePair("ctl00$PlaceHolderCentro$fieldpara1", "Digite o destino"));
		urlParameters.add(new BasicNameValuePair("ctl00$PlaceHolderCentro$fieldmultide", "Digite a origem"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketOrigin2", ""));
		urlParameters.add(new BasicNameValuePair("ctl00$PlaceHolderCentro$fieldmultipara", "Digite o destino"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketDestination2", ""));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_ADT", "1"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_CHD", "0"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_INFANT", "0"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$textBoxPromoCode", ""));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$RadioButtonMarketStructure", "RoundTrip"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListSearchBy", ""));
		urlParameters.add(new BasicNameValuePair("PageFooter_SearchView$DropDownListOriginCountry", "pt-br"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$ButtonSubmit", "compre aqui"));
		urlParameters.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		urlParameters.add(new BasicNameValuePair("__EVENTTARGET", ""));
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
	}
	
	private static void addHeaders(HttpRequestBase req) {
		req.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		req.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		req.addHeader("Accept-Language", "en-US,en;q=0.8,pt-BR;q=0.6,pt;q=0.4,es;q=0.2");
		req.addHeader("Origin", "http://www.voegol.com.br");
		req.addHeader("Cache-Control", "max-age=0");
		req.addHeader("Connection", "keep-alive");
		req.addHeader("Host", "compre2.voegol.com.br");
		req.addHeader("Referer", "http://www.voegol.com.br/pt-br/Paginas/default.aspx");
		req.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
	}
	
    private static List<Schedule> buildSchedules(Document doc, final String trip, final LocalDate date) throws Exception {
		XPath xpath = XPathFactory.newInstance().newXPath();
    	final List<Schedule> schedules = new ArrayList<>();
    	
    	NodeList lines = (NodeList) xpath.evaluate(format("/html//div[@id='%s']/div[@class='ContentTable']/div[@class='lineTable']", trip), doc, XPathConstants.NODESET);
		for (int i = 0; i < lines.getLength(); i++) {
			Element line = (Element) lines.item(i);
    		Element leftHeader = (Element)xpath.evaluate("./div[@class='status']", line, XPathConstants.NODE);
			String flyNumber = (String) xpath.evaluate("./span[@class='titleAirplane']/div[@class='operatedBy']", leftHeader, XPathConstants.STRING);
			flyNumber = flyNumber.contains("&nbsp;")? flyNumber.substring(0, flyNumber.indexOf("&nbsp;")) : flyNumber;
			String takeofTime = (String) xpath.evaluate("./div[@class='scale']/div[@class='infoScale']/span[@class='timeGoing']", leftHeader, XPathConstants.STRING);
			String landingTime = (String) xpath.evaluate("./div[@class='scale']/div[@class='infoScale']/span[@class='timeoutGoing']", leftHeader, XPathConstants.STRING);
			Schedule s = new Schedule(flyNumber, date, LocalTime.parse(takeofTime), LocalTime.parse(landingTime));
			schedules.add(s);

			NodeList ops = (NodeList) xpath.evaluate("./div[contains(@class, 'taxa ')]", line, XPathConstants.NODESET);
			for (int j = 0; j < ops.getLength(); j++) {
				Element op = (Element) ops.item(j);
		        String type = op.getAttribute("class").substring(5);
		        NodeList list = (NodeList) xpath.evaluate("./div/strong[@class='fareValue']", op, XPathConstants.NODESET);
				if(list.getLength()>0){
					Element el = (Element)list.item(0);
				    try {
				        Number value = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).parse(el.getTextContent().replace("&nbsp;", " "));
				        s.addOption(new TripOption(s, type, new BigDecimal(value.toString())));
				    } catch (Exception e) {
				        e.printStackTrace();
				    }
				}
			}    
        }
        return schedules;
    }

	private static Document html2Dom(InputStream in) throws IOException, ParserConfigurationException {
		HtmlCleaner cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        props.setAllowHtmlInsideAttributes(true);
        props.setAllowMultiWordAttributes(true);
        props.setRecognizeUnicodeChars(true);
        props.setOmitComments(true);

        TagNode root = cleaner.clean(in);
        return new DomSerializer(new CleanerProperties()).createDOM(root);
	}

}