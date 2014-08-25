
/**
 * Database.java
 *
 * Criada em 03-12-2004
 */

package IntelligentWebSearch;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Classe abstracta que representa a camada de dados
 * @author André Barbosa e Filipe Montenegro
 */
public abstract class Database
{
	private static String path = null;
    private static Connection connection = null;
    private static Statement statement = null;
	private static BufferedWriter sentencesFileWriter = null;
	private static BufferedReader sentencesFileReader = null;
	private static LinkedList invalidWords = null;
	private static LinkedList links = null;

	/**
	 * Estabelece a ligação à base de dados e cria os ficheiros de dados
	 * @param url servidor da base de dados
	 * @param user nome de utilizador
	 * @param pass palavra-chave do utilizador
	 * @param path directório onde irão ser criados os ficheiros
	 */
    public static void connect(String url, String user, String pass, String classPath)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url,user,pass);
            statement = connection.createStatement();
            statement.executeUpdate("use ei01043;");
			path = classPath;
			sentencesFileWriter = new BufferedWriter(new FileWriter(path + "sentences.txt",true));
			initInvalidWords();
			links = new LinkedList();
        }
        catch(Exception e)
        {
            System.out.println("Error connecting to database (" + e.toString() + ") in connect. Leaving program...");
            System.exit(0);
        }
    }

	/**
	 * Inicia a lista que contém as palavras inválidas
	 */
	private static void initInvalidWords()
	{
		String line = null;
		invalidWords = new LinkedList();

		try
		{
			BufferedReader bufReader = new BufferedReader(new FileReader(path + "invalidWords.txt"));

			line = bufReader.readLine();
			while(line != null)
			{
				if(!line.equals("") && line.charAt(0) != '|' && !invalidWords.contains(line))
					invalidWords.add(line);

				line = bufReader.readLine();
			}

			bufReader.close();
		}
		catch(Exception e)
		{
			System.out.println("Error connecting to database (" + e.toString() + ") in initInvalidWords. Leaving program...");
            System.exit(0);
		}
	}

    /**
	 * Insere uma palavra na base de dados, na tabela "AIAD_WORDS"
	 * @param word palavra
	 */
	public static void insertWord(String word)
    {
		try
		{
			statement.executeUpdate("INSERT INTO AIAD_WORDS_COPY (word,ocurrances) VALUES('" + word + "',1);");
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in insertWord. Leaving program...");
			System.exit(0);
		}
    }	

	/**
	 * Insere um link na base de dados, na tabela "AIAD_LINKS"
	 * @param link link
	 * @param priority prioridade do link
	 */
	public static void insertLinkDatabase(String link, int priority)
    {
		ResultSet rs = null;

        try
        {
			rs = statement.executeQuery("SELECT priority FROM AIAD_LINKS where link = '" + link + "';");
			rs.next();
			int oldPriority = Integer.parseInt(rs.getString(1));

			if(priority < oldPriority)
				statement.executeUpdate("UPDATE AIAD_LINKS SET priority = " + priority + " where link = '" + link + "';");
        }
        catch(Exception e)
        {
            try
            {
				statement.executeUpdate("INSERT INTO AIAD_LINKS (link,priority) VALUES('" + link + "'," + priority + ");");
            }
            catch(Exception e2)
            {
				System.out.println("Error acceding database (" + e2.toString() + ") in insertLinkDatabase. Leaving program...");
                System.exit(0);
            }
        }
    }

	public static boolean insertLinkList(String link)
	{
		if(links.contains(link))
			return false;

		links.add(link);
		return true;
	}

	/**
	 * Insere um agente na base de dados, na tabela "AIAD_AGENTS"
	 * @param name nome do agente
	 * @param link link que o agente pesquisou
	 * @param numWords número de palavras que o agente analisou
	 */
	public static void insertAgent(String name, String link, int numWords)
	{
		try
		{
			statement.executeUpdate("INSERT INTO AIAD_AGENTS (name,link,numWords) VALUES('" + name + "','" + link + "'," + numWords + ");");
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") insertAgent. Leaving program...");
			System.exit(0);
		}
	}

	/**
	 * Insere uma frase no ficheiro de dados "senteces.txt"
	 * @param sentence frase
	 */
	/*public static void insertSentence(String sentence)
	{
		try
		{
			sentencesFileWriter.write(sentence,0,sentence.length());
			sentencesFileWriter.newLine();
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + "). Leaving program...");
			System.exit(0);
		}
	}*/

	/**
	 * Verifica se uma palavra é invalida
	 * @param word palavra
	 * @return true se a palavra for inválida, false se a palavra for inválida
	 */
	public static boolean isInvalidWord(String word)
	{
		if(invalidWords.contains(word))
			return true;

		if(word.indexOf("&") != -1)
			return true;

		return false;
	}

	/**
	 * Retorna uma palavra
	 * @param code código da palavra
	 * @return palavra
	 */
	public static String getWord(int code)
	{
		String word = null;
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT word FROM AIAD_WORDS WHERE code = " + code + ";");
			rs.next();
			word = rs.getString(1);
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getWord. Leaving program...");
			System.exit(0);
		}

		return word;
	}

	/**
	 * Retorna o número de ocorrências de uma palavra
	 * @param code código da palavra
	 * @return número de ocorrências da palavra
	 */
	public static int getWordOcurrances(int code)
	{
		int ocurrances = 0;
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT ocurrances FROM AIAD_WORDS WHERE code = " + code + ";");
			rs.next();
			ocurrances = Integer.parseInt(rs.getString(1));
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getWordOcurrances. Leaving program...");
			System.exit(0);
		}

		return ocurrances;
	}

	public static int getWordOcurrances()
	{
		int ocurrances = 0;
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT SUM(ocurrances) FROM AIAD_WORDS;");
			rs.next();
			ocurrances = Integer.parseInt(rs.getString(1));
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getWordOcurrances. Leaving program...");
			System.exit(0);
		}

		return ocurrances;
	}

	/**
	 * Retorna o número de palavras existentes na base de dados
	 * @return número de palavras existentens na base de dados
	 */
	public static int getNumWords()
	{
		int numWords = 0;
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT COUNT(*) FROM AIAD_WORDS;");
			rs.next();
			numWords = Integer.parseInt(rs.getString(1));
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getNumWords. Leaving program...");
			System.exit(0);
		}

		return numWords;
	}

	/**
	 * Remove palavras duplicadas e actualiza as suas ocorrências
	 */
	public static void updateWords()
	{
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT word,SUM(ocurrances) AS ocur FROM AIAD_WORDS_COPY GROUP BY word ORDER BY ocur DESC;");

			while(rs.next())
				statement.executeUpdate("INSERT INTO AIAD_WORDS (word,ocurrances) VALUES('" + rs.getString(1) + "'," + Integer.parseInt(rs.getString(2)) + ");");

			statement.executeUpdate("DELETE FROM AIAD_WORDS_COPY;");
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in updateWords. Leaving program...");
			System.exit(0);
		}
	}

	/**
	 * Retorna um link
	 * @param code código do link
	 * @return link
	 */
	public static String getLinkDatabase(int code)
	{
		String link = null;
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT link FROM AIAD_LINKS WHERE code = " + code + ";");
			rs.next();
			link = rs.getString(1);
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getLinkDatabase. Leaving program...");
			System.exit(0);
		}

		return link;
	}

	public static String getLinkList(int index)
	{
		return (String) links.get(index);
	}

	/**
	 * Retorna os links que tem uma determinada prioridade
	 * @param priority prioridade
	 * @return lista com os links
	 */
	public static LinkedList getLinks(int priority)
	{
		LinkedList links = new LinkedList();
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT link FROM AIAD_LINKS WHERE priority = " + priority + ";");

			while(rs.next())
				links.add(rs.getString(1));
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getLinks. Leaving program...");
			System.exit(0);
		}

		return links;
	}

	/**
	 * Retorna a prioridade de um link
	 * @param code código do link
	 * @return prioridade do link
	 */
	public static int getLinkPriority(int code)
	{
		int priority = 0;
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT priority FROM AIAD_LINKS WHERE code = " + code + ";");
			rs.next();
			priority = Integer.parseInt(rs.getString(1));
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getLinkPriority. Leaving program...");
			System.exit(0);
		}

		return priority;
	}

	/**
	 * Retorna o número de links existentes na base de dados
	 * @return número de links
	 */
	public static int getNumLinks()
	{
		return links.size();
		/*int numLinks = 0;
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT COUNT(*) FROM AIAD_LINKS;");
			rs.next();
			numLinks = Integer.parseInt(rs.getString(1));
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + "). Leaving program...");
			System.exit(0);
		}

		return numLinks;*/
	}

	/**
	 * Retorna o nome do agente
	 * @param code código do agente
	 * @return nome do agente
	 */
	public static String getAgentName(int code)
	{
		String name = null;
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT name FROM AIAD_AGENTS WHERE code = " + code + ";");
			rs.next();
			name = rs.getString(1);
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getAgentName. Leaving program...");
			System.exit(0);
		}

		return name;
	}

	/**
	 * Retorna o link do agente
	 * @param code código do agente
	 * @return link do agente
	 */
	public static String getAgentLink(int code)
	{
		String link = null;
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT link FROM AIAD_AGENTS WHERE code = " + code + ";");
			rs.next();
			link = rs.getString(1);
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getAgentLink. Leaving program...");
			System.exit(0);
		}

		return link;
	}

	/**
	 * Retorna o número de palavras analisadas pelo agente
	 * @param code código do agente
	 * @return número de palavras analisadas pelo agente
	 */
	public static int getAgentNumWords(int code)
	{
		int numWords = 0;
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT numWords FROM AIAD_AGENTS WHERE code = " + code + ";");
			rs.next();
			numWords = Integer.parseInt(rs.getString(1));
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getAgentNumWords. Leaving program...");
			System.exit(0);
		}

		return numWords;
	}

	/**
	 * Retorna o número de agentes existentes na base de dados
	 * @return número de agentes existentes na base de dados
	 */
	public static int getNumAgents()
	{
		int numAgents = 0;
		ResultSet rs = null;

		try
		{
			rs = statement.executeQuery("SELECT COUNT(*) FROM AIAD_AGENTS;");
			rs.next();
			numAgents = Integer.parseInt(rs.getString(1));
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getNumAgents. Leaving program...");
			System.exit(0);
		}

		return numAgents;
	}

	/**
	 * Retorna uma frase
	 * @param line linha do ficheiro onde está a frase
	 * @return frase
	 */
	public static String getSentence(int line)
	{
		String lineAux = null;
		String sentence = "";

		try
		{
			sentencesFileReader = new BufferedReader(new FileReader(path + "sentences/sentences.txt"));
			for(int i = 0; i < line; i++)
			{
				if(i == (line - 1))
				{
					lineAux = sentencesFileReader.readLine();
					while(lineAux != null)
					{
						if(!lineAux.equals("\n") && !lineAux.equals(""))
							sentence += lineAux + "\n";
						else
							break;
						lineAux = sentencesFileReader.readLine();
					}
				}

				lineAux = sentencesFileReader.readLine();
				while(lineAux != null && !lineAux.equals("\n") && !lineAux.equals(""))
					lineAux = sentencesFileReader.readLine();
			}

			sentencesFileReader.close();
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getSentene. Leaving program...");
			System.exit(0);
		}

		return sentence;
	}

	public static String getSentence1(int line)
	{
		String lineAux = null;
		String sentence = "";

		try
		{
			sentencesFileReader = new BufferedReader(new FileReader(path + "sentences/sentences.txt"));
			for(int i = 0; i < line; i++)
			{
				if(i == (line - 1))
				{
					lineAux = sentencesFileReader.readLine();
					while(lineAux != null)
					{
						if(!lineAux.equals("\n") && !lineAux.equals(""))
							sentence += lineAux + "\n";
						lineAux = sentencesFileReader.readLine();
					}
				}

				lineAux = sentencesFileReader.readLine();
				while(lineAux != null && !lineAux.equals("\n") && !lineAux.equals(""))
					lineAux = sentencesFileReader.readLine();
			}

			sentencesFileReader.close();
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getSentene. Leaving program...");
			System.exit(0);
		}

		return sentence;
	}

	/**
	 * Retorna o número de frases existentes no ficheiro
	 * @return número de frases existentes no ficheiro
	 */
	public static int getNumSentences()
	{
		String line = null;
		int numSentences = 0;

		try
		{
			sentencesFileReader = new BufferedReader(new FileReader(path + "sentences/sentences.txt"));
			line = sentencesFileReader.readLine();
			while(line != null)
			{
				if(line.equals("\n") || line.equals(""))
					numSentences++;
				line = sentencesFileReader.readLine();
			}
			sentencesFileReader.close();
		}
		catch(Exception e)
		{
			System.out.println("Error acceding database (" + e.toString() + ") in getNumSentences. Leaving program...");
			System.exit(0);
		}

		return numSentences;
	}

	/**
	 * Limpa todas as tabelas da base de dados
	 */
	public static void clean()
	{
		try
		{
			statement.executeUpdate("DELETE FROM AIAD_WORDS;");
			statement.executeUpdate("DELETE FROM AIAD_LINKS;");
			statement.executeUpdate("DELETE FROM AIAD_AGENTS;");

			sentencesFileWriter = new BufferedWriter(new FileWriter(path + "sentences/sentences.txt"));
			sentencesFileWriter.close();

			invalidWords = null;
		}
		catch(Exception e)
		{
			System.out.println("Error connecting to database (" + e.toString() + ") in clean. Leaving program...");
            System.exit(0);
		}
	}

    /**
	 * Termina a ligação à base de dados e fecha os ficheiros de dados
	 */
	public static void disconnect()
    {
        try
        {
            statement.close();
            connection.close();
        }
        catch(Exception e)
        {
            System.out.println("Error acceding database (" + e.toString() + ") in disconnect. Leaving program...");
            System.exit(0);
        }
    }
}
