package br.datamaio.fly.check.gol;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.TemporalAdjusters.next;

import static br.datamaio.fly.DayPeriod.AFTERNOON;
import static br.datamaio.fly.DayPeriod.MORNING;
import static br.datamaio.fly.DayPeriod.NIGHT;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import br.datamaio.fly.DayPeriod;
import br.datamaio.fly.Option;
import br.datamaio.fly.check.gol.pages.SearchPage;
import br.datamaio.fly.check.gol.pages.SelectFlyPage;

public class VoeGolCheck {

    private static final int PERIOD_IN_MONTH = 6;
    private static final String CONGONHAS = "Congonhas";
    private static final String CAXIAS = "Caxias do Sul";
    private static final DateTimeFormatter DFbr = ofPattern("dd/MM/yyyy");

    private static WebDriver driver;

    @BeforeClass
    public static void setUp(){
        Path f = Paths.get("chromedriver.exe");
        System.setProperty("webdriver.chrome.driver",f.toAbsolutePath().toString());
        driver = new ChromeDriver();
    }

    @AfterClass
    public static void tearDown(){
        driver.quit();
    }

    @Test
    public void sp2caxias() throws Exception {
        String repName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Path logFile = Paths.get("reports", "sp2caxias" + repName + ".txt");
        try (BufferedWriter writter = Files.newBufferedWriter(logFile)) {
            Period period = Period.ofMonths(PERIOD_IN_MONTH);
            LocalDate fromDate = LocalDate.now();
            LocalDate untilDate = fromDate.plus(period);

            write(writter, String.format("Searching Flyies from '%s' to '%s' ", fromDate.format(DFbr), untilDate.format(DFbr)));

            LocalDate next = fromDate;
            do {
                LocalDate friday    = next.with(next(DayOfWeek.FRIDAY));
                LocalDate saturday  = friday.with(next(DayOfWeek.SATURDAY));
                LocalDate sunday    = friday.with(next(DayOfWeek.SUNDAY));
                LocalDate monday    = friday.with(next(DayOfWeek.MONDAY));

                check(writter, friday, AFTERNOON, sunday, NIGHT);
                check(writter, friday, AFTERNOON, monday, MORNING);

                check(writter, saturday, MORNING, sunday, NIGHT);
                check(writter, saturday, MORNING, monday, MORNING);

                next = monday;
            } while(next.compareTo(untilDate)<0);
        }
    }

    @Test
    public void caxias2sp() throws Exception {
        String repName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Path logFile = Paths.get("reports", "caxias2sp" + repName + ".txt");
        try (BufferedWriter writter = Files.newBufferedWriter(logFile)) {
            Period period = Period.ofMonths(PERIOD_IN_MONTH);
            LocalDate fromDate = LocalDate.now();
            LocalDate untilDate = fromDate.plus(period);

            write(writter, String.format("Searching Flyies from '%s' to '%s' ", fromDate.format(DFbr), untilDate.format(DFbr)));

            LocalDate next = fromDate;
            do {
                LocalDate friday    = next.with(next(DayOfWeek.FRIDAY));
                LocalDate saturday  = friday.with(next(DayOfWeek.SATURDAY));
                LocalDate sunday    = friday.with(next(DayOfWeek.SUNDAY));
                LocalDate monday    = friday.with(next(DayOfWeek.MONDAY));

                check2(writter, friday, NIGHT, sunday, NIGHT);
                check2(writter, friday, NIGHT, monday, MORNING);

                check2(writter, saturday, MORNING, sunday, NIGHT);
                check2(writter, saturday, MORNING, monday, MORNING);

                next = monday;
            } while(next.compareTo(untilDate)<0);
        }
    }

    private void check(final BufferedWriter writter,
            final LocalDate ddep, final DayPeriod pdep,
            final LocalDate dret, final DayPeriod pret) throws Exception {

        SearchPage search = new SearchPage(driver).navigate()
                .selectRoundTrip().from(CONGONHAS).to(CAXIAS)
                .departure(ddep).returning(dret);
        SelectFlyPage selectFly = search.buy();
        Option od = selectFly.getBestDepartureOption(pdep);
        Option or = selectFly.getBestReturningOption(pret);
        if (od == null || or == null) {
            return;
        }
        write(writter, "==============================================================================================");
        write(writter, String.format("%s -> %s", CONGONHAS, CAXIAS));
        write(writter, String.format("\tIDA     : %s dia %s (%s): %s", ddep.getDayOfWeek(), ddep.format(DFbr), pdep, od.getValue()));
        write(writter, String.format("\tVOLTA   : %s dia %s (%s): %s", dret.getDayOfWeek(), dret.format(DFbr), pret, or.getValue()));
        write(writter, String.format("\t** TOTAL ** : %s", od.getValue().add(or.getValue()) ));
    }

    private void check2(final BufferedWriter writter,
            final LocalDate ddep, final DayPeriod pdep,
            final LocalDate dret, final DayPeriod pret) throws Exception {

        SearchPage search = new SearchPage(driver).navigate()
                .selectRoundTrip().from(CAXIAS).to(CONGONHAS)
                .departure(ddep).returning(dret);
        SelectFlyPage selectFly = search.buy();
        Option od = selectFly.getBestDepartureOption(pdep);
        Option or = selectFly.getBestReturningOption(pret);
        if (od == null || or == null) {
            return;
        }
        write(writter, "==============================================================================================");
        write(writter, String.format("%s -> %s", CAXIAS, CONGONHAS));
        write(writter, String.format("\tIDA     : %s dia %s (%s): %s", ddep.getDayOfWeek(), ddep.format(DFbr), pdep, od.getValue()));
        write(writter, String.format("\tVOLTA   : %s dia %s (%s): %s", dret.getDayOfWeek(), dret.format(DFbr), pret, or.getValue()));
        write(writter, String.format("\t** TOTAL ** : %s", od.getValue().add(or.getValue()) ));
    }

    private void write(final BufferedWriter writter, final String msg) throws IOException {
        System.out.println(msg);
        writter.write(msg);
        writter.newLine();
        writter.flush();
    }
}
