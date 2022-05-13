package structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiValMap<K, V> extends HashMap<K, ArrayList<V>> {

    @Override
    public ArrayList<V> put(K key, ArrayList<V> value) {
        this.computeIfPresent(key, (k, v) -> {
            v.addAll(value);
            return v;
        });
        this.putIfAbsent(key, value);
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends ArrayList<V>> m) {
        m.forEach((this::put));
    }

    public boolean putItem(K key, V value) {
        final AtomicBoolean b = new AtomicBoolean(false);
        this.computeIfPresent(key, (k, v) -> {
            v.add(value);
            b.set(true);
            return v;
        });
        this.computeIfAbsent(key, k -> {
            final ArrayList<V> v = new ArrayList<>();
            v.add(value);
            b.set(true);
            return v;
        });
        return b.get();
    }

}
