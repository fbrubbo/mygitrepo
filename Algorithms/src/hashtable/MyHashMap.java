package hashtable;

public class MyHashMap<K, V> {
	Entry<K, V>[] table;
	private int size = 0;

	@SuppressWarnings("unchecked")
	public MyHashMap(int size) {
		this.table = new Entry[size];
	}

	public void put(K key, V value) {
		if (key == null || value == null)
			return;

		V existingValue = get(key);
		if(existingValue!=null)
			remove(key);
		
		Entry<K, V> newEntry = new Entry<K, V>(key, value);
		int posi = key.hashCode() % table.length;

		if (table[posi] == null) {
			table[posi] = newEntry;
		} else {
			Entry<K, V> current = table[posi];
			while (current.next != null) {
				current = current.next;
			}
			current.next = newEntry;
			newEntry.prev = current;
		}
		size++;
	}

	public V get(K key) {
		if (key == null)
			return null;

		int posi = key.hashCode() % table.length;
		for (Entry<K, V> current = table[posi]; current != null; current = current.next) {
			if (current.key.equals(key)) {
				return current.value;
			}
		}

		return null;
	}

	public void remove(K key) {
		if (key == null)
			return;

		int posi = key.hashCode() % table.length;
		for (Entry<K, V> current = table[posi]; current != null; current = current.next) {
			if (current.key.equals(key)) {
				Entry<K, V> prev = current.prev;
				Entry<K, V> next = current.next;
				if (prev == null) {
					table[posi] = next;
					next.prev = null;
				} else {
					prev.next = next;
					if(next!=null)
						next.prev = prev;
				}
				size--;
				return;
			}
		}
	}

	public int size() {
		return size;
	}

	static class Entry<K1, V1> {
		K1 key;
		V1 value;
		Entry<K1, V1> prev;
		Entry<K1, V1> next;

		public Entry(K1 key, V1 value) {
			this.key = key;
			this.value = value;
		}
	}
}