package com.techelevator.tenmo;

import com.techelevator.tenmo.exception.AcctNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.view.ConsoleService;
import java.math.BigDecimal;


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

    public static void main(String[] args) throws TransferNotFoundException, AcctNotFoundException {
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

	public void run() throws TransferNotFoundException, AcctNotFoundException {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() throws TransferNotFoundException, AcctNotFoundException {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
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

	private void viewCurrentBalance() {
    	Account a = accountService.getAccountById(currentUser);
    	System.out.println("\nYour current balance is: " + a.getBalance());
	}

	private void viewTransferHistory() {
		Transfer[] transfers = transferService.getAllTransfers(currentUser);
		for (Transfer t : transfers) {
			System.out.println(t.toString());
		}
		viewTransferDetails(transfers);
	}

	private void viewPendingRequests() {
		Transfer[] transfers = transferService.getAllPendingTransfers(currentUser);
		for (Transfer t : transfers) {
			System.out.println(t.toStringPending());
		}
		approveOrRejectTransfer(transfers);
	}

	private void viewTransferDetails(Transfer[] transfers) {
		Integer transferId = console.getUserInputInteger("\nEnter Transfer ID for the transfer you'd like to view details for (0 to cancel)");

		if (transferId != 0) {
			boolean found = false;

			for (Transfer t : transfers) {
				if (t.getTransferId().equals(transferId)) {
					found = true;
					transferService.getTransferById(currentUser, transferId).printFullTransferDetails();
				}
			}

			if (!found) {
				System.out.println("\nTransfer not found!");
			}
		}

	}

	private void approveOrRejectTransfer(Transfer[] transfers) {

		Integer transferId = console.getUserInputInteger("\nEnter Transfer ID for the transfer you'd like to approve or reject (0 to cancel)");

		if (transferId != 0) {
			boolean found = false;

			for (Transfer t : transfers) {
				if (t.getTransferId().equals(transferId)) {
					found = true;
					transferService.getTransferById(currentUser, transferId).printFullTransferDetails();
				}
			}

			if (!found) {
				System.out.println("\nTransfer not found!");
			}
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
		boolean found = false;

		if (toUserId != 0) {
			for (User u : users) {
				if (u.getId().equals(toUserId)) {
					for (Account a : accounts) {
						if (a.getUserId().equals(toUserId)) {
							toAccountId = a.getAccountId();
						}
					}

					if (toUserId.equals(currentUser.getUser().getId())) {
						break;
					}

					System.out.println("\nFound user " + u.getId());
					found = true;
					viewCurrentBalance();
					BigDecimal amountToTransfer = new BigDecimal(console.getUserInput("Enter amount to transfer (0 to cancel)"));

					//lines below are checking if transfer amount is greater than zero, and if account has enough to transfer
					if ((amountToTransfer.compareTo(BigDecimal.ZERO) > 0) &&
					   (amountToTransfer.compareTo(accountService.getAccountById(currentUser).getBalance()) <= 0) ) {
						Transfer t = new Transfer();
						t.setAccountFrom(accountService.getAccountById(currentUser).getAccountId());
						t.setAccountTo(toAccountId);
						t.setAmount(amountToTransfer);
						t.setTransferStatusId(2);
						t.setTransferTypeId(2);
						transferService.createTransfer(currentUser, t);
						System.out.println("\nTransfer successful!");
					}
					else {
						System.out.println("\nSorry, that's not a valid amount to transfer!");
					}
				}
			}

			if (!found) {
				System.out.println("\nUser not found!");
			}

		}

	}

	private void requestBucks() {
		User[] users = userService.getAllUsers(currentUser);
		Account[] accounts = accountService.getAllAccounts(currentUser);

		for (User u : users) {
			System.out.println(u.toString());
		}

		Integer fromUserId = console.getUserInputInteger("Enter ID of user you'd like to request TE bucks from (0 to cancel)");
		Integer fromAccountId = null;
		boolean found = false;

		if (!(fromUserId == 0)) {
			for (User u : users) {
				if (u.getId().equals(fromUserId)) {
					for (Account a : accounts) {
						if (a.getUserId().equals(fromUserId)) {
							fromAccountId = a.getAccountId();
						}
					}

					if (fromUserId.equals(currentUser.getUser().getId())) {
						break;
					}

					System.out.println("\nFound user " + u.getId());
					found = true;
					viewCurrentBalance();
					BigDecimal amountToTransfer = new BigDecimal(console.getUserInput("Enter amount to request (0 to cancel)"));

					//line below is checking if transfer amount is greater than zero
					if (amountToTransfer.compareTo(BigDecimal.ZERO) > 0) {
						Transfer t = new Transfer();
						t.setAccountFrom(fromAccountId);
						t.setAccountTo(accountService.getAccountById(currentUser).getAccountId());
						t.setAmount(amountToTransfer);
						t.setTransferStatusId(1);
						t.setTransferTypeId(1);
						transferService.createTransfer(currentUser, t);
						System.out.println("\nTransfer request successful!");
					}
					else {
						System.out.println("\nSorry, that's not a valid amount to request!");
					}
				}
			}

			if (!found) {
				System.out.println("\nUser not found!");
			}

		}

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
