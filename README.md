# Juice'taposting
 Juxtaposting of juice prices

# Raport:

## Author: Miłosz Momot

#### Steps and premises:

1. Utworzenie Main Acticity z mapą
2. Utworzenie customowego toolbara
3. Dodanie przycisku rozwijanego menu
4. Basic, hardcoded Activity Station List and Add new Station
5. Hardcoded Activity Add Your Station
6. Hardcoded Autocomplete Text Boxes in AddYourStation Activity
7. Hardcoded Markers in Jaslo
8. Get and set current location in Jaslo
  + Permission access in manifest
  + Include map location service in dependencies
9. Adapter of Marker InfoWindow and description using LatLang to get street and city
10. Your Location marker icon update
11. GetAddress() method, which uses street and city to get LatLang
12. Dynamically added station markers in Jaslo using GetAddress
13. Internal database connection added
14. Creation of GasStation and YourGasStation classes
  + GasStation is a simple representation of station stored in Database
  + YourGasStation is a representation of station from YourStationList stored
  in separate db table, includes ID_your_station and implementation of delete method
    + Smart constructor which uses street and city to get complete info from database
      + Drawback: if there are eg. two Orlen franchise there will be only the first found created
15. Hardcoded YourStationList examples
15. Stations database by UKOiK found
16. External database connection added
  + Problems:
    + 0 string interpreted as "", causes NONNULL parser and database error
    + Unknown postal-codes, streets and building number
  + Solution:
    + Replacement of "" string
    + Using city as street name if unknown    
17. Improved Your Station addition. Uses:
  + Stations table to create correct object and put it into
  + Database YourStation table
18. Correct adaptation of YourStation db table and representation in
YourStationList
19. Setting icon in list using station name
(if name contains popular franchise name then set Official station icon)
20. Dynamic update of Station Marker info
  + Problems:
    + using Address from data base  and GetAddress method to create marker
    and geocoder to get LatLang of station. As consequence there is different building number
    in street string between Database and created marker. Unable to get Station name neither from LatLang nor from database
  + Solutions:
    + Passing info about marker from database in snippet then reformat it
21. Create table containing lat LatLang
  + Problems:
    + Table Coordinates doesn't exist
    + Unique constraint failed when connecting table and inserting values
    + *The application may be doing too much work on its main thread* error while running app
  + Solutions:
    + Clear cache and storage of app in emulator to force reloading DB
    + Found out that Emulator uses my externally created database only to download data.
    Then it creates it's own version and override it in app files. I found this unique database
    in Emulator files and noticed that my commands works well and unique constraint was caused
    by the fact that data i wanted to save already existed
    + Making app run hard calculations (like finding lat lng using city+street that i pass 
    from database) in background thread separated from UI thread. Android apps try to avoid
    being stucked and throw an error while UI thread (the main thread where we mostly operate)
    is losing frames. Doing a lot of though calculation was causing an error.
    Now we store necessary info from database in a big array, create new array with lat lang 
    collected from geocoder and send it using pipe as a single big string array. Then handler can
    insert it to new database. That's how we analysed data and got what we needed.
    In next verions of app we can delete this block of code because we have lat and lng of station.
    It will work faster that way. Exceptions like station where exact street is unknown
    and geocoder can't handle them are omitted by replacing their "lat;lng" with "1.0;1.0".
    That way they will be easy to spot and analyse. What's more they won't cause NonNull exception
    As a result i received 148 stations that was not found. I can omit them because they are so
    small that probability of looking for them is very low. Also i will add an option to *add custom*
    station in the future
    
22. Redesigning of YourStationList rows and MarkerInfoWindow
23. Create markers on the map using lat lng from database
    + Problems:
        + While making table with lat lng Geocoder made some mistakes and some stations are misplaced
    + Solutions:
        + ??
24. AddYourStation activity update - street text field, intelligent auto complete
    + Problems:
        + Updating final array of strings from inner class method( onFocusChange) to show
        accurate names/streets from the city
    + Solutions:
        + Using 1-item final array names[0] due to that holds another array and setting adapter
        inside of the function
25. Delete from YourStationsList button
    + Problems:
        + Accessing YourStationListActivity from inner class method( onClickListener)
    + Solutions:
        + Abstract Handler class for adapter. There is instance of handler class that defines 
        deleteRow method using actual Activity object and methods. There is field of the 
        Handler in Adapter class that allows us to call this method. THIS IS THE RIGHT WAY OF
        CONNECTING ADAPTER TO ACTIVITY. Otherwise there might be some memory leaks.
