package br.datamaio.fly.check.gol.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import org.apache.http.client.CookieStore;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import br.datamaio.fly.Schedule;
import br.datamaio.fly.TripOption;

public class SearchFly {
	public static void main(String[] args) throws Exception {

		
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
	    CookieStore cookieStore = new BasicCookieStore();
	    HttpClientContext context = HttpClientContext.create();
	    context.setCookieStore(cookieStore);

	    CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();
	    HttpGet get = new HttpGet("http://www.voegol.com.br/pt-br/Paginas/default.aspx");
	    HttpResponse r1 = client.execute(get);
	    System.out.println(context.getCookieStore().getCookies());
		System.out.println("Response Code : " + r1.getStatusLine().getStatusCode());
	    
		
		HttpPost post = new HttpPost("http://compre2.voegol.com.br/CSearch.aspx?culture=pt-br");
		
		post.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		post.addHeader("Accept-Language", "en-US,en;q=0.8,pt-BR;q=0.6,pt;q=0.4,es;q=0.2");
		post.addHeader("Cache-Control", "max-age=0");
		post.addHeader("Connection", "keep-alive");
		post.addHeader("Host", "compre2.voegol.com.br");
		post.addHeader("Referer", "http://compre2.voegol.com.br/Search.aspx");
		post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

		urlParameters.add(new BasicNameValuePair("AdultPaxCount", "1"));
		urlParameters.add(new BasicNameValuePair("ChildrenPaxCount", "0"));
		urlParameters.add(new BasicNameValuePair("DepartureStation", "CXJ"));
		urlParameters.add(new BasicNameValuePair("DepartureDate", "2014-08-22"));
		urlParameters.add(new BasicNameValuePair("ReturnDate", "2014-08-24"));
		urlParameters.add(new BasicNameValuePair("MaxPrice", ""));
		urlParameters.add(new BasicNameValuePair("MarketStructure", "RoundTrip"));
		urlParameters.add(new BasicNameValuePair("bttrechos", "on"));
		urlParameters.add(new BasicNameValuePair("ctl00$PlaceHolderCentro$fieldde", "Caxias do Sul (CXJ)"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketOrigin1", "CXJ"));
		urlParameters.add(new BasicNameValuePair("ctl00$PlaceHolderCentro$fieldpara", "São Paulo - Congonhas (CGH)"));
		urlParameters.add(new BasicNameValuePair("ctl00$PlaceHolderCentro$fieldpara1", "Digite o destino"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketDestination1", "CGH"));
		urlParameters.add(new BasicNameValuePair("ctl00$PlaceHolderCentro$fieldmultide", "Digite a origem"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketOrigin2", ""));
		urlParameters.add(new BasicNameValuePair("ctl00$PlaceHolderCentro$fieldmultipara", "Digite o destino"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketDestination2", ""));
		urlParameters.add(new BasicNameValuePair("data_ida", "22/08/2014"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay1", "22"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth1", "2014-08"));
		urlParameters.add(new BasicNameValuePair("data_volta", "24/08/2014"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay2", "24"));
		urlParameters.add(new BasicNameValuePair("ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth2", "2014-08"));
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

		HttpResponse r2 = client.execute(post);
	    System.out.println(context.getCookieStore().getCookies());
	    System.out.println(r2.getHeaders("Location")[0]);
		System.out.println("Response Code : " + r2.getStatusLine().getStatusCode());
		
		
	    HttpGet get3 = new HttpGet("http://compre2.voegol.com.br/Search2.aspx");
	    HttpResponse r3 = client.execute(get3);

		BufferedReader rd = new BufferedReader(new InputStreamReader(r3.getEntity().getContent()));

//		StringBuffer result = new StringBuffer();
//		String line = "";
//		while ((line = rd.readLine()) != null) {
//			result.append(line);
//		}
//		
//		System.out.println(result.toString());
		
		

	 
		
 
        // THIS IS WHERE THE HTMLCLEANER COMES IN, I INITIALIZE IT HERE
        HtmlCleaner cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        props.setAllowHtmlInsideAttributes(true);
        props.setAllowMultiWordAttributes(true);
        props.setRecognizeUnicodeChars(true);
        props.setOmitComments(true);
 
        TagNode node = cleaner.clean(rd);
        List<Schedule> list = buildSchedules(node, "ida", LocalDate.now());
        for (Schedule schedule : list) {
			System.out.println(schedule);
		}
 
//        // ONCE THE HTML IS CLEANED, THEN YOU CAN RUN YOUR XPATH EXPRESSIONS ON THE NODE, WHICH WILL THEN RETURN AN ARRAY OF TAGNODE OBJECTS (THESE ARE RETURNED AS OBJECTS BUT GET CASTED BELOW)
//        Object[] info_nodes = node.evaluateXPath("//div[@id='ida']/div[@class='ContentTable']/div[@class='lineTable']");
//        for (Object object : info_nodes) {
//			System.out.println(object);
//		}
	}
    private static List<Schedule> buildSchedules(TagNode node, final String trip, final LocalDate date) throws Exception {
        final List<Schedule> schedules = new ArrayList<>();
        Object[] aa = node.evaluateXPath(String.format("//div[@id='%s']/div[@class='ContentTable']/div[@class='lineTable']", trip));
		List<TagNode> lines = Arrays.asList((TagNode[])aa);
        lines.forEach(line -> {
        	try {
				TagNode leftHeader = (TagNode)line.evaluateXPath("./div[contains(@class,'status')]")[0];
				String flyNumber = ((TagNode)leftHeader.evaluateXPath("./span[@class='titleAirplane']/div[@class='operatedBy']")[0]).getText().toString();
				String takeofTime = ((TagNode)leftHeader.evaluateXPath("./div[@class='scale']/div[@class='infoScale']/span[@class='timeGoing']")[0]).getText().toString();
				String landingTime = ((TagNode)leftHeader.evaluateXPath("./div[@class='scale']/div[@class='infoScale']/span[@class='timeoutGoing']")[0]).getText().toString();
				Schedule s = new Schedule(flyNumber, date, LocalTime.parse(takeofTime), LocalTime.parse(landingTime));
				schedules.add(s);
				List<TagNode> ops = Arrays.asList((TagNode[])line.evaluateXPath("./div[contains(@class,'taxa ')]"));
				ops.forEach(op -> {
				    try {
						String type = op.getAttributeByName("class").substring(5);
						Object[] list = op.evaluateXPath("./div/strong[@class='fareValue']");
						if(list.length>0){
							TagNode el = (TagNode)list[0];
						    try {
						        Number value = NumberFormat.getCurrencyInstance().parse(el.getText().toString());
						        s.addOption(new TripOption(s, type, new BigDecimal(value.toString())));
						    } catch (Exception e) {
						        e.printStackTrace();
						    }
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
        return schedules;
    }
	
}