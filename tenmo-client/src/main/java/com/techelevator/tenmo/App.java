package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.view.ConsoleService;
import io.cucumber.java.bs.A;

import java.math.BigDecimal;
import java.util.Arrays;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private TransferService transferService;
    private UserService userService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),
				          new AccountService(API_BASE_URL), new TransferService(API_BASE_URL), new UserService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService,
			   AccountService accountService, TransferService transferService, UserService userService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
		this.userService = userService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
				viewTransferDetails();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private BigDecimal viewCurrentBalance() {
    	Account a = accountService.getAccountById(currentUser);
		System.out.println("Your current balance is: " + a.getBalance());
		return a.getBalance();
	}

	private void viewTransferHistory() {
		Transfer[] transfers = transferService.getAllTransfers(currentUser);

		for (Transfer t : transfers) {
			System.out.println(t.toString());
		}
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void viewTransferDetails() {
		Integer transferId = console.getUserInputInteger("Enter Transfer ID for the transfer you'd like to view details for (0 to cancel)");
		try {
			System.out.println(transferService.getTransferById(currentUser, transferId).toString());
		}
		catch (Exception e) {
			System.out.println("Transfer not found!");
		}
	}

	private void sendBucks() {
    	User[] users = userService.getAllUsers(currentUser);
    	Account[] accounts = accountService.getAllAccounts(currentUser);

		for (User u : users) {
			System.out.println(u.toString());
		}

		Integer toUserId = console.getUserInputInteger("Enter ID of user you'd like to send TE bucks to (0 to cancel)");
		Integer toAccountId = null;

		if (!(toUserId == 0)) {
			for (User u : users) {
				if (u.getId().equals(toUserId)) {
					for (Account a : accounts) {
						if (a.getUserId().equals(toUserId)) {
							toAccountId = a.getAccountId();
						}
					}
					System.out.println("Found user " + u.getId());
					BigDecimal amountToTransfer = new BigDecimal(console.getUserInput("Enter amount to transfer (0 to cancel)"));

					if ((amountToTransfer.compareTo(BigDecimal.ZERO) > 0) &&
					(amountToTransfer.compareTo(accountService.getAccountById(currentUser).getBalance()) <= 0) ) {
						Transfer t = new Transfer();
						t.setAccountFrom(accountService.getAccountById(currentUser).getAccountId());
						t.setAccountTo(toAccountId);
						t.setAmount(amountToTransfer);
						t.setTransferStatusId(2);
						t.setTransferTypeId(2);
						transferService.createTransfer(currentUser, t);
					}
				}
			}
		}
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
