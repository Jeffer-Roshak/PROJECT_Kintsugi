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
	public int nextFitPtr=0;
	public static void main(String[] args) {	
		LinkedList<Process> memory = new LinkedList<Process>();
		LinkedList<Process> processTable = new LinkedList<Process>();
		Process test = new Process(1000,true,0,10);
		memory.add(test);
		processTable.add(test);
	}
}
