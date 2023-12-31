import java.util.*;
import java.io.*;

public class AllUsers {
  LinkedList<User> userList;

  public AllUsers() {
    userList = new LinkedList<User>();
  }

  // USER RELATED METHODS
  /////////////////////////////////////////////////////////////////////////////

  // Add a user to the list.
  public synchronized void addUser(String name, String ppsn, String email, String password, String address,
      String balance) {
    User newUser = new User();

    // Check if the PPSN is unique.
    if (isUniquePPSN(ppsn) == false) {
      System.out.println("PPSN already exists. User not added.");
      return;
    }

    // Check if the e-mail is unique.
    if (isUniqueEmail(email) == false) {
      System.out.println("E-mail already exists. User not added.");
      return;
    }

    // Set the rest of the user's details.
    newUser.setName(name);
    newUser.setPPSN(ppsn);
    newUser.setEmail(email);
    newUser.setPassword(password);
    newUser.setAddress(address);
    newUser.setBalance(Double.parseDouble(balance));

    // Add the user to the list.
    userList.add(newUser);
    System.out.println("User " + name + " added. ");
  }

  // Check if the PPSN is unique.
  public synchronized boolean isUniquePPSN(String ppsn) {
    Iterator<User> i = userList.iterator();
    int found = 0;

    while (i.hasNext() && found == 0) {
      User newUser = i.next();

      if (newUser.getPPSN().equalsIgnoreCase(ppsn)) {
        found = 1;
      }
    }
    if (found == 1) {
      return false;
    } 
    else {
      return true;
    }
  }

  // Check if the E-mail is unique.
  public synchronized boolean isUniqueEmail(String email) {
    Iterator<User> i = userList.iterator();
    int found = 0;

    while (i.hasNext() && found == 0) {
      User newUser = i.next();

      if (newUser.getEmail().equalsIgnoreCase(email)) {
        found = 1;
      }
    }
    if (found == 1) {
      return false;
    } 
    else {
      return true;
    }
  }
  
  // Add balance.
  public synchronized void addBalance(String email, double amount) {
    Iterator<User> i = userList.iterator();
    int found = 0;

    while (i.hasNext() && found == 0) {
      User user = i.next();

      if (user.getEmail().equalsIgnoreCase(email)) {
        found = 1;
        user.setBalance(user.getBalance() + amount);
      }
    }
    if (found == 0) {
      System.out.println("User not found. ");
    }
  }

  // Subtract balance.
  public synchronized void subtractBalance(String email, double amount) {
    Iterator<User> i = userList.iterator();
    int found = 0;

    while (i.hasNext() && found == 0) {
      User user = i.next();

      if (user.getEmail().equalsIgnoreCase(email)) {
        found = 1;
        double newBalance = user.getBalance() - amount;
        if (newBalance < 0) {
          System.out.println("Insufficient balance. ");
        } 
        else {
          user.setBalance(newBalance);
        }
      }
    }
    if (found == 0) {
      System.out.println("User not found. ");
    }
  }

  // Change password.
  public synchronized void changePassword(String email, String password) {
    Iterator<User> i = userList.iterator();
    int found = 0;

    while (i.hasNext() && found == 0) {
      User user = i.next();

      if (user.getEmail().equalsIgnoreCase(email)) {
        found = 1;
        user.setPassword(password);
      }
    }
    if (found == 0) {
      System.out.println("User not found. ");
    }
  }
  /////////////////////////////////////////////////////////////////////////////


  // LIST RELATED METHODS
  /////////////////////////////////////////////////////////////////////////////

  // Search the list for a user based on name, PPSN or e-mail.
  public synchronized String searchList(String searchTerm) {
    Iterator<User> i = userList.iterator();
    int found = 0;
    String response = "Not found. ";

    while (i.hasNext() && found == 0) {
      User newUser = i.next();

      if (newUser.getName().equalsIgnoreCase(searchTerm) || newUser.getPPSN().equalsIgnoreCase(searchTerm) || newUser.getEmail().equalsIgnoreCase(searchTerm)) {
        found = 1;
        response = newUser.toString();
      }
    }
    return response;
  }

  // Check if the list is empty or not.
  public synchronized boolean hasUsers() {
    return !userList.isEmpty();
  }
  /////////////////////////////////////////////////////////////////////////////


  // PRINT RELATED METHODS
  /////////////////////////////////////////////////////////////////////////////

  // These methods I had to get help with online. I don't fully understand them.
  // Print new user information to a text file.
  public synchronized void printNewUserInfoToFile(String filePath) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
      Iterator<User> iterator = userList.iterator();
      while (iterator.hasNext()) {
        User user = iterator.next();
        if (!userExistsInFile(user, filePath)) {
          writer.println(user.toString());
        }
      }
      System.out.println("New user information appended to file: " + filePath);
    } 
    catch (IOException e) {
      System.out.println("Error appending new user information to file: " + e.getMessage());
    }
  }

  // Find a specific user in the list and then replace the line in the text file with the new user information.
  public synchronized void printUpdatedUserInfoToFile(String filePath, String email) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      StringBuilder inputBuffer = new StringBuilder();

      while ((line = reader.readLine()) != null) {
        if (line.contains(email)) {
          line = searchList(email);
        }
        inputBuffer.append(line);
        inputBuffer.append('\n');
      }
      reader.close();

      // Write the new string with the replaced line OVER the same file.
      FileOutputStream fileOut = new FileOutputStream(filePath);
      fileOut.write(inputBuffer.toString().getBytes());
      fileOut.close();
      System.out.println("User information updated in file: " + filePath);
    } 
    catch (IOException e) {
      System.out.println("Error updating user information in file: " + e.getMessage());
    }
  }

  // Print all users to the console.
  public synchronized String printAllUsers(int option) {
    StringBuilder users = new StringBuilder();
    Iterator<User> i = userList.iterator();

    while (i.hasNext()) {
      User newUser = i.next();
      if (option == 1) {
        users.append(newUser.getName()).append(", ").append(newUser.getEmail()).append("\n");
      } 
      else {
        users.append(newUser.toString()).append("\n");
      }
    }

    return users.toString();
  }
  // Check if a user already exists in the file.
  private boolean userExistsInFile(User user, String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains(user.toString())) {
          return true;
        }
      }
    } 
    catch (IOException e) {
      System.out.println("Error checking if user exists in file: " + e.getMessage());
    }
    return false;
  }

  // Read all user information from a text file.
  public synchronized void readUserInfoFromFile(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] user = line.split(",");
        addUser(user[0], user[1], user[2], user[3], user[4], user[5]);
      }
      System.out.println("User information read from file: " + filePath);
    } 
    catch (IOException e) {
      System.out.println("Error reading user information from file: " + e.getMessage());
    }
  }

  // Every time the user makes a transaction, add it to the (ppsn)-transactions.txt file.
  public synchronized void printTransactionToFile(String filePath, String ppsn, double beforeBalance, double afterBalance, String transactionType) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
      Iterator<User> iterator = userList.iterator();
      while (iterator.hasNext()) {
        User user = iterator.next();
        if (user.getPPSN().equalsIgnoreCase(ppsn)) {
          writer.println(beforeBalance + ", " + afterBalance + ", " + transactionType);
        }
      }
      System.out.println("Transaction appended to file: " + filePath);
    } 
    catch (IOException e) {
      System.out.println("Error appending transaction to file: " + e.getMessage());
    }
  }
  /////////////////////////////////////////////////////////////////////////////
}