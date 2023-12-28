import java.util.*;
import java.io.*;

public class AllUsers {
  LinkedList<User> userList;

  public AllUsers() {
    userList = new LinkedList<User>();
  }

  // Add a user to the list.
  public synchronized void addUser(String name, String ppsn, String email, String password, String address,
      String balance) {
    User newUser = new User();

    // Check if the PPSN is unique.
    if (isUniquePPSN(ppsn) == false) {
      System.out.println("PPSN already exists. User not added.");
      return;
    }

    // Check if the E-mail is unique.
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

    // Print the user's details to a file.
    printUserInfoToFile("users.txt");
  }

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

  // Print all user information to a text file.
  public synchronized void printUserInfoToFile(String filePath) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
      Iterator<User> iterator = userList.iterator();
      while (iterator.hasNext()) {
        User user = iterator.next();
        writer.println(user.toString());
      }
      System.out.println("User information printed to file: " + filePath);
    } catch (IOException e) {
      System.out.println("Error printing user information to file: " + e.getMessage());
    }
  }
}