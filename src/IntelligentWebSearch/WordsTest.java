
package IntelligentWebSearch;

import java.io.*;
import java.awt.*;
import java.util.*;

public class WordsTest
{
	private LinkedList sentences = new LinkedList();
	private ArrayList invalidWords = new ArrayList();
	private ArrayList words = new ArrayList();
	private LinkedList listAll = new LinkedList();
	private int ocurs[][];
	private float p[][];
	private int points[]; private int numPoints = 0;
	private double delta = 0.005;

	private String strResults = null;
	private BufferedWriter bufResults = null;

	public WordsTest(String searchWord)
	{
		System.out.println();

		init();
		initSentences(searchWord);
		initInvalidWords();
		initWords(searchWord);
		initOcurs();
		initP();

		points = new int[words.size()];

		Point startPoint = getBest();
		String startWord1 = (String) words.get(startPoint.x);
		String startWord2 = (String) words.get(startPoint.y);

		LinkedList listStart = new LinkedList();
		listStart.add(startWord1);
		listStart.add(startWord2);

		System.out.println("Cluster com palavras iniciais: " + startWord1 + "," + startWord2 + " - " + p[startPoint.x][startPoint.y]);

		for(int i = 0; i < words.size(); i++)
		{
			String word = (String) words.get(i);

			if(!listStart.contains(word))
			{
				if((Math.abs(p[startPoint.x][startPoint.y] - p[startPoint.x][i]) <= delta) || (Math.abs(p[startPoint.x][startPoint.y] - p[startPoint.y][i]) <= delta))
				{
					listStart.add(word);
					points[numPoints++] = i;
					System.out.println("Palavra " + word + " adicionada ao Cluster");
				}
			}
		}

		listAll.add(listStart);
		System.out.println("Fim do Cluster");

		while(listAllSize() != words.size())
		{
			System.out.println(listAllSize() + " : " + words.size());
			Point point = getBest();
			String word1 = (String) words.get(point.x);
			String word2 = (String) words.get(point.y);

			LinkedList list = new LinkedList();

			if(point.x == point.y)
			{
				list.add(word1);
				listAll.add(list);
				break;
			}

			list.add(word1);
			list.add(word2);

			System.out.println("Cluster com as palavras iniciais: " + word1 + "," + word2 + " - " + p[point.x][point.y]);

			for(int i = 0; i < words.size(); i++)
			{
				String word = (String) words.get(i);
				
				if(word1.equals(word) || word2.equals(word) || listAllContains(word))
				{
					System.out.println("Palavra " + word + " ja tem Cluster");
					continue;
				}

				if((Math.abs(p[point.x][point.y] - p[point.x][i]) <= delta) || (Math.abs(p[point.x][point.y] - p[point.y][i]) <= delta))
				{
					list.add(word);
					points[numPoints++] = i;
					System.out.println("Palavra " + word + " adicionada ao Cluster");
				}
			}

			listAll.add(list);
			System.out.println("Fim do Cluster");
		}

		printWords();
		printOcurs();
		printP();
		printClusters();
	}

	private void init()
	{
		strResults = this.getClass().getResource("").getPath() + "results.txt";

		try
		{
			bufResults = new BufferedWriter(new FileWriter(strResults));
			bufResults.close();
		}
		catch(Exception e)
		{
			System.out.println("Error opening results file: " + e.toString() + ". Leaving program...");
			System.exit(0);
		}
	}

	private void initSentences(String searchWord)
	{
		String line = null;
		StringTokenizer lineToken = null;

		try
		{
			BufferedReader bufReader = new BufferedReader(new FileReader(this.getClass().getResource("").getPath() + "sentences.txt"));

			line = bufReader.readLine();
			while(line != null)
			{
				line = line.toLowerCase();
				lineToken = new StringTokenizer(line," ,.:;");
				while(lineToken.hasMoreTokens())
				{
					if(lineToken.nextToken().equals(searchWord))
						sentences.add(line);
				}
				line = bufReader.readLine();
			}

			bufReader.close();
		}
		catch(Exception e)
		{
			System.out.println("Erro reading sentences: " + e.toString());
		}
	}

