package br.com.datamaio.fwk.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;


/** 
 *
 * Classe utilitária para operações que envolvem reflexão.
 *
 * @author Fernando Rubbo
 */
public final class ReflectionUtils
{

    private static final PropertyUtilsBean PROPERTY_UTILS = new PropertyUtilsBean();

    private static final Object[] EMPTY_PARAMS = new Object[]{};
    private static final Map<Class<?>, List<Field>> FIELDS = new HashMap<Class<?>, List<Field>>();
    private static final Map<Class<?>, Map<String, Field>> FIELDS_BY_NAME = new HashMap<Class<?>, Map<String, Field>>();

    private ReflectionUtils()
    {
    }

    
    /**
     * Retorna todos os fields de uma classe. Inclusive os fields das super classes.<br>
	 * CUIDADO: Mantém cache dos fields. Isto é, pode gerar problema de
	 * concorrência se chamar o método
	 * {@link #setValueInField(Object, Field, Object)} ou
	 * {@link #getValueFromObject(Object, Field)} 
     *
     * @param c Classe
     * @return List
     */
    public static List<Field> getDeclaredFields(Class<?> c)
    {
        List<Field> fields = FIELDS.get(c);
        if(fields == null)
        {
            fields = getRecursiveDeclaredFields(c);
            FIELDS.put(c, fields);
        }
        return fields;
    }

    /**
	 * 
	 * Retorna o "field'. <br>
	 * CUIDADO: Mantém cache dos fields. Isto é, pode gerar problema de
	 * concorrÊncia se chamar o método
	 * {@link #setValueInField(Object, Field, Object)} ou
	 * {@link #getValueFromObject(Object, Field)}
	 * 
	 * @param c
	 *            Classe
	 * @param fieldName
	 *            Nome do campo
	 * @return Field
	 */
    public static Field getDeclaredField(Class<?> c, String fieldName)
    {
        Map<String, Field> fieldsMap = FIELDS_BY_NAME.get(c);
        if(fieldsMap == null)
        {
            fieldsMap = new HashMap<String, Field>();
            FIELDS_BY_NAME.put(c, fieldsMap);

            for(Field field : getDeclaredFields(c))
            {
                fieldsMap.put(field.getName(), field);
            }
        }

        return fieldsMap.get(fieldName);
    }

    /**
     * Cria uma nova instância de uma classe
     *
     * @param className nome da Classe a ser instanciada
     * @param <E> Tipo do objeto retornado.
     * @return Nova instancia de "E"
     */
    public static <E> E newInstance(final String className)
    {
        try
        {
            @SuppressWarnings("unchecked")
            Class<E> clazz = (Class<E>) Class.forName(className);
            return newInstance(clazz);
        }
        catch(ClassNotFoundException ignore)
        {
            throw new RuntimeException("Não foi possível criar a classe com nome " + className);
        }

    }

    /**
     * Cria uma nova instância de uma classe
     *
     * @param c Classe a ser instanciada
     * @param <E> Tipo do objeto retornado.
     * @return Nova instancia de "E"
     */
    public static <E> E newInstance(Class<E> c)
    {
        if(c == null)
        {
            throw new RuntimeException("Não foi possível instanciar pois a classe Não foi informada.");
        }
        try
        {
            return c.newInstance();
        }
        catch(Exception e)
        {
            throw new RuntimeException("Não foi possível instanciar ou acessar classe '" + c.getName() + "'", e);
        }
    }

    /**
     * Cria uma nova instância de uma classe
     * @param <E> Tipo do objeto retornado.
     *
     * @param c Classe a ser instanciada
     * @param params os parametros que serão passados para o construtor
     * @return Nova instancia de "E"
     */
    public static <E> E newInstance(Class<E> c, final Object... params)
    {
        if(c == null)
        {
            throw new RuntimeException("Não foi possível instanciar pois a classe Não foi informada.");
        }

        // pega tipo dos parametros
        Class<?>[] classes = new Class<?>[params.length];
        for(int i=0; i<params.length; i++)
        {
            if(params[i]==null)
            {
                throw new IllegalArgumentException("Parâmetro Não pode ser null quando se "
                        + "chama um construtor com parâmetros por reflection");
            }

            classes[i] = params[i].getClass();
        }

        // cria o objeto
        try
        {
            final Constructor<E> constructor = c.getConstructor(classes);
            return constructor.newInstance(params);
        }
        catch(Exception e)
        {
            StringBuilder msg = new StringBuilder(200);
            msg.append("Não foi possível instanciar ou acessar classe '")
                .append(c.getName())
                .append("' com os parâmetros : ");
            int i = 0;
            for(Object param : params)
            {
                msg.append((i++==0)?"":", ").append(param);
            }
            throw new RuntimeException(msg.toString(), e);
        }
    }


