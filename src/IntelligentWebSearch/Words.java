
/**
 * Words.java
 *
 * Criada em 04-12-2004
 */

package IntelligentWebSearch;

import java.io.*;
import java.util.*;

/**
 * Classe que faz a gestão das palavras e suas ocorrências
 * @author André Barbosa e Filipe Montenegro
 */
public class Words
{
	private int ocurs[][];
	private double delta = 0.0;
	private int numSentences = 0;
	private int numAnalysedWords = 0;
	private int numWords = 0;
	private int analysedWords[];

	private LinkedList listAll = new LinkedList();
	private LinkedList sentences = new LinkedList();

	public Words(int depthLevel, int wordsOrientation, int numWords, double delta)
	{
		int numWordsDatabase = Database.getNumWords();

		if(numWordsDatabase < numWords)
			this.numWords = numWordsDatabase;
		else
			this.numWords = numWords;

		this.delta = delta;

		numSentences = Database.getNumSentences();

		for(int i = 0; i < numSentences; i++)
		{
			if(wordsOrientation == 1)
				sentences.add(Database.getSentence1(i + 1));
			else
				sentences.add(Database.getSentence(i + 1));
		}

		System.out.println("Numero frases: " + numSentences);

		System.out.println("A iniciar ocorrencias...");
		initOcurs(wordsOrientation);
		System.out.println("Ocorrencias terminadas");

		System.out.println("A iniciar clusters...");

		analysedWords = new int[this.numWords];

		for(int i = 0; i < this.numWords; i++)
		{
			if(!analysedWord(i))
			{
				LinkedList list = new LinkedList();
				list.addLast(Database.getWord(i + 1));
				analysedWords[numAnalysedWords++] = i;

				for(int j = 0; j < this.numWords; j++)
				{
					if(i != j)
					{
						if(!analysedWord(j))
						{
							//System.out.println(Database.getWord(i + 1) + "-" + Database.getWord(j + 1) + " : " + Database.getWordOcurrances(i + 1) + " : " + Database.getWordOcurrances(j + 1) + " : " + i + " : " + j + " : " + ocurs[i][j]);
							double p = ((double) ocurs[i][j] / (double) (Database.getWordOcurrances(i + 1) + Database.getWordOcurrances(j + 1) - ocurs[i][j]));
							//System.out.println(Database.getWord(i + 1) + "-" + Database.getWord(j + 1) + " : " + Database.getWordOcurrances(i + 1) + " : " + Database.getWordOcurrances(j + 1) + " : " + i + " : " + j + " : " + p + " : " + ocurs[i][j]);

							if(p <= delta && p > 0.0)
							{
								list.addLast(Database.getWord(j + 1));
								analysedWords[numAnalysedWords++] = j;
							}
						}
					}
				}

				listAll.addLast(list);
			}
		}

		System.out.println("Clusters terminados");

		//printClusters();
	}

	public LinkedList getWordsClusters()
	{
		return listAll;
	}

	private void printClusters()
	{
		ListIterator listAllIt = listAll.listIterator();

		while(listAllIt.hasNext())
		{
			ListIterator listIt = ((LinkedList) listAllIt.next()).listIterator();

			System.out.println("CLUSTER:");

			while(listIt.hasNext())
				System.out.println("\t" + (String) listIt.next());
		}
	}

	private void initOcurs(int wordsOrientation)
	{
		ocurs = new int[numWords][numWords];

		LinkedList words = new LinkedList();

		for(int i = 0; i < numWords; i++)
			words.addLast(Database.getWord(i + 1));

		for(int i = 0; i < ocurs[0].length; i++)
		{
			String word1 = (String) words.get(i);

			for(int j = 0; j < ocurs[0].length; j++)
			{
				String word2 = (String) words.get(j);

				for(int k = 0; k < numSentences; k++)
				{
					if(!word1.equals(word2))
					{
						String sentence = (String) sentences.get(k);

						//if(i == 0 && j == 0)
							//System.out.println(sentence + " : " + word1 + " : " + word2);

						if(sentence.indexOf(word1) != -1 && sentence.indexOf(word2) != -1)
						{
							//System.out.println(sentence + ": " + word1 + " : " + word2);
							if(wordsOrientation == 1)
								ocurs[i][j] = countTokens(k,word2);
							else
								ocurs[i][j]++;
						}
					}
				}
			}
		}
	}

	private int countTokens(int sentenceIndex, String word)
	{
		int count = 0;

		StringTokenizer strTok = new StringTokenizer((String) sentences.get(sentenceIndex),"\n");
		while(strTok.hasMoreTokens())
		{
			if(word.equals(strTok.nextToken()))
				count++;
		}

		return count;
	}

	/**
	 * Retorna o número de ocorrências de uma palavra
	 * @param i indíce da palavra
	 * @return número de ocorrências de uma palavra
	 */
	public int nocurs(int i)
	{
		int n = 0;

		for(int j = 0; j < ocurs[i].length; j++)
			n += ocurs[i][j];

		if(n == 0)
			n = 1;

		return n;
	}

	/**
	 * Verifica se uma palavra já foi analisada
	 * @param index indíce da palavra
	 * @return true se a palavra já tiver sido analisada, false caso contrário
	 */
	private boolean analysedWord(int index)
	{
		for(int i = 0; i < numAnalysedWords; i++)
		{
			if(analysedWords[i] == index)
				return true;
		}

		return false;
	}
}
