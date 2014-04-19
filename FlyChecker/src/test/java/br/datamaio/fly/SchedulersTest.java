package br.datamaio.fly;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import static br.datamaio.fly.SchedulerHelper.build1_OP1_OP2_OP3;
import static br.datamaio.fly.SchedulerHelper.build2_OP3_OP2;
import static br.datamaio.fly.SchedulerHelper.build3_OP3;
import static br.datamaio.fly.SchedulerHelper.build4;

import org.junit.Test;

public class SchedulersTest {
    @Test
    public void retrieveTheBestScheduler(){

        Schedule s1 = build1_OP1_OP2_OP3();
        Schedule s2 = build2_OP3_OP2();
        Schedule s3 = build3_OP3();
        Schedule s4 = build4();

        ScheduleOptions s = new ScheduleOptions(s1, s2, s3, s4);
        assertThat(s.getBestSchedule(), is(s1));

        s = new ScheduleOptions(s2, s3);
        assertThat(s.getBestSchedule(), is(s2));

        s = new ScheduleOptions(s3, s4);
        assertThat(s.getBestSchedule(), is(s3));

        s = new ScheduleOptions(s4);
        assertThat(s.getBestSchedule(), is(nullValue()));

//        s = build1_OP2_OP1_OP3();
//        assertThat(s.getBestOption(), is(OP1));
//
//        s = build1_OP3_OP2_OP1();
//        assertThat(s.getBestOption(), is(OP1));
    }
}
