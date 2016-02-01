package ch.dietrich.database;

public interface AsyncTaskCompleteListener<T> {
	
	public void onTaskComplete(T result);
	
	public void onTaskError(String errorString);
}
