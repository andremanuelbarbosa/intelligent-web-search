
/**
 * Links.java
 *
 * Criada em 03-12-2004
 */

package IntelligentWebSearch;

import websphinx.*;
import java.util.*;
import java.io.*;

/**
 * Classe que faz a gest�o dos links a analisar pelos agentes
 * @author Andr� Barbosa e Filipe Montenegro
 */
public class Links
{
	private String validExtensions[] = {"htm","html","php","asp","txt"};

	/**
	 * Constr�i um gestor de links
	 * @param depth profundidade de pesquisa
	 * @param initialResults links retornados pelo Google
	 */
	public Links(int depth, int code, String initialResults[])
	{
		int index = 0;
		LinkedList links = new LinkedList();

		links.add(Database.getLinkDatabase(code));

		for(int i = 1; i <= depth; i++)
		{
			ListIterator linksIterator = links.listIterator();

			int indexCopy = index;
			index = links.size();

			for(int j = indexCopy; j < links.size(); j++)
			{
				try
				{
					Page page = new Page(new Link((String) linksIterator.next()), new DownloadParameters(), new HTMLParser());
					Link[] linksAux = page.getLinks();

					for(int k = 0; k < linksAux.length; k++)
					{
						String linkReturn = hasValidExtension(linksAux[k].toURL());

						if(linkReturn != null)
						{
							Database.insertLinkDatabase(linkReturn.replaceAll("'","\""),i);
							links.add(linkReturn.replaceAll("'","\""));
						}
					}
				}
				catch(Exception e)
				{
				}
			}
		}
	}

	/**
	 * Verifica se um link tem uma extens�o v�lida
	 * @param link link
	 * @return o link se este tiver uma extens�o v�lida, null caso contr�rio
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
	 * Verifica se uma extens�o � v�lida
	 * @param extensions extens�o
	 * @return true se a extens�o for v�lida, false caso contr�rio
	 */
	private boolean isValidExtension(String extension)
	{
		for(int i = 0; i < validExtensions.length; i++)
			if(validExtensions[i].equals(extension))
				return true;

		return false;
	}
}
