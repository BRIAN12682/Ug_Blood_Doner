package gr.gdschua.bloodapp.DatabaseAccess;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gr.gdschua.bloodapp.Entities.Event;

public class DAOEvents {
    Map<String, Object> eventMap = new HashMap<>();


    public DAOEvents() {
        populateEventMap();
    }

    public static Event mapToEvent(Map singleEvent) {
        return new Event(singleEvent.get("name").toString(), singleEvent.get("date").toString(), singleEvent.get("owner").toString(), Double.parseDouble(singleEvent.get("lat").toString()), Double.parseDouble(singleEvent.get("lon").toString()));
    }

    public Task<Void> insertEvent(Event newEvent) {

        return FirebaseDatabase.getInstance().getReference("Events")
                .child(newEvent.getId())
                .setValue(newEvent);
    }

    private void populateEventMap() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Events");
        ref.addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        if (dataSnapshot.exists()) {
                            eventMap.putAll((Map<String, Object>) dataSnapshot.getValue());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

    }

    //Maybe make static
    //Maybe in the future add a parameter to limit how many hospitals it should return or the general location?
    public ArrayList<Event> getAllEvents() {


        ArrayList<Event> allEventsList = new ArrayList<>();
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : eventMap.entrySet()) {

            Map singleEvent = (Map) entry.getValue();
            allEventsList.add(mapToEvent(singleEvent));
        }
        return allEventsList;
    }


}
