package myPackage;

import java.io.*;
import java.util.*;

import myPackage.Process;

import java.text.SimpleDateFormat;  

public class mmu {
	//Variables
	private static int memorySize=-1, memStrat=-1, nextPID=0;//Size of memory available and mem alloc strat
	//PID 0 is usually paging or swapping process, PID 1 is for init process
	private static LinkedList<Process> ProcessTable= new LinkedList<Process>();
	private static MemoryList memoryMap = new MemoryList();
	
	//Functions
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
			//If exactly 2 parameters passed, ensure they are numbers
			boolean isInteger = true;
			try{
			   memorySize = Integer.parseInt(args[0]);	//First parameter is size of memory
			}catch (NumberFormatException ex) {
			    isInteger = false;
			}
			try{
				   memStrat = Integer.parseInt(args[1]);	//Second parameter is memory allocation strategy type
			}catch (NumberFormatException ex) {
				isInteger = false;
			}
			if(!isInteger) {
				//If parameters are not integers
				System.out.println("ERROR: Incorrect parameter type! Must be integers!");
				System.exit(-1);
			}
			if((memorySize<0)||(memStrat<1)||(memStrat>4)) {
				//If integers are not in right range
				System.out.println("ERROR: Incorrect values passed for parameters!");
				System.exit(-1);
			}
		}
		//For debugging print parameters passed to main function
		/*
		for(int i = 0; i < args.length; i++) {
            System.out.println("Argument "+i+": "+args[i]);
        }*/
	}
	
	//Create Process related functions
	static void firstFit(int processSize) {
		memNode m = new memNode(true, -1, processSize);	//Create an element	(for memory list)
		memoryMap.firstFit(m);	//Pass element for insertion (first fit)
		if(m.type) {	//If element could not be inserted, type would have been set to hole
			Process p = new Process(nextPID++, new Date(), m.base, processSize);	//Create Process object (for process table)
			ProcessTable.add(p);	//Add process to process table
			System.out.printf("PID: %d | Base: %d | Limit: %d\n", p.getpID(), p.getBase(), p.getLimit());	//Print process details (date and time is irrelevant here)
		}	
	}
	
	static void nextFit(int processSize) {
		memNode m = new memNode(true, -1, processSize); 
		memoryMap.nextFit(m);	//Pass element for insertion (next fit)
		if(m.type) {
			Process p = new Process(nextPID++, new Date(), m.base, processSize);
			ProcessTable.add(p);
			System.out.printf("PID: %d | Base: %d | Limit: %d\n", p.getpID(), p.getBase(), p.getLimit());
		}	
	}
	
	static void bestFit(int processSize) {
		memNode m = new memNode(true, -1, processSize);
		memoryMap.bestFit(m);	//Pass element for insertion (best fit)
		if(m.type) {
			Process p = new Process(nextPID++, new Date(), m.base, processSize);
			ProcessTable.add(p);
			System.out.printf("PID: %d | Base: %d | Limit: %d\n", p.getpID(), p.getBase(), p.getLimit());
		}	
	}
	
	static void worstFit(int processSize) {
		memNode m = new memNode(true, -1, processSize);
		memoryMap.worstFit(m); //Pass element for insertion (worst fit)
		if(m.type) {
			Process p = new Process(nextPID++, new Date(), m.base, processSize);
			ProcessTable.add(p);
			System.out.printf("PID: %d | Base: %d | Limit: %d\n", p.getpID(), p.getBase(), p.getLimit());
		}	
	}
	
	/* Command Functions
	 * These functions are called directly from the commands.
	 */
	static void createProcess(int memStrat, int processSize) {
		//Create Process command (cr)
		if(processSize>memorySize) {
			//If process is larger than the memory
			System.out.println("ERROR: Process larger than memory! Cannot create.");
			return;
		}
		switch(memStrat) {
		case 1:
			//First Fit
			//System.out.println("First Fit"); //For Debug
			firstFit(processSize);
			break;
		case 2:
			//Next Fit
			//System.out.println("Next Fit"); //For Debug
			nextFit(processSize);
			break;
		case 3:
			//Best Fit
			//System.out.println("Best Fit"); //For Debug
			bestFit(processSize);
			break;
		case 4:
			//Worst Fit
			//System.out.println("Worst Fit"); //For Debug
			worstFit(processSize);
			break;
		}//Switch doesn't need default, memStrat is always 1-4
	}
	
	static void deleteProcess(int pID) {
		//Delete Process command (dl)
		boolean notFound = true;
		for(int i=0;i<ProcessTable.size();i++) {
			//Iterate through process table to find the process
			if(ProcessTable.get(i).getpID()==pID) {
				//If requested PID is found, then
				notFound=false;	//Process has been found
				memoryMap.deleteProcess(ProcessTable.get(i).getBase());	//Call deleteProcess, pass base of process to it
				ProcessTable.remove(i);	//Remove entry from process table
				break;	//Break from loop (process found)
			}
		}
		if(notFound) {
			//If requested PID was not found
			System.out.println("ERROR: No process with requested pID.");
		}
	}
	
	static void convertAddress(int pID, int virtualAddress) {
		//Address Conversion command (cv)
		boolean notFound = true;
		for(int i=0;i<ProcessTable.size();i++) {
			//Iterate through process table to find the process
			if(ProcessTable.get(i).getpID()==pID) {
				//If requested PID is found, then
				notFound=false;	//Process has been found
				if(ProcessTable.get(i).getLimit()> virtualAddress) {
					//If virtual address is in range of limit
					System.out.printf("PID: %d\nVirtual Address: %d | Physical Address: %d\n", ProcessTable.get(i).getpID(), virtualAddress, (ProcessTable.get(i).getBase()+virtualAddress));
				}
				else {
					//If the requested virtual address exceeds the limit
					System.out.println("Error: Address out of bounds"); 
				}
				break;
			}
		}
		if(notFound) {
			//If requested PID was not found
			System.out.println("Error: No process with requested pID");
		}
	}
	
	static void printMemoryMap() {
		//Print Memory Map command (pm)
		System.out.printf("%6s %15s %15s\n", "P/H", "Base", "Limit");	//Title of table
		memoryMap.printList();	//Call the function to print list
	}
	
	static void printProcessTable() {
		//Print Process Table command (pt)
		System.out.printf("%10s %15s %13s %15s %15s\n", "PID", "Date", "Time", "Base", "Limit");	//Title of table
		for(int i=0; i<ProcessTable.size();i++) {
			System.out.println(ProcessTable.get(i));	//Print the elements of the process table
		}
	}
		
	//Driver Function
	public static void main(String[] args) throws IOException{
		mainArgumentHandler(args);	//Call main function argument handler
		//System.out.println("Memory size ="+memorySize+"| Strat Mode: "+memStrat);	//Debug Line
		memoryMap.addItem(new memNode(false, 0, memorySize));
		//memoryMap.printList();	//Debug Line
		//MMU command line
		String command;	//For storing entire command
		BufferedReader console = new BufferedReader (new InputStreamReader(System.in));	//Input stream buffer to obtain command
		while(true) {
			System.out.print(">");
			command = console.readLine();	//Read command
				if (command.equals("")) {
					//We loop again if nothing was entered
					continue;
				}
				else if (command.equalsIgnoreCase("exit")) {
					//Exit the program
					System.out.println("Exiting MMU");
					System.exit(0);
				}
			String[] params = command.split(" ");	//Create list of parameters
			
			int size = params.length;
			
			/*
			for (int j=0; j<size; j++) {	//For debug
				System.out.println("Param "+j+"= "+params[j]);
			}*/
			
			String comm = params[0];	//First parameter is command
			if((comm.equals("cr"))&&(size==2)) {	//Command and total numbers of parameters required
				//Create process command
				//System.out.println("Create Process"); //For Debug
				try{
					createProcess(memStrat,Integer.parseInt(params[1])); 
				}catch (NumberFormatException ex) {
					System.out.println("Incorrect parameter type! Must be integers!");
					continue;
				}
				continue;
			}
			if((comm.equals("dl"))&&(size==2)) {
				//Delete process command
				System.out.println("Delete Process"); //For Debug
				try{
					deleteProcess(Integer.parseInt(params[1])); 
				}catch (NumberFormatException ex) {
					System.out.println("Incorrect parameter type! Must be integers!");
					continue;
				}
				continue;
			}
			if((comm.equals("cv"))&&(size==3)) {
				//Convert address command
				//System.out.println("Convert Address"); //For Debug
				try{
					convertAddress(Integer.parseInt(params[1]), Integer.parseInt(params[2])); 
				}catch (NumberFormatException ex) {
					System.out.println("Incorrect parameter type! Must be integers!");
					continue;
				}
				continue;
			}
			if((comm.equals("pm"))&&(size==1)) {
				//Print Memory Map
				//System.out.println("Print Map"); //For Debug
				printMemoryMap();
				continue;
			}
			if((comm.equals("pt"))&&(size==1)) {
				//Print process table
				//System.out.println("Print Table"); //For Debug
				printProcessTable();
				continue;
			}
			System.out.println("Invalid command, incorrect parameters!");	//If invalid command or invalid param amount
			continue;
		}
	}
}

