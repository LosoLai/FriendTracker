package com.example.loso.friendtracker.Service;

// dummy friend location provider by Caspar for MAD s2, 2017
// Usage: add this class to project in appropriate package
// add dummy_data.txt to res/raw folder
// see: TestLocationService.test() method for example

// NOTE: you may need to expliity add the import for the generated some.package.R class
// which is based on your package declaration in the manifest

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import com.example.loso.friendtracker.Model.FriendLocation;
import com.example.loso.friendtracker.R;

//import mad.friend.simple.R;

public class DummyLocationService {
    // PRIVATE PORTION
    private static final String LOG_TAG = DummyLocationService.class.getName();
    private HashMap<String, FriendLocation> locationList = new HashMap<String, FriendLocation>();
    private static Context context;

    // Singleton
    private DummyLocationService() {
    }

    // check if the source time is with the range of target time +/- minutes and seconds
    private boolean timeInRange(Date source, Date target, int periodMinutes, int periodSeconds) {
        Calendar sourceCal = Calendar.getInstance();
        sourceCal.setTime(source);

        // set up start and end range match
        // +/- period minutes/seconds to check
        Calendar targetCalStart = Calendar.getInstance();
        targetCalStart.setTime(target);
        targetCalStart.set(Calendar.MINUTE, targetCalStart.get(Calendar.MINUTE) - periodMinutes);
        targetCalStart.set(Calendar.SECOND, targetCalStart.get(Calendar.SECOND) - periodSeconds);
        Calendar targetCalEnd = Calendar.getInstance();
        targetCalEnd.setTime(target);
        targetCalEnd.set(Calendar.MINUTE, targetCalEnd.get(Calendar.MINUTE) + periodMinutes);
        targetCalEnd.set(Calendar.SECOND, targetCalEnd.get(Calendar.SECOND) + periodMinutes);

        // return if source time in the target range
        return sourceCal.after(targetCalStart) && sourceCal.before(targetCalEnd);
    }

    // called internally before usage
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void parseFile(Context context) {
        //locationList.clear();
     // resource reference to dummy_data.txt in res/raw/ folder of your project
      try (Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.dummy_data)))
     {
         // match comma and 0 or more whitepace (to catch newlines)
         scanner.useDelimiter(",\\s*");
         while (scanner.hasNext())
         {
             FriendLocation friend = new FriendLocation();
             friend.setTime(DateFormat.getTimeInstance(DateFormat.MEDIUM).parse(scanner.next()));
             String name = scanner.next();
             friend.setLatitude(Double.parseDouble(scanner.next()));
             friend.setLongitude(Double.parseDouble(scanner.next()));
             locationList.put(name, friend);
         }
      } catch (Resources.NotFoundException e)
      {
         Log.i(LOG_TAG, "File Not Found Exception Caught");
     } catch (ParseException e)
     {
        Log.i(LOG_TAG, "ParseException Caught (Incorrect File Format)");
      }
    }

    // singleton support
    private static class LazyHolder {
        static final DummyLocationService INSTANCE = new DummyLocationService();
    }

    // PUBLIC METHODS

    // singleton
    // thread safe lazy initialisation: see https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
    public static DummyLocationService getSingletonInstance(Context context) {
        DummyLocationService.context = context;
        return LazyHolder.INSTANCE;
    }

    //log contents of file (for testing/logging only)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logAll() {
        // we reparse file contents for latest data on every call
        parseFile(context);
        Iterator itr = locationList.entrySet().iterator();
        while (itr.hasNext()){
            Map.Entry pair = (Map.Entry) itr.next();
            Log.i(LOG_TAG, pair.getValue().toString());
        }
    }

    // log contents of provided list (for testing/logging and example purposes only)
    public void log(FriendLocation location) {
        Log.i(LOG_TAG, location.toString());
    }

    // the main method you can call periodcally to get data that matches a given time period
    // time +/- period minutes/seconds to check
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public FriendLocation getFriendLocationsForTime(Date time, String name, int periodMinutes, int periodSeconds) {
        // we reparse file contents for latest data on every call
        parseFile(context);
        FriendLocation location = locationList.get(name);
        return location;
    }
}
