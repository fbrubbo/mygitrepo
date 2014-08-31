import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Test;


public class GroovyTemplates {

	@Test
	public void test() throws CompilationFailedException, ClassNotFoundException, IOException{
		File fle = new File("groovy.tmpl");
	    Map binding = new HashMap();
		binding.put("favlang", "Groovy");
		binding.put("favlang2", "Groovy2");
	    SimpleTemplateEngine engine = new SimpleTemplateEngine();
	    Writable template = engine.createTemplate(fle).make(binding);
	    System.out.println(template.toString());
	}
}
