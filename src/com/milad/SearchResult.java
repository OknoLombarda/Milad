package com.milad;

public class SearchResult<T> implements Comparable<SearchResult<?>> {
	private T result;
	private double index;
	
	public SearchResult(T result, double index) {
		this.result = result;
		this.index = index;
	}
	
	public T getResult() {
		return result;
	}
	
	public double getIndex() {
		return index;
	}
	
	public int compareTo(SearchResult<?> other) {
		return Double.compare(other.getIndex(), index);
	}
	
	public String toString() {
		return "[".concat(result.toString()).concat(", index=").concat(String.valueOf(index)).concat("]");
	}
	
	public boolean equals(Object other) {
		if (this == other)
			return true;
		
		if (other == null)
			return false;
		
		if (this.getClass() != other.getClass())
			return false;
		
		SearchResult<?> otherResult = (SearchResult<?>) other;
		return result.equals(otherResult.getResult()) && index == otherResult.getIndex();
	}
	
	public int hashCode() {
		return (int) (result.hashCode() + index * 12);
	}
}