class Process{
	//Variables
	private int pID;
	private Date dateTime;	//Stores the date and time of creation, use new Date() to assign value
	private int base;
	private int limit;
	
	//Functions
	public Process(int pID, Date dateTime, int base, int limit) {
		//Constructor
		this.pID = pID;
		this.dateTime = dateTime;
		this.base = base;
		this.limit = limit;
	}
	
	public int getpID() {
		return pID;
	}

	public void setpID(int pID) {
		this.pID = pID;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String toString() {
		//Print the object
		String s="";
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	    String[] splitList = formatter.format(dateTime).split(" ");
		s=String.format("%10d %15s %13s %15d %15d", pID, splitList[0], splitList[1], base, limit);
		return s;
	}
}

class memNode {
	
	//Variables
	boolean type;
	int base;
	int limit;
	
	//Functions
	public memNode(boolean type, int base, int limit) {
		this.type = type;
		this.base = base;
		this.limit = limit;
	}


	public boolean isType() {
		return type;
	}
	public void setType(boolean type) {
		this.type = type;
	}
	public int getBase() {
		return base;
	}
	public void setBase(int base) {
		this.base = base;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int toInt() {
		return base;
	}
}


class MemoryList{
	static int nextFitPtr=0;
	
	public class Node { 
		//Inner class Node
		public memNode element;
		public Node next;
		public Node(memNode e, Node n) {
			element =e;
			next=n;
		}
		public memNode getElement() { return element;}
		public Node getNext() { return next;}
		public void setNext(Node n) { next=n;}

	} //End of inner class Node   
	
