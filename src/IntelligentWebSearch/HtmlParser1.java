
/**
 * HtmlParser.java
 *
 * Criada em 03-12-2004
 */

package IntelligentWebSearch;

import java.io.*;
import java.net.*;
import java.util.*;
import websphinx.*;

/**
 * Classe que representa um parser para retirar texto de páginas web
 * @author André Barbosa e Filipe Montenegro
 */
public class HtmlParser1
{
	private String wordsPath = null;
	private String sentencesPath = null;
	private String path = this.getClass().getResource("").getPath();
	private char invalidChars[] = {' ','!','"','#','$','%','&','\'','(',')','*','+',',','.','/',':',';','<','=','>','?','@','[','\\',']','^','_','`','{','|','}','~','\t'};
	private char validChars[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','á','é','í','ó','ú','ã','à','ê','ô','õ','â'};

	/**
	 * Contrói um novo parser
	 * @param link link sobre o qual vai ser feito o parser
	 */
	public HtmlParser1(String link)
	{
		String originalContent = null;
		String body = null;
		String text = null;

		wordsPath = path + "htmlpages/" + link.replaceAll(":","_").replaceAll("/","_") + ".words.txt";
		sentencesPath = path + "htmlpages/" + link.replaceAll(":","_").replaceAll("/","_") + ".sentences.txt";

		try
		{
			Page page = new Page(new Link(link), new DownloadParameters(), new HTMLParser());
			initWords(page);
			initSentences(page);
		}
		catch(Exception e)
		{
		}
	}

	/**
	 * Inicia as palavras contidas na página
	 * @param page página
	 */
	private void initWords(Page page)
	{
		try
		{
			String word = null;
			BufferedWriter wordsBuf = new BufferedWriter(new FileWriter(wordsPath));

			Text words[] = page.getWords();
			for(int i = 0; i < words.length; i++)
			{
				word = removeSpaces(words[i].toText().toLowerCase());

				if(word != null && !isInvalidWord(word))
				{
					wordsBuf.write(word,0,word.length());
					wordsBuf.newLine();
				}
			}

			wordsBuf.close();
		}
		catch(Exception e)
		{
		}
	}

	/**
	 * Inicia as frases contidas na página
	 * @param page página
	 */
	private void initSentences(Page page)
	{
	}

	/**
	 * Retorna as palavras contidas na página
	 * @return palavras contidas na página
	 */
	public String getWords()
	{
		String words = "";

		try
		{
			String word = null;
			BufferedReader wordsBuf = new BufferedReader(new FileReader(wordsPath));

			word = wordsBuf.readLine();
			while(word != null)
			{
				words += word + " ";
				word = wordsBuf.readLine();
			}
			words = words.substring(0,words.length() - 1);

			wordsBuf.close();
		}
		catch(Exception e)
		{
			return null;
		}

		return words;
	}

	/**
	 * Retorna as frases contidas na página
	 * @return frases contidas na página
	 */
	public LinkedList getSentences()
	{
		return null;
	}
	
	/**
	 * Remove espaços e caracteres inválidos duma palavra
	 * @return palavra sem espaços e caracteres inválidos
	 */
	private String removeSpaces(String originalWord)
	{
		String finalWord = new String(originalWord);

		int ind = 0;
		while(isInvalidChar(finalWord.charAt(ind)) && ind < (finalWord.length() - 1))
			ind++;
		finalWord = finalWord.substring(ind);

		if(ind == (finalWord.length() - 1))
			return null;
		else
		{
			ind = finalWord.length() - 1;
			while(isInvalidChar(finalWord.charAt(ind)) && ind > 1)
				ind--;
			finalWord = finalWord.substring(0,ind + 1);

			if(ind == 1)
				return null;
			else
				return finalWord;
		}
	}

	/**
	 * Verifica se uma um caracter é inválido
	 * @param c caracter
	 * @return true se o caracter for inválido, false caso contrário
	 */
	private boolean isInvalidChar(char c)
	{
		for(int i = 0; i < validChars.length; i++)
		{
			if(validChars[i] == c)
				return false;
		}

		return true;
	}

	/**
	 * Verifica se uma palavra é inválida
	 * @param word palavra
	 * @return true se a palavra for inválida, false caso contrário
	 */
	private boolean isInvalidWord(String word)
	{
		for(int i = 0; i < word.length(); i++)
		{
			if(isInvalidChar(word.charAt(i)))
				return true;
		}

		return false;
	}
}
