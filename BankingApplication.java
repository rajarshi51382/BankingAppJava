import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.util.*;

class BankingInfo implements Serializable {
  private String account_id = "";
  private byte[] pinHash;
  private byte[] pinSalt;
  private String first_name = "";
  private String last_name = "";
  private double balance = 0.00;

  private static Set<String> bank_ids = new HashSet<String>();
  private static ArrayList<BankingInfo> all_accounts = new ArrayList<BankingInfo>();

  public static void saveAccounts() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("accounts.dat"))) {
      oos.writeObject(all_accounts);
    } catch (IOException e) {
      System.out.println("Error saving accounts: " + e.getMessage());
    }
  }

  public static void loadAccounts() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("accounts.dat"))) {
      all_accounts = (ArrayList<BankingInfo>) ois.readObject();
      for (BankingInfo info : all_accounts) {
        bank_ids.add(info.getAcct());
      }
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("Error loading accounts: " + e.getMessage());
    }
  }

  public static BankingInfo authenticate(String acct, int pin) {
    for (BankingInfo info : all_accounts) {
      if (info.account_id.equals(acct) && info.validatePin(pin)) {
        return info;
      }
    }
    return null;
  }

  public String generateBankID() {
    String to_ret = "";
    do {
      Random random = new Random();
      for (int i = 1; i <= 8; i++) {
        to_ret += random.nextInt(10);
      }
    } while (bank_ids.contains(to_ret));
    bank_ids.add(to_ret);
    return to_ret;
  }

  public byte[] generatePinSalt() {
    byte[] salt = new byte[16];
    new Random().nextBytes(salt);
    return salt;
  }

  public byte[] hashPin(int pin, byte[] salt) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(salt);
      byte[] pinBytes = String.valueOf(pin).getBytes(StandardCharsets.UTF_8);
      byte[] hashedPin = md.digest(pinBytes);
      return hashedPin;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error hashing pin: " + e.getMessage());
    }
  }

  public BankingInfo(String name) {
    String[] nameArr = name.split(" ");
    first_name = nameArr[0];
    last_name = nameArr[nameArr.length - 1];
    account_id = generateBankID();
    pinSalt = generatePinSalt();
    int generatedPin = generatePin();
    pinHash = hashPin(generatedPin, pinSalt);
    all_accounts.add(this);
    balance = 0;
    System.out.println("SUCCESS!");
    System.out.println("Your Banking ID is " + account_id);
    System.out.println("Your Pin is " + String.format("%04d", generatedPin).substring(0, 4));
  }

  public boolean validatePin(int pin) {
    byte[] pinHashToValidate = hashPin(pin, pinSalt);
    return Arrays.equals(pinHash, pinHashToValidate);
  }

  public int generatePin() {
    Random random = new Random();
    String pinString = "";
    for (int i = 1; i <= 4; i++) {
      pinString += random.nextInt(10);
    }
    return Integer.parseInt(pinString);
  }

  public String getPin() {
    return String.valueOf(generatePin());
  }

  public String getFirstName() {
    return first_name;
  }

  public String getAcct() {
    return account_id;
  }

  public String getBalance() {
    DecimalFormat df = new DecimalFormat("0.00");
    return df.format(balance);
  }

  public void deposit(double amount) {
    balance += amount;
    System.out.println("Deposit successful. Current balance: " + getBalance());
  }

  public void withdraw(double amount) {
    if (amount > balance) {
      System.out.println("Sorry, your balance is too low for this withdrawal.");
    } else {
      balance -= amount;
      System.out.println("Withdrawal successful. Current balance: " + getBalance());
    }
  }
}

class BankingApplication {
  private static Scanner input = new Scanner(System.in);

  public static void withdrawMoney(BankingInfo info) {
    System.out.println("How much money would you like to withdraw?");
    double withdrawalAmount = input.nextDouble();
    input.nextLine();
    info.withdraw(withdrawalAmount);
  }

  public static BankingInfo authenticator() {
    System.out.println("Please enter your Banking ID:");
    String acct = input.nextLine();

    if (!acct.matches("\\d{8}")) {
      System.out.println("Invalid Banking ID format. Please try again.");
      return authenticator();
    }

    System.out.println("Please enter your PIN:");
    String pinStr = input.nextLine();

    if (!pinStr.matches("\\d{4}")) {
      System.out.println("Invalid PIN format. Please try again.");
      return authenticator();
    }

    int pin = Integer.parseInt(pinStr);
    BankingInfo info = BankingInfo.authenticate(acct, pin);

    if (info != null) {
      System.out.println("\nSUCCESS!");
      System.out.println("Logged in as " + info.getFirstName());
      return info;
    } else {
      System.out.println("Invalid Banking ID or PIN. Please try again.");
      return authenticator();
    }
  }

  public static void bankingInterface(BankingInfo info) {
    int inp = 0;
    while (inp != 3) {
      System.out.println("\nSUCCESS!");
      System.out.println("\nGreetings, " + info.getFirstName());
      System.out.println("Your current balance is " + info.getBalance());
      System.out.println("Would you like to \n1. Withdraw Money\n2. Deposit Money\n3. Log Out and Exit");
      inp = input.nextInt();
      input.nextLine();
      switch (inp) {
        case 1:
          withdrawMoney(info);
          break;
        case 2:
          System.out.println("How much money would you like to deposit?");
          double depositAmount = input.nextDouble();
          input.nextLine();
          info.deposit(depositAmount);
          break;
        case 3:
          System.out.println("Thank you for using The Bank! Goodbye!");
          break;
        default:
          System.out.println("Invalid option. Please try again.");
          break;
      }
    }
  }

public static void main(String[] args) {
    // Load existing accounts from file
    File storage = new File("accounts.dat");
    BankingInfo.loadAccounts();

    int inp = 0;
    System.out.println("Welcome to The Bank.");
    System.out.println("  1. Log In");
    System.out.println("  2. Make an Account");
    inp = input.nextInt();
    input.nextLine();

    if (inp == 1) {
      BankingInfo info = authenticator();
      if (info != null) {
        bankingInterface(info);
      } else {
        System.out.println("Invalid Banking ID or PIN. Please try again.");
      }
    } else if (inp == 2) {
      System.out.println("\nPlease enter your name:");
      String name = input.nextLine();
      new BankingInfo(name);

      System.out.println("Would you like to log in now? (Y/N)");
      String choice = input.nextLine();
      if (choice.equalsIgnoreCase("Y")) {
        BankingInfo info = authenticator();
        if (info != null) {
          bankingInterface(info);
        } else {
          System.out.println("Invalid Banking ID or PIN. Please try again.");
        }
      }
      else {
        System.out.println("Thank you for using The Bank! Goodbye!");
      }
    }

    // Save all_accounts to file before exiting
    BankingInfo.saveAccounts();
  }

}
