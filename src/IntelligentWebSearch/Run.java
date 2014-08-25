
package IntelligentWebSearch;

import java.io.*;
import java.util.*;

public class Run
{
    private final int PROCESS_END_CHAR = 65535;

    private BufferedWriter file;
    private String path = this.getClass().getResource("").getPath();

    public Run()
    {
        String packageName = this.getClass().getPackage().toString().substring(8);
        String classPath = path.substring(0,path.lastIndexOf(packageName));

        System.out.println();

        try
        {
            this.file = new BufferedWriter(new FileWriter(this.path + "run.bat"));
            //this.setPath();
            //this.writeFile(("javac " + path + "*.java"),true);
            //this.setLibsPath();
            //this.writeFile(("jade.Boot -gui a:BookBuyerAgent"),false);
            this.writeFile("set classpath=" + classPath,true);
			this.writeFile("java -cp " + classPath + ";" + classPath + "com/googleapi/googleapi.jar; " + packageName + ".Googly dog",false);
            this.file.close();

            Process process = Runtime.getRuntime().exec("cmd.exe /C run.bat",null,new File(this.path));
            InputStream processInputStream = process.getInputStream();

            char c = (char) processInputStream.read();	
            while(c != this.PROCESS_END_CHAR)
            {
                System.out.print(c);
                c = (char) processInputStream.read();
            }
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    private void setPath()
    {
        File path = null;
        
        path = new File("C:/forte_jdk/j2sdk1.4.0/bin");
        if(path == null)
        {
            path = new File("C:/forte_jdk/j2sdk1.4.1/bin");
            if(path == null)
            {
                path = new File("C:/forte_jdk/j2sdk1.4.2/bin");
                if(path == null)
                {
                    path = new File("C:/Programs/j2sdk1.4.0/bin");
                    if(path == null)
                    {
                        path = new File("C:/Programs/j2sdk1.4.1/bin");
                        if(path == null)
                        {
                            path = new File("C:/Programs/j2sdk1.4.2/bin");
                            if(path == null)
                            {
                                System.out.println("No java detected in the system!");
                                System.out.println("Aborting...");
                                System.exit(1);
                                System.out.println("Program aborted!");
                            }
                        }
                    }
                }
            }
        }

        this.writeFile(("set path=" + path.getPath()),true);
    }

    private void setLibsPath()
    {
        String libsName[] = {"jade.jar"};
        String libsPath = new String();

        for(int i = 0; i < libsName.length; i++)
            libsPath = libsPath + this.path + "Jade/" + libsName[i] + ";. ";
        
        this.writeFile(("java -cp " + libsPath),false);
    }

    private void writeFile(String str, boolean newLine)
    {
        try
        {
            if(newLine)
                this.file.write(str + "\n");
            else
                this.file.write(str);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args)
    {
        new Run();
    }
}
