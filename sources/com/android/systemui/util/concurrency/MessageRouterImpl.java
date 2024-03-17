package com.android.systemui.util.concurrency;

import com.android.systemui.util.concurrency.MessageRouter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageRouterImpl implements MessageRouter {
    public final Map<Class<Object>, List<Runnable>> mDataMessageCancelers = new HashMap();
    public final Map<Class<?>, List<MessageRouter.DataMessageListener<Object>>> mDataMessageListenerMap = new HashMap();
    public final DelayableExecutor mDelayableExecutor;
    public final Map<Integer, List<Runnable>> mIdMessageCancelers = new HashMap();
    public final Map<Integer, List<MessageRouter.SimpleMessageListener>> mSimpleMessageListenerMap = new HashMap();

    public MessageRouterImpl(DelayableExecutor delayableExecutor) {
        this.mDelayableExecutor = delayableExecutor;
    }

    public void sendMessageDelayed(int i, long j) {
        addCanceler(i, this.mDelayableExecutor.executeDelayed(new MessageRouterImpl$$ExternalSyntheticLambda0(this, i), j));
    }

    public void sendMessageDelayed(Object obj, long j) {
        addCanceler((Class<Object>) obj.getClass(), this.mDelayableExecutor.executeDelayed(new MessageRouterImpl$$ExternalSyntheticLambda1(this, obj), j));
    }

    public void cancelMessages(int i) {
        synchronized (this.mIdMessageCancelers) {
            if (this.mIdMessageCancelers.containsKey(Integer.valueOf(i))) {
                for (Runnable run : this.mIdMessageCancelers.get(Integer.valueOf(i))) {
                    run.run();
                }
                this.mIdMessageCancelers.remove(Integer.valueOf(i));
            }
        }
    }

    public <T> void cancelMessages(Class<T> cls) {
        synchronized (this.mDataMessageCancelers) {
            if (this.mDataMessageCancelers.containsKey(cls)) {
                for (Runnable run : this.mDataMessageCancelers.get(cls)) {
                    run.run();
                }
                this.mDataMessageCancelers.remove(cls);
            }
        }
    }

    public void subscribeTo(int i, MessageRouter.SimpleMessageListener simpleMessageListener) {
        synchronized (this.mSimpleMessageListenerMap) {
            this.mSimpleMessageListenerMap.putIfAbsent(Integer.valueOf(i), new ArrayList());
            this.mSimpleMessageListenerMap.get(Integer.valueOf(i)).add(simpleMessageListener);
        }
    }

    public <T> void subscribeTo(Class<T> cls, MessageRouter.DataMessageListener<T> dataMessageListener) {
        synchronized (this.mDataMessageListenerMap) {
            this.mDataMessageListenerMap.putIfAbsent(cls, new ArrayList());
            this.mDataMessageListenerMap.get(cls).add(dataMessageListener);
        }
    }

    public final void addCanceler(int i, Runnable runnable) {
        synchronized (this.mIdMessageCancelers) {
            this.mIdMessageCancelers.putIfAbsent(Integer.valueOf(i), new ArrayList());
            this.mIdMessageCancelers.get(Integer.valueOf(i)).add(runnable);
        }
    }

    public final void addCanceler(Class<Object> cls, Runnable runnable) {
        synchronized (this.mDataMessageCancelers) {
            this.mDataMessageCancelers.putIfAbsent(cls, new ArrayList());
            this.mDataMessageCancelers.get(cls).add(runnable);
        }
    }

    /* renamed from: onMessage */
    public final void lambda$sendMessageDelayed$0(int i) {
        synchronized (this.mSimpleMessageListenerMap) {
            if (this.mSimpleMessageListenerMap.containsKey(Integer.valueOf(i))) {
                for (MessageRouter.SimpleMessageListener onMessage : this.mSimpleMessageListenerMap.get(Integer.valueOf(i))) {
                    onMessage.onMessage(i);
                }
            }
        }
        synchronized (this.mIdMessageCancelers) {
            if (this.mIdMessageCancelers.containsKey(Integer.valueOf(i)) && !this.mIdMessageCancelers.get(Integer.valueOf(i)).isEmpty()) {
                this.mIdMessageCancelers.get(Integer.valueOf(i)).remove(0);
                if (this.mIdMessageCancelers.get(Integer.valueOf(i)).isEmpty()) {
                    this.mIdMessageCancelers.remove(Integer.valueOf(i));
                }
            }
        }
    }

    /* renamed from: onMessage */
    public final void lambda$sendMessageDelayed$1(Object obj) {
        synchronized (this.mDataMessageListenerMap) {
            if (this.mDataMessageListenerMap.containsKey(obj.getClass())) {
                for (MessageRouter.DataMessageListener onMessage : this.mDataMessageListenerMap.get(obj.getClass())) {
                    onMessage.onMessage(obj);
                }
            }
        }
        synchronized (this.mDataMessageCancelers) {
            if (this.mDataMessageCancelers.containsKey(obj.getClass()) && !this.mDataMessageCancelers.get(obj.getClass()).isEmpty()) {
                this.mDataMessageCancelers.get(obj.getClass()).remove(0);
                if (this.mDataMessageCancelers.get(obj.getClass()).isEmpty()) {
                    this.mDataMessageCancelers.remove(obj.getClass());
                }
            }
        }
    }
}
