package storage;

import model.*;
import java.io.*;
import java.util.*;

/**
 * Implementation of DataStorage that uses files for persistence.
 * Demonstrates polymorphism through implementation of the DataStorage interface.
 * @param <T> Type of objects to store
 */
public class FileStorage<T> implements DataStorage<T> {
    private List<T> objects;
    private final String fileName;
    private final Class<T> typeClass;

    /**
     * Constructor
     * @param fileName Name of the file to store data
     * @param typeClass Class of the objects being stored
     */
    public FileStorage(String fileName, Class<T> typeClass) {
        this.fileName = fileName;
        this.typeClass = typeClass;
        this.objects = new ArrayList<>();
        loadAll();
    }

    @Override
    public boolean save(T object) {
        // Add the object if it doesn't already exist
        if (!objects.contains(object)) {
            objects.add(object);
            return saveAll();
        }
        return false;
    }

    @Override
    public Optional<T> findById(String id) {
        for (T obj : objects) {
            String objId = "";

            // Determine which type of object we're dealing with and get its ID
            if (obj instanceof User) {
                objId = ((User) obj).getUserId();
            } else if (obj instanceof Paper) {
                objId = ((Paper) obj).getPaperId();
            } else if (obj instanceof Review) {
                objId = ((Review) obj).getReviewId();
            }

            // Return the object if IDs match
            if (objId.equals(id)) {
                return Optional.of(obj);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(objects);  // Return a copy to prevent external modification
    }

    @Override
    public boolean update(T object) {
        String id = "";

        // Determine which type of object we're dealing with and get its ID
        if (object instanceof User) {
            id = ((User) object).getUserId();
        } else if (object instanceof Paper) {
            id = ((Paper) object).getPaperId();
        } else if (object instanceof Review) {
            id = ((Review) object).getReviewId();
        }

        // Find and replace the object with the updated version
        for (int i = 0; i < objects.size(); i++) {
            T obj = objects.get(i);
            String objId = "";

            if (obj instanceof User) {
                objId = ((User) obj).getUserId();
            } else if (obj instanceof Paper) {
                objId = ((Paper) obj).getPaperId();
            } else if (obj instanceof Review) {
                objId = ((Review) obj).getReviewId();
            }

            if (objId.equals(id)) {
                objects.set(i, object);
                return saveAll();
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        for (int i = 0; i < objects.size(); i++) {
            T obj = objects.get(i);
            String objId = "";

            if (obj instanceof User) {
                objId = ((User) obj).getUserId();
            } else if (obj instanceof Paper) {
                objId = ((Paper) obj).getPaperId();
            } else if (obj instanceof Review) {
                objId = ((Review) obj).getReviewId();
            }

            if (objId.equals(id)) {
                objects.remove(i);
                return saveAll();
            }
        }
        return false;
    }

    @Override
    public boolean saveAll() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(objects);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadAll() {
        File file = new File(fileName);

        // If file doesn't exist yet, return without loading
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            objects = (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }
}