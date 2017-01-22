package eg.edu.alexu.csd.filestructure.sort;

import java.util.ArrayList;

public class Sort<T extends Comparable<T>> implements ISort<T> {
	@Override
	public IHeap<T> heapSort(ArrayList<T> unordered) {
	    Heap<T> heap = new Heap<T> ();
		if (unordered.size() == 0) {
			return heap;
		}
		int sz = unordered.size();
		for (int i = sz / 2; i >= 0; i--) {
			heapify(unordered, i, sz);
		}
		for (int i = sz - 1; i > 0; i--) {
			T temp = unordered.get(0);
			unordered.set(0, unordered.get(i));
			unordered.set(i, temp);
			sz--;
			heapify(unordered, 0, sz);
		}
		/*heap.build(unordered);
		IHeap<T> ret =  heap.clone();
		for(int i=unordered.size()-1;i>=0;i--){
			unordered.set(i, ret.extract());
		}*/
		heap.setList(unordered);
		return heap;
	}
	
	public void heapify(ArrayList<T> list, int current, int sz) {
		int  l = 2 * current + 1;
		while (l < sz) {
			int mx = l;
			if (l + 1 < sz) {
				if (list.get(l + 1).compareTo(list.get(l)) > 0) {
					mx = l + 1;
				}
			}
			if (list.get(mx).compareTo(list.get(current)) > 0) {
				T temp = list.get(current);
				list.set(current, list.get(mx));
				list.set(mx, temp);
				current = mx;
				l = 2 * current + 1;
			}
			else {
				break;
			}
		}
	}

	@Override
	public void sortSlow(ArrayList<T> unordered) {
		if (unordered == null) {
			return;
		}
		if (unordered.isEmpty()) {
			return;
		}
		for (int i = 0; i < unordered.size(); i++) {
			for (int j = 0; j < unordered.size() - 1; j++) {
				if (unordered.get(j)
						.compareTo(unordered.get(j + 1)) > 0) {
					T temp = unordered.get(j);
					unordered.set(j, unordered.get(j + 1));
					unordered.set(j + 1, temp);
				}
			}
		}
		
	}

	@Override
	public void sortFast(ArrayList<T> unordered) {
		sortQuick(unordered, 0, unordered.size() - 1);
	}
	private void sortQuick(ArrayList<T> arr, int l, int r) {
		if (l < r) {
			int target = divide(arr, l, r);
			sortQuick(arr, l, target - 1);
			sortQuick(arr, target + 1, r);
		}
	}
	private int divide(ArrayList<T> arr, int l, int r) {
		T pivot = arr.get(r);
		int j = l - 1;
		for (int i = l; i <= r; i++) {
			if (arr.get(i).compareTo(pivot) <= 0) {
				j++;
				T temp = arr.get(i);
				arr.set(i, arr.get(j));
				arr.set(j, temp);
			}
		}
		return j;
	}
	
	/*private ArrayList<T> merge(ArrayList<T> arr, int l, int r){
		if(l>=r)return arr;
		merge(arr, l, (l+r)/2);
		merge(arr, (l+r)/2+1, r);
		int i=l, j=(l+r)/2+1;
		ArrayList<T> arr2 = new ArrayList<T>();
		while(i<=(l+r)/2&&j<=r){
			if(arr.get(i).compareTo(arr.get(j))>0){
				arr2.add(arr.get(j));
				j++;
			}
			else{
				arr2.add(arr.get(i));
				i++;
			}
		}
		while(i<=(l+r)/2){
			arr2.add(arr.get(i));
			i++;
		}
		while(j<=r){
			arr2.add(arr.get(j));
			j++;
		}
		for(int k=l;k<=r;k++){
			arr.set(k, arr2.get(k-l));
		}
		return arr;
	}
	*/
}
