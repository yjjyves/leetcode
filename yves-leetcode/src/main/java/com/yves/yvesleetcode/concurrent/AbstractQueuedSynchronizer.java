package com.yves.yvesleetcode.concurrent;

import sun.misc.Unsafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements java.io.Serializable {
    protected AbstractQueuedSynchronizer() {
    }

    //当前同步器的状态
    private volatile int state;

    //等待队列的头结点
    private transient volatile Node head;

    //等待队列的尾结点
    private transient volatile Node tail;

    static final class Node {
        //前节点
        volatile Node prev;

        //前节点
        volatile Node next;

        //等待的线程
        volatile Thread thread;

        //等待状态,该状态实际代表的是后继节点的等待状态 todo why?
        int waitStatus;

        //condition 用作条件等待锁
        Node nextWaiter;

        /**
         * 共享模式
         */
        static final Node SHARED = new Node();

        /**
         * 排他模式
         */
        static final Node EXCLUSIVE = null;

        /**
         * 此节点取消获取锁
         */
        static final int CANCELLED = 1;

        /**
         * 表示后续线程需要等待获取锁
         */
        static final int SIGNAL = -1;

        /**
         * 等待 condition锁
         */
        static final int CONDITION = -2;
        /**
         * waitStatus value to indicate the next acquireShared should
         * unconditionally propagate
         */
        static final int PROPAGATE = -3;

        public Node(Thread currentThread, Node mode) {
            this.thread = currentThread;
            this.nextWaiter = mode;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }

        public Node() {
        }

        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

    }

    //获取排他锁
    public void acquire(int args) {
        //尝试获取锁
        boolean tryAcquire = tryAcquire(args);
        //尝试获取锁失败,加入到等待队列
        if (!tryAcquire) {
            //添加到等待队列
            Node addWaiter = addWaiter(Node.EXCLUSIVE);
            //继续获取锁
            if (acquireQueued(addWaiter, args)) ;
        }
    }

    //释放锁
    public final boolean release(int arg) {
        //释放锁之后 需要重新设置头结点 ,并唤醒下一等待的节点
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0) {
                unparkSuccessor(h);
                return true;
            }
        }
        return false;
    }


    //自旋获取锁
    private boolean acquireQueued(final Node node, int arg) {
        //标记是否获取锁失败
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (; ; ) {
                //获取当前节点的前驱节点
                final Node p = node.predecessor();
                //如果当前节点的前驱节点是头节点 则可以再次尝试获取锁,因为前驱节点不是头节点 说明前面还有其他节点在等待获取锁,不必再做多余的获取锁的行为
                if (head == p && tryAcquire(arg)) {
                    //获取锁成功之后,需要移除当前节点,重新设置头节点
                    setHead(node);
                    //取消头结点的后驱节点 help GC
                    p.next = null;
                    failed = false;

                    return interrupted;
                }
                //如果没有获取到锁,则1 判断是否要park,等待unpark再去尝试获取锁 2 如果需要park则park
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt()) {
                    interrupted = true;
                }
            }
        } finally {
            if (failed) {
                cancelAcquire(node);
            }
        }
    }

    //判断获取锁之后是否需要park进入等待
    private boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        //前驱节点的等待状态
        int waitStatus = pred.waitStatus;
        //-1 代表前驱的后继节点需要等待
        if (waitStatus == Node.SIGNAL) {
            return true;
        }
        //代表前驱节点已经取消获取锁,需要将前驱节点从等待队列中移除
        if (waitStatus > 0) {
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            //将新的前驱节点与当前节点关联起来
            pred.next = node;
        } else {
            //0 -2 -3 未获取到锁的节点都设置等待状态为-1此后不需要要自旋浪费CPU资源获取锁,等待被通知去获取锁即可
            //0代表初始添加的节点 还未设置等待状态,设置后继节点的等待状态
            //cas设置节点的等待状态
            compareAndSetWaitStatus(pred, waitStatus, Node.SIGNAL);
        }
        return false;
    }

    private boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

    //将该线程添加到等待队列
    private Node addWaiter(Node mode) {
        //1 构造节点
        Node node = new Node(Thread.currentThread(), mode);

        //2 将节点添加到队尾
        Node pred = tail;
        //前节点不为空
        if (pred != null) {
            //如果先给tail节点设置next节点,如果失败则无法回滚,有影响 先给该节点赋前驱节点
            node.prev = tail;
            //因为有并发情况,所依cas给同步器设置为节点
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }

        //如果添加到队尾失败/队尾节点为空,需要初始化并自旋加入队尾
        enq(node);

        return node;
    }

    //悲观入队
    private Node enq(Node node) {
        //自旋加入队尾
        for (; ; ) {
            Node t = tail;
            //头尾节点未初始化则初始化
            if (t == null) {
                //cas设置初始头结点
                if (compareAndSetHead(head, new Node())) {
                    tail = head;
                }
            } else {
                node.prev = tail;
                if (compareAndSetTail(tail, node)) {
                    tail.next = node;
                    return t;
                }
            }
        }
    }

    //设置头节点
    private void setHead(Node node) {
        head = node;
    }


    //TODO 为啥不是直接A-B-C 变成A-C
    private void cancelAcquire(final Node node) {
        if (node == null) {
            return;
        }
        node.thread = null;
        //前驱节点
        Node pred = node.prev;

        //找到当前节点的不是0的前驱节点,也就是跳过状态为0的前驱无效节点
        while (pred.waitStatus > 0) {
            node.prev = pred = pred.prev;
        }

        Node predNext = pred.next;
        node.waitStatus = Node.CANCELLED;

        //如果是尾结点 单独处理
        if (node == tail && compareAndSetTail(node, pred)) {
            //将前驱节点的后继节点置为空
            compareAndSetNext(pred, predNext, null);
        } else {
            int predWaitStatus = pred.waitStatus;
            //前驱节点状态是-1 / 前驱节点设置状态为-1成功
            boolean isSignal = predWaitStatus == Node.SIGNAL ||
                    (predWaitStatus <= 0 && compareAndSetWaitStatus(pred, predWaitStatus, Node.SIGNAL));
            if (pred != head && pred.thread != null && isSignal) {
                Node next = node.next;
                //当前节点的后继节点是有效节点,则cas设置成前驱节点的后继节点
                if (next != null && next.waitStatus <= 0) {
                    compareAndSetNext(pred, predNext, next);
                }
            } else {
                //todo 如果是头结点！ 唤醒下一节点获取锁
                unparkSuccessor(node);
            }
            // 最后将当前节点的后继节点设置为Null，help GC
            node.next = null;
        }
    }


    private void unparkSuccessor(Node node) {
        int ws = node.waitStatus;
        if (ws < 0) {
            compareAndSetWaitStatus(node, ws, 0);
        }

        //获取当前节点的后继节点,以唤醒下一节点去获取锁
        Node s = node.next;
        //如果当前节点已经无效
        if (s == null || s.waitStatus > 0) {
            s = null;
            //从尾节点倒序去获取里链表上最后一个有效节点? 为什么不正序去遍历呢？
            //因为在每个Node中的后继节点都是cas设置的一定有效的,而在addWaiter方法中是设置前驱节点,并不能保证在并发情况是有效的前驱节点
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        if (s != null)
            LockSupport.unpark(s.thread);
    }

    //判断当前节点是否有入队等待获取锁
    public final boolean hasQueuedPredecessors() {
        Node t = tail;
        Node h = head;
        Node s;
        //h != t 意味着不是刚初始化
        //h.next == null代表只有一个头结点 或者 头结点的下一节点不是当前节点 s.thread != Thread.currentThread() 代表当前线程并未入队
        return h != t && ((s = h.next) == null || s.thread != Thread.currentThread());
    }

    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }


    public class ConditionObject implements Condition, java.io.Serializable {
        private transient Node firstWaiter;
        /**
         * Last node of condition queue.
         */
        private transient Node lastWaiter;

        public ConditionObject() {
        }

        /**
         * Mode meaning to reinterrupt on exit from wait
         */
        private static final int REINTERRUPT = 1;
        /**
         * Mode meaning to throw InterruptedException on exit from wait
         */
        private static final int THROW_IE = -1;

        @Override
        public void await() throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            //添加等待节点到等待队列
            Node node = addConditionWaiter();
            //释放锁,并保存同步器的状态
            int savedState = fullyRelease(node);

            int interruptMode = 0;
            //是否是在同步队列去获取锁,如果不是则等待
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                //如果当前节点没有中断 则跳出等待
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            //重新获取锁
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            //TODO ??
            if (node.nextWaiter != null) // clean up if cancelled
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
        }

        @Override
        public boolean await(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void signal() {
            //是否是当前线程占有锁
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node first = firstWaiter;
            if (first != null)
                doSignal(first);
        }

        /**
         * 添加一个新的节点到等待队列
         *
         * @return its new wait node
         */
        private Node addConditionWaiter() {
            Node t = lastWaiter;
            // 如果lastWaiter取消了 ,则遍历链表清楚出去
            if (t != null && t.waitStatus != Node.CONDITION) {
                unlinkCancelledWaiters();
                t = lastWaiter;
            }
            Node node = new Node(Thread.currentThread(), Node.CONDITION);
            if (t == null)
                firstWaiter = node;
            else
                t.nextWaiter = node;
            lastWaiter = node;
            return node;
        }

        //遍历移除已取消等待的节点
        private void unlinkCancelledWaiters() {
            //遍历的节点
            Node t = firstWaiter;

            //
            Node trail = null;
            while (t != null) {
                Node next = t.nextWaiter;
                if (t.waitStatus != Node.CONDITION) {
                    //等待节点不是等待状态,则需要移除节点
                    t.nextWaiter = null;

                    //前驱节点是空,意思是头节点为空,则将后继节点设置为头节点
                    if (trail == null)
                        firstWaiter = next;
                    else
                        trail.nextWaiter = next;
                    //如果没有后继接节点,将当前节点设置为尾节点
                    if (next == null)
                        lastWaiter = trail;
                } else {
                    trail = t;
                }
                //将后继节点当做检查检点
                t = next;
            }
        }

        private int fullyRelease(Node node) {
            boolean failed = true;
            try {
                int savedState = getState();
                //释放锁
                if (release(savedState)) {
                    failed = false;
                    return savedState;
                } else {
                    throw new IllegalMonitorStateException();
                }
            } finally {
                if (failed)
                    node.waitStatus = Node.CANCELLED;
            }
        }

        private boolean isOnSyncQueue(Node node) {
            //如果没有前驱节点 或者状态为等待,则不在队列中
            //// 以节点状态作为判断条件，如果等于CONDITION（说明在等待队列中）、或者前置节点为空，是一个独立节点
            if (node.waitStatus == Node.CONDITION || node.prev == null)
                return false;
            //如果有后继节点 说明它还在同步队列中。todo 不是一个node怎么会是在同步队列中？
            if (node.next != null) // If has successor, it must be on queue
                return true;
            //遍历整个同步队列
            return findNodeFromTail(node);
        }

        private boolean findNodeFromTail(Node node) {
            Node t = tail;
            for (; ; ) {
                if (t == node)
                    return true;
                if (t == null)
                    return false;
                t = t.prev;
            }
        }


        private int checkInterruptWhileWaiting(Node node) {
            return Thread.interrupted() ?
                    //线程已中断则取消等待,并从等待队列转到同步队列获取锁
                    (transferAfterCancelledWait(node) ? THROW_IE : REINTERRUPT) :
                    0;
        }

        private void reportInterruptAfterWait(int interruptMode) throws InterruptedException {
            if (interruptMode == THROW_IE)
                throw new InterruptedException();
            else if (interruptMode == REINTERRUPT)
                selfInterrupt();
        }

        void selfInterrupt() {
            Thread.currentThread().interrupt();
        }

        private void doSignal(Node first) {
            do {
                if ((firstWaiter = first.nextWaiter) == null)
                    lastWaiter = null;
                first.nextWaiter = null;
            } while (!transferForSignal(first) &&
                    (first = firstWaiter) != null);
        }

        final boolean transferForSignal(Node node) {
            /*
             * If cannot change waitStatus, the node has been cancelled.
             */
            if (!compareAndSetWaitStatus(node, Node.CONDITION, 0))
                return false;

            /*
             * Splice onto queue and try to set waitStatus of predecessor to
             * indicate that thread is (probably) waiting. If cancelled or
             * attempt to set waitStatus fails, wake up to resync (in which
             * case the waitStatus can be transiently and harmlessly wrong).
             */
            Node p = enq(node);
            int ws = p.waitStatus;
            if (ws > 0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL))
                LockSupport.unpark(node.thread);
            return true;
        }


        final boolean transferAfterCancelledWait(Node node) {
            //设置当前节点的等待状态
            if (compareAndSetWaitStatus(node, Node.CONDITION, 0)) {
                //入同步队列
                enq(node);
                return true;
            }
            /*
             * If we lost out to a signal(), then we can't proceed
             * until it finishes its enq().  Cancelling during an
             * incomplete transfer is both rare and transient, so just
             * spin.
             */
            while (!isOnSyncQueue(node))
                Thread.yield();
            return false;
        }
    }

    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    static {
        try {
            //同步器各个状态在内存中相对于对象的起始偏移量
            stateOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("tail"));

            //等待节点Node变量在内存中相对于对象的起始偏移量
            waitStatusOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("next"));

        } catch (Exception ex) {
            throw new Error(ex);
        }
    }


    /**
     * CAS tail field. Used only by enq.
     */
    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    private final boolean compareAndSetHead(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, expect, update);
    }

    private static final boolean compareAndSetWaitStatus(Node node, int expect, int update) {
        return unsafe.compareAndSwapObject(node, waitStatusOffset, expect, update);
    }

    private static final boolean compareAndSetNext(Node node, Node expect, Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }

    protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }


}
