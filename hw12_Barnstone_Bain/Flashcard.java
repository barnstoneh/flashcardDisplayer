import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
*The flashcard class creates flashcard objects that store front and back text
* along with a due date where the flashcard should be mastered by this time. 
* Flashcards can be compared based on their due date.
*
* @author Hannah Barnstone
* @author Melissa Bain
*/

public class  Flashcard implements Comparable<Flashcard> {

    //instance variables
    String dueDate; //record the due date that the flashcard must be mastered by
    String front; //the text on the front of the flashcard
    String back; //text on the back of the flashcard
    LocalDateTime dueDateLDT; //due date stored as a LocalDateTime object

    /**
     * Creates a new flashcard with the given dueDate, text for the front
     * of the card (front), and text for the back of the card (back).
     * dueDate must be in the format YYYY-MM-DDTHH-MM. For example,
     * 2016-11-04T13:03 represents 1:03PM on November 4, 2016. It's
     * okay if this method crashes if the date format is incorrect.
     * In the format above, the time may be omitted, or the time may be 
     * more precise (e.g., seconds or milliseconds may be included). 
     * The parse method in LocalDateTime can deal with these situations
     *  without any changes to your code.
     */
    public Flashcard(String dueDate, String front, String back){
        this.dueDate = dueDate;
        this.front = front;
        this.back = back;
        dueDateLDT = LocalDateTime.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
 
    /**
     * Gets the text for the front of this flashcard.
     */
    public String getFrontText(){
        return front;
    }
 
    /**
     * Gets the text for the Back of this flashcard.
     */
    public String getBackText(){
        return back;
    }
 
    /**
     * Gets the time when this flashcard is next due.
     */
    public LocalDateTime getDueDate(){
        return dueDateLDT;
    }
    
    /**
    * Returns -1 if "otherCard" is greater than the given card, 0 if they're equal
    * and 1 if "otherCard is less than the given card. These comparisons are based
    * on their due date.
    */
    public int compareTo(Flashcard otherCard){
        return this.getDueDate().compareTo(otherCard.getDueDate());
    }
    
    /**
    * Returns true if the due date is before now (i.e. due). Otherwise returns false.
    */
    public boolean due(){
        if(dueDateLDT.compareTo(LocalDateTime.now())<0){
            return true;
        }
        return false;
    }
    
    /**
    * Changes the due date to a day from right now.
    */
    public void addDay(){
        dueDateLDT = LocalDateTime.now().plusDays(1);
    } 
    
    /**
    * Changes the due date to a minute from right now.
    */
    public void addMinute(){
        dueDateLDT= LocalDateTime.now().plusMinutes(1);
    } 
    
    /**
    * We used this main for our own test purposes to make sure get due date and compareto
    * methods worked.
    */
    public static void main(String[] args){
        Flashcard testCard = new Flashcard("2016-11-01T01:03", "Hannah", "Melissa");
        System.out.println(testCard.getDueDate());
        Flashcard testCard2 = new Flashcard("2016-01-01T01:03", "Hannah", "Melissa");
        System.out.println(testCard.compareTo(testCard2));

    }
}