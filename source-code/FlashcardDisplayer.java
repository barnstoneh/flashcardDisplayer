import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;

/**
* This class is how the user interfaces with their flashcards. It can load a text file
* and read it into flash cards. It can quiz the user on these flashcards and save the
* updated versions (i.e. with new due dates) to a text file.
*
* @author Hannah Barnstone
* @author Melissa Bain
*/

public class FlashcardDisplayer{

    //instance variable to store flashcards
    FlashcardPriorityQueue cardQueue = new FlashcardPriorityQueue();
    
    
    /**
     * Creates a flashcard displayer with the flashcards in file.
     * File has one flashcard per line. On each line, the date the flashcard 
     * should next be shown is first (format: YYYY-MM-DDTHH-MM), followed by a tab, 
     * followed by the text for the front of the flashcard, followed by another tab.
     * followed by the text for the back of the flashcard. You can assume that the 
     * front/back text does not itself contain tabs. (I.e., a properly formatted file
     * has exactly 2 tabs per line.)
     * In the format above, the time may be omitted, or the time may be more precise
     * (e.g., seconds may be included). The parse method in LocalDateTime can deal
     * with these situations without any changes to your code.
     */
    public FlashcardDisplayer(String file){
        Scanner cardScanner = null;
        File cardFile = new File(file);
        try{
            cardScanner = new Scanner(cardFile);
        }catch(FileNotFoundException e){//prints a helpful message when the file is not found
            System.out.println("Your file does not seem to exist");
            System.exit(1);
        }
        String curString;
        
        //scan and parse each line of file
        while (cardScanner.hasNextLine()){
            String[] info = null;
            curString = cardScanner.nextLine();
            info= curString.split("\\s+");
            //use info to make a new Flashcard object and add this to cardQueue
            cardQueue.add(new Flashcard(info[0],info[1],info[2]));
        }
    }
 
    /**
     * Writes out all flashcards to a file so that they can be loaded
     * by the FlashcardDisplayer(String file) constructor. Returns true
     * if the file could be written. The FlashcardDisplayer should still
     * have all of the same flashcards after this method is called as it
     * did before the method was called. However, it may be that flashcards
     * with the same exact same next display date are removed in a different order.
     */
    public boolean saveFlashcards(String outFile){
        //make temp queue to save current cardQueue even as we poll flashcards to 
        //write them to a file
        FlashcardPriorityQueue tempQueue = new FlashcardPriorityQueue(); 
        
        try{
            PrintWriter writer = new PrintWriter(outFile, "UTF-8"); //file writer
            while(!cardQueue.isEmpty()){
                Flashcard curCard = cardQueue.poll();
                //write with correct tab syntax so it can be read again
                writer.println(curCard.getDueDate() + "\t" + curCard.getFrontText() + "\t" +
                    curCard.getBackText());
                tempQueue.add(curCard); //save polled card to temp queue
            }
            writer.close();
            cardQueue = tempQueue; //set cardQueue back to normal using tempQueue
            return true;
        }catch(Exception e){
            System.out.println("The program failed to save your file correctly");
            return false;
        }
    }
 
    /**
     * Displays any flashcards that are currently due to the user, and 
     * asks them to report whether they got each card correct. If the
     * card was correct, it is added back to the deck of cards with a new
     * due date that is one day later than the current date and time; if
     * the card was incorrect, it is added back to the card with a new due
     * date that is one minute later than that the current date and time.
     */
    public void displayFlashcards(){
        Flashcard curCard = cardQueue.peek();
        Scanner commandScanner = new Scanner(System.in);//be able to get user's input
        
        //go through each card in queue that is due
        while(curCard.due()){
            curCard = cardQueue.poll();
            System.out.println(curCard.getFrontText());
            System.out.println("[Press return for back of card]");
            
            String input = "go";//keep getting input until user successfully presses return
            while(!input.equals("")){
                input = commandScanner.nextLine();
            }
            //show answer to flashcard
            System.out.println(curCard.getBackText());
            
            //keep getting input until user answers with 1 or 2
            int inputInt = 0;
            while(inputInt!=1 && inputInt!=2){
                System.out.println("Press 1 if you got the card correct and 2 if you got the card incorrect.");
                try{
                    inputInt = Integer.parseInt(commandScanner.nextLine());
                }catch(NumberFormatException e){
                    System.out.println("Please enter a valid int");
                } 
            }
            
            //if correct, call addDay method, otherwise only add a minute until due again
            if (inputInt==1){
                curCard.addDay();
            }else{
                curCard.addMinute();
            }
            cardQueue.add(curCard);//put card back in queue
            curCard = cardQueue.peek();
        }
            
    }
    
    /**
    * The main provides a basic user interface, based on command line prompts and responses.
    * It takes in a text file when the program is initially run. It will continue to allow
    * the user to quiz herself and save the resulting flashcards until she decides to exit.
    */
    public static void main(String[] args){
        //if user fails to enter a file when first running this program print warning
        if(args.length==0){
            System.out.println("Please provide a flashcard text file");  
        }else{
            FlashcardDisplayer ourDisplayer = new FlashcardDisplayer(args[0]);
            System.out.println("Time to practice flashcards! The computer will display your flashcards,");
            System.out.println("you generate the response in your head, and then see if you got it right.");
            System.out.println("The computer will show you cards that you miss more often than those you know!");
            System.out.println("The command options are: quiz, save and exit");
            
            Scanner commandScanner = new Scanner(System.in);//be able to get user input
            String command = "continue";//initialize command as something other than "exit"
            String saveFileName; //declare variable to store file name for saving file
            
            //keep asking for command until user wants to exit the program
            while(!command.equals("exit")){
                System.out.println("Enter a command:");
                command = commandScanner.next();
                
                //if user wants to quiz herself, call displayFlashcard method
                if(command.equals("quiz")){
                    ourDisplayer.displayFlashcards();
                }
                
                //if user wants to save cards, call saveFlashcards method
                else if (command.equals("save")){
                    System.out.println("Please enter a file name to save your file as");
                    //get file name
                    saveFileName = commandScanner.next();
                    ourDisplayer.saveFlashcards(saveFileName);
                }
            }
            //salute the user for a good study sesh. 
            System.out.println("Goodbye!"); 
        }
    }
}