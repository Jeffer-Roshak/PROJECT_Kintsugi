//package myPackage;
//
//public class SortedLinkedList<E> {
//	   private static class Node<E> { //inner class Node
//	   	private E element;
//	   	private Node<E> next;
//	   	public Node(E e, Node<E> n) {
//	   		element=e;
//	   		next=n;
//	   	}
//	   	public E getElement() { return element;}
//	   	public Node<E> getNext() { return next;}
//	   	public void setNext(Node<E> n) { next=n;}
//	   } //end of inner class Node   
//	   private Node<E> head = null;
//	   //private Node<E> tail = null; // we will ignore the tail here
//	   private int size=0;
//	   public SortedLinkedList(){} //default constructor
//	   public int size() { return size;}
//	   public boolean isEmpty(){return size==0;}
//	   public void  addItem( E  item )  {
//	      Node <E> newNode  = new  Node<E>(item,null) ;
//	      Node <E> prev =null;
//	      Node <E> loc= head;
//	      if(head!=null) {
//	         while ( true)  {
//	            if( (Integer)item > (Integer)loc.getElement()) {
//	               prev=loc;
//	               loc=loc.next;   
//	               if(loc==null) break;
//	            } else {
//	               break;
//	            }
//	         }
//	      }
//	      // prepare for insertion
//	      if  ( prev == null ) {     //insert as first
//	         newNode.next = head;
//	         head=newNode;
//	      } else {          //insert in middle
//	         newNode.next = loc;
//	         prev.next=newNode;
//	      }
//	      size++;
//	   }
//	   public void deleteItem ( E  item ) {
//	      /*Node <E> temp, loc=head;
//	      if(item==head.element) {          
//	         temp= loc;     // If so, delete first node   
//	         head= head.next;
//	      } else {       // search for item in rest of list
//	         while ( item!=loc.next.element)
//	            loc= loc.next;
//	         temp= loc.next; // node will be deleted at loc.next
//	         loc.next= (loc.next).next;
//	      }
//	      size--;*/
//		  Node <E> cur = head;
//		  Node <E> prev = null;
//		  while(cur!=null) {
//			  if(item==cur.getElement()) {
//				  size--;
//				  if(size==0) {
//					  head=null;
//				  }
//				  else {
//					  prev.next=cur.next;
//				  }
//				  break;
//			  }
//			  else {
//				  prev=cur;
//				  cur=cur.next;
//			  }
//		  }
//	   }
//	   public boolean getItem( E item)  {        
//	      Node<E> loc=head;
//	      boolean found=false; 
//	      System.out.print("Checking items: ");
//	      while(true) {
//	    	 System.out.print(loc.element+" ");
//	         if(item==loc.element) {
//	             found=true; break;
//	         } else if((Integer)item>(Integer)loc.element) {
//	            loc = loc.next;
//	            if(loc==null) break; 
//	         } else   break; // item<loc.element
//	      }
//	      return found;
//	   }
//	   public String toString() { 
//	      Node<E> loc=head;
//	      String s="";
//	      while(loc!=null) {
//	         s+=loc.element+ " ";
//	         loc=loc.next;
//	      }
//	      return s;
//	   }
//	   public void mergeLists(SortedLinkedList<E> sll) {
//		   Node<E> slider= sll.head;	//Node object that pointing to the head of sll
//		   while(slider!=null) {	//Run loop till we reach NULL
//			   addItem(slider.getElement());	//Adding element of sll to encapsulated list
//			   slider=slider.next;		//Moving sll pointer to next element
//		   }
//	   }       
//	}
