package hu.szikorazoltan.model;

public enum Status {

  A("Active"), P("Progress"), D("Deleted");

  private final String displayValue;

  private Status(String displayValue) {
    this.displayValue = displayValue;
  }

  public String getDisplayValue() {
    return displayValue;
  }
}
