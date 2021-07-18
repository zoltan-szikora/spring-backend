package hu.szikorazoltan.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private long id;

  @Column(name = "role", unique = true)
  private String role;

  @JsonBackReference
  @ManyToMany(mappedBy = "roles")
  private Set<User> users = new HashSet<User>();

  public Role() {
  }

  public Role(String role) {
    this.role = role;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  @Override
  public String toString() {
    return "Role [id=" + id + ", role=" + role + "]";
  }
}
