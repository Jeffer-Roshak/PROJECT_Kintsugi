package myPackage;
import java.util.*;

class memNode {

	boolean type;
	int base;
	int limit;

	public memNode( int base, boolean type, int limit) {
		super();
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

@SuppressWarnings({ "serial","unused", "rawtypes"})
class SortedLinkedList extends LinkedList{
	static int nextFitPtr=0;
	public class Node { //inner class Node
		public memNode element;
		public Node next;
		public Node(memNode e, Node n) {
			element =e;
			next=n;
		}
		public memNode getElement() { return element;}
		public Node getNext() { return next;}
		public void setNext(Node n) { next=n;}

	} //end of inner class Node   
	private Node head = null;
	//private Node<E> tail = null; // we will ignore the tail here
	private int size=0;
	public SortedLinkedList(){} //default constructor
	public int size() { return size;}
	public boolean isEmpty(){return size==0;}
	public Node getHead() { return head;}
	public void  addItem( memNode  item )  {
		Node  newNode  = new  Node(item,null) ;
		Node prev =null;
		Node  loc= head;
		if(head!=null) {
			while ( true)  {
				if( item.base > loc.getElement().base) {
					prev=loc;
					loc=loc.next;   
					if(loc==null) break;
				} else {
					break;
				}
			}
		}
		// prepare for insertion
		if  ( prev == null ) {     //insert as first
			newNode.next = head;
			head=newNode;
		} else {          //insert in middle
			newNode.next = loc;
			prev.next=newNode;
		}
		size++;
	}
	public void deleteItem ( memNode  item ) {
		Node  temp, loc=head;
		if(item.base==head.element.base) {          
			temp= loc;     // If so, delete first node   
			head= head.next;
		} else {       // search for item in rest of list
			if(loc.next!=null) 
				while ( item.base!=loc.next.element.base) {
					if(loc.next!=null) 
						break;
					loc= loc.next;
				}

			temp= loc.next; // node will be deleted at loc.next
			loc.next= (loc.next).next;
		}
		size--;
	}
	public boolean getItem( memNode item)  {        
		Node loc=head;
		boolean found=false; 
		System.out.print("Checking items: ");
		while(true) {
			System.out.print(loc.element+" ");
			if(item==loc.element) {
				found=true; break;
			} else if(item.base>loc.element.base) {
				loc = loc.next;
				if(loc==null) break; 
			} else   break; // item<loc.element
		}
		return found;
	}
	public String toString() { 
		Node loc=head;
		String s="";
		while(loc!=null) {
			s+=loc.element+ " ";
			loc=loc.next;
		}
		return s;
	}
	public void mergeLists(SortedLinkedList sll) {
		Node slider= sll.head;	//Node object that pointing to the head of sll
		while(slider!=null) {	//Run loop till we reach NULL
			addItem(slider.getElement());	//Adding element of sll to encapsulated list
			slider=slider.next;		//Moving sll pointer to next element
		}
	}       


	static void firstFit(SortedLinkedList mem,memNode m) {//List and process are passed
		boolean fit=false;//Boolean to check if we found a fit yet
		Node loc=mem.getHead();
		memNode x=loc.getElement();
		while(true) {//iterating through all linked list
			if(x.type) {
			}
			else if(x.limit>=m.limit&&!fit&&!x.type) {// if type is 0 and it  fits we add
				m.base=x.base;
				if(x.limit>m.limit) {
					memNode hole= new memNode(x.base+m.limit,false,x.limit);
					mem.addItem(hole);
				}
				x.type=true;
				x.limit=m.limit;
				fit=true;
			}
			else {
			}

			loc=loc.next;
			if(loc==null) {
				break;
			}
			x=loc.getElement();
		}
		if(!fit) {
			System.out.println("No space");// if its never added we print no space
		}



	}

	static void nextFit(SortedLinkedList mem,memNode m) {

		boolean fit=false;//Boolean to check if we found a fit yet
		Node loc=mem.getHead();
		Node temp=mem.getHead();
		memNode x=loc.getElement();
		int nxp=nextFitPtr;//getting last position we were at 
		for(int i=0;i<nxp;i++) {
			loc=loc.next;//skipping positions already checked
			if(loc==null) {
				loc=temp;
			}
			x=loc.getElement();
		}
		Node start=loc;
		while(true) {//iterating through all linked list
			
			if(x.type) {
				nxp++;//adding a position to never come back in nextFit
			}
			else if(x.limit>=m.limit&&!fit) {// if type is 0 and it  fits we add
				m.base=x.base;
				if(x.limit>m.limit) {
					memNode hole= new memNode(x.base+m.limit,false,x.limit-m.limit);
					mem.addItem(hole);
				}
				x.type=true;
				x.limit=m.limit;
				fit=true;
				nxp++;
				break;
			}
			else {
				nxp++;
			}

			loc=loc.next;
			if(loc!=start&&loc==null) {
				loc=temp;
				x=temp.getElement();
				nxp=0;
			}
			else if(loc==start) {
				break;
			}
			x=loc.getElement();
		}
		if(!fit) {
			System.out.println("No space");// if its never added we print no space
		}
		nextFitPtr=nxp;
	}

	static void bestFit(SortedLinkedList mem,memNode m) {

		boolean fit=false;//Boolean to check if we found a fit yet
		Node loc=mem.getHead();
		memNode x=loc.getElement();
		int n=0,best=0,bestIndex=0;
		memNode bestBase=null;
		while(true) {//iterating through all linked list
			if(x.type) {
				n++;
			}
			else if(x.limit>=m.limit&&!x.type&&(x.limit<best||best==0)) {// if type is 0 and it  fits we add
				bestIndex=n;//saving best index
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
				memNode hole= new memNode(bestBase.base+m.limit,false,bestBase.limit-m.limit);
				mem.addItem(hole);
			}
			bestBase.type=true;
			bestBase.limit=m.limit;

		}
		else
			System.out.println("No space");// if its never added we print no space

	}

	static void worstFit(SortedLinkedList mem,memNode m) {
		boolean fit=false;//Boolean to check if we found a fit yet
		Node loc=mem.getHead();
		memNode x=loc.getElement();
		int n=0,largest=0,largestIndex=0;
		memNode worst=null;
		while(true) {//iterating through all linked list
			if(x.type) {
				n++;
			}
			else if(x.limit>=m.limit&&!x.type&&x.limit>largest) {// if type is 0 and it  fits we add
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
				memNode hole= new memNode(worst.base+m.limit,false,worst.limit-m.limit);
				mem.addItem(hole);
			}
			worst.type=true;
			worst.limit=m.limit;

		}
		else
			System.out.println("No space");// if its never added we print no space


	}

	static void print(SortedLinkedList mem) {
		Node start=mem.getHead();
		for(int i=0;i<mem.size;i++) {
			memNode getEl=start.getElement();
			System.out.println("Base: "+getEl.base+" Type: "+getEl.type+" Limit: "+getEl.limit);
			start=start.next;
		}
	}
}




public class Driver {


	public static void main(String[] args) {
		int nextFitPtr=0;//Pointer used to remember where we stopped last for next fit
		SortedLinkedList memory = new SortedLinkedList();//memory list

		memNode test1 = new memNode(41,true,10);
		memNode test2 = new memNode(1,true,20);
		memNode test3 = new memNode(21,true,10);		
		memNode test5 = new memNode(31,false,10);
		memNode test6 = new memNode(0,false,1);//Initial test list
		memNode test9 = new memNode(51,true,4);//Test input

		memNode test4 = new memNode(5,true,10);
		memNode test7 = new memNode(6,true,1);//Test input
		memNode test8 = new memNode(6,true,4);//Test input
		
		memory.addItem(test1);
		memory.addItem(test2);
		memory.addItem(test3);
		memory.addItem(test5);
		memory.addItem(test9);
		memory.addItem(test6);//Adding test list to memory


		//		SortedLinkedList.firstFit(memory,test4);
		//		System.out.println();
		//		SortedLinkedList.firstFit(memory,test7);


		SortedLinkedList.nextFit(memory,test4);
		SortedLinkedList.print(memory);	
		System.out.println();
		SortedLinkedList.nextFit(memory,test8);
		SortedLinkedList.print(memory);	
		System.out.println();
		SortedLinkedList.nextFit(memory,test7);
		SortedLinkedList.print(memory);	
		System.out.println();

//				SortedLinkedList.worstFit(memory,test4);
//				System.out.println();
//				SortedLinkedList.worstFit(memory,test7);

//				SortedLinkedList.bestFit(memory,test4);
//				SortedLinkedList.print(memory);	
//				System.out.println();
//				SortedLinkedList.bestFit(memory,test8);
//				SortedLinkedList.print(memory);	
//				System.out.println();
//				SortedLinkedList.bestFit(memory,test7);
//
//
//		SortedLinkedList.print(memory);	

	}
}
