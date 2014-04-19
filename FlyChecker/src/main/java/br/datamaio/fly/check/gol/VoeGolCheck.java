package br.datamaio.fly.check.gol;

import static java.time.LocalTime.MAX;
import static java.time.LocalTime.NOON;
import static java.time.temporal.TemporalAdjusters.next;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import br.datamaio.fly.Option;
import br.datamaio.fly.Schedule;
import br.datamaio.fly.check.gol.pages.SearchPage;
import br.datamaio.fly.check.gol.pages.SelectFlyPage;

public class VoeGolCheck {

    @Test
    public void testdate(){
        Period period = Period.ofMonths(6);
        LocalDate fromDate = LocalDate.now();
        LocalDate untilDate = fromDate.plus(period);
        System.out.println(String.format("from %s to %s ", fromDate, untilDate ));

        LocalDate next = fromDate;
        do {
            next = next.with(next(DayOfWeek.FRIDAY));
            print(next);

            next = next.with(next(DayOfWeek.SATURDAY));
            print(next);

            System.out.println("=================");
        } while(next.compareTo(untilDate)<0);

    }

    private void print(final LocalDate next) {
        LocalDate op1 = next.with(next(DayOfWeek.SUNDAY));
        LocalDate op2 = next.with(next(DayOfWeek.MONDAY));
        System.out.println(String.format("%s (%s, %s)", next, op1, op2));
    }

    @Test
    public void test(){
        Path f = Paths.get("chromedriver.exe");
        System.setProperty("webdriver.chrome.driver",f.toAbsolutePath().toString());
        WebDriver driver = new ChromeDriver();

//        WebDriver driver = new FirefoxDriver();

//        File f2 = new File("IEDriverServer_x86.exe");
//        System.setProperty("webdriver.ie.driver",f2.getAbsolutePath());
//        WebDriver driver = new InternetExplorerDriver();


        SearchPage search = new SearchPage(driver).navigate()
            .selectRoundTrip().from("Caxias do Sul").to("Congonhas")
            .departure(LocalDate.now().plus(Period.ofDays(1))).returning(LocalDate.now().plus(Period.ofDays(2)));
        SelectFlyPage selectFly = search.buy();
        Schedule sd = selectFly.getBestDepartureSchedule();
        System.out.println(sd);
        Option od = sd.getBestOption();
        System.out.println(od);
        Option or = selectFly.getBestReturningOption(NOON, MAX);
        System.out.println(or);



//        SearchPage search2 = new SearchPage(driver).navigate()
//            .selectOneWay().from("Caxias do Sul").to("Congonhas")
//            .departure(LocalDate.now());
//        SelectFlyPage selectFly2 = search2.buy();

        driver.quit();
    }
}
