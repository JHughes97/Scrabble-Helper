/**
* @(#)ScrabbleMain.java
* This class prints longest words that can be made given conditions
* specified by user
* @author Jack Hughes
* @version 1.4 2016/7/11
*/

import java.util.*;
import java.io.*;

public class ScrabbleMain{

	/**
	* Main method reads in a sequence of letters and spaces which are available,
	* where spaces mean any letter can be used, a sequence of letters which must be used,
	* and the number of results the user would like to be printed, then prints out 
	* the longest words that can be made with given parameters.
	* @param args
	*/
	public static void main(String[] args) throws Exception{
		//read in letters which are available to be used
		Scanner scan = new Scanner(System.in);
		String input = new String();
		while(true){
			System.out.println("\nEnter a string of letters and spaces");
			System.out.println("where spaces mean any letter can be used:");
			String str = scan.nextLine();
			input = str.toLowerCase();
			//validate input
			if(valid(input))
				break;
		}

		//put letters in array and count the number of spaces
		char[] letters = input.toCharArray();
		int spaceCount = spaces(letters);

		printDivider();
		//read in letters which must be used
		String m = new String();
		while(true){
			System.out.println("Enter letters which must be used:");
			String str = scan.nextLine();
			m = str.toLowerCase();
			//validate input
			if(valid2(m))
				break;
		}
		//put letters in an array
		char[] must = m.toCharArray();

		printDivider();
		//read in how many results to print
		int numResults;
		while(true){
			System.out.println("How many results would you like?");
			numResults = scan.nextInt();
			if(numResults >= 0)
				break;
		}

		//create priority queue containing words
		PriorityQueue<String> wordQueue = wordQueue(letters.length);

		String[] results = new String[numResults];
		int count = 0;
		//put eligible words from queue into results array
		while(wordQueue.size() > 0 && count < numResults){
			String str = wordQueue.remove();
			if(isEligible(str,letters,must,spaceCount)){
				results[count] = str;
				count++;
			}
		}

		//create hash map containing letter scores
		HashMap<Character,Integer> values = scoreMap();

		printDivider();
		//print results
		for(int i = 0; i < results.length; i++){
			if(results[i] != null){
				System.out.println((i+1)+":\t"+results[i]+" \t\t\t-Score: "+getScore(values,results[i],letters));
			}else{
				System.out.println("No more words could be found.");
				break;
			}
		}
		printDivider();

	}

	/**
	* Checks if a word is eligible
	* @param string charArray charArray intSpaces
	* @return boolean
	*/
	public static boolean isEligible(String word, char[] letters, char[] must, int spaceCount){
		//loop through letters a-z
		for(int i = 97; i < 123; i++){
			//count1 gets frequency of current letter in word
			//count2 gets frequency of current letter in inputted letters
			//count3 gets frequency of current letter in sequence of letters which must be used
			int count1 = 0, count2 = 0, count3 = 0;

			for(int j = 0; j < word.length(); j++){
				if((int)word.charAt(j) == i)
					count1++;
			}

			for(int k = 0; k < letters.length; k++){
				if((int)letters[k] == i)
					count2++;
			}

			for(int n = 0; n < must.length; n++){
				if((int)must[n] == i)
					count3++;
			}

			/* if word contains letters that are not available but there
			   is enough spaces to cover it, adjust the spaceCount variable
			   and continue looping
			*/
			if(count1 > count2 && (count2+spaceCount >= count1))
				spaceCount = (count2+spaceCount)-count1;
			/* else if word contains letters which are not available or
			   word doesn't contain letters which must be used, return false
			*/
			else if((count1 > count2) || (count1 < count3))
				return false;
		}

		//if get through loop, word is eligible, return true
		return true;
	}

	/**
	* Validates user input
	* @param string
	* @return boolean
	*/
	public static boolean valid(String input){
		//returns true if string contains only lowercase letters and spaces
		//and is at least one character long
		return input.matches("[ a-z]{1,}");
	}

	/**
	* Validates user input
	* @param string
	* @return boolean
	*/
	public static boolean valid2(String input){
		//returns true if string contains only lowercase letters
		return input.matches("[a-z]{0,}");
	}

	/**
	* Counts number of spaces in character array
	* @param charArray
	* @return int
	*/
	public static int spaces(char[] array){
		int count = 0;
		for(int i = 0; i < array.length; i++){
			if(array[i] == ' ')
				count++;
		}
		return count;
	}

	/**
	* Reads in dictionary and creates priority queue of words with
	* length less than or equal to length of inputted letters where
	* words are prioritised by length.
	* @param int
	* @return PriorityQueue
	*/
	public static PriorityQueue<String> wordQueue(int maxWordLength) throws Exception{
		//create filereader and bufferedreader
		FileReader file = new FileReader("Dictionary.txt");
		BufferedReader reader = new BufferedReader(file);

		//create priority queue with custom comparator
		PriorityQueue<String> PQ = new PriorityQueue<String>(
			new Comparator<String>(){
				public int compare(String one, String two){
					return two.length()-one.length();
				}
		});

		//read in words and add them to the queue if they are within allowed length
		String word = new String();
		while((word = reader.readLine()) != null){
			if(word.length() <= maxWordLength)
				PQ.add(word);
		}

		//return priority queue
		return PQ;
	}

	/**
	* Prints a line to divide up output
	*/
	public static void printDivider(){
		System.out.println("\n________________________________________");
	}

	/**
	* Creates hash map which maps letters to their scores
	* @return HashMap
	*/
	public static HashMap<Character,Integer> scoreMap(){
		//create hash map
		HashMap<Character,Integer> map = new HashMap<Character,Integer>();
		//put letters and their scores in map
		map.put('a',1);
		map.put('b',3);
		map.put('c',3);
		map.put('d',2);
		map.put('e',1);
		map.put('f',4);
		map.put('g',2);
		map.put('h',4);
		map.put('i',1);
		map.put('j',8);
		map.put('k',5);
		map.put('l',1);
		map.put('m',3);
		map.put('n',1);
		map.put('o',1);
		map.put('p',3);
		map.put('q',10);
		map.put('r',1);
		map.put('s',1);
		map.put('t',1);
		map.put('u',1);
		map.put('v',4);
		map.put('w',4);
		map.put('x',8);
		map.put('y',4);
		map.put('z',10);
		return map;
	}

	public static int getScore(HashMap<Character,Integer> map, String word, char[] letters){
		int score = 0;

		for(int i = 97; i < 123; i++){
			int count1 = 0, count2 = 0;

			for(int j = 0; j < word.length(); j++){
				if((int)word.charAt(j) == i)
					count1++;
			}

			for(int k = 0; k < letters.length; k++){
				if((int)letters[k] == i)
					count2++;
			}

			score += (map.get((char)i))*(Math.min(count1,count2));
		}

		return score;
	}
}