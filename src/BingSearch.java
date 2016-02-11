import org.apache.commons.cli.*;

import java.io.File;
import java.net.*;
import java.util.*;
import java.io.*;
import java.io.BufferedWriter;

import org.jsoup.*;
import org.jsoup.parser.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class BingSearch{
	String query;
	File inputFile;
        File outputFile;

	public BingSearch(String query, String inputFile,  String outputFile, char mode){
		if(mode == 'f'){
			if(!inputFile.equals(""))
				this.inputFile = new File(inputFile);
			if(!outputFile.equals(""))
				this.outputFile = new File(outputFile);
			this.query = "";
				
			//TODO open and read from input file, parse information, write to output file top 5 results 
			openInputFile();
		}
		else if(mode == 'q'){
			if(!query.equals(""))
				this.query = query;	
			if(!outputFile.equals(""))
				this.outputFile = new File(outputFile);
			this.inputFile = null;
			
			//TODO contact bing, parse feedback, output top 5 results to outputFile
			scrapBing();
		}	
	}

	private void openInputFile(){
		ArrayList<String> lList = new ArrayList<String>();
		System.out.println("Openning file...");
		try{
			Document doc = Jsoup.parse(inputFile, "UTF-8", "");
			Elements results = doc.getElementsByClass("b_algo");
			for(Element link : results){
				Elements temp = link.select("h2");
				temp = temp.select("[href]");
				lList.add(temp.attr("href"));
				//System.out.println(temp.attr("href"));
			}
			openOutputFile(lList);
		}catch(IOException e){
			System.out.println("Could not connect to provided file path!");
			System.exit(0);
		}
	}

	private void openOutputFile(ArrayList<String> links){
		System.out.println(links);
		try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"))){
			for(int i = 0; i < 5; i++){
				writer.write(links.get(i) + "\n");
			}
			//writer.write(links.toString());
		}catch(Exception e){
			System.out.println("Error with opening the file output file...");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void scrapBing(){
		ArrayList<String> lList = new ArrayList<String>();
		System.out.println("Connecting...");
		try{
			Document doc = Jsoup.connect(query).get();
			Elements results = doc.getElementsByClass("b_algo");
			for(Element link : results){
				Elements temp = link.select("h2");
				temp = temp.select("[href]");
				lList.add(temp.attr("href"));
				//System.out.println(temp.attr("href"));
			}
			openOutputFile(lList);
		}catch(IOException e){
			System.out.println("Could not connect to provided url!");
			System.exit(0);
		}
	}	
	
	public static void main(String[] args) throws ParseException{
		//Apache commons-cli variables
		Options options = new Options();
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();	
		CommandLine cmd;

		options.addOption("q",true,"takes in a query to send to bing: http://www.bing.com/search?q=uga&go=Submit+Query&qs=ds&form=QBLH");
		options.addOption("o",true,"define the output file where the output file will be stored.");
		options.addOption("f",true,"define the input file name.");

		cmd = parser.parse(options, args);
		
		//BingSearch instance
		BingSearch bsObject;

		if(cmd.hasOption("q") && cmd.hasOption("o") && cmd.hasOption("f")){	
			System.out.println("Scrapping from file...");
			bsObject = new BingSearch("", cmd.getOptionValue("f"), cmd.getOptionValue("o"), 'f');
			//TODO do only "o" and "f"
		}
		else if(cmd.hasOption("f") && cmd.hasOption("o")){
			System.out.println("Scrapping from file...");
			bsObject = new BingSearch("", cmd.getOptionValue("f"), cmd.getOptionValue("o"), 'f');
			//TODO open file and read html from there and output to file results
		}
		else if(cmd.hasOption("q") && cmd.hasOption("o")){	
			System.out.println("Scrapping from bing...");
			bsObject = new BingSearch(cmd.getOptionValue("q"), "", cmd.getOptionValue("o"), 'q');
			//TODO connecting to bing and output to file 
		}
		else{
			System.out.println("Program must contain -o option");
			formatter.printHelp("BingSearch", options);
			System.exit(0);
		}
	}


}
