package ro.rmarius.cmdrunner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 * 
 */
public class App
{
	/**
	 * Cauta intr-un text cuvintele care indeplinesc expresia regulata
	 * primita ca parametru
	 * 
	 * @param inputText
	 * @param keyWordRule
	 * @return
	 */
	private static Set<String> getKeyWords(String inputText, String keyWordRule)
	{
		Pattern p = Pattern.compile(keyWordRule);
		Matcher m = p.matcher(inputText);
		Set<String> matches = new HashSet<String>();
		
		while (m.find())
			matches.add(m.group());

		return matches;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		// citire fisier primit in linia de comanda
		String commandFile = args[0];
		FileInputStream fis = new FileInputStream(commandFile);
		StringBuffer sb = new StringBuffer();
		
		while (fis.available() != 0)
		{
			int c = fis.read();
			sb.append((char)c);
		}
		
		fis.close();
		
		// extragerea cuvintelor cheie
		Set<String> keyWords = getKeyWords(sb.toString(), "\\$\\{\\w+\\}");
		
		// valorile cuvintelor cheie
		String outputText = sb.toString();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		for (Iterator<String> iterator = keyWords.iterator(); iterator.hasNext();)
		{
			String keyWord = iterator.next();
			System.out.print(keyWord + ": ");
			String value = br.readLine();
			outputText = outputText.replace(new StringBuffer(keyWord), new StringBuffer(value));
		}
		
		// executie comanda
		execCommand(outputText);
	}
	
	/**
	 * Executa o comanda.
	 * @param command
	 */
	public static void execCommand(String command)
	{
		try
		{
			String line;
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			while ((line = input.readLine()) != null)
			{
				System.out.println(line);
			}
			
			input.close();
		} catch (Exception err)
		{
			err.printStackTrace();
		}
	}
}
