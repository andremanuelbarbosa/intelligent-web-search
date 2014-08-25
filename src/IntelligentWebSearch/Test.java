
package IntelligentWebSearch; 

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class Test
{
	private LinkedList listAll = new LinkedList();
	private int ocurs[][];
	private float p[][];
	int numSentences;
	double delta = 0.03;

	int numAnalysedWords = 0;
	int numWords = 0;
	int analysedWords[];

    public Test(String link)
    {
		int a = 10;
		int b = 7;
		int nab = 7;

		float p = ((float) nab / (float) (a + b - nab));

		//double p = (double) (nab / (a + b - nab));

		System.out.println(p);

		try
		{
			URL url = new URL("http://news.mredir.sapo.pt/sic.sapo.pt/");
			BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = buf.readLine();
			while(line != null)
			{
				System.out.println(line);
				line = buf.readLine();
			}
			buf.close();
		}
		catch(Exception e){}
	}

	public static void main(String args[])
	{
		new Test("");
	}
		/*String sentence = null;

		Database.connect("jdbc:mysql://paranhos.fe.up.pt:3306/","ei01043","ax",this.getClass().getResource("").getPath());

		System.out.println("A iniciar palavras invalidas...");
		Database.initInvalidWords();
		System.out.println("Palavras invalidas inicializadas.");

		System.out.println("A ler as frases so ficheiro sentences.txt e a adicinar a base de dados...");
		try
		{
			BufferedReader buf = new BufferedReader(new FileReader(this.getClass().getResource("").getPath() + "sentences.txt"));
			String line = buf.readLine();
			while(line != null)
			{
				line = line.toLowerCase();
				if(Utils.isToken(line,"tsp"))
					Database.insertSentence(line);
				line = buf.readLine();
			}
			buf.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		System.out.println("Frases adicionadas com sucesso a base de dados.");

		numSentences = Database.getNumSentences();

		System.out.println("A detectar palavras validas e a coloca-las na base de dados...");
		initWords("tsp");
		System.out.println("Palavras validas adicionadas a base de dados.");
		//numWords = Database.getNumWords();*/

		/*for(int i = 0; i < words.size(); i++)
			System.out.println((String) words.get(i));*/

		/*System.out.println("A iniciar matriz de ocorrencias das palavras...");
		initOcurs();
		System.out.println("Matriz de ocorrencias inicializada.");*/

		/*for(int i = 0; i < ocurs[0].length; i++)
		{
			for(int j = 0; j < ocurs[0].length; j++)
				System.out.print(ocurs[i][j] + " ");
			System.out.print("\n");
		}*/

		/*System.out.println("A executar algoritmo de agrupamento em clusters...");
		algoritmo1();
		System.out.println("Algoritmo executado com sucesso.");*/

		//Database.disconnect();

		/*for(int i = 0; i < listAll.size(); i++)
		{
			LinkedList l = (LinkedList) listAll.get(i);

			for(int j = 0; j < l.size(); j++)
				System.out.print((String) l.get(j) + " ");
			System.out.print("\n");
		}
    }*/

	/*private void algoritmo1()
	{
		int index = 0;
		analysedWords = new int[numWords];

		while(numAnalysedWords != numWords)
		{
			while(analysedWords[index] != 0)
				index++;

			LinkedList list = new LinkedList();

			for(int i = 0; i < numWords; i++)
			{
				if(i != index && !analysedWord(i))
				{
					double p = (double) (ocurs[index][i] / (Database.getWordOcurrances(index + 1) + Database.getWordOcurrances(i + 1) - ocurs[index][i]));
					System.out.println(Database.getWord(index + 1) + "-" + Database.getWord(i + 1) + " : " + Database.getWordOcurrances(index + 1) + " : " + Database.getWordOcurrances(i + 1) + " : " + index + " : " + i);

					if(p <= delta)
					{
						list.addLast(Database.getWord(i + 1));
						analysedWords[numAnalysedWords++] = i;
					}
				}
			}
			listAll.addLast(list);
			index = 0;
		}
	}*/

	/*public void algoritmo2()
	{
		for(int i = 0; i < numWords; i++)
		{
			LinkedList list = new LinkedList();
			list.add(new Integer(i));
			listAll.add(list);
		}

		int bestIndex = 0;
		double bestP = 0.0;

		for(int i = 0; i < numWords; i++)
		{
			for(int j = 0; j < numWords; j++)
			{
				double p = (double) (ocurs[index][i] / (Database.getWordOcurrances(index + 1) + Database.getWordOcurrances(i + 1) - ocurs[index][i]));


			}
		}

		while(numAnalysedWords != numWords)
		{
		}
	}*/
}
