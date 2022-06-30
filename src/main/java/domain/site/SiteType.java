package domain.site;

public enum SiteType {
  Cultural,
  Natural,
  Mixed;

  public boolean isCultural() {
    return this == Cultural || this == Mixed;
  }

  public boolean isNatural() {
    return this == Natural || this == Mixed;
  }
}
