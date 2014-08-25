
package IntelligentWebSearch;

import java.io.*;
import java.util.*;

public class WordCluster
{
    private LinkedList sentences, words, invalidWords;
    private int ocurrances[][];

    /*public WordCluster(LinkedList sentences)
    {
        this.words = new LinkedList();
        this.sentences = new LinkedList();
        this.invalidWords = new LinkedList(Utils.invalidWords);

        ListIterator sentencesIt = sentences.listIterator();
        while(sentencesIt.hasNext())
            this.sentences.addLast(removeInvalidWords((String) sentencesIt.next()));

        buildWords();
        buildOcurrances();
    }*/
    
    public WordCluster(File textFile)
    {
        words = new LinkedList();

        try
        {
            BufferedReader textFileBuf = new BufferedReader(new FileReader(textFile));
            buildWords(textFileBuf);
            textFileBuf.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    private void buildWords(BufferedReader textFile)
    {
        try
        {
            BufferedWriter wordsFile = new BufferedWriter(new FileWriter(this.getClass().getResource("").getPath() + "Words.txt"));
            String textString = textFile.readLine();

            while(textString != null)
            {
                StringTokenizer textStringTok = new StringTokenizer(textString," ,;.:(){}!?«»<>+*'~^¨´`[]’");

                while(textStringTok.hasMoreTokens())
                {
                    String word = (String) textStringTok.nextToken().toLowerCase();

                    if((word.length() > 1) && (!words.contains(word)) && (!Utils.invalidWords.contains(word)))
                    {
                        words.addLast(word);
                        wordsFile.write(word + "\n");
                    }
                }
                
                textString = textFile.readLine();
            }

            wordsFile.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    private void buildOcurrances()
    {
        try
        {
            BufferedWriter ocurrancesFile = new BufferedWriter(new FileWriter(this.getClass().getResource("").getPath() + "Ocurrances.txt"));
            
            for(int i = 0; i < words.size(); i++)
            {
                for(int j = 0; j < words.size(); j++)
                {
                    
                }
            }
            ocurrancesFile.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    /*private void buildWords()
    {
        Utils.writeLog("building words list...");

        String sentence = new String();
        String word = new String();
        ListIterator sentencesIt = sentences.listIterator();

        while(sentencesIt.hasNext())
        {
            sentence = (String) sentencesIt.next();
            StringTokenizer strTok = new StringTokenizer(sentence," .,");

            while(strTok.hasMoreTokens())
            {
                word = strTok.nextToken().toLowerCase();
                if(!words.contains(word) && !invalidWords.contains(word))
                    words.addLast(word);
            }
        }

        Utils.writeLog("words list build");
    }

    private void buildOcurrances()
    {
        Utils.writeLog("building words ocurrances...");

        String sentence = new String();
        ocurrances = new int[words.size()][words.size()];

        for(int i = 0; i < words.size(); i++)
        {
            for(int j = 0; j < words.size(); j++)
            {
                for(int k = 0; k < sentences.size(); k++)
                {
                    String word1 = (String) words.get(i);
                    String word2 = (String) words.get(j);
                    sentence = (String) sentences.get(k);

                    if(isToken(sentence,word1))
                    {
                        if(isToken(removeFirstOcurrance(sentence,word1),word2))
                            ocurrances[i][j]++;
                    }
                }
            }
        }

        Utils.writeLog("words ocurrances build");
    }*/

    private boolean isToken(String sentence, String word)
    {
        StringTokenizer strTok = new StringTokenizer(sentence," ");

        while(strTok.hasMoreTokens())
        {
            if(strTok.nextToken().equalsIgnoreCase(word))
                return true;
        }

        return false;
    }

    private String removeInvalidWords(String sentence)
    {
        ListIterator invalidWordsIt = invalidWords.listIterator();
        String newSentence = new String(sentence);

        while(invalidWordsIt.hasNext())
        {
            String invalidWord = " " + (String) invalidWordsIt.next() + " ";
            int lastIndex = newSentence.lastIndexOf(invalidWord);

            while(lastIndex != -1)
            {
                if(lastIndex > 1)
                    newSentence = newSentence.substring(0,lastIndex) + newSentence.substring(lastIndex + invalidWord.length() - 1);
                else
                    newSentence = newSentence.substring(lastIndex + invalidWord.length());

                lastIndex = newSentence.lastIndexOf(invalidWord);
            }
        }

        return newSentence;
    }

    private String removeFirstOcurrance(String sentence, String word)
    {
        String newSentence = new String();
        StringTokenizer strTok = new StringTokenizer(sentence," ");
        boolean test = true;

        while(test)
        {
            String str = strTok.nextToken();

            if(str.equalsIgnoreCase(word))
                test = false;
            else
                newSentence = newSentence + str + " ";
        }

        while(strTok.hasMoreTokens())
            newSentence = newSentence + strTok.nextToken() + " ";
        newSentence = newSentence.substring(0,(newSentence.length() - 1));

        return newSentence;
    }

    public int[][] getOcurrances()
    {
        return this.ocurrances;
    }

    public LinkedList getWords()
    {
        return this.words;
    }
}
