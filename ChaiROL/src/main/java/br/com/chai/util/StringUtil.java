package br.com.chai.util;

public class StringUtil {

    public static boolean isBlank(final String str)
    {
        if(str == null)
        {
            return true;
        }

        int length = str.length();
        for(int i = 0; i < length; i++)
        {
            if(!Character.isWhitespace(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

}
