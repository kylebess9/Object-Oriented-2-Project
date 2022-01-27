import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class betTable extends VBox{
	int playerBet = 0;
	int playerBank = 1000;
	int setBet = 0;
	setBetBox mainBet = new setBetBox();
	playerBetStats betStats = new playerBetStats();
	submitBetBox betStuff = new submitBetBox();
	resetButton bankReset = new resetButton();
	betTable(){
		super(20);
		this.getChildren().addAll(new titleBox("Bet Table"), new titleBox("Current Bet", 15), mainBet, betStuff, betStats, bankReset);
		this.setPadding(new Insets(0,10,0,30));
	}
	
	public class titleBox extends AnchorPane{
		Label Title;
		titleBox(String textIn){
			Title = new Label(textIn);
			Title.setFont(new Font(20));
			Title.setTextAlignment(TextAlignment.CENTER);
			Title.setAlignment(Pos.CENTER);
			this.setPadding(new Insets(0,20,0,0));
			this.getChildren().add(Title);
		}
		
		titleBox(String textIn, int fontSize){
			Title = new Label(textIn);
			Title.setFont(new Font(fontSize));
			Title.setTextAlignment(TextAlignment.CENTER);
			Title.setAlignment(Pos.CENTER);
			this.setPadding(new Insets(0,20,0,0));
			this.getChildren().add(Title);
		}
	}
	
	public class submitBetBox extends AnchorPane{
		Button submitButton = new Button("Submit Bet");
		
	
		submitBetBox(){
			this.getChildren().add(submitButton);
			
			submitButton.setAlignment(Pos.CENTER);
			
			submitButton.setOnAction(e -> {
				playerBank -= playerBet;
				setBet = playerBet;
				submitButton.setDisable(true);
				betStats.updateMainStat();
			});
		}
		public void disableButton(){
			submitButton.setDisable(true);
		}
		public void enableButton(){
			submitButton.setDisable(false);
		}
	}
	
	public class playerBetStats extends VBox{
		Label playerBankText = new Label("Player Bank: " + playerBank);
		Label playerBetText = new Label("Player Bet: " + setBet);
		playerBetStats(){
			super(10);
			this.getChildren().addAll(playerBankText, playerBetText);
		}
		
		public void updateMainStat(){
			playerBankText.setText("Player Bank: " + playerBank);
			playerBetText.setText("Player Bet: " + setBet);
		}
	}
	
	public class resetButton extends AnchorPane{
		Button resetBank = new Button("Reset Betting");
		resetButton(){
			this.getChildren().add(resetBank);
			
			resetBank.setOnAction(e ->{
				playerBank = 1000;
				playerBet = 0;
				setBet = 0;
				mainBet.updateBetText();
				betStats.updateMainStat();
			});
		}
		public void disableButton(){
			resetBank.setDisable(true);
		}
		public void enableButton(){
			resetBank.setDisable(false);
		}
	}
	
	public class setBetBox extends HBox{
		Button upBet = new Button("+");
		Label setBetText = new Label("0");
		Button downBet = new Button("-");
	
		setBetBox(){
			super(20);
			setBetText.setPrefWidth(50);
			
			upBet.setOnAction(e ->{
				if(playerBet < playerBank){
					playerBet += 10;
					updateBetText();
				}
			});
			
			downBet.setOnAction(e ->{
				if(playerBet > 0){
					playerBet -= 10;
					updateBetText();
				}
			});
			this.getChildren().addAll(upBet, setBetText, downBet);
		}
		
		public void updateBetText(){
			setBetText.setText(playerBet+"");
		}
		
	}
	
	public void disableBetting(){
		betStuff.disableButton();
		bankReset.disableButton();
	}
	public void enableBetting(){
		betStuff.enableButton();
		bankReset.enableButton();
	}
	public void betWon(){
		playerBank += setBet * 2;
		playerBet = 0;
		setBet = 0;
		mainBet.updateBetText();
		betStats.updateMainStat();
	}
	public void betTied(){
		playerBank += setBet;
		playerBet = 0;
		setBet = 0;
		mainBet.updateBetText();
		betStats.updateMainStat();
	}
	public void betLost(){
		playerBet = 0;
		setBet = 0;
		mainBet.updateBetText();
		betStats.updateMainStat();
	}
	public int getBank(){
		return playerBank;
	}
	public void updateBank(int bankIn){
		playerBank = bankIn;
		mainBet.updateBetText();
		betStats.updateMainStat();
	}
}
