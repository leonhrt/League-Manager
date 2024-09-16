package persistence.dbDAO;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Class that creates the database
 */
public class DatabaseDAO {
    private static Firestore bd;

    /**
     * Method that creates the database
     * @throws IOException if there is an error
     */
    public void createDB () throws IOException {
        FileInputStream serviceAccount = new FileInputStream("data/config_database.json");
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
        FirebaseApp.initializeApp(options);
        bd = FirestoreClient.getFirestore();
        System.out.println("Connectat");
    }

    /**
     * Method that get the database
     * @return database
     */
    public Firestore getBd() {
        return bd;
    }
}
