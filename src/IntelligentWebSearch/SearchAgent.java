
/**
 * SearchAgent.java
 *
 * Criada em 03-12-2004
 */

package IntelligentWebSearch;

import jade.core.Agent;
import jade.core.AID;

import jade.core.behaviours.Behaviour;

import jade.lang.acl.ACLMessage;

import java.io.*;
import java.util.*;
import java.sql.Time;

/**
 * Classe que representa o agente responsável pela pesquisa
 * @author André Barbosa e Filipe Montenegro
 */
public class SearchAgent extends Agent
{
	private static final int WORDS_ALGORITHM = 1;
	private static final int SENTENCES_ALGORITHM = 2;

	private String link = null;
	private int algorithm = 0;
	private int depthLevel = 0;
	private String searchText = null;
	private int wordsOrientation = 0;
	private BufferedWriter logFile = null;

	/**
	 * Inicia as definições do agente
	 */
	protected void setup()
	{
		openLog();
		writeLog("Iniciou processo");

		String args[] = (String[]) getArguments();

		link = (String) args[0];
		algorithm = Integer.parseInt((String) args[1]);
		depthLevel = Integer.parseInt((String) args[2]);
		searchText = ((String) args[3]).replaceAll(","," ");
		wordsOrientation = Integer.parseInt((String) args[4]);

		writeLog("Link inicial: " + link);
		writeLog("Algoritmo a utilizar: " + algorithm);
		writeLog("Profundidade da pesquisa: " + depthLevel);
		writeLog("Texto a pesqusiar: " + searchText);
		writeLog("Orientacao das palavras: " + wordsOrientation);

		System.out.println(getAID().getName() + " a correr...");

		addBehaviour(new SearchBehaviour());
	}

	/**
	 * Termina o agente
	 */
	protected void takeDown()
	{
		writeLog("Terminou processo");
		closeLog();
	}

	/**
	 * Cria o ficheiro log do agente
	 */
	private void openLog()
	{
		try
		{
			String fileName = getAID().getName().substring(0,getAID().getName().indexOf("@")) + ".txt";
			logFile = new BufferedWriter(new FileWriter(this.getClass().getResource("").getPath() + "agents/" + fileName));
		}
		catch(Exception e)
		{
			System.out.println(getAID().getName() + " : Error opening log file (" + e.toString() + ")");
		}
	}

	/**
	 * Escreve no log do agente
	 * @param str acçao realizada pelo agente
	 */
	private void writeLog(String str)
	{
		try
		{
			String log = new Time(System.currentTimeMillis()).toString() + " - " + str + "\n";
			logFile.write(log,0,log.length());
		}
		catch(Exception e)
		{
			System.out.println(getAID().getName() + " : Error writing to log file (" + e.toString() + ")");
		}
	}

	/**
	 * Fecha o log do agente
	 */
	private void closeLog()
	{
		try
		{
			logFile.close();
		}
		catch(Exception e)
		{
			System.out.println(getAID().getName() + " : Error closing log file (" + e.toString() + ")");
		}
	}

	/**
	 * Classe que representa o comportamento do agente de pesquisa
	 * @author André Barbosa e Filipe Montenegro
	 */
	private class SearchBehaviour extends Behaviour
	{
		/**
		 * Executa as acções do agente
		 */
		public void action()
		{
			writeLog("Iniciou accao");

			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID("mainAgent",AID.ISLOCALNAME));

			new HtmlParser(getAID().getName().substring(0,getAID().getName().indexOf("@")),link,depthLevel,searchText,wordsOrientation);

			System.out.println(getAID().getName() + " terminou analise do link");

			msg.setContent("END " + getAID().getName() + " terminou pesquisa");
			send(msg);
			writeLog("Mensagem enviada: " + msg.getContent());
		}

		/**
		 * Representa o fim das acções do agente
		 */
		public boolean done()
		{
			writeLog("Terminou accao");
			return true;
		}
	}
}
