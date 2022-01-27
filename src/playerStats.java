import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class playerStats {
	
	int savedWins = 0;
	int savedLosses = 0;
	int savedTies = 0;
	int playerBank = 1000;
	playerStats(String nameIn){
		File playerStats = new File(nameIn+".txt");
		if(playerStats.exists()){
			try{
				Scanner readIn = new Scanner(playerStats);
				while(!readIn.hasNextInt())
					readIn.next();
				savedWins = readIn.nextInt();
				savedLosses = readIn.nextInt();
				savedTies = readIn.nextInt();
				playerBank = readIn.nextInt();
				readIn.close();
			}
			
			catch(Exception e){
				System.out.println("Cannot find file playerStats.txt");
			}	
		}
		else{
			try{
				PrintWriter newFile = new PrintWriter(playerStats);
				newFile.write("0\n0\n0\n1000");
				newFile.close();
			}
			catch(Exception e){
				System.out.println("Cannot create the file playerStats.txt");
			}
		}
	}
	public int getWins(){
		return savedWins;
	}
	public int getLosses(){
		return savedLosses;
	}
	public int getTies(){
		return savedTies;
	}
	public void saveStats(int wins, int losses, int ties, int bank, String nameIn){
		try{
			PrintWriter newFile = new PrintWriter(nameIn+".txt");
			newFile.write(nameIn+"\n"+wins+"\n"+losses+"\n"+ties+"\n"+bank);
			newFile.close();
		}
		catch(Exception e){
			System.out.println("Cannot create the file playerStats.txt");
		}
	}
	public int getBank(){
		return playerBank;
	}
}