	private Node head = null;	//For beginning of list
	private Node nxp = null;	//Pointer for next fit
	private int size=0;	//Size of the list
	
	public MemoryList(){} //default constructor
	public int size() { 
		return size;	//Return size of the list
	}
	
	public boolean isEmpty() {
		return size==0;	//Check if list is empty
		}
	
	public Node getHead() {
		return head;	//Return pointer to first node of the list
	}
	
	public void  addItem( memNode  item )  {
		Node  newNode  = new  Node(item,null) ;
		Node prev =null;
		Node  loc= head;
		if(head!=null) {
			//Searching for element
			while ( true)  {
				if( item.base > loc.getElement().base) {
					//Since list is sorted, move to next node if requested base is larger
					prev=loc;
					loc=loc.next;   
					if(loc==null) break;
				} else {
					break;
				}
			}
		}
		//Prepare for insertion
		if  ( prev == null ) {     
			//Insert as first
			newNode.next = head;
			head=newNode;
		} else {          
			//Insert in middle
			newNode.next = loc;
			prev.next=newNode;
		}
		size++;
	}
	
	public void printList() {
		//Print the entire memoryList
		Node loc=head;
		String s="", t;
		while(loc!=null) {
			t="";
			if(loc.element.type)	//Choose if process or hole
				t+="P";
			else
				t+="H";
			s=String.format("%6s %15d %15d", t, loc.element.base, loc.element.limit );	//Print node
			System.out.println(s);
			loc=loc.next;
		}
	}
	      
