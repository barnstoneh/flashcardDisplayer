import java.util.NoSuchElementException;

public class FlashcardPriorityQueue implements PriorityQueue<Flashcard>{
    
    Flashcard[] ourCards = new Flashcard[25];
    int numCards = 0;
         
    /**
    * Creates an empty priority queue.
    */
    public FlashcardPriorityQueue(){
    
    }
    
    /** 
    * Adds the given item to the queue. 
    */
    public void add(Flashcard item){
        if(numCards+1 == ourCards.length){
            resize();
        }
        //Ensure capacity remember num cards is always 1 fewer than total spaces
        numCards++;
        int curPos = numCards;
        while(curPos> 1 && ourCards[getParentPos(curPos)].compareTo(item)>0){
        //While not at the root and the current card is smaller than the parent, swap them.
            ourCards[curPos] = ourCards[getParentPos(curPos)];
            curPos = getParentPos(curPos);
        }
        ourCards[curPos] = item;//Put item in spot identified above.
    }
    
    /**
    * This method resizes an array by doubling it and preserving the order that the items
    * are in as it transfers them to the new array.
    */
    public void resize(){
        Flashcard[] tempCards = new Flashcard[ourCards.length*2]; 
        for(int i = 0; i< ourCards.length; i++){
            tempCards[i]=ourCards[i];
        }
        ourCards = tempCards;
    }
    
    /** 
    * Removes the first item according to compareTo from the queue, and returns it.
    * Throws a NoSuchElementException if the queue is empty.
    */
    public Flashcard poll(){
        Flashcard itemToReturn = peek();//Saves the the item to return.
        
        Flashcard lastCard = ourCards[numCards];
        int curPos = 1;
        while((curPos*3)-1<numCards && lastCard.compareTo(ourCards[getSmallestChild(curPos)])> 0){
        // While parent has a child and the smallest child is greater than its parent, swap them.
               ourCards[curPos] =  ourCards[getSmallestChild(curPos)];
               curPos = getSmallestChild(curPos);   
        }
        
        ourCards[curPos] =  lastCard;// puts bottom right most child in position identified above.
        numCards--; // decreases numCards by 1.
        return itemToReturn;
    }
    
    /**
    * Takes in the index of a flash card and returns the index of the smallest child.
    */
    public int getSmallestChild(int index){
        
        int childIndex = (3*index)-1;// this equations gets the left most child in a ternary tree.
        int indexToCompare = childIndex;
        
        if(childIndex+1> numCards){// if there isn't a middle or right child, return left
            return childIndex;
        // Otherwise compare to middle child
        }else if(ourCards[childIndex].compareTo(ourCards[childIndex+1])>0){ 
        //If left child is greater than middle child.
            if( childIndex+2<= numCards && ourCards[childIndex+1].compareTo(ourCards[childIndex+2])>0){
            //If there is a right child and it is smaller than the left child return the right child's index.
                return childIndex+2;
            }else{//Otherwise return middle.
                return childIndex+1;
            }
        }else if(childIndex+2> numCards){//If there is not right child, return left child.
            return childIndex;
        }else if (ourCards[childIndex].compareTo(ourCards[childIndex+2])>0){
        //If right child exist and is smaller than left, return right child
            return childIndex+2;
        }
        return childIndex;
    }
    
    /** 
    * Returns the first item according to compareTo in the queue, without removing it.
    * Throws a NoSuchElementException if the queue is empty.
    */
    public Flashcard peek(){
        if(this.isEmpty()){
            throw new NoSuchElementException();
        }else{
            return ourCards[1];
        }
    }
    
    /**
    * Returns true if the queue is empty. 
    */
    public boolean isEmpty(){
        return(numCards==0);
    }
    
    /** 
    * Removes all items from the queue. It does this by setting numCards equal to 0.
    * We are able to implement clear this way because this a priority queue and thus, only
    * polls from the front.
    */
    public void clear(){        
        numCards = 0;
    }
    
    /**
    * This method returns the parent index of any given index.
    */
    public int getParentPos(int index){
        if(index%3==2){ //if leftmost child, add one to make it round up to correct parent
            index++;
        }
        return index/3;
    }
    
    /**
    * Our main test the above code.
    */
    public static void main(String[] args) {
        FlashcardPriorityQueue testQueue = new FlashcardPriorityQueue();
        
        System.out.println("The following checks if empty and should print true: " + testQueue.isEmpty()); 
        
        //testQueue.poll();// should throw NoSuchElementException
        Flashcard Test1 = new Flashcard("2016-11-02T01:03","Beijing","China");
        Flashcard Test2 = new Flashcard("2016-11-02T01:03","Hannah","Melissa");
        Flashcard Test3 = new Flashcard("2000-11-02T01:03","Lucky","Cosmo");
        Flashcard Test4 = new Flashcard("2016-11-06T01:03","Dog","Cat");
        Flashcard Test5 = new Flashcard("1901-09-02T01:03","Hello","Cool");
        Flashcard Test6 = new Flashcard("1951-07-22T01:03","testing","again");
        
        //test adding items
        testQueue.add(Test1);
        System.out.println("The following should print Beijing, it actually prints: " +testQueue.peek().getFrontText());
        testQueue.add(Test2);
        // added additional item, however will stil print Beijing because its due date is 
        //less than the added card's due date
        
        System.out.println("The following should print Beijing, it actually prints: " + testQueue.peek().getFrontText());
        testQueue.add(Test3);
        System.out.println("The following should print Lucky, it actually prints: " + testQueue.peek().getFrontText());
        testQueue.add(Test4);
        // added additional item, however will stil print Lucky because its due date is 
        //less than the added card's due date
        System.out.println("The following should print Lucky, it actually prints: " + testQueue.peek().getFrontText());
        testQueue.add(Test5);
        System.out.println("The following should print Hello, it actually prints: " + testQueue.peek().getFrontText());
        
        //test poll method
        System.out.println("Should print Hello, actually prints: " + testQueue.poll().getFrontText());
        System.out.println("Should print Lucky, actually prints: " +testQueue.peek().getFrontText());
        
        //test resize
        testQueue.add(Test5);
        System.out.println("Should print Hello, actually prints: " +testQueue.peek().getFrontText());
        testQueue.add(Test6); //should have to resize here if array size set to 6 
                              // we made it 6 temporarily to fill it faster.
        
        //poll each item to check that they come out in expected priority order
        System.out.println("We are now going to pull all the items in the queue." +
        "\nThey should print out: Hello, testing, Lucky, Hannah, Beijing, Dog");
        for(int i = 0; i <6; i++){
            System.out.println("Item polled: " + testQueue.poll().getFrontText());
        }
        
        //test clear method
        testQueue.clear();
        System.out.println("Should print true, actually prints: " + testQueue.isEmpty());
        testQueue.add(Test1);
        System.out.println("Should print Beijing, actually prints: " + testQueue.peek().getFrontText());



    }
}