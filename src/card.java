public class card{
//Original Creator was MAJK
//I (KYLE) implemented this file into my Black Jack game
//Changes have been marked with comments starting with KYLE
//create initial variables
   String suit;
   String value;
   int deck;


   //constructor for a blank card
   public card(){
     //suit of card to default initial value
      suit="";
      
      //value of card to default initial value
      value="";
      
      //what deck did it originate from
      deck=0;
   
   }

   
   //return the card's suit
   public String getSuit(){
      return this.suit;
   }

   
   //return the card's suit
   public String getValue(){
      return this.value;
   }
   
   //return the card's suit
   public int getDeck(){
      return this.deck;
   }
   
   //set card information for creating a new card
   public void setCard(String mySuit, String myValue, int myDeck){
   
      //set values to those passed in
      this.suit = mySuit;
      this.value = myValue;
      this.deck = myDeck;
   
   }
   
   //output card info
   public void printCard(){
       System.out.println(this.value + " of " + this.suit + " in deck " + this.deck);
   
   }
   
   //matchCard
   public boolean matchCard(card temp){
   
      //this statement below is interesting as we only check each subsequent value
      //if the previous is true.  This saves unnecessary checks
   
      //check suit for a match
      if(this.suit.equals(temp.suit)){
         //check value only if suit true
         if(this.value.equals(temp.value)){
            if(this.deck == temp.deck){
            //the values match
               return true;
            }
         
         }
         
      }
      
      //no match
      return false;
   }

}