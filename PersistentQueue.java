import java.util.NoSuchElementException;

/**
 * This Queue class presents an immutable FIFO query of objects. The
 * implementation comes from Eric Lippert's "Immutability in C#".
 * 
 * 
 */
public class PersistentQueue<E> {
	private PersistentStack<E> enqueueStack;
	private PersistentStack<E> dequeueStack;

	@SuppressWarnings("unchecked")
	public PersistentQueue() {
		this.enqueueStack = PersistentStack.empty();
		this.dequeueStack = PersistentStack.empty();
	}

	private PersistentQueue(PersistentStack<E> enqueueStack,
			PersistentStack<E> dequeueStack) {
		this.enqueueStack = enqueueStack;
		this.dequeueStack = dequeueStack;
	}

	/**
	 * Returns the queue that adds an item into tail of this without modifying
	 * queue.
	 * 
	 * <pre>
	 * e.g. 
	 * When this queue represents the (2, 1, 2, 2, 6) and we enqueue value 4 into queue,
	 * this method returns a new queue (2, 1, 2, 2, 6, 4)
	 * and this object still represents the queue (2, 1, 2, 2, 6)
	 * </pre>
	 * 
	 * If the element e is null, throws IllegalArgumentException.
	 * 
	 * @param e
	 * @return the queue that adds an item into tail of this without modifying
	 *         queue
	 * @throws IllegalArgumentException
	 */

	@SuppressWarnings("unchecked")
	public PersistentQueue<E> enqueue(E e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		if (this.size() == 0) {// can help peek() achieve O(1), because no
								// enqueueStack.reverse() needed.
			return new PersistentQueue<E>(PersistentStack.empty(),
					this.dequeueStack.push(e));
		}
		return new PersistentQueue<E>(this.enqueueStack.push(e),
				this.dequeueStack);
	}

	/**
	 * Returns the queue that removes the object at the head of this queue
	 * without modifying this queue.
	 * 
	 * <pre>
	 * e.g.
	 * When this queue represents the queue (7,1,3,3,5,1),
	 * this method returns (1,3,3,5,1) and this object still represents the queue (7,1,3,3,5,1)
	 * </pre>
	 * 
	 * If the queue is empty, throws java.util.NoSuchElementException.
	 * 
	 * @return a new PersistentQueue that removes the object at the head of this
	 *         queue.
	 * @throws java.util.NoSuchElementException
	 */
	@SuppressWarnings("unchecked")
	public PersistentQueue<E> dequeue() {
		if (size() == 0) {
			throw new NoSuchElementException();
		} else if (this.dequeueStack.size() > 1) { // dequeueStack.size > 1
			return new PersistentQueue<E>(this.enqueueStack,
					this.dequeueStack.pop());
		} else if (this.enqueueStack.isEmpty()) { // dequeueStack.size = 1 &&
													// enqueueStack.size = 0
			return new PersistentQueue<E>(PersistentStack.empty(),
					PersistentStack.empty());
		} else { // enqueueStack.size > 0
			return new PersistentQueue<E>(PersistentStack.empty(),
					this.enqueueStack.reverse());
		}
	}

	/**
	 * Looks at the objects which is the head of this queue without removing it
	 * form the queue.
	 * 
	 * <pre>
	 * e.g.
	 * When this queue represents the queue (7,1,3,3,5,1),
	 * this method returns 7 and this object still represents the queue (7,1,3,3,5,1)
	 * </pre>
	 * 
	 * If the queue is empty, throws java.util.NoSuchElementException.
	 * 
	 * @return the head of the queue
	 * @throws java.util.NoSuchElementException
	 */

	public E peek() {
		if (this.size() == 0) {
			throw new NoSuchElementException();
		}
		return this.dequeueStack.peek();
	}

	/**
	 * Returns the number of objects in this queue
	 * 
	 * @return the number of objects in this queue
	 */
	public int size() {
		return this.enqueueStack.size() + this.dequeueStack.size();
	}

}

/**
 * This Stack class presents an immutable LIFO stack of objects. The
 * implementation idea is from Eric Lippert's "Immutability in C#".
 * 
 * @author Ouyang Siyu
 * 
 */

class PersistentStack<E> {
	private E _head;
	private PersistentStack<E> _tail;
	private int _size;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final static PersistentStack EMPTY_STACK = new PersistentStack(
			null, null, 0);

	private PersistentStack(E head, PersistentStack<E> tail, int size) {
		this._head = head;
		this._tail = tail;
		this._size = size;
	}

	public PersistentStack<E> push(E head) {
		return new PersistentStack<E>(head, this, _size + 1);
	}

	public PersistentStack<E> reverse() {
		@SuppressWarnings("unchecked")
		PersistentStack<E> reversedStack = PersistentStack.EMPTY_STACK;
		PersistentStack<E> tmp = this;
		while (!tmp.isEmpty()) {
			reversedStack = reversedStack.push(tmp.peek());
			tmp = tmp.pop();
		}
		return reversedStack;
	}

	@SuppressWarnings("rawtypes")
	public static PersistentStack empty() {
		return EMPTY_STACK;
	}

	public boolean isEmpty() {
		return this == null || (this._size == 0);
	}

	public E peek() {
		if (this.isEmpty()) {
			throw new NoSuchElementException();
		}
		return this._head;
	}

	public PersistentStack<E> pop() {
		if (this.isEmpty()) {
			throw new NoSuchElementException();
		}
		return this._tail;
	}

	public int size() {
		return this._size;
	}

	
	public static void print(String head, PersistentQueue<Integer> q){
		System.out.println("==============="+head+"================");
		System.out.println("size :"+q.size());
		int size = q.size();
		PersistentQueue<Integer> tmp = q;
		if(size > 0){
			System.out.println("peek :"+q.peek());
		}
		while(size > 0){
			System.out.print(tmp.peek()+" ");
			tmp = tmp.dequeue();
			size = tmp.size();
		}
		System.out.println();
	}
	
	public static void main(String args[]){
		PersistentQueue<Integer> q1 = new PersistentQueue<Integer>();
		PersistentQueue<Integer> q2 = q1.enqueue(7).enqueue(1).enqueue(3).enqueue(3).enqueue(5).enqueue(1);
		print("Empty queue",q1);
		print("Queue 7 1 3 3 5 1",q2);
		PersistentQueue<Integer> q3 = q2.dequeue().dequeue();
		print("Queue 3 3 5 1",q3);
		PersistentQueue<Integer> q4 = q3.dequeue().dequeue().dequeue().dequeue();
		print("Empty Queue",q4);
		print("Queue 3 3 5 1",q3);
		print("Queue 7 1 3 3 5 1",q2);
		print("Queue 3 3 5 1 8 9",q3.enqueue(8).enqueue(9));
		print("Queue 10 1",q4.enqueue(10).enqueue(1));
		print("Queue 1",q4.enqueue(10).enqueue(1).dequeue());
		print("Empty Queue",q4.enqueue(10).enqueue(1).dequeue().dequeue());
		print("Empty queue",q1);
	}
	
}