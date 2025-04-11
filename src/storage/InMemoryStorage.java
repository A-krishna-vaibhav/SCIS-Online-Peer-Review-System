package storage;

import model.*;
import java.util.*;

/**
 * Implementation of DataStorage that keeps everything in memory.
 * Useful for testing or when persistence is not required.
 * Also demonstrates polymorphism through implementation of the DataStorage interface.
 */
public class InMemoryStorage<T> implements DataStorage<T> {
    private List<T> objects;

    public InMemoryStorage() {
        this.objects = new ArrayList<>();
    }

    @Override
    public boolean save(T object) {
        if (!objects.contains(object)) {
            objects.add(object);
            return true;
        }
        return false;
    }

    @Override
    public Optional<T> findById(String id) {
        for (T obj : objects) {
            String objId = "";

            if (obj instanceof User) {
                objId = ((User) obj).getUserId();
            } else if (obj instanceof Paper) {
                objId = ((Paper) obj).getPaperId();
            } else if (obj instanceof Review) {
                objId = ((Review) obj).getReviewId();
            }

            if (objId.equals(id)) {
                return Optional.of(obj);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(objects);
    }

    @Override
    public boolean update(T object) {
        String id = "";

        if (object instanceof User) {
            id = ((User) object).getUserId();
        } else if (object instanceof Paper) {
            id = ((Paper) object).getPaperId();
        } else if (object instanceof Review) {
            id = ((Review) object).getReviewId();
        }

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
                return true;
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
                return true;
            }
        }
        return false;
    }

    // These methods are no-ops for in-memory storage
    @Override
    public boolean saveAll() {
        return true;
    }

    @Override
    public void loadAll() {
    }
}