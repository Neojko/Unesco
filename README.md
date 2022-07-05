# Unesco
Cazoo assessment project

To run the .jar, please type the following:
java -jar unesco.jar arg1

where arg1 is a JSON config file path that contains:
- the Unesco file containing all sites 
  - Such a file can be found in src/main/resources/whc-sites-2021.xls
- the matrix containing the travel time at a 80km/h speed between all pairs of sites in the Unesco file
  - Such a file can be found in src/main/resources/matrix.csv
- the latitude of the start location of your choice (from -90 to 90)
- the longitude of the start location of your choice (from -180 to 180)
- the time budget in seconds of the LNS algorithm
- the removal percentage of the visited sites when running RandomSiteDestroyer at each iteration of LNS
- the weight of each of the following (fixed) objectives:
  - NumberOfSitesObjective
  - NumberOfCountriesObjective
  - NumberOfEndangeredSitesObjective
  - SiteTypeParityObjective
  - TripRemainingTimeObjective

An example of JSON config is given in src/main/resources/
- Note: it is hideous, but the author didn't have more time before submitting to make the desired clean config file

