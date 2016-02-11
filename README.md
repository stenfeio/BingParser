#Test cases for bing scrapper:
## Querying bing directly and outputing to file:
java -cp ./:../lib/jsoup-1.8.3.jar:../lib/commons-cli-1.3.1.jar BingSearch -q http://www.bing.com/search?q=uga -o results.txt

##Scrapping from input html file and outputing results to output file:
java -cp ./:../lib/jsoup-1.8.3.jar:../lib/commons-cli-1.3.1.jar BingSearch -f bing.html -o results.txt

##When both -f and -q are inputted, scrapping happens from input file:
java -cp ./:../lib/jsoup-1.8.3.jar:../lib/commons-cli-1.3.1.jar BingSearch -f bing.html -q http://www.bing.com/search?q=uga -o results.txt
