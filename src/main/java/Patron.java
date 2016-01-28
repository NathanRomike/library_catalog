import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sql2o.*;

public class Patron {
  private int mId;
  private String mName;
  private String mAddress;
  private String mPhoneNumber;

  public int getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public String getAddress() {
    return mAddress;
  }

  public String getPhoneNumber() {
    return mPhoneNumber;
  }

  @Override
  public boolean equals(Object otherPatron) {
    if (!(otherPatron instanceof Patron)) {
      return false;
    } else {
      Patron newPatron = (Patron) otherPatron;
      return (newPatron.getName().equals(this.getName())) &&
             (newPatron.getAddress().equals(this.getAddress())) &&
             (newPatron.getPhoneNumber().equals(this.getPhoneNumber())) &&
             (newPatron.getId() == this.getId());
    }
  }

  public Patron(String name, String address, String phoneNumber) {
    this.mName = name;
    this.mAddress = address;
    this.mPhoneNumber = phoneNumber;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patrons (name, address, phone_number) VALUES (:name, :address, :phoneNumber)";
      mId = (int) con.createQuery(sql, true)
        .addParameter("name", mName)
        .addParameter("address", mAddress)
        .addParameter("phoneNumber", mPhoneNumber)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Patron> all() {
    String sql = "SELECT id AS mId, name AS mName, address AS mAddress, phone_number AS mPhoneNumber FROM patrons";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .executeAndFetch(Patron.class);
    }
  }

  public static Patron find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id AS mId, name AS mName, address AS mAddress, phone_number AS mPhoneNumber FROM patrons WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Patron.class);
    }
  }

  public void update(String name, String address, String phoneNumber) {
    String sql = "UPDATE patrons SET name = :name, address = :address, phone_number = :phoneNumber";
    try (Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("address", address)
        .addParameter("phoneNumber", phoneNumber)
        .executeUpdate();
    }
  }

  public void delete() {
    String sql = "DELETE FROM patrons WHERE id = :id";
    try (Connection con = DB.sql2o.open()) {
      con.createQuery(sql)
        .addParameter("id", this.mId)
        .executeUpdate();
    }
  }
}
