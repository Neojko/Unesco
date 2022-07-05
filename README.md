# Unesco
Cazoo assessment project

To run the .jar, please type the following:
java -jar unesco.jar arg1 arg2 arg3 arg4 arg5 arg6

where:
- arg1 is the Unesco file containing all sites 
  - Such a file can be found in src/main/resources/
- arg2 is a matrix containing the travel time at a 80km/h speed between all pairs of sites in the Unesco file
  - Such a file can be found in src/main/resources/
- arg3 is the latitude of the start location of your choice (from -90 to 90)
- arg4 is the longitude of the start location of your choice (from -180 to 180)
- arg5 is the time budget in seconds of the LNS algorithm
- arg6 is the removal percentage of the visited sites when running RandomSiteDestroyer at each iteration of LNS