	void deleteProcess(int base) {
		//Required base will always be found if this function will be found 
		Node cur=head, pre=null, post;
		while(true) {
			//Finding the required node
			if(cur.element.base==base)
				break;	//Required element always will be found
			else {
				pre=cur;
				cur=cur.next;
			}		
		}
		if(cur==head) {
			//If the first node is the required element
			post=cur.next;
			if (post.element.type) {
				//Check if the next element is process
				cur.element.type = false; //Set first element to hole
			}
			else {
				//If next node is a hole then combine
				int nlimit=cur.element.limit+ post.element.limit;
				head = head.next; //Move head to next node
				head.element.base = 0;	//Set next node base to 0
				head.element.limit = nlimit;	//Set limit to combined limit
				size--; //Since two holes become one
			}	
		}
		else if(cur.next==null) {
			//This means process is last node, therefore check if the previous node is a process or hole
			if(pre.element.type) {
				//If previous element is a process
				cur.element.type = false;	//Set first element to hole
			}
			else {
				//If previous element is hole then we need to combine
				int nlimit=pre.element.limit+ cur.element.limit;
				pre.next = null; //Remove last element
				pre.element.limit = nlimit;	//Set limit to combined limit
				size--; //Since two holes become one
			}
		}
		else {
			//If not first or last element, it has to be middle element
			post = cur.next;
			if((pre.element.type)&&(post.element.type)) {
				//If surrounded by processes
				cur.element.type = false;
			}
			else if((!pre.element.type)&&(post.element.type)) {
				//If only previous element is hole
				int nlimit=pre.element.limit+ cur.element.limit;
				pre.next = post; //Remove cur element
				pre.element.limit = nlimit;	//Set limit to combined limit
				size--; //Since two holes become one
			}
			else if((pre.element.type)&&(!post.element.type)) {
				//If next node is a hole then combine
				int nlimit=cur.element.limit+ post.element.limit;
				post.element.base = cur.element.base;
				pre.next = post;	//Deleting element
				post.element.limit = nlimit;
				size--; //Since two holes become one
			}
			else {
				//If both surrounding nodes are holes
				int  nlimit = pre.element.limit+cur.element.limit+post.element.limit;
				pre.element.limit = nlimit;
				pre.next=post.next; //Post needs to be removed too
				size-=2;	//Decrement size by two, combining three holes into 1
			}
		}
	}

	void firstFit(memNode m) {
		boolean fit=false;//Boolean to check if we found a fit yet
		Node loc= getHead();	//Get pointer to first node
		memNode x=loc.getElement();	//Get first node
		while(!fit) {
			//Iterating through all linked list
			if(x.limit>=m.limit&&!fit&&!x.type) {//If type is hole and it  fits, we add
				m.base=x.base;	//Set base so it can be passed
				if(x.limit>m.limit) { //Adding a hole if process is smaller than original hole
					memNode hole= new memNode(false,x.base+m.limit,x.limit-m.limit);	//Creating the hole
					addItem(hole);	//Adding the new hole
				}
				x.type=true;	//Changing the original hole to process
				x.limit=m.limit;	//Changing the limit to process limit
				fit=true;	//fit has been found
			}

			loc=loc.next;
			if(loc==null) {
				break;
			}
			x=loc.getElement();
		}
		if(!fit) {
			System.out.println("ERROR: No space to create process");	//If we cannot add, print error
			m.type = false;
		}
	}
	
