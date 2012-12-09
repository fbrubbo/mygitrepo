package br.com.datamaio.fwk.dao;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.gradle.util.ReflectionUtil;
import org.junit.Assert;
import org.junit.Test;


public class SearchableDaoTest {
	
	@Test
	public void test(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
		EntityManager em = emf.createEntityManager();
		
		SearchableDao<A, ACriteria> dao = new SearchableDao<A, ACriteria>(A.class);
		ReflectionUtil.setProperty(dao, "em", em);
		ACriteria crit = new ACriteria();
		crit.setBnome("teste");
		List<A> list = dao.findBy(crit);
		Assert.assertThat(list.size(), is(0));
		
		long size = dao.count(crit);
		Assert.assertThat(size, is(0L));
	}

}
