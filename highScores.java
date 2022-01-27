import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class highScores extends VBox{
	String[] Names;
	int[] wins;
	int[] losses;
	int[] ties;
	int[] bank;
	ArrayList<File> goodFiles = new ArrayList<File>();
	highScores(){
		File main = new File(".");
		if(main.isDirectory()){
			File[] files = main.listFiles();
			for(int i = 0; i < files.length;i++){
				String fileName = files[i].toString();
				System.out.println(fileName);
				if(fileName.contains(".txt")){
					goodFiles.add(files[i]);
				}
			}
		}
		int size = goodFiles.size();
		wins = new int[size];
		losses = new int[size];
		ties = new int[size];
		bank = new int[size];
		for(int i = 0; i < goodFiles.size(); i++){
			try{
				Scanner readIn = new Scanner(goodFiles.get(i));
				System.out.println(goodFiles.get(i).toString());
				//Names[i] = readIn.next();
				//System.out.println(Names[i]);
				wins[i] = readIn.nextInt();
				System.out.println(wins[i]);
				losses[i] = readIn.nextInt();
				System.out.println(losses[i]);
				ties[i] = readIn.nextInt();
				System.out.println(ties[i]);
				bank[i] = readIn.nextInt();
				System.out.println(bank[i]);
				readIn.close();
			}
			catch(Exception e){
				System.out.println("Error with loading HighScores");
			}
		}
		System.out.println(goodFiles.size());
		if(goodFiles.size() > 0)
			sortHighScores();
		
		if(goodFiles.size() > 10){
			for(int i = 0; i < 10; i++){
				this.getChildren().add(new displayHBox(Names[i], wins[i], losses[i], ties[i], bank[i]));
			}
		}
		else{
			for(int i = 0; i < goodFiles.size(); i++){
				this.getChildren().add(new displayHBox(Names[i], wins[i], losses[i], ties[i], bank[i]));
			}
		}
	}
	public void sortHighScores(){
		boolean flag = true;
		while(flag){
			flag = false;
			for(int i = 0; i < Names.length - 1; i++){
				if(wins[i] < wins[i + 1]){
					String temp;
					int Wtemp, Ltemp, Ttemp, Btemp;
					temp = Names[i];
					Wtemp = wins[i];
					Ltemp = losses[i];
					Ttemp = ties[i];
					Btemp = bank[i];
					bank[i] = bank[i + 1];
					wins[i] = wins[i + 1];
					losses[i] = losses[i + 1];
					ties[i] = ties[i + 1];
					Names[i] = Names[i + 1];
					Names[i + 1] = temp;
					wins[i + 1] = Wtemp;
					losses[i + 1] = Ltemp;
					bank[i + 1] = Btemp;
					ties[i + 1] = Ttemp;
					flag = true;
				}
			}
		}
	}
	
	class displayHBox extends HBox{
		Label name, wins, losses, ties, bank;
		
		displayHBox(String nameIn, int win, int loss, int tie, int banks){
			super(10);
			name = new Label(nameIn);
			wins = new Label(win+"");
			losses = new Label(loss+"");
			bank = new Label(banks+"");
			ties = new Label(tie+"");
		}
	}
}
