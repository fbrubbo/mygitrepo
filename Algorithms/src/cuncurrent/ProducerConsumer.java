package cuncurrent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class ProducerConsumer {

    private static Queue<Integer> queue = new LinkedList<>();
    private static Semaphore sem4Consumer = new Semaphore(0);
    private static Semaphore sem4Producer = new Semaphore(10);
    
    private static synchronized void printQueue() {
    	for (Integer i : queue) {
			System.out.print(", " + i);
		}
    	System.out.println();
    }

	private static synchronized Integer get() {
		return queue.poll();
	}
	
	private static synchronized void add(Integer info) {
		queue.add(info);
		printQueue();
	}
	
	private static void consume(String name) throws InterruptedException {
		sem4Consumer.acquire(1);
		System.out.println("Consumer \""+name+"\" read: "+ get());
		sem4Producer.release(1);
	}
	
	private static void produce(Integer info) throws InterruptedException {
		sem4Producer.acquire(1);
		add(info);
        sem4Consumer.release(1);
	}

    static class Consumer extends Thread {
        String name;
        public Consumer(String name) {
            this.name = name;
        }
        public void run() {
        	try {
	        	while (true) {
	            	consume(name);
	                Thread.sleep(500);
	            }
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}

        }
    }

    static class Producer extends Thread {
        static int info = 0;
        public void run() {
            try {
                while (true) {
                	produce(info++);
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String [] args) {
        new Consumer("Alice").start();
        new Producer().start();
        new Consumer("Bob").start();
    }
}