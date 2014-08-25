
package IntelligentWebSearch;

import java.io.*;

public class AgentsTest
{
	private final int PROCESS_END_CHAR = 65535;

	public AgentsTest()
	{
		String classPath = this.getClass().getResource("").getPath();
		String path = classPath.substring(0,classPath.indexOf("build/classes/IntelligentWebSearch/"));

		Database.connect("jdbc:mysql://paranhos.fe.up.pt:3306/","ei01043","ax",classPath);
		Google.defineSearch("sapo",10,true);
		System.out.println("Google OK");
		//Links links = new Links(0,Google.getSearchResults());
		System.out.println("Links OK");
		Database.disconnect();

		try
		{
			BufferedWriter buf = new BufferedWriter(new FileWriter(path + "agents.bat"));
			String filePath = "java -cp build/classes;build/classes/com/jade/jade.jar;build/classes/com/jade/http.jar; jade.Boot mainAgent:IntelligentWebSearch.MainAgent(1)";
			buf.write(filePath,0,filePath.length());
			buf.close();

			Process process = Runtime.getRuntime().exec("cmd.exe /C agents.bat",null,new File(path));
			InputStream processInputStream = process.getInputStream();

            char c = (char) processInputStream.read();	
            while(c != this.PROCESS_END_CHAR)
            {
                System.out.print(c);
                c = (char) processInputStream.read();
            }
			System.out.println("Agents OK");
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}

		/*String classPath = this.getClass().getResource("").getPath();
		String path = classPath.substring(0,classPath.indexOf("build/classes/IntelligentWebSearch/"));

		try
		{
			Database.connect("jdbc:mysql://paranhos.fe.up.pt:3306/","ei01043","ax",classPath);
			BufferedWriter buf = new BufferedWriter(new FileWriter(path + "agents.bat"));

			int numAgents = Database.getNumLinks();
			String searchAgents = "";
			for(int i = 0; i < numAgents; i++)
				searchAgents += "searchAgent" + (i + 1) + ":IntelligentWebSearch.SearchAgent ";

			String filePath = "java -cp build\\classes;build\\classes\\com\\jade\\jade.jar; jade.Boot -gui mainAgent:IntelligentWebSearch.MainAgent " + searchAgents;
			buf.write(filePath,0,filePath.length());

			buf.close();

			Process process = Runtime.getRuntime().exec("cmd.exe /C agents.bat",null,new File(path));
            InputStream processInputStream = process.getInputStream();

            char c = (char) processInputStream.read();	
            while(c != this.PROCESS_END_CHAR)
            {
                System.out.print(c);
                c = (char) processInputStream.read();
            }

			//Database.disconnect();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}*/
	}

	public static void main(String args[])
	{
		new AgentsTest();
	}
}
