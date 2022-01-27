//Original Creator was MAJK
//I (KYLE) implemented this file into my Black Jack game
//Changes have been marked with comments starting with KYLE


import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class deck{
   
   //create array of suits
   String [] suits ={"Hearts","Diamonds","Spades","Clubs"};
   
   //create array of cards
   String [] values = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};

   //initialization of set of decks (i.e. deck we actually play from) 
   //KYLE - I changed this array to an ArrayList
   ArrayList<card> playingDeck = new ArrayList<card>();
   //how many decks in use
   int myNumDecks;
   
   //keep track of how far we have filled
   int currentSpotInDeck;
   
   //notes how many cards have been drawn
   int cardDrawn;
   
   public deck(int numDecks){
   
      //set how many decks for game.  Needed for other functions
      myNumDecks = numDecks;
      
      //create a deck that can hold as many card decks as we want
      //KYLE - No longer need to set the array since I switched to an arraylist
      //playingDeck = new card[(numDecks*52)];
      
      //set our spot to 0
      currentSpotInDeck = 0;
      
      //set how many cards drawn to 0
      cardDrawn=0;
   
   }
   public void dumpDeck(){
	   playingDeck.clear();
	   currentSpotInDeck = 0;
	   cardDrawn = 0;
   }
   public void shuffleDeck(){
	   dumpDeck();
	   fillDeck();
   }
   //pick card function will return card value
   public card pickCard(){
   
      //create temp variables
      card tempCard = new card();
      int randomDeck;
      
      // loop value
      boolean keepCheckin = true;
      
      while(keepCheckin){
      
         //get random value
         int randomValue = ThreadLocalRandom.current().nextInt(0, 12+ 1);
      
         //set card Suit
         int randomSuit = ThreadLocalRandom.current().nextInt(0, 3 + 1);
      
         //pick from decks
         if(myNumDecks>1){
            randomDeck = ThreadLocalRandom.current().nextInt(1, myNumDecks+1);
         }
         else 
            randomDeck = 1;
            
            //set card values
         tempCard.setCard(suits[randomSuit], values[randomValue], randomDeck);
         
         //System.out.println(suits[randomSuit] + " " + values[randomValue] + " "+ randomDeck);
         
         //only check if not the first card
         if(currentSpotInDeck>0){
         //check if exists
            if(checkCard(tempCard)){
               //if true we keep checking
               keepCheckin=true;
            }
            else{
               //kill the loop
               keepCheckin = false;
            }
         
         }
         else{
            keepCheckin = false; 
         }
      }
     
      return tempCard;
   }
   
   
   //check if card is already in deck
   private boolean checkCard(card cardToFind){
     
      //look through deck up to where we have filled
      for(int i =0; i<currentSpotInDeck;i++){
      
         //compare to array value at spot
         if(playingDeck.get(i).matchCard(cardToFind)){
            //card matches try again
            return true;
         }
         
      }
   
      //if no value found
      return false;
   }

   //fill playing deck
   public void fillDeck(){
   
      for(int i = 0; i<(myNumDecks*52);i++){
         //pick card and place in deck
    	 playingDeck.add(pickCard());
         
    	 //KYLE - I no longer need to print the card
    	 //playingDeck.get(i).printCard();
         //increment spot in deck
         currentSpotInDeck++;
      }
   
   }

   public card drawCard(){
   
      //draw a card if there are any left
      if(cardDrawn <currentSpotInDeck)
      {
         //increment cards drawn
         cardDrawn++;
         
         return playingDeck.get(cardDrawn-1);
         
      }
      else{
         System.out.println("The deck is empty");
         return null;
      }
   
   }
}