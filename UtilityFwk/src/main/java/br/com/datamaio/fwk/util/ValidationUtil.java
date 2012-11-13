package br.com.datamaio.fwk.util;

public class ValidationUtil {
	
	public static final int CNPJ_UNFORMATTED_LENGTH = 14;
	public static final int CPF_UNFORMATTED_LENGTH = 11;
	
    public static Boolean isValidCNPJ(String cnpj)
    {
        String cnpjToTest = cnpj;
        int length = cnpjToTest.length();
        boolean valid = false;

        if(length != CNPJ_UNFORMATTED_LENGTH)
        {
            return false;
        }

        for(int i = 1; !valid && (i < cnpjToTest.length()); i++)
        {
            if(cnpjToTest.charAt(i) != cnpjToTest.charAt(0))
            {
                valid = true;
            }
        }
        if(!valid)
        {
            return false;
        }

        for(int i = 0; i < length; i++)
        {
            if(!Character.isDigit(cnpjToTest.charAt(i)))
            {
                return false;
            }
        }

        int dv1 = 0;
        for(int i = length - 3, j = 2; i >= 0; i--, j = (j != 9 ? j + 1 : 2))
        {
            dv1 += (cnpjToTest.charAt(i) - '0') * j;
        }

        dv1 = 11 - (dv1 % 11);
        dv1 = dv1 > 9 ? '0' : dv1 + '0';
        if(cnpjToTest.charAt(length - 2) != dv1)
        {
            return false;
        }

        int dv2 = 0;
        for(int i = length - 2, j = 2; i >= 0; i--, j = (j != 9 ? j + 1 : 2))
        {
            dv2 += (cnpjToTest.charAt(i) - '0') * j;
        }

        dv2 = 11 - (dv2 % 11);
        dv2 = dv2 > 9 ? '0' : dv2 + '0';

        return cnpjToTest.charAt(length - 1) == dv2;
    }
    
    public static Boolean isValidCPF(String cpf)
    {
    	String cpfToTest = cpf;
        int length = cpfToTest.length();

        if(length != CPF_UNFORMATTED_LENGTH)
        {
            return false;
        }
        
        int repeatedNumber = 0;
        for(int x = 1 ; x < cpfToTest.length(); x++){
        	if(cpfToTest.charAt(x) == cpfToTest.charAt(x - 1))
        		++repeatedNumber;
        }
        
        if(repeatedNumber == 10){
        	return false;
        }
        
        int verifierCalculation1Counter = 10;
        int verifierCalculation2Counter = 11;
        int verifierCalculation1Acum = 0;
        int verifierCalculation2Acum = 0;
        int verifier1 = 0;
        int verifier2 = 0;
        
        for(int x = 0; verifierCalculation1Counter > 1; x++, verifierCalculation1Counter--){
        	int y = new Integer(new Character(cpfToTest.charAt(x)).toString());
        	y = y * verifierCalculation1Counter;
        	verifierCalculation1Acum = verifierCalculation1Acum + y;
        }
        
        if(verifierCalculation1Acum % 11 >= 2){
        	verifier1 = 11 - (verifierCalculation1Acum % 11); 
        }
        
        for(int x = 0; verifierCalculation2Counter > 1; x++, verifierCalculation2Counter--){
        	int y = new Integer(new Character(cpfToTest.charAt(x)).toString());
        	y = y * verifierCalculation2Counter;
        	verifierCalculation2Acum = verifierCalculation2Acum + y;
        }
        
        if(verifierCalculation2Acum % 11 >= 2){
        	verifier2 = 11 - (verifierCalculation2Acum % 11); 
        }
        
        int value1 = new Integer(new Character(cpfToTest.charAt(9)).toString()).intValue();
        int value2 = new Integer(new Character(cpfToTest.charAt(10)).toString()).intValue();
        if( value1 != verifier1 ||  value2 != verifier2)
        	return false;
        
        return true;
    }

    public static Boolean isValidEmail(String email) {
    	return (email.indexOf("@") != -1 && email.indexOf(".") != -1);
    }
    
    public static Boolean isValidEmailLoja(String email) {
    	return (email.indexOf("@") != -1 && email.indexOf(".") != -1) || (email.toUpperCase().replaceAll("[Ã]","A").indexOf("NAO ") != -1);
    }
    
}
