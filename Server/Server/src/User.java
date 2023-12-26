public class User {

  private String theName, thePPSN, theEmail, thePassword, theAddress;
  private double theBalance;

  // Default constructor.
  public User() {
    setName("Unknown");
    setPPSN("Unknown");
    setEmail("Unknown");
    setPassword("Unknown");
    setAddress("Unknown");
    setBalance(0);
  }

  // Constructor with params.
  public User(String name, String ppsn, String email, String password, String address, double balance) {
    setName(name);
    setPPSN(ppsn);
    setEmail(email);
    setPassword(password);
    setAddress(address);
    setBalance(balance);
  }

  // Setters.
  public void setName(String name) {
    theName = name;
  }

  public void setPPSN(String ppsn) {
    thePPSN = ppsn;
  }

  public void setEmail(String email) {
    theEmail = email;
  }

  public void setPassword(String password) {
    thePassword = password;
  }

  public void setAddress(String address) {
    theAddress = address;
  }

  public void setBalance(double balance) {
    theBalance = balance;
  }

  // Getters.
  public String getName() {
    return theName;
  }

  public String getPPSN() {
    return thePPSN;
  }

  public String getEmail() {
    return theEmail;
  }

  public String getPassword() {
    return thePassword;
  }

  public String getAddress() {
    return theAddress;
  }

  public double getBalance() {
    return theBalance;
  }

  public String toString() {
    return theName + "*" + thePPSN + "*" + theEmail + "*" + thePassword + "*" + theAddress + "*" + theBalance;
  }
}