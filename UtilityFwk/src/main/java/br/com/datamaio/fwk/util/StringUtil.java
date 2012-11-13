package br.com.datamaio.fwk.util;

import java.text.Normalizer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.text.MaskFormatter;

public final class StringUtil
{

    private static final String EMPTY_STRING = "";

    private StringUtil()
    {
    }

    public static boolean isEmpty(String str)
    {
        return (str == null) || (str.trim().length() == 0);
    }

    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    public static boolean isEquals(String str1, String str2)
    {
        if(str1 == str2)
        {
            return true;
        }
        return (str1!=null) && str1.equals(str2);
    }

    public static boolean isValueChanged(String newStr, String oldStr)
    {
        return !(((newStr == null) && (oldStr == null)) || isEquals(newStr, oldStr));
    }

    public static boolean isAlpha(String str)
    {
        if((str == null) || (str.length() == 0))
        {
            return false;
        }

        for(int i = 0; i < str.length(); i++)
        {
            if(!Character.isLetter(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String str)
    {
        if((str == null) || (str.length() == 0))
        {
            return false;
        }

        for(int i = 0; i < str.length(); i++)
        {
            if(!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaNumeric(String str)
    {
        if((str == null) || (str.length() == 0))
        {
            return false;
        }

        for(int i = 0; i < str.length(); i++)
        {
            if(!Character.isLetterOrDigit(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaNumeric(String str, boolean allowSpaces)
    {
        if((str == null) || (str.length() == 0))
        {
            return false;
        }

        for(int i = 0; i < str.length(); i++)
        {
            if(!Character.isLetterOrDigit(str.charAt(i)) && !(allowSpaces && Character.isSpaceChar(str.charAt(i))))
            {
                return false;
            }
        }
        return true;
    }

    public static String leftPad(String str, char c, int resultLength)
    {
        if(str == null)
        {
            return null;
        }

        int padLength = resultLength - str.length();
        return padLength > 0 ? StringUtil.repeat(c, padLength) + str : str;
    }

    public static String rightPad(String str, char c, int resultLength)
    {
        return rightPad(str, c, resultLength, false);
    }

    public static String rightPad(String str, char c, int resultLength, boolean truncateIfLarger)
    {
        if(str == null)
        {
            return null;
        }

        if(truncateIfLarger && (str.length() > resultLength))
        {
            return str.substring(0, resultLength + 1);
        }

        int padLength = resultLength - str.length();
        return padLength > 0 ? str + StringUtil.repeat(c, padLength) : str;
    }

    public static String leftStrip(String str, char c)
    {
        if(str == null)
        {
            return null;
        }

        for(int i = 0; i < str.length(); i++)
        {
            if(str.charAt(i) != c)
            {
                return str.substring(i);
            }
        }
        return EMPTY_STRING;
    }

    public static String rightStrip(String str, char c)
    {
        if(str == null)
        {
            return null;
        }

        for(int i = str.length() - 1; i >= 0; i--)
        {
            if(str.charAt(i) != c)
            {
                return str.substring(0, i + 1);
            }
        }
        return EMPTY_STRING;
    }

    public static String strip(String str, char c)
    {
        if(str == null)
        {
            return null;
        }

        StringBuilder buffer = new StringBuilder(str.length());
        for(int i = 0; i < str.length(); i++)
        {
            char currentChar = str.charAt(i);
            if(currentChar != c)
            {
                buffer.append(currentChar);
            }
        }
        return buffer.toString();
    }

    public static String stripNonNumeric(String str)
    {
        if(str == null)
        {
            return null;
        }
        if(str.length() == 0)
        {
            return EMPTY_STRING;
        }

        StringBuilder buffer = new StringBuilder(str.length());
        for(int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            if(Character.isDigit(ch))
            {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    public static String stripNonAlpha(String str)
    {
        if(str == null)
        {
            return null;
        }
        if(str.length() == 0)
        {
            return EMPTY_STRING;
        }

        StringBuilder buffer = new StringBuilder(str.length());
        for(int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            if(Character.isLetter(ch))
            {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    public static String stripNonAlphaNumeric(String str)
    {
        if(str == null)
        {
            return null;
        }
        if(str.length() == 0)
        {
            return EMPTY_STRING;
        }

        StringBuilder buffer = new StringBuilder(str.length());
        for(int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            // Est� sendo adicionado mais dois testes para retirar
            // os caracter '�'(186) e '�'(170) que s�o considerados letras
            // pelo m�todo "Character.isLetter".
            if((ch != 186) && (ch != 170) && (Character.isLetter(ch) || Character.isDigit(ch)))
            {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    public static String stripAccents(String str)
    {
        if(str == null)
        {
            return null;
        }

        char[] buffer = new char[str.length()];
        for(int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            switch(ch)
            {
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                    buffer[i] = 'a';
                    break;
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                    buffer[i] = 'A';
                    break;
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                    buffer[i] = 'e';
                    break;
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                    buffer[i] = 'E';
                    break;
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                    buffer[i] = 'i';
                    break;
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                    buffer[i] = 'I';
                    break;
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                    buffer[i] = 'o';
                    break;
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                    buffer[i] = 'O';
                    break;
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                    buffer[i] = 'u';
                    break;
                case '�' :
                case '�' :
                case '�' :
                case '�' :
                    buffer[i] = 'U';
                    break;
                case '�' :
                case '�' :
                    buffer[i] = 'y';
                    break;
                case '�' :
                    buffer[i] = 'Y';
                    break;
                case '�' :
                    buffer[i] = 'c';
                    break;
                case '�' :
                    buffer[i] = 'C';
                    break;
                case '�' :
                    buffer[i] = 'n';
                    break;
                case '�' :
                    buffer[i] = 'N';
                    break;
                default :
                    buffer[i] = ch;
                    break;
            }
        }
        return new String(buffer);
    }

    public static List<String> split(String string)
    {
        return split(string, ",");
    }

    public static List<String> split(String string, String regex)
    {
        if(StringUtil.isEmpty(string))
        {
            return null;
        }

        return Arrays.asList(string.split(regex));
    }

    public static String repeat(char c, int length)
    {
        if(length <= 0)
        {
            return EMPTY_STRING;
        }

        char[] buffer = new char[length];
        for(int i = 0; i < length; i++)
        {
            buffer[i] = c;
        }
        return new String(buffer);
    }

    public static String reverse(String str)
    {
        if(str == null)
        {
            return null;
        }
        if(str.length() == 0)
        {
            return EMPTY_STRING;
        }

        int length = str.length();
        char[] buffer = new char[length];
        for(int i = length - 1, j = 0; i >= 0; i--, j++)
        {
            buffer[j] = str.charAt(i);
        }
        return new String(buffer);
    }

    public static String trimToNull(String str)
    {
        if(str == null)
        {
            return null;
        }

        String trimmed = str.trim();
        return trimmed.length() > 0 ? trimmed : null;
    }

    public static String trimToBlank(String str)
    {
        String trimmed = trimToNull(str);
        return trimmed != null ? trimmed : "";
    }

    public static Boolean isAlphaNumericNoSpecials(String str)
    {
        String noAccents = stripAccents(str);
        if(!noAccents.equalsIgnoreCase(str))
        {
            return false;
        }
        return isAlphaNumeric(str);
    }

    public static String toTitleCase(String str)
    {
        StringTokenizer st = new StringTokenizer(str, " ");
        StringBuffer tc = new StringBuffer();
        String curWord;
        while(st.hasMoreTokens())
        {
            curWord = st.nextToken().toLowerCase();
            tc.append(String.valueOf(Character.toTitleCase(curWord.charAt(0))).concat(curWord.substring(1)));
            tc.append(" ");
        }
        tc.setLength(tc.length() - 1);
        return tc.toString();
    }

    public static Integer toInteger(String str)
    {
        return isEmpty(str) ? null : new Integer(str.trim());
    }

    public static Long toLong(String str)
    {
        return isEmpty(str) ? null : new Long(str.trim());
    }

    public static Short toShort(String str)
    {
        return isEmpty(str) ? null : new Short(str.trim());
    }

    public static Byte toByte(String str)
    {
        return isEmpty(str) ? null : new Byte(str.trim());
    }

    public static String toString(Number valor)
    {
        return valor == null ? null : valor.toString();
    }

    public static String onlyNumbers(String str)
    {
        StringBuilder result = new StringBuilder();
        if((str == null) || (str.trim().length() <= 0))
        {
            return EMPTY_STRING;
        }

        for(int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            result.append(Character.isDigit(c) ? c : "");
        }
        return result.toString();
    }

    public static String[] onlyNumbers(String[] strs)
    {
        for(int i = 0; i < strs.length; i++)
        {
            strs[i] = onlyNumbers(strs[i]);
        }
        return strs;
    }

    public static String format(String text, String mask) {
		MaskFormatter mf;
		try {
			mf = new MaskFormatter(mask);
			mf.setValueContainsLiteralCharacters(false);
			return mf.valueToString(text);
		} catch (ParseException e) {
			return "";
		}
	}
    
    public static String centralize(String str, Integer tam) {
		Integer spaceLeft = (tam + str.trim().length()) / 2;
		return rightPad(leftPad(str.trim(), ' ', spaceLeft), ' ', tam); 
	}

    public String removeSpecial(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);  
		str = str.replaceAll("[^\\p{ASCII}]", "");  
		return str;
    }
    
}
