package com.example.f1mpdresit;
// Name                 Craig Adumuah_________________
// Student ID           S2026435_________________
// Programme of Study   Computing_________________
//
//
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView rawDataDisplay;
    private TextView updateTimeDisplay;
    private Button startButton;
    private Button raceButton;
    private Button mapButton;
    private RecyclerView infoRecyclerView;
    private String result;
    private final String urlSource = "http://ergast.com/api/f1/current/driverStandings";
    private final String scheduleUrl = "http://ergast.com/api/f1/current";
    private DriverAdapter driverAdapter;
    private RaceAdapter raceAdapter;
    private List<Driver> driverList;
    private List<Race> raceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate started");
        setContentView(R.layout.activity_main);

        // Initialize UI components
        rawDataDisplay = findViewById(R.id.rawDataDisplay);
        startButton = findViewById(R.id.startButton);
        raceButton = findViewById(R.id.raceButton);
        mapButton = findViewById(R.id.mapButton);
        updateTimeDisplay = findViewById(R.id.updateTimeDisplay);
        infoRecyclerView = findViewById(R.id.infoRecyclerView);

        // Initialize the RecyclerView
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mapButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.startButton) {
            startProgress(urlSource);
        } else if (view.getId() == R.id.raceButton) {
            startProgress(scheduleUrl);
        } else if (view.getId() == R.id.mapButton) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }

        // Display the current date and time of the data fetch
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        updateTimeDisplay.setText("Last Updated: " + currentDateTimeString);
    }

    private void startProgress(String url) {
        // Determine the type of data to process based on the URL
        boolean isDriver = url.equals(urlSource);

        // Run network access on a separate thread
        new Thread(new Task(url, isDriver)).start();
    }

    private class Task implements Runnable {
        private final String url;
        private final boolean isDriver;

        public Task(String url, boolean isDriver) {
            this.url = url;
            this.isDriver = isDriver;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection yc;
            BufferedReader in;
            StringBuilder inputLine = new StringBuilder();

            try {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                String input;
                while ((input = in.readLine()) != null) {
                    inputLine.append(input);
                }
                in.close();
                result = inputLine.toString();

                // Log the raw XML data
                Log.d("XMLData", result);

                // Remove XML declaration if present
                result = result.replaceFirst("<\\?xml.*?\\?>", "");

                if (isDriver) {
                    parseDriverStandings(result);
                } else {
                    parseRaceSchedule(result);
                }
            } catch (IOException e) {
                Log.e("MyTag", "IOException in run", e);
                runOnUiThread(() -> rawDataDisplay.setText("Error: Unable to retrieve data."));
            }
        }
    }

    private void parseDriverStandings(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            boolean insideDriverStanding = false;
            driverList = new ArrayList<>();

            Driver currentDriver = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("DriverStanding")) {
                        insideDriverStanding = true;
                        currentDriver = new Driver();
                        currentDriver.setPosition(xpp.getAttributeValue(null, "position"));
                    } else if (insideDriverStanding) {
                        if (xpp.getName().equalsIgnoreCase("points")) {
                            currentDriver.setPoints(xpp.nextText());
                        } else if (xpp.getName().equalsIgnoreCase("wins")) {
                            currentDriver.setWins(xpp.nextText());
                        } else if (xpp.getName().equalsIgnoreCase("GivenName")) {
                            currentDriver.setGivenName(xpp.nextText());
                        } else if (xpp.getName().equalsIgnoreCase("FamilyName")) {
                            currentDriver.setFamilyName(xpp.nextText());
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("DriverStanding")) {
                        insideDriverStanding = false;
                        driverList.add(currentDriver);
                    }
                }
                eventType = xpp.next();
            }

            runOnUiThread(() -> {
                driverAdapter = new DriverAdapter(driverList);
                infoRecyclerView.setAdapter(driverAdapter);
            });

        } catch (Exception e) {
            Log.e("MyTag", "Error parsing data", e);
            runOnUiThread(() -> rawDataDisplay.setText("Error: Unable to parse driver standings."));
        }
    }

    private void parseRaceSchedule(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            raceList = new ArrayList<>();

            Race currentRace = null;
            Date currentDate = new Date();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("Race")) {
                        currentRace = new Race();
                    } else if (currentRace != null) {
                        if (xpp.getName().equalsIgnoreCase("Round")) {
                            currentRace.setRound(xpp.nextText());
                        } else if (xpp.getName().equalsIgnoreCase("RaceName")) {
                            currentRace.setRaceName(xpp.nextText());
                        } else if (xpp.getName().equalsIgnoreCase("Date")) {
                            String raceDateStr = xpp.nextText();
                            currentRace.setDate(raceDateStr);
                            Date raceDate = new SimpleDateFormat("yyyy-MM-dd").parse(raceDateStr);
                            if (raceDate.before(currentDate)) {
                                currentRace.setPast(true);
                            } else {
                                currentRace.setPast(false);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("Time")) {
                            currentRace.setTime(xpp.nextText());
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("Race")) {
                        raceList.add(currentRace);
                    }
                }
                eventType = xpp.next();
            }

            runOnUiThread(() -> {
                raceAdapter = new RaceAdapter(raceList);
                infoRecyclerView.setAdapter(raceAdapter);
            });

        } catch (Exception e) {
            Log.e("MyTag", "Error parsing data", e);
            runOnUiThread(() -> rawDataDisplay.setText("Error: Unable to parse race schedule."));
        }
    }
}
