
/**
 * Google.java
 *
 * Criada em 03-12-2004
 */

package IntelligentWebSearch;

import java.io.*;
import java.util.*;
import com.google.soap.search.*;

/**
 * Classe abstracta que representa a camada de ligação ao Google
 * @author André Barbosa e Filipe Montengro
 */
public abstract class Google
{
	private static String googleKey = "mJ2Y1dpQFHKWki88JUjwW9P5opIa2WuM";
	private static GoogleSearchResult googleSearchResult = null;
	private static int googleSearchResults = 0;

	/**
	 * Define o texto a pesquisar e o número de resultados iniciais
	 * @param query texto a pesquisar
	 * @param maxResults número de resultados iniciais
	 * @return true se conseguir definir a pesquisa com sucesso, false caso contrário
	 */
	public static boolean defineSearch(String query, int maxResults, boolean portugal)
	{
		googleSearchResult = null;
		GoogleSearch googleSearch = new GoogleSearch();

		try
		{
			googleSearch.setKey(googleKey);
			googleSearch.setQueryString(query);
			googleSearch.setMaxResults(maxResults);
			googleSearch.setLanguageRestricts("lang_pt");

			if(portugal)
				googleSearch.setRestrict("countryPT");

			googleSearchResult = googleSearch.doSearch();
		}
		catch(Exception e)
		{
			System.out.println("GoogleSearchFault: " + e.toString());
			return false;
		}

		return true;
	}

	/**
	 * Retorna os resultados iniciais da pesquisa
	 * @return array com os resultados iniciais
	 */
	public static String[] getSearchResults()
	{
		GoogleSearchResultElement[] googleSearchResultElements = googleSearchResult.getResultElements();
		googleSearchResults = googleSearchResultElements.length;

		String searchResults[] = new String[googleSearchResultElements.length];

		for(int i = 0; i < googleSearchResultElements.length; i++)
			searchResults[i] = googleSearchResultElements[i].getURL();

		return searchResults;
	}

	/**
	 * Retorna o tempo de pesquisa
	 * @retorna tempo de pesquisa
	 */
	public static double getSearchTime()
	{
		return googleSearchResult.getSearchTime();
	}

	/**
	 * Retorna o número de resultados iniciais
	 * @return número de resultados iniciais
	 */
	public static int getGoogleSearchResults()
	{
		return googleSearchResults;
	}
}
