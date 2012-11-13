package br.com.datamaio.fwk.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import br.com.datamaio.fwk.entity.BasicEntity;

public class HibernateUtil {
	
	   @SuppressWarnings("unchecked")
		public static <E> Class<E> inferEntityClass(Object instance, Class<?> limitClass) {
			
			Class<? extends Object> instClass = instance.getClass();
			Type superType = instClass.getGenericSuperclass();
	        // procura o primeiro tipo parametrizado na hierarquia
	        while(!(superType instanceof ParameterizedType))
	        {
	            final Class<?> superclass = (Class<?>) superType;
	            if(superclass.isAssignableFrom(limitClass))
	            {
	                // se chegou at� o CrudDao e n�o � um tipo parametrizado
	                // Isto pode acontecer se algu�m estender diretamente a CrudDAO e n�o passar o par�metro de tipo
	                final String daoName = instClass.getName();
	                throw new RuntimeException("A classe " + daoName
	                        + " ou uma das suas super classes devem declarar explicitamente o par�metro de tipo E!");
	            }
	            superType = superclass.getGenericSuperclass();
	        }

	        final ParameterizedType parameterizedType = (ParameterizedType) superType;
	        final Type typeArgument = parameterizedType.getActualTypeArguments()[0];
	        if(typeArgument instanceof TypeVariable<?>)
	        {
	            // o supertipo � parametrizado, mas o par�metro � uma variavel de tipo
	            // Isto pode acontecer se o DAO extende CrudDao, por exemplo, e n�o informa o par�metro de tipo
	            final String daoName = instClass.getName();
	            throw new RuntimeException("O DAO com nome " + daoName
	                    + " ou uma das suas super classes devem declarar explicitamente o par�metro de tipo E!");
	        }


	        final Class<E> entityClazz = (Class<E>) typeArgument;
	        if(!BasicEntity.class.isAssignableFrom(entityClazz))
	        {
	            // cai aqui se o programador informou uma classe que n�o persistable como par�metro de tipo
	            final String daoName = instClass.getName();
	            throw new RuntimeException("O DAO com nome " + daoName
	                    + " ou uma das suas super classes devem declarar explicitamente o primeiro par�metro de tipo com "
	                    + "uma subclasse de Persistable");
	        }
			return entityClazz;
		}


	
    /**
    *
    * Verifica se os dois objetos tem a mesma classe
    *
    * @param o1 Objeto 1
    * @param o2 Objeto 2
    * @return true se as classes s�o as mesmas
    */
   public static boolean isSameClass(Object o1, Object o2)
   {
       return getEntityClass(o1).equals(getEntityClass(o2));
   }

	
	   /**
	    * Retorna a real classe de uma entidade
	    *
	    * @param obj Objeto
	    * @return classe da entidade
	    */
	   public static Class<?> getEntityClass(Object obj)
	   {
	       return getBean(obj).getClass();
	   }
	   
	   /**
	    * Pega o objeto real que esta sendo utilizado. Isto � necess�rio pois o hibernate empacota alguns objetos em
	    * proxies.
	    *
	    * @param example o exemplo do bean que ser� utilizado na convers�o
	    * @return o objeto real que esta sendo utilizado
	    */
	   public static Object getBean(Object example)
	   {
	       if(example instanceof HibernateProxy)
	       {
	           HibernateProxy proxy = (HibernateProxy) example;
	           LazyInitializer initializer = proxy.getHibernateLazyInitializer();

	           // este c�digo � soh para garantir que n�o vai dar um lazy initialization.
	           SessionImplementor implementor = initializer.getSession();
	           if(initializer.isUninitialized())
	           {
	               try
	               {
	                   // getImplementation is going to want to talk to a session
	                   if(implementor.isClosed())
	                   {
	                       // Give up and return example.getClass();
	                       return example;
	                   }
	               }
	               catch(NoSuchMethodError ex)
	               {
	                   // We must be using Hibernate 3.0/3.1 which doesn't have
	                   // this method
	               }
	           }

	           return initializer.getImplementation();
	       }
	       else
	       {
	           return example;
	       }
	   }

}
