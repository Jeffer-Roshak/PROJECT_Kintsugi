package myPackage;
import java.util.*;


class Process {

	int pid;
	boolean type;
	int base;
	int limit;

	public Process(int pid, boolean type, int base, int limit) {
		super();
		this.pid = pid;
		this.type = type;
		this.base = base;
		this.limit = limit;
	}

	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
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

}

public class Driver {
	
	
	
	static void firstFit(LinkedList<Process> mem,Process m) {//List and process are passed
		LinkedList<Process> S = new LinkedList<Process>();
		S=mem;
		boolean fit=false;//Boolean to check if we found a fit yet
		for (Iterator<Process> i = S.iterator(); i.hasNext();) {//iterating through all linked list
			Process x=i.next();
			if(x.type) {
				System.out.println("Process");//if type is 1 its a process
			}
			else if(x.limit>=m.limit&&!fit) {// if type is 0 and it  fits we add
				System.out.println("Added");
				x.type=true;
				fit=true;
			}
			else {
				System.out.println("Hole");//otherwise it is a hole we dont add in
			}
	      }
		if(!fit) {
			System.out.println("No space");// if its never added we print no space
		}
		
		
	}
	
	static int nextFit(LinkedList<Process> mem,Process m,int nextFitPtr) {
		LinkedList<Process> S = new LinkedList<Process>();
		S=mem;
		boolean fit=false;
		int nxp=nextFitPtr;//getting last position we were at 
		boolean itr=false;
		for (Iterator<Process> i = S.iterator(); i.hasNext();) {//iterating through all linked list
			if(!itr) {
				for(int x=0;x<nxp;x++) {
					i.next();//skipping positions already checked
				}
				itr=true;
			}
			Process x=i.next();
			if(x.type) {//same as best fit after this
				System.out.println("Process");
				nxp++;//adding a position to never come back in nextFit
			}
			else if(x.limit>=m.limit&&!fit) {
				System.out.println("Added");
				x.type=true;
				nxp++;
				fit=true;
				break;
			}
			else {
				System.out.println("Hole");
				nxp++;
			}
	      }
		if(!fit) {
			System.out.println("No space");// if its never added we print no space
		}
		return nxp;
	}
	
	static void bestFit(LinkedList<Process> mem,Process m) {
		LinkedList<Process> S = new LinkedList<Process>();
		S=mem;
		int best=0;
		int bestIndex=0;
		int n=0;//current index
		boolean fit=false;
		for (Iterator<Process> i = S.iterator(); i.hasNext();) {//iterating through all linked list to find best fit
			Process x=i.next();
			if(x.type) {
				n++;
			}
			else if(x.limit>=m.limit&&(x.limit<best||best==0)) {//if a hole is larger than file and is smaller than best we make this index as best, if best is 0 we add
				bestIndex=n;//saving best index
				best=x.limit;
				n++;
				
			}
			else {
				n++;
			}
		}
		
		for (Iterator<Process> i = S.iterator(); i.hasNext();) {//iterating through all linked list to add element
			Process x=i.next();
			if(bestIndex==0&&x.limit>=m.limit&&!fit) {//if we reach best  index and it fits we add
				System.out.println("Added");
				x.type=true;
				fit=true;
				bestIndex--;//subtracting so we can reach 0 after we reach bestfit position
			}
			else if(x.type) {
				System.out.println("Process");
				bestIndex--;//subtracting so we can reach 0 after we reach bestfit position
			}
			else {
				System.out.println("Hole");
				bestIndex--;//subtracting so we can reach 0 after we reach bestfit position
			}
			
			
		}
		if(!fit) {
			System.out.println("No space");// if its never added we print no space
		}
	}
	
	static void worstFit(LinkedList<Process> mem,Process m) {
		LinkedList<Process> S = new LinkedList<Process>();
		S=mem;
		int largest=0;
		int largestIndex=0;
		int n=0;//current index
		boolean fit=false;
		for (Iterator<Process> i = S.iterator(); i.hasNext();) {//iterating through all linked list to find biggest space that fits
			Process x=i.next();
			if(x.type) {
				n++;
			}
			else if(x.limit>=m.limit&&x.limit>largest) {//if we reach a hole larger than item we need to add and larger than largest hole found so far
				largestIndex=n;//saving the largest index
				largest=x.limit;
				n++;
				
			}
			else {
				n++;
			}
		}
		
		for (Iterator<Process> i = S.iterator(); i.hasNext();) {//iterating through all linked list to  add element in worst fit
			Process x=i.next();
			if(largestIndex==0&&x.limit>=m.limit&&!fit) {//if we iterated till the biggest we check if it fits
				System.out.println("Added");
				x.type=true;
				fit=true;
				largestIndex--;//subtracting so we can reach 0 after we reach worstfit position
			}
			else if(x.type) {
				System.out.println("Process");
				largestIndex--;//subtracting so we can reach 0 after we reach worstfit position
			}
			else {
				System.out.println("Hole");
				largestIndex--;//subtracting so we can reach 0 after we reach worstfit position
			}
			
			
		}
		if(!fit) {
			System.out.println("No space");// if its never added we print no space
		}
	}
	public static void main(String[] args) {
		int nextFitPtr=0;//Pointer used to remember where we stopped last for next fit
		LinkedList<Process> memory = new LinkedList<Process>();//memory list
		LinkedList<Process> processTable = new LinkedList<Process>();
		Process test1 = new Process(1000,true,0,10);
		Process test2 = new Process(1001,false,0,20);
		Process test3 = new Process(1002,true,0,10);		
		Process test5 = new Process(1001,false,0,10);
		Process test6 = new Process(1001,false,0,3);//Initial test list
		
		
		Process test4 = new Process(1002,true,0,10);
		Process test7 = new Process(1002,true,0,3);//Test input
		memory.add(test1);
		memory.add(test2);
		memory.add(test3);
		memory.add(test5);
		memory.add(test6);//Adding test list to memory
		//firstFit(memory,test4);
//		nextFitPtr=nextFit(memory,test4,nextFitPtr);
//		System.out.println(nextFitPtr);
//		nextFitPtr=nextFit(memory,test7,nextFitPtr);
//		System.out.println(nextFitPtr);
		
//		worstFit(memory,test4);
//		System.out.println();
//		worstFit(memory,test7);
		
		bestFit(memory,test4);
		System.out.println();
		bestFit(memory,test7);
		
		
		
	}
}
