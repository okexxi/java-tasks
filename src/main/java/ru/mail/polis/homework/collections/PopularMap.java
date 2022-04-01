package ru.mail.polis.homework.collections;


import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Написать структуру данных, реализующую интерфейс мапы + набор дополнительных методов.
 * 4 дополнительных метода должны возвращать самый популярный ключ и его популярность. (аналогично для самого популярного значения)
 * Популярность - это количество раз, который этот ключ/значение учавствовал/ло в других методах мапы, такие как
 * containsKey, get, put, remove (в качестве параметра и возвращаемого значения).
 * Считаем, что null я вам не передаю ни в качестве ключа, ни в качестве значения
 * <p>
 * Так же надо сделать итератор (подробности ниже).
 * <p>
 * Важный момент, вам не надо реализовывать мапу, вы должны использовать композицию.
 * Вы можете использовать любые коллекции, которые есть в java.
 * <p>
 * Помните, что по мапе тоже можно итерироваться
 * <p>
 * for (Map.Entry<K, V> entry : map.entrySet()) {
 * entry.getKey();
 * entry.getValue();
 * }
 * <p>
 * Всего 10 тугриков (3 тугрика за общие методы, 2 тугрика за итератор, 5 тугриков за логику популярности)
 *
 * @param <K> - тип ключа
 * @param <V> - тип значения
 */
public class PopularMap<K, V> implements Map<K, V> {

    private final Map<K, V> map;
    private final Map<Object, Integer> keyPopularity = new HashMap<>();
    private final Map<Object, Integer> valuePopularity = new HashMap<>();

    private void addValuePopularity(Object key) {
        V value = map.get(key);
        if (value != null) {
            valuePopularity.compute(value, (k, v) -> (v == null) ? 1 : v + 1);
        }
    }

    public PopularMap() {
        this.map = new HashMap<>();
    }

    public PopularMap(Map<K, V> map) {
        this.map = map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        keyPopularity.compute(key, (k, v) -> (v == null) ? v = 1 : v + 1);
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        valuePopularity.compute(value, (k, v) -> (v == null) ? 1 : v + 1);
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        keyPopularity.compute(key, (k, v) -> (v == null) ? v = 1 : v + 1);
        V value = map.get(key);
        if (value != null) {
            valuePopularity.compute(value, (k, v) -> (v == null) ? 1 : v + 1);
        }
        return value;
    }

    @Override
    public V put(K key, V value) {
        keyPopularity.compute(key, (k, v) -> (v == null) ? 1 : v + 1);
        valuePopularity.compute(value, (k, v) -> (v == null) ? 1 : v + 1);
        addValuePopularity(key);
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        keyPopularity.compute(key, (k, v) -> (v == null) ? 1 : v + 1);
        addValuePopularity(key);
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    /**
     * Возвращает самый популярный, на данный момент, ключ
     */
    public K getPopularKey() {
        return (K) keyPopularity
                .entrySet().stream()
                .max(Entry.comparingByValue())
                .get().getKey();
    }


    /**
     * Возвращает количество использование ключа
     */
    public int getKeyPopularity(K key) {
        return keyPopularity.getOrDefault(key, 0);
    }

    /**
     * Возвращает самое популярное, на данный момент, значение. Надо учесть что значени может быть более одного
     */
    public V getPopularValue() {
        return (V) valuePopularity
                .entrySet().stream()
                .max(Entry.comparingByValue())
                .get().getKey();
    }

    /**
     * Возвращает количество использований значений в методах: containsValue, get, put (учитывается 2 раза, если
     * старое значение и новое - одно и тоже), remove (считаем по старому значению).
     */
    public int getValuePopularity(V value) {
        return valuePopularity.getOrDefault(value, 0);
    }

    /**
     * Вернуть итератор, который итерируется по значениям (от самых НЕ популярных, к самым популярным)
     * 2 тугрика
     */
    public Iterator<V> popularIterator() {
        return new MapIterator();
    }

    private class MapIterator implements Iterator<V> {

        List<V> list;

        public MapIterator() {
            list = valuePopularity.entrySet().stream()
                    .sorted(Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .map(value -> (V) value)
                    .collect(Collectors.toList());
        }

        @Override
        public boolean hasNext() {
            return !list.isEmpty();
        }

        @Override
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return list.remove(0);
        }
    }
}
