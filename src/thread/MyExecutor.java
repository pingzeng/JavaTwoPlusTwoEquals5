package thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class MyThread implements Runnable{

	private List<Integer> myList;
	private Vector<Integer> myVector;
	private Object host;
	public MyThread(List<Integer> list, Vector<Integer> vector, Object object){
		this.myList = list;
		this.myVector = vector;
		this.host = object;
	}
	
	public void updateListSafe(){
		synchronized(this.host){
			ArrayList<Integer> safe = new ArrayList<Integer>();
			safe.add(1);
		}
	}
	
	private void updateList(int i){
		synchronized(this.host){
			myList.add(i);
		}
	}
	
	public void updateListSafe(int i){
			myList.add(i);
	}
	private void updateVector(int i){
		myVector.add(i);
	}
	
	@Override
	public void run() {
		for( int i = 0; i < 10;i++){
			updateListSafe(i);
			// updateVector(i);
		}
		System.out.println("end: " + myList.size());
	}
	
}
public class MyExecutor {

	private ArrayList<Integer> taskList = new ArrayList<Integer>();
	private List<Integer> safe = Collections.synchronizedList(taskList);
	private Vector<Integer> vector = new Vector<Integer>();
	private Object object = new Object();
	private void launch(){
		
        ExecutorService executorService= Executors.newFixedThreadPool(10);

        executorService.execute(new MyThread(safe, vector, object));
        executorService.execute(new MyThread(safe, vector, object));
	}
	public static void main(String[] args) {
		MyExecutor test = new MyExecutor();
		test.launch();
	}

}
