package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class KeyFrames {
    public static HashMap<String, Constructor<? extends Key>> sKeyMakers;
    public HashMap<Integer, ArrayList<Key>> mFramesMap = new HashMap<>();

    static {
        HashMap<String, Constructor<? extends Key>> hashMap = new HashMap<>();
        sKeyMakers = hashMap;
        try {
            hashMap.put("KeyAttribute", KeyAttributes.class.getConstructor(new Class[0]));
            sKeyMakers.put("KeyPosition", KeyPosition.class.getConstructor(new Class[0]));
            sKeyMakers.put("KeyCycle", KeyCycle.class.getConstructor(new Class[0]));
            sKeyMakers.put("KeyTimeCycle", KeyTimeCycle.class.getConstructor(new Class[0]));
            sKeyMakers.put("KeyTrigger", KeyTrigger.class.getConstructor(new Class[0]));
        } catch (NoSuchMethodException e) {
            Log.e("KeyFrames", "unable to load", e);
        }
    }

    public final void addKey(Key key) {
        if (!this.mFramesMap.containsKey(Integer.valueOf(key.mTargetId))) {
            this.mFramesMap.put(Integer.valueOf(key.mTargetId), new ArrayList());
        }
        this.mFramesMap.get(Integer.valueOf(key.mTargetId)).add(key);
    }

    public KeyFrames(Context context, XmlPullParser xmlPullParser) {
        HashMap<String, ConstraintAttribute> hashMap;
        Key key;
        Exception e;
        Key key2 = null;
        try {
            int eventType = xmlPullParser.getEventType();
            while (eventType != 1) {
                if (eventType == 2) {
                    String name = xmlPullParser.getName();
                    if (sKeyMakers.containsKey(name)) {
                        try {
                            key = (Key) sKeyMakers.get(name).newInstance(new Object[0]);
                            try {
                                key.load(context, Xml.asAttributeSet(xmlPullParser));
                                addKey(key);
                            } catch (Exception e2) {
                                e = e2;
                            }
                        } catch (Exception e3) {
                            Exception exc = e3;
                            key = key2;
                            e = exc;
                            Log.e("KeyFrames", "unable to create ", e);
                            key2 = key;
                            eventType = xmlPullParser.next();
                        }
                        key2 = key;
                    } else if (!(!name.equalsIgnoreCase("CustomAttribute") || key2 == null || (hashMap = key2.mCustomConstraints) == null)) {
                        ConstraintAttribute.parse(context, xmlPullParser, hashMap);
                    }
                } else if (eventType == 3) {
                    if ("KeyFrameSet".equals(xmlPullParser.getName())) {
                        return;
                    }
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e4) {
            e4.printStackTrace();
        } catch (IOException e5) {
            e5.printStackTrace();
        }
    }

    public void addFrames(MotionController motionController) {
        ArrayList arrayList = this.mFramesMap.get(Integer.valueOf(motionController.mId));
        if (arrayList != null) {
            motionController.addKeys(arrayList);
        }
        ArrayList arrayList2 = this.mFramesMap.get(-1);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                Key key = (Key) it.next();
                if (key.matches(((ConstraintLayout.LayoutParams) motionController.mView.getLayoutParams()).constraintTag)) {
                    motionController.addKey(key);
                }
            }
        }
    }
}