	private void initInvalidWords()
	{
		invalidWords.add("a");
		invalidWords.add("o");
		invalidWords.add("as");
		invalidWords.add("os");
		invalidWords.add("de");
		invalidWords.add("do");
		invalidWords.add("da");
		invalidWords.add("no");
		invalidWords.add("na");
		invalidWords.add("nos");
		invalidWords.add("nas");
		invalidWords.add("um");
		invalidWords.add("uma");
		invalidWords.add("mais");
		invalidWords.add("menos");
		invalidWords.add("que");
	}

	private void initWords(String searchWord)
	{
		StringTokenizer strTok = null;

		for(int i = 0; i < sentences.size(); i++)
		{
			strTok = new StringTokenizer((String) sentences.get(i)," ,.;:");

			String word = strTok.nextToken();
			if(invalidWords.contains(word))
				word = strTok.nextToken();

			if(!words.contains(word) && !word.equals(searchWord))
				words.add(word);

			strTok.nextToken();

			while(strTok.hasMoreTokens())
			{
				word = strTok.nextToken();

				try
				{
					Integer.parseInt(word);
					word = strTok.nextToken();
				}
				catch(Exception e)
				{
					if(invalidWords.contains(word))
						word = strTok.nextToken();
				}

				if(!words.contains(word) && !word.equals(searchWord))
					words.add(word);

				if(strTok.hasMoreTokens())
					strTok.nextToken();
			}
		}
	}
	
	private void initOcurs()
	{
		ocurs = new int[words.size()][words.size()];

		for(int i = 0; i < ocurs[0].length; i++)
		{
			for(int j = 0; j < ocurs[0].length; j++)
			{
				for(int k = 0; k < sentences.size(); k++)
				{
					String word1 = (String) words.get(i);
					String word2 = (String) words.get(j);

					if(!word1.equals(word2))
					{
						String sentence = (String) sentences.get(k);

						/*if(sentence.contains(word1) && sentence.contains(word2))
							ocurs[i][j]++;*/
					}
				}
			}
		}
	}

	private void initP()
	{
		p = new float[ocurs.length][ocurs.length];

		for(int i = 0; i < ocurs[0].length; i++)
		{
			for(int j = 0; j < ocurs[0].length; j++)
				p[i][j] = (ocurs[i][j] * ocurs[i][j]) / ((float) (nocurs(i) * nocurs(j)));
		}
	}

	private int nocurs(int i)
	{
		int n = 0;

		for(int j = 0; j < ocurs[i].length; j++)
			n += ocurs[i][j];

		if(n == 0)
			n = 1;

		return n;
	}

	private Point getBest()
	{
		Point point = new Point(-1,-1);
		float value = 0;

		if(numPoints == (words.size() - 1))
		{
			for(int i = 0; i < (numPoints + 1); i++)
			{
				if(!pointsContains(i))
				{
					point = new Point(i,i);
					points[numPoints++] = i;
					return point;
				}
			}
		}

		for(int i = 0; i < p[0].length; i++)
		{
			for(int j = 0; j < p[0].length; j++)
			{
				if(i <= j)
				{
					if(p[i][j] >= value)
					{
						if(!pointsContains(i) && !pointsContains(j))
						{
							value = p[i][j];
							point = new Point(i,j);
						}
					}
				}
			}
		}

		points[numPoints++] = point.x;
		points[numPoints++] = point.y;

		return point;
	}

	private int listAllSize()
	{
		int size = 0;
		ListIterator listAllIt = listAll.listIterator();

		while(listAllIt.hasNext())
		{
			ListIterator listIt = ((LinkedList) listAllIt.next()).listIterator();

			while(listIt.hasNext())
			{
				listIt.next();
				size++;
			}
		}

		return size;
	}

	private boolean listAllContains(String word)
	{
		ListIterator listAllIt = listAll.listIterator();

		while(listAllIt.hasNext())
		{
			ListIterator listIt = ((LinkedList) listAllIt.next()).listIterator();

			while(listIt.hasNext())
			{
				if(word.equals((String) listIt.next()))
					return true;
			}
		}

		return false;
	}

