
package IntelligentWebSearch;

import websphinx.*;
import java.util.*;
import java.io.*;

public class HtmlParser
{
	private String validProtocols[] = {"http","ftp"};
	private String validExtensions[] = {"htm","html","php","asp","txt"};

	private char invalidChars[] = {' ','!','"','#','$','%','&','\'','(',')','*','+',',','.','/',':',';','<','=','>','?','@','[','\\',']','^','_','`','{','|','}','~','\t'};
	private char validChars[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','á','é','í','ó','ú','ã','à','ê','ô','õ','â'};

	private LinkedList searchWords = null;
	private String sentencesFile = null;

	public HtmlParser(String agentName, String initialURL, int depth, String searchText, int wordsOrientation)
	{
		searchWords = new LinkedList();
		StringTokenizer strTok = new StringTokenizer(searchText," ");

		while(strTok.hasMoreTokens())
			searchWords.add(strTok.nextToken());

		sentencesFile = this.getClass().getResource("").getPath() + "sentences/" + agentName + ".txt";

		try
		{
			BufferedWriter buf = new BufferedWriter(new FileWriter(sentencesFile));
			buf.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}

		if(wordsOrientation == 1)
			fullPageOrientation(initialURL,depth);
		else
			sentencesOrientation(initialURL,depth);
	}

	private void fullPageOrientation(String initialURL, int depth)
	{
		LinkedList localList = new LinkedList();
		LinkedList localListAux = null;

		localList.add(initialURL);

		for(int i = 0; i <= depth; i++)
		{
			localListAux = new LinkedList();

			for(int j = 0; j < localList.size(); j++)
			{
				try
				{
					Page page = new Page(new Link((String) localList.get(j)), new DownloadParameters(), new HTMLParser());
					addSentence(page.getWords());

					if(i != depth)
					{
						Link[] links = page.getLinks();
						addLinks(localListAux,links,(i + 1));
					}
				}
				catch(Exception e)
				{
				}
			}

			localList = new LinkedList(localListAux);
		}
	}

	private void sentencesOrientation(String initialURL, int depth)
	{
		LinkedList localList = new LinkedList();
		LinkedList localListAux = null;

		localList.add(initialURL);

		for(int i = 0; i <= depth; i++)
		{
			localListAux = new LinkedList();

			for(int j = 0; j < localList.size(); j++)
			{
				try
				{
					//System.out.println((String) localList.get(j));
					Page page = new Page(new Link((String) localList.get(j)), new DownloadParameters(), new HTMLParser());
					findSentences(page);

					if(i != depth)
					{
						Link[] links = page.getLinks();
						addLinks(localListAux,links,(i + 1));
						System.out.println("Number links: " + localListAux.size());
					}
				}
				catch(Exception e)
				{
				}
			}

			localList = new LinkedList(localListAux);
		}
	}

	private void findSentences(Page page)
	{
		try
		{
			BufferedWriter buf = new BufferedWriter(new FileWriter(sentencesFile,true));
			LinkedList originalSentences = new LinkedList();

			if(page.hasContent())
			{
				String body = null;

				String content = page.getContent();

				if(content.indexOf("<body") != -1)
				{
					if(content.indexOf("</body>") != -1)
						body = content.substring(content.indexOf("<body"),content.indexOf("</body>"));
					else
						return ;
				}

				if(content.indexOf("<BODY") != -1)
				{
					if(content.indexOf("</BODY>") != -1)
						body = content.substring(content.indexOf("<BODY"),content.indexOf("</BODY>"));
					else
						return ;
				}

				while(body.indexOf("<script") != -1)
				{
					if(body.indexOf("</script>") != -1 && body.indexOf("<script") < body.indexOf("</script>"))
					{
						if(body.substring(0,body.indexOf("<script")) != null)
							body = body.substring(0,body.indexOf("<script")) + "\n" + body.substring(body.indexOf("</script>") + 9);
						else
							return ;
					}
					else
						return ;
				}

				while(body.indexOf("<SCRIPT") != -1)
				{
					if(body.indexOf("</SCRIPT>") != -1 && body.indexOf("<SCRIPT") < body.indexOf("</SCRIPT>"))
					{
						if(body.substring(0,body.indexOf("<SCRIPT")) != null)
							body = body.substring(0,body.indexOf("<SCRIPT")) + "\n" + body.substring(body.indexOf("</SCRIPT>") + 9);
						else
							return ;
					}
					else
						return ;
				}

				body = body.replaceAll("<[^>]*>"," ");
				body = body.replaceAll("&nbsp;"," ");
				body = body.replaceAll("&copy;"," ");
				body = body.replaceAll("&ndash;"," ");

				StringTokenizer bodyTok = new StringTokenizer(body,"\n");
				String line = null;
				String bodyCopy = "";

				while(bodyTok.hasMoreTokens())
				{
					line = bodyTok.nextToken();

					if(line != null && !line.equals("") && countWords(line) > 1)
					{
						if(removeSpaces(line) != null)
							bodyCopy += removeSpaces(line) + "\n";
					}
				}

				String sentences = bodyCopy.replaceAll("\\s{2,}","\n").toLowerCase();

				StringTokenizer sentencesTok = new StringTokenizer(sentences,"\n");

				while(sentencesTok.hasMoreTokens())
				{
					String sen = sentencesTok.nextToken().replaceAll(",*\\.*;*:*!*\\?*\\(*\\)*»*«*\"*'*","");
					sen = sen.replaceAll(" - "," ");

					//System.out.println("SEN: " + sen);

					if(sen != null && sen.length() > 0 && countWords(sen) > 1)
					{
						sen = removeSpaces(sen);

						if(isSearchTextSentence(sen))
							originalSentences.addLast(sen);
					}
				}

				ListIterator sentencesIt = originalSentences.listIterator();
				while(sentencesIt.hasNext())
				{
					String sentence = (String) sentencesIt.next();

					//System.out.println("SEN: " + sentence);

					LinkedList blocos = new LinkedList();
					StringTokenizer sentenceTok = new StringTokenizer(sentence," ");

					String bloco = "";
					while(sentenceTok.hasMoreTokens())
					{
						String word = sentenceTok.nextToken();

						while(!Database.isInvalidWord(word) || bloco.length() == 0 && word.charAt(0) <= 57 && word.charAt(0) >= 48)
						{
							bloco += word + " ";

							if(sentenceTok.hasMoreTokens())
								word = sentenceTok.nextToken();
							else
							{
								if(bloco.length() > 1)
									blocos.add(bloco.substring(0,bloco.length() - 1));
								break;
							}
						}

						if(sentenceTok.hasMoreTokens() && bloco.length() > 1)
							blocos.add(bloco.substring(0,bloco.length() - 1));

						bloco = word + " ";
					}

					for(int i = 0; i < blocos.size(); i++)
					{
						String block = (String) blocos.get(i);

						//System.out.println("BLOCO: " + block);

						if(countWords(block) == 2)
						{
							String word1 = block.substring(0,block.indexOf(" "));
							String word2 = block.substring(block.indexOf(" ") + 1);

							if(Database.isInvalidWord(word1) && !Database.isInvalidWord(word2) && i > 0)
							{
								String previousBlock = (String) blocos.get(i - 1);

								if(countWords(previousBlock) > 1)
								{
									previousBlock = previousBlock.substring(0,previousBlock.lastIndexOf(" "));
									blocos.remove(i - 1);
									blocos.add((i - 1),previousBlock);
								}
							}
						}
					}

					String validSentence = "";
					for(int i = 0; i < blocos.size(); i++)
					{
						String block = (String) blocos.get(i);
						int numWords = countWords(block) + 1;

						if(numWords == 1)
						{
							if(!Database.isInvalidWord(block))
								validSentence += block + " ";
						}
						else if(numWords == 2)
						{
							String word2 = block.substring(block.indexOf(" ") + 1);
							validSentence += word2 + " ";
						}
						else if(numWords == 3)
						{
							String word2 = block.substring(block.indexOf(" ") + 1,block.lastIndexOf(" "));
							String word3 = block.substring(block.lastIndexOf(" ") + 1);

							validSentence += word2 + " " + word3 + " ";
						}
						else if(numWords == 4)
						{
							String wordWithoutArticle = block.substring(block.indexOf(" ") + 1);
							String word2 = wordWithoutArticle.substring(0,wordWithoutArticle.indexOf(" "));
							String word4 = wordWithoutArticle.substring(wordWithoutArticle.lastIndexOf(" ") + 1);

							validSentence += word2 + " " + word4 + " ";
						}
						else if(numWords == 5)
						{
							String wordWithoutArticle = block.substring(block.indexOf(" ") + 1);
							String word2 = wordWithoutArticle.substring(0,wordWithoutArticle.indexOf(" "));
							String word4 = wordWithoutArticle.substring(wordWithoutArticle.lastIndexOf(word2) + 1).substring(wordWithoutArticle.lastIndexOf(" "));

							validSentence += word2 + " " + word4 + " ";
						}
					}

					if(validSentence.length() > 0 && (countWords(validSentence) + 1) > 1)
					{
						validSentence = validSentence.substring(0,validSentence.length() - 1);
						validSentence = validSentence.replaceAll(" ","\n");
						buf.write(validSentence,0,validSentence.length());
						buf.newLine();
						buf.newLine();
					}
				}
			}

			buf.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}

	private boolean isSearchTextSentence(String sentence)
	{
		boolean wordsPresences[] = new boolean[searchWords.size()];

		StringTokenizer sentenceTok = new StringTokenizer(sentence," ");

		while(sentenceTok.hasMoreTokens())
		{
			String word = sentenceTok.nextToken();

			if(searchWords.contains(word))
				wordsPresences[searchWords.indexOf(word)] = true;
		}

		for(int i = 0; i < wordsPresences.length; i++)
		{
			if(!wordsPresences[i])
				return false;
		}

		return true;
	}

	private String removeSpaces(String sentence)
	{
		String newSentence = null;

		int index = 0;
		while(sentence.charAt(index) == ' ' && index < sentence.length())
			index++;

		if(index < (sentence.length() - 1))
			newSentence = sentence.substring(index);

		return newSentence;
	}

	private int countWords(String sentence)
	{
		int numWords = 0;

		StringTokenizer sentenceTok = new StringTokenizer(sentence," ");

		while(sentenceTok.hasMoreTokens())
		{
			String word = sentenceTok.nextToken();
			if(word != null && !word.equals(""))
				numWords++;
		}

		return (numWords - 1);
	}

	private void addSentence(Text words[])
	{
		try
		{
			BufferedWriter buf = new BufferedWriter(new FileWriter(sentencesFile,true));

			if(containsSearchText1(words))
			{
				for(int i = 0; i < words.length; i++)
				{
					String word = removeSpaces1(words[i].toText().toLowerCase());

					if(word != null && !Database.isInvalidWord(word) && !searchWords.contains(word))
					{
						buf.write(word,0,word.length());
						buf.newLine();
					}
				}

				buf.newLine();
			}

			buf.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}

	/**
	 * Adiciona palavras à base de dados
	 * @param searchText texto a pesquisar
	 * @param words array com as palavras que vão ser adicionadas
	 */
	private void addWords(Text words[])
	{
		if(containsSearchText(words))
		{
			for(int i = 0; i < words.length; i++)
			{
				String word = removeSpaces1(words[i].toText().toLowerCase());

				if(word != null && !Database.isInvalidWord(word) && !searchWords.contains(word))
					Database.insertWord(word);
			}
		}
	}

	/**
	 * Adiciona links à base de dados
	 * @param localList lista local de links
	 * @param links array com os possíveis links a adicionar
	 * @param depthLevel nível de profundidade dos links
	 */
	private void addLinks(LinkedList localList, Link links[], int depthLevel)
	{
		for(int i = 0; i < links.length; i++)
		{
			String linkReturn = hasValidExtension(links[i].toURL());

			if(linkReturn != null && hasValidProtocol(linkReturn))
			{
				if(Database.insertLinkList(linkReturn.replaceAll("'","\"")))
				{
					Database.insertLinkDatabase(linkReturn.replaceAll("'","\""),depthLevel);
					localList.add(linkReturn.replaceAll("'","\""));
				}
			}
		}
	}

	/**
	 * Verifica se uma página contém o texto a pesquisar
	 * @param searchText texto a pesquisar
	 * @param words array com todas as palavras da página
	 * @return true se a página contiver o texto a pesquisar, false caso contrário
	 */
	private boolean containsSearchText(Text words[])
	{
		boolean wordsPresences[] = new boolean[searchWords.size()];

		for(int i = 0; i < words.length; i++)
		{
			String word = removeSpaces(words[i].toText().toLowerCase());

			if(searchWords.contains(word))
				wordsPresences[searchWords.indexOf(word)] = true;
		}

		for(int i = 0; i < wordsPresences.length; i++)
		{
			if(!wordsPresences[i])
				return false;
		}

		return true;
	}

	private boolean containsSearchText1(Text words[])
	{
		boolean wordsPresences[] = new boolean[searchWords.size()];

		for(int i = 0; i < words.length; i++)
		{
			String word = removeSpaces1(words[i].toText().toLowerCase());

			if(searchWords.contains(word))
				wordsPresences[searchWords.indexOf(word)] = true;
		}

		for(int i = 0; i < wordsPresences.length; i++)
		{
			if(!wordsPresences[i])
				return false;
		}

		return true;
	}

	/**
	 * Verifica se um link tem um protocolo válido
	 * @param link link
	 * @return true se o link tiver um protocolo válido, false caso contrário
	 */
	private boolean hasValidProtocol(String link)
	{
		String protocol = link.substring(0,link.indexOf("://"));

		for(int i = 0; i < validProtocols.length; i++)
		{
			if(protocol.equals(validProtocols[i]))
				return true;
		}

		return false;
	}

	/**
	 * Verifica se um link tem uma extensão válida
	 * @param link link
	 * @return o link se este tiver uma extensão válida, null caso contrário
	 */
	private String hasValidExtension(String link)
	{
		String afterProtocol = link.substring(link.indexOf("//") + 2);

		if(afterProtocol.indexOf("/") == -1)
			return link + "/";
		else if(afterProtocol.lastIndexOf("/") == (afterProtocol.length() - 1))
			return link;
		else
		{
			String extension1 = link.substring(link.length() - 3);
			String extension2 = link.substring(link.length() - 4);

			if(isValidExtension(extension1) || isValidExtension(extension2))
				return link;

			return null;
		}
	}

	/**
	 * Verifica se uma extensão é válida
	 * @param extensions extensão
	 * @return true se a extensão for válida, false caso contrário
	 */
	private boolean isValidExtension(String extension)
	{
		for(int i = 0; i < validExtensions.length; i++)
			if(validExtensions[i].equals(extension))
				return true;

		return false;
	}

	/**
	 * Remove espaços e caracteres inválidos duma palavra
	 * @return palavra sem espaços e caracteres inválidos
	 */
	private String removeSpaces1(String originalWord)
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

			if(ind == 1 || containsInvalidChar(finalWord))
				return null;
			else
				return finalWord;
		}
	}

	/**
	 * Verifica se uma palavra tem caracteres inválidos
	 * @param word palavra
	 * @return true se a palavra tiver caracteres inválidos, false caso contrário
	 */
	private boolean containsInvalidChar(String word)
	{
		for(int i = 0; i < word.length(); i++)
		{
			if(isInvalidChar(word.charAt(i)))
				return true;
		}

		return false;
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
}
