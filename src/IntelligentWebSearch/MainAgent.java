
/**
 * MainAgent.java
 *
 * Criada em 03-12-2004
 */

package IntelligentWebSearch;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;

import jade.lang.acl.ACLMessage;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.io.*;
import java.sql.Time;
import java.util.*;

/**
 * Classe que representa o agente principal
 * @author André Barbosa e Filipe Montenegro
 */
public class MainAgent extends Agent
{
	private int algorithm = 0;
	private int depthLevel = 0;
	private int searchAgents = 0;
	private String searchText = null;
	private int wordsOrientation = 0;
	private String searchAgentsNames[];
	private BufferedWriter logFile = null;
	private AgentController agentControllers[];

	/**
	 * Inicia as definições do agente
	 */
	public void setup()
	{
		openLog();
		writeLog("Iniciou processo");

		System.out.println("mainAgent a correr...");

		Object args[] = getArguments();

		algorithm = Integer.parseInt((String) args[0]);
		depthLevel = Integer.parseInt((String) args[1]);
		searchAgents = Integer.parseInt((String) args[2]);
		searchText = (String) args[3];
		wordsOrientation = Integer.parseInt((String) args[4]);

		writeLog("Algoritmo a utilizar: " + algorithm);
		writeLog("Profundidade da pesquisa de links: " + depthLevel);
		writeLog("Nº de agentes de pesquisa a lancar: " + searchAgents);
		writeLog("Texto a pesquisar: " + searchText);
		writeLog("Orientacao das palavras: " + wordsOrientation);

		Database.connect("jdbc:mysql://paranhos.fe.up.pt:3306/","ei01043","ax",this.getClass().getResource("").getPath());

		searchAgentsNames = new String[searchAgents];
		agentControllers = new AgentController[searchAgents];

		for(int i = 0; i < searchAgents; i++)
			searchAgentsNames[i] = "searchAgent" + (i + 1);

		addBehaviour(new MainBehaviour());
	}

	/**
	 * Termina o agente
	 */
	public void takeDown()
	{
		writeLog("Terminou processo");
		closeLog();
		Database.disconnect();
		System.exit(0);
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
	 * Classe que representa o comportamento do agente principal
	 * @author André Barbosa e Filipe Montenegro
	 */
	private class MainBehaviour extends Behaviour
	{
		/**
		 * Executa as acções do agente
		 */
		public void action()
		{
			writeLog("Iniciou accao");

			String args[][] = new String[searchAgents][5];
			AgentContainer agentContainer = getContainerController();

			for(int i = 0; i < searchAgentsNames.length; i++)
			{
				try
				{
					args[i][0] = Database.getLinkDatabase(i + 1);
					args[i][1] = "" + algorithm;
					args[i][2] = "" + depthLevel;
					args[i][3] = searchText;
					args[i][4] = "" + wordsOrientation;

					agentControllers[i] = agentContainer.createNewAgent(searchAgentsNames[i],"IntelligentWebSearch.SearchAgent",args[i]);
					agentControllers[i].start();
				}
				catch(Exception e)
				{
					System.out.println(e.toString());
				}
			}

			int end_agents = 0;

			//for(int i = 0; i < searchAgentsNames.length; i++)
			while(end_agents < searchAgents)
			{
				try
				{
					ACLMessage msg = null;
					String msgType = null;
					boolean msgError = false;
					String msgContent = null;
					String msgSentence = null;
					boolean termination = false;
					String msgSearchAgent = null;
					String msgErrorDescription = null;
					StringTokenizer msgTokenizer = null;

					//while(!termination)
					//{
						msg = blockingReceive();
						msgContent = msg.getContent();

						msgTokenizer = new StringTokenizer(msgContent," ");
						msgType = msgTokenizer.nextToken();

						if(msgType.equals("END") || msgType.equals("SEN") || msgType.equals("ERR"))
						{
							msgSearchAgent = msgTokenizer.nextToken();

							if(msgSearchAgent != null)
							{
								if(msgType.equals("END"))
								{
									termination = true;
									writeLog("O agente de pesquisa " + msgSearchAgent + " terminou");
									int i = (Integer.parseInt(msgSearchAgent.substring(11,msgSearchAgent.indexOf("@")))) - 1;
									Database.insertAgent(agentControllers[i].getName(),args[i][0],0);
									agentControllers[i].kill();
									end_agents++;
								}
								/*else if(msgType.equals("SEN"))
								{
									msgSentence = msgContent.substring(msgContent.indexOf(msgSearchAgent) + msgSearchAgent.length());

									if(msgSentence != null)
									{
										Database.insertSentence(msgSentence);
										writeLog("O agente de pesquisa " + msgSearchAgent + " enviou a frase \"" + msgSentence + "\"");
									}
									else
										msgError = true;
								}*/
								else
								{
									msgErrorDescription = msgContent.substring(msgContent.indexOf(msgSearchAgent) + msgSearchAgent.length());

									if(msgErrorDescription != null)
										writeLog("Mensagem de erro \"" + msgErrorDescription + "\" recebida do agente de pesquisa " + msgSearchAgent);
									else
										msgError = true;
								}
							}
							else
								msgError = true;
						}
						else
							msgError = true;

						if(msgError)
							writeLog("A mensagem \"" + msgContent + "\" nao foi reconhecida pelo sistema");
					//}

				}
				catch(Exception e)
				{
					System.out.println(e.toString());
				}
			}

			writeLog("Os agentes de pesquisa terminaram as suas tarefas e foram removidos");
		}

		/**
		 * Representa o fim das acções do agente
		 */
		public boolean done()
		{
			writeLog("Terminou accao");
			takeDown();
			return true;
		}
	}
}