	private void printSentences()
	{
		for(int i = 0; i < sentences.size(); i++)
			System.out.println((String) sentences.get(i));
	}

	public void printWords()
	{
		String str = null;

		try
		{
			bufResults = new BufferedWriter(new FileWriter(strResults,true));

			//System.out.println("| Palavras |");
			bufResults.write("| Palavras |\n",0,13);

			ListIterator wordsIt = words.listIterator();
			while(wordsIt.hasNext())
			{
				str = (String) wordsIt.next();

				//System.out.println(str);
				bufResults.write(str + "\n",0,str.length() + 1);
			}

			//System.out.println();
			bufResults.write("\n",0,1);

			bufResults.close();
		}
		catch(Exception e)
		{
			System.out.println("Error opening results file: " + e.toString() + ". Leaving program...");
			System.exit(0);
		}
	}

	public void printOcurs()
	{
		String str = null;

		try
		{
			bufResults = new BufferedWriter(new FileWriter(strResults,true));

			//System.out.println("| Ocorrencias |");
			bufResults.write("| Ocorrencias |\n",0,16);

			for(int i = 0; i < ocurs[0].length; i++)
			{
				for(int j = 0; j < ocurs[0].length; j++)
				{
					if(ocurs[i][j] < 10)
						str = " " + new Integer(ocurs[i][j]).toString();
					else
						str = new Integer(ocurs[i][j]).toString();

					//System.out.print(str + " ");
					bufResults.write(str + " ",0,str.length() + 1);
				}

				//System.out.print("\n");
				bufResults.write("\n",0,1);
			}

			//System.out.println();
			bufResults.write("\n",0,1);

			bufResults.close();
		}
		catch(Exception e)
		{
			System.out.println("Error opening results file: " + e.toString() + ". Leaving program...");
			System.exit(0);
		}
	}

	public void printP()
	{
		try
		{
			bufResults = new BufferedWriter(new FileWriter(strResults,true));

			//System.out.println("| P |");
			bufResults.write("| P |\n",0,6);

			for(int i = 0; i < p[0].length; i++)
			{
				for(int j = 0; j < p[0].length; j++)
				{
					String str = new Double(p[i][j]).toString();
					while(str.length() < 5)
						str += "0";

					//System.out.print(str.substring(0,5) + " ");
					bufResults.write(str.substring(0,5) + " ",0,6);
				}
				//System.out.print("\n");
				bufResults.write("\n",0,1);
			}

			//System.out.println();
			bufResults.write("\n",0,1);

			bufResults.close();
		}
		catch(Exception e)
		{
			System.out.println("Error opening results file: " + e.toString() + ". Leaving program...");
			System.exit(0);
		}
	}

	public void printPoints()
	{
		System.out.println("| POINTS |");

		for(int i = 0; i < numPoints; i++)
			System.out.print(points[i] + " ");

		System.out.println();
	}

	private void printClusters()
	{
		String str = null;

		try
		{
			bufResults = new BufferedWriter(new FileWriter(strResults,true));

			ListIterator listAllIt = listAll.listIterator();

			while(listAllIt.hasNext())
			{
				//System.out.println("| CLUSTER |");
				bufResults.write("| CLUSTER |\n",0,12);
				
				ListIterator listIt = ((LinkedList) listAllIt.next()).listIterator();
				while(listIt.hasNext())
				{
					str = (String) listIt.next() + " ";

					//System.out.print(str);
					bufResults.write(str,0,str.length());
				}
				//System.out.print("\n\n");
				bufResults.write("\n",0,1);
			}

			bufResults.close();
		}
		catch(Exception e)
		{
			System.out.println("Error opening results file: " + e.toString() + ". Leaving program...");
			System.exit(0);
		}
	}

	private boolean pointsContains(int value)
	{
		for(int i = 0; i < numPoints; i++)
			if(points[i] == value)
				return true;

		return false;
	}

	public static void main(String args[])
	{
		new WordsTest("psp");
	}
}