	void nextFit(memNode m) {
		boolean fit=false;//Boolean to check if we found a fit yet
		Node loc=null;
		if(nxp==null)	
			loc = getHead();	//If nxp has not been assigned yet
		else
			loc = nxp;	//If nxp has been assigned
		memNode x=loc.getElement();
		while(!fit) {//iterating through all linked list
			if(x.limit>=m.limit&&!fit&&!x.type) {//If type is hole and it  fits, we add
				m.base=x.base;	//Set base so it can be passed
				if(x.limit>m.limit) {	//Adding a hole if process is smaller than original hole
					memNode hole= new memNode(false, x.base+m.limit, x.limit-m.limit);	//Creating the hole
					addItem(hole);	//Adding the new hole
				}
				x.type=true;	//Changing the original hole to process
				x.limit=m.limit;	//Changing the limit to process limit
				if(loc.next==null) {
					nxp=getHead();
					//System.out.println("loc head"+nxp); //Debug
				}
				else {
					nxp= loc.next; //Getting  next pointer
					//System.out.println("loc next"+nxp); //Debug
				}
				//Setting the next pointer for next fit
				Node start = nxp;
				do {
					if(nxp.element.type)
						if(nxp.next==null) {
							nxp=getHead();
							//System.out.println("nxp head"+nxp); //Debug
						}		
						else {
							nxp= nxp.next; //Getting  next pointer
							//System.out.println("nxp next"+nxp); //Debug
						}
							
					else {
						//System.out.println("Obtained Nxp"); //Debug
						break;
					}
				} while(nxp!=start);
				
				fit=true;	//fit has been found
			}
			loc=loc.next;
			//For circular looping
			if(loc==null) {
				loc=getHead();
			}
			if(loc==nxp) {
				break;
			}
			x=loc.getElement();	//Load the next node for comparison
		}
		if(!fit) {
			System.out.println("ERROR: No space to create process");	//If we cannot add, print error
			m.type = false;	//Set type to hole, so we know process wasn't added
		}
	}
	
	void bestFit(memNode m) {
		boolean fit=false;//Boolean to check if we found a fit yet
		Node loc= getHead();
		memNode x=loc.getElement();
		int n=0,best=0, bestIndex=0;
		memNode bestBase=null;
		while(!fit) {//iterating through all linked list
			if(x.type) {
				n++;
			}
			else if(x.limit>=m.limit&&!x.type&&(x.limit<best||best==0)) {//If type is hole and it  fits, we add
				bestIndex=n; //saving best index
				best=x.limit;
				bestBase=x;
				n++;
				fit=true;
				m.base=x.base;
			}
			else {
				n++;
			}

			loc=loc.next;
			if(loc==null) {
				break;
			}
			x=loc.getElement();
		}
		if(fit) {
			m.base=bestBase.base;
			if(bestBase.limit>m.limit) {
				memNode hole= new memNode(false, bestBase.base+m.limit, bestBase.limit-m.limit);
				addItem(hole);
			}
			bestBase.type=true;
			bestBase.limit=m.limit;

		}
		else {
			System.out.println("ERROR: No space to create process");	//If we cannot add, print error
			m.type = false;	//Set type to hole, so we know process wasn't added
		}
			

	}

	void worstFit(memNode m) {
		boolean fit=false;//Boolean to check if we found a fit yet
		Node loc= getHead();
		memNode x=loc.getElement();
		int n=0,largest=0, largestIndex=0;
		memNode worst=null;
		while(!fit) {//iterating through all linked list
			if(x.type) {
				n++;
			}
			else if(x.limit>=m.limit&&!x.type&&x.limit>largest) {//If type is hole and it  fits, we add
				largestIndex=n;//saving best index
				largest=x.limit;
				worst=x;
				n++;
				fit=true;
				m.base=x.base;
			}
			else {
				n++;
			}

			loc=loc.next;
			if(loc==null) {
				break;
			}
			x=loc.getElement();
		}
		if(fit) {
			m.base=worst.base;
			if(worst.limit>m.limit) {
				memNode hole= new memNode(false, worst.base+m.limit, worst.limit-m.limit);
				addItem(hole);
			}
			worst.type=true;
			worst.limit=m.limit;

		}
		else {
			System.out.println("ERROR: No space to create process");	//If we cannot add, print error
			m.type = false;	//Set type to hole, so we know process wasn't added
		}
	}
}
