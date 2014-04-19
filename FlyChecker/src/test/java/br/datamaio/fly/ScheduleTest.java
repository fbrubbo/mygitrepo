package br.datamaio.fly;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import static br.datamaio.fly.SchedulerHelper.OP1;
import static br.datamaio.fly.SchedulerHelper.OP2;
import static br.datamaio.fly.SchedulerHelper.OP3;
import static br.datamaio.fly.SchedulerHelper.build4;
import static br.datamaio.fly.SchedulerHelper.build1_OP1_OP2_OP3;
import static br.datamaio.fly.SchedulerHelper.build3_OP3;
import static br.datamaio.fly.SchedulerHelper.build2_OP3_OP2;

import org.junit.Test;

public class ScheduleTest {

    @Test
    public void retrieveTheBestOption(){

        Schedule s = build1_OP1_OP2_OP3();
        assertThat(s.getBestOption(), is(OP1));

        s = build2_OP3_OP2();
        assertThat(s.getBestOption(), is(OP2));

        s = build3_OP3();
        assertThat(s.getBestOption(), is(OP3));

        s = build4();
        assertThat(s.getBestOption(), is(nullValue()));

    }

}
