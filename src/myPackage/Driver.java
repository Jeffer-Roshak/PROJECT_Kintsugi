package myPackage;

import java.io.*;
import java.util.*;

public class Driver {
	//Variables
	private static int memorySize=-1, memStrat=-1;	//Size of memory available and mem alloc strat
	
	static void mainArgumentHandler(String[] args) {
		//Function to handle MMU call
		int l= args.length;	//Get the amount of arguments
		if((l == 0)||(l>2)) {
			if(l==0)	//If no parameters were passed 
				System.out.println("Command parameters missing!");
			if(l>2)		//If more than 2 params were passed
				System.out.println("Incorrect amount of parameters!");
			System.exit(-1);	//-1 exit for error
		}
		if(l==1) {	//If only param, check if it is help
			if(args[0].equalsIgnoreCase("help")) {
				//Call help function here
				System.out.println("Print help menu here");	
			}
			else {
				//If not help, it should be num, and we need two nums
				System.out.println("Incorrect amount of parameters!");
				System.exit(-1);
			}
		}
		if(l==2) {
			//If exactly 2 params passed, ensure they are numbers
			boolean isInteger = true;
			try{
			   memorySize = Integer.parseInt(args[0]);	//First param is size of memory
			}catch (NumberFormatException ex) {
			    isInteger = false;
			}
			try{
				   memStrat = Integer.parseInt(args[1]);	//Second param is memory allocation strat type
			}catch (NumberFormatException ex) {
				isInteger = false;
			}
			if(!isInteger) {
				//If params are not integers
				System.out.println("Incorrect parameter type! Must be integers!");
				System.exit(-1);
			}
			if((memorySize<0)||(memStrat<1)||(memStrat>4)) {
				//If integers are not in right range
				System.out.println("Incorrect values passed for parameters!");
				System.exit(-1);
			}
		}
		//For debugging print params passed to main function
		for(int i = 0; i < args.length; i++) {
            System.out.println("Argument "+i+": "+args[i]);
        }
	}
	public static void main(String[] args) throws IOException{
		mainArgumentHandler(args);	//Call main function argument handler
		System.out.println("Memory size ="+memorySize+"| Strat Mode: "+memStrat);	//Debug Line
		
		//MMU command line
		String command;	//For storing entire command
		BufferedReader console = new BufferedReader (new InputStreamReader(System.in));	//Input stream buffer to obtain command
		while(true) {
			System.out.print(">");
			command = console.readLine();	//Read command
				if (command.equals("")) {
					//loop again if nothing was passed
					continue;
				}
				else if (command.equalsIgnoreCase("exit")) {
					//Exit the program
					//Additional code for data storage
					System.out.println("Exiting MMU");
					System.exit(0);
				}
			ArrayList<String> params = new ArrayList<String>();	//List to store params
			String[] splitList = command.split(" ");	//Create list of params
			
			int size = splitList.length;
			
			for (int j=0; j<size; j++) {
				System.out.println("Param "+j+"= "+splitList[j]);
				params.add(splitList[j]);	//Initialize list of params
			}
			String comm = params.get(0);	//First param is command
			if((comm.equals("cr"))&&(size==2)) {	//Command and total num of params required
				//Create process command
				System.out.println("Create Process");
				continue;
			}
			if((comm.equals("dl"))&&(size==2)) {
				//Delete process command
				System.out.println("Delete Process");
				continue;
			}
			if((comm.equals("cv"))&&(size==3)) {
				//Convert address command
				System.out.println("Convert");
				continue;
			}
			if((comm.equals("pm"))&&(size==1)) {
				//Print Memory Map
				System.out.println("Print Map");
				continue;
			}
			if((comm.equals("pt"))&&(size==1)) {
				//Print process table
				System.out.println("Print Table");
				continue;
			}
			System.out.println("Invalid command, incorrect parameters!");	//If invalid command or invalid param amount
			continue;
		}
	}
}