    /**
     * Seta um valor diretamente em um field.
     *
     * <p>
     * <b>NOTA:</b> Este método Não deve ser utilizado para setar propriedades. Para setar propriedades, favor dar
     * preferência a métodos getteres e setters
     * </p>
     *
     * @param objectInstance Objeto
     * @param field Campo
     * @param value Valor
     *
     *
     */
    public static void setValueInField(Object objectInstance, Field field, Object value)
    {
        boolean isAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            field.set(objectInstance, value);
        }
        catch(IllegalAccessException e)
        {
            throw new RuntimeException("Não foi possível acessar o field '" + field.getName() + "'", e);
        }
        finally
        {
            field.setAccessible(isAccessible);
        }
    }

    /**
     * Pega um valor diretamente de um field.
     * <p>
     * <b>NOTA:</b> Este método Não deve ser utilizado para ler propriedades. Para ler propriedades, favor dar
     * preferência a métodos getteres e setters.
     * </p>
     *
     * @param objectInstance Objeto
     * @param field Campo
     * @return Value
     */
    public static Object getValueFromObject(Object objectInstance, Field field)
    {
        boolean isAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.get(objectInstance);
        }
        catch(IllegalAccessException e)
        {
            throw new RuntimeException("Não foi possível acessar o field '" + field.getName() + "'", e);
        }
        finally
        {
            field.setAccessible(isAccessible);
        }
    }

    /**
     * Retorna o valor de um atributo em um objeto (o atributo deve ter getter)
     * Caso o fieldName contenha ponto "." busca recursivamente pelos atributos internos
     * ex.: getValueFromObject(setorLocal, "tipoLocal.tipo")
     *
     * @param objectInstance objeto contendo o atributo desejado
     * @param fieldName
     * @return valor do atributo
     */
    public static Object getValueFromObject(Object objectInstance, String fieldName)
    {
        if(objectInstance == null)
        {
            return null;
        }

        String[] fieldsName = fieldName.split("\\.");

        if(fieldsName.length > 1)
        {
            String nextField = fieldName.substring(fieldName.indexOf('.') + 1);
            return getValueFromObject(getValueFromObject(objectInstance, fieldsName[0]), nextField);
        }

        Method method = null;
        try
        {
            method = findGetter(objectInstance.getClass(), fieldName);
            return method.invoke(objectInstance);
        }
        catch(IntrospectionException e)
        {
            throw new RuntimeException("Não foi possível encontrar o método getter do atributo " + fieldName);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Houve um erro ao acessar o método " + method.getName(), e);
        }
    }

    /**
     *
     * Chama um método
     *
     * @param method método
     * @param bean Bean
     * @return Objeto
     */
    public static Object invokeMethod(Method method, Object bean)
    {
        return invokeMethod(method, bean, EMPTY_PARAMS);
    }

    /**
     *
     * Chama um método
     *
     * @param method método
     * @param bean Bean
     * @param values Valores
     * @return Objeto
     */
    public static Object invokeMethod(Method method, Object bean, Object[] values)
    {
        try
        {
            return method.invoke(bean, values);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Cannot invoke "
                    + method.getDeclaringClass().getSimpleName() + "." + method.getName() + " - " + e.getMessage(), e);
        }
    }
    
    public static Object invokeStaticMethod(Class clazz, String methodName, Object[] values)
    {
        try
        {
        	Class<?>[] parameterTypes = new Class[values.length];
        	for (int i = 0; i< values.length; i++) {
				parameterTypes[i] = values[i].getClass();
			}
        	Method method = clazz.getMethod(methodName, parameterTypes);
            return method.invoke(null, values);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Cannot invoke "
                    + clazz.getSimpleName() + "." + methodName + " - " + e.getMessage(), e);
        }
    }

    private static List<Field> getRecursiveDeclaredFields(Class<?> c)
    {
        Field[] f = c.getDeclaredFields();
        List<Field> fields = new ArrayList<Field>(Arrays.asList(f));

        Class<?> supperClass = c.getSuperclass();
        if(supperClass != Object.class)
        {
            fields.addAll(getRecursiveDeclaredFields(supperClass));
        }
        return fields;
    }

    /**
     * Wrapper para {@link PropertyUtilsBean#getPropertyDescriptors(Object)}.
     *
     * @param bean o objeto que se deseja saber as informações das propriedades
     * @return um array com as informações das propriedades
     */
    public static PropertyDescriptor[] getPropertyDescriptors(Object bean)
    {
		Class<? extends Object> clazz = bean.getClass();
		return getPropertyDescriptors(clazz);
    }

    /**
     * Wrapper para {@link PropertyUtilsBean#getPropertyDescriptors(Class)}.
     *
     * @param beanClass a classe que se deseja saber as informações das propriedades
     * @return um array com as informações das propriedades
     */
    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass)
    {
        return PROPERTY_UTILS.getPropertyDescriptors(beanClass);
    }

    /**
     * Wrapper para {@link PropertyUtilsBean#describe(Object)}.
     *
     * @param bean o objeto que se deseja ler as informações
     * @return um mapa contendo chave o nome da propriedade e, como valor, o valor da propriedade.
     *
     * @throws IllegalAccessException ilegal acesso aos métodos das propriedades
     * @throws InvocationTargetException empacota a exceção lançada pelo método invocado via reflection
     * @throws NoSuchMethodException se algum método Não existir
     */
    public static Map<String, Object> describe(Object bean)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {

        if(bean == null)
        {
            throw new IllegalArgumentException("No bean specified");
        }

        Map<String, Object> description = new HashMap<String, Object>();
        PropertyDescriptor[] descriptors = getPropertyDescriptors(bean);
        for(int i = 0; i < descriptors.length; i++)
        {
            String name = descriptors[i].getName();
            if(descriptors[i].getReadMethod() != null)
            {
                description.put(name, getSimpleProperty(bean, name));
            }
        }

        return description;
    }

    /**
     * Pega o valor de uma propriedade.
     *
     * @param bean O bean que se deseja ler o valor
     * @param name O nome da propriedade
     * @return o valor lido da propriedade
     *
     * @throws IllegalAccessException ilegal acesso aos métodos da propriedade
     * @throws InvocationTargetException empacota a exceção lançada pelo método invocado via reflection
     * @throws NoSuchMethodException se o método Não existe
     */
    public static Object getSimpleProperty(Object bean, String name)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        return (PROPERTY_UTILS.getSimpleProperty(bean, name));
    }
    
    public static Object getProperty(Object bean, String name)
    {
        try {
			return (PROPERTY_UTILS.getSimpleProperty(bean, name));
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
    }
    

    /**
     * Seta um dado valor em uma propriedade.
     *
     * @param bean O bean que se deseja setar o valor
     * @param name O nome da propriedade
     * @param value o valor a ser setado
     *
     * @throws IllegalAccessException ilegal acesso aos métodos da propriedade
     * @throws InvocationTargetException empacota a exceção lançada pelo método invocado via reflection
     * @throws NoSuchMethodException se o método Não existe
     */
    public static void setSimpleProperty(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        PROPERTY_UTILS.setSimpleProperty(bean, name, value);
    }

    /**
     * Pega o tipo declarado da propriedade.
     *
     * @param bean O bean que se deseja verificar
     * @param name O nome da propriedade
     * @return o tipo declarado da propriedade
     *
     * @throws IllegalAccessException ilegal acesso aos métodos da propriedade
     * @throws InvocationTargetException empacota a exceção lançada pelo método invocado via reflection
     * @throws NoSuchMethodException se o método Não existe
     */
    public static Class<?> getPropertyType(Object bean, String name)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        return PROPERTY_UTILS.getPropertyType(bean, name);
    }

    /**
     * Verifica se a dada propriedade possui o seu respectivo método get.
     *
     * @param bean O bean que se deseja verificar
     * @param name O nome da propriedade
     * @return true se existe um método leitor da propriedade, falso caso contrário
     */
    public static boolean isReadable(Object bean, String name)
    {
        // --- Início do código experimental --
        // se alguém tiver usando isXxx().. este cara Não funciona e ai Não vale a pena.
        try
        {
            Method method = findGetter(bean.getClass(), name);
            return method != null;
        }
        catch(IntrospectionException ignore)
        {
            // ignora e usa o default
        }
        // --- Fim do código experimental --

        // este cara foi deixado para evitar problemas, mas o código acima � ligeiramente mais r�pido.
        return PROPERTY_UTILS.isReadable(bean, name);
    }

    /**
     *
     * Acha o Getter de uma propriedade
     *
     * @param clazz Classe
     * @param property Propriedade
     * @return Metodo
     * @throws IntrospectionException Erro
     */
    public static Method findGetter(Class<?> clazz, String property) throws IntrospectionException
    {
        if((clazz == null) || (property == null))
        {
            return null;
        }

        PropertyDescriptor[] props = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        for(int i = 0; i < props.length; i++)
        {
            if(props[i].getName().equalsIgnoreCase(property))
            {
                return props[i].getReadMethod();
            }
        }

        return null;
    }

    /**
     *
     * Acha o Setter de uma propriedade
     *
     * @param clazz Classe
     * @param property Propriedade
     * @return Metodo
     * @throws IntrospectionException Erro
     */
    public static Method findSetter(Class<?> clazz, String property) throws IntrospectionException
    {
        if((clazz == null) || (property == null))
        {
            return null;
        }

        PropertyDescriptor[] props = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        for(int i = 0; i < props.length; i++)
        {
            if(props[i].getName().equalsIgnoreCase(property))
            {
                return props[i].getWriteMethod();
            }
        }

        return null;
    }

    /**
     * Verifica se a dada propriedade possui o seu respectivo método set.
     *
     * @param bean O bean que se deseja verificar
     * @param name O nome da propriedade
     * @return true se existe um método modificador da propriedade, falso caso contrário
     */
    public static boolean isWriteable(Object bean, String name)
    {
        return PROPERTY_UTILS.isWriteable(bean, name);
    }

    /**
     * Copia uma propriedade do objeto origem para o objeto destino.<br>
     *
     * @param origem objeto para ler as propriedades
     * @param destino objeto para setar as propriedades
     * @param name o nome da propriedade
     * @return true se conseguiu setar o valor, false caso contrário
     *
     * @throws IllegalAccessException ilegal acesso aos métodos de alguma propriedade
     * @throws InvocationTargetException empacota a exceção lançada por um método invocado via reflection
     * @throws NoSuchMethodException método Não existe
     */
    public static boolean copyProperty(Object origem, Object destino, String name)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        if(isValidAttribution(origem, destino, name))
        {
            Object value = getSimpleProperty(origem, name);
            setSimpleProperty(destino, name, value);
            return true;
        }
        return false;
    }

    private static boolean isValidAttribution(Object origem, Object destino, String name)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        return isReadable(origem, name)
                && isWriteable(destino, name)
                && getPropertyType(destino, name).isAssignableFrom(getPropertyType(origem, name));
    }

    /**
     * Copia propriedades do objeto origem para o objeto destino.<br>
     *
     * @param origem objeto para ler as propriedades
     * @param destino objeto para setar as propriedades
     *
     * @throws IllegalAccessException ilegal acesso aos métodos de alguma propriedade
     * @throws InvocationTargetException empacota a exceção lançada por um método invocado via reflection
     * @throws NoSuchMethodException método Não existe
     */
    public static void copyProperties(Object origem, Object destino)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        PropertyDescriptor[] descriptors = getPropertyDescriptors(destino);
        for(PropertyDescriptor propertyDescriptor : descriptors)
        {
            String name = propertyDescriptor.getName();
            copyProperty(origem, destino, name);
        }
    }
    
     
}
