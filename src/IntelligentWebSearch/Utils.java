
package IntelligentWebSearch; 

import java.io.*;
import java.util.*;
import java.text.*;

public abstract class Utils
{
    private static BufferedWriter log;
    public static LinkedList invalidWords = new LinkedList();

	public static boolean isToken(String sentence, String searchWord)
	{
		StringTokenizer sentenceToken = new StringTokenizer(sentence," .;:");

		while(sentenceToken.hasMoreTokens())
		{
			if(sentenceToken.nextToken().equals(searchWord))
				return true;
		}
		
		return false;
	}

    public static void openLog(String fileName)
    {
        try
        {
            log = new BufferedWriter(new FileWriter(fileName,true));
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    public BufferedWriter getLogFile()
    {
        return log;
    }
    
    public static void writeLog(String str)
    {
        try
        {
            System.out.println(str);
            log.write(getStringWithTime(str));
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    public static void closeLog()
    {
        try
        {
            log.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public static String getStringWithTime(String str)
    {
         DateFormat dateFormat = DateFormat.getTimeInstance();
         return "(" + dateFormat.format(new Date()) + ") " + str;
    }
    
    public static void buildInvalidWords(String fileName)
    {
        try
        {
            //getLogFile().write("building invalid words list...");

            String invalidWord = new String();
            BufferedReader invalidWordsFile = new BufferedReader(new FileReader(fileName));

            invalidWord = invalidWordsFile.readLine();
            while(invalidWord != null)
            {
                invalidWords.addLast(invalidWord);
                invalidWord = invalidWordsFile.readLine();
            }

            invalidWordsFile.close();
            
            //getLogFile().write("invalid words list build");
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

	public static boolean checkFile(BufferedReader bufReader)
	{
		String line = null;

		try
		{
			// Check File Header
			line = bufReader.readLine();
			if(!line.equals("IntelligentWebSearch"))
				return false;
			line = bufReader.readLine();
			if(!line.equals("AndréBarbosa&FilipeMontenegro"))
				return false;
			line = bufReader.readLine();
			if(!line.equals("2004/2005_Copyright"))
				return false;

			do { line = bufReader.readLine(); } while(line.equals(""));

			// Check For INTELLIGENT_WEB_SEARCH
			if(!line.equals("START_INTELLIGENT_WEB_SEARCH"))
				return false;

			do { line = bufReader.readLine(); } while(line.equals(""));

			if(!line.equals("START_SEARCH_TEXT"))
				return false;

			line = bufReader.readLine();
			line = bufReader.readLine();
			if(!line.equals("END_SEARCH_TEXT"))
				return false;

			do { line = bufReader.readLine(); } while(line.equals(""));

			if(!line.equals("START_GOOGLE_RESULTS"))
				return false;

			line = bufReader.readLine();
			while(!line.equals("END_GOOGLE_RESULTS"))
				line = bufReader.readLine();

			do { line = bufReader.readLine(); } while(line.equals(""));

			if(!line.equals("START_AGENTS"))
				return false;

			line = bufReader.readLine();
			while(!line.equals("END_AGENTS"))
				line = bufReader.readLine();

			do { line = bufReader.readLine(); } while(line.equals(""));

			if(!line.equals("START_WORDS"))
				return false;

			line = bufReader.readLine();
			while(!line.equals("END_WORDS"))
				line = bufReader.readLine();

			do { line = bufReader.readLine(); } while(line.equals(""));

			if(line.equals("START_FINAL_RESULTS"))
			{
				line = bufReader.readLine();
				while(!line.equals("END_FINAL_RESULTS"))
					line = bufReader.readLine();

				do { line = bufReader.readLine(); } while(line.equals(""));

				if(!line.equals("END_INTELLIGENT_WEB_SEARCH"))
					return false;
			}
			else if(!line.equals("END_INTELLIGENT_WEB_SEARCH"))
				return false;

			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
