package br.com.chai.util;

import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import br.com.chai.util.NumberUtil;

public class NumberUtilTest {

    @Test
    public void test(){
        BigDecimal b = NumberUtil.parse("2.665.205,97");
        assertThat(b, CoreMatchers.is(new BigDecimal("2665205.97")));
    }
}
