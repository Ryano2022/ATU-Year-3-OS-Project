import java.util.Iterator;
import java.util.LinkedList;

public class AllUsers {
  LinkedList<User> userList;

  public AllUsers() {
    userList = new LinkedList<User>();
  }

  public synchronized void addUser(String name, String ppsn, String email, String password, String address,
      String balance) {
    User newUser = new User();
    newUser.setName(name);
    newUser.setPPSN(ppsn);
    newUser.setEmail(email);
    newUser.setPassword(password);
    newUser.setAddress(address);
    newUser.setBalance(Double.parseDouble(balance));

    userList.add(newUser);
  }

  public synchronized String searchList(String name) {
    Iterator<User> i = userList.iterator();
    int found = 0;
    String response = "Not found";

    while (i.hasNext() && found == 0) {
      User newUser = i.next();

      if (newUser.getName().equalsIgnoreCase(name)) {
        found = 1;
        response = newUser.toString();
      }
    }
    return response;
  }

  public synchronized boolean hasUsers() {
    return !userList.isEmpty();
  }
}