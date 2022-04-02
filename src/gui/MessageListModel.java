//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import gui.Nios2SimPanelMessage.message_type;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Iterator;

class MessageListModel implements ListModel {
    private ArrayList<Integer> messagesLine = new ArrayList();
    private ArrayList<message_type> messagesType = new ArrayList();
    private ArrayList<String> messages = new ArrayList();
    private ArrayList<ListDataListener> listeners = new ArrayList();

    MessageListModel() {
    }

    public void clear() {
        int var1 = this.messages.size();
        this.messagesLine.clear();
        this.messagesType.clear();
        this.messages.clear();
        Iterator var2 = this.listeners.iterator();

        while(var2.hasNext()) {
            ListDataListener var3 = (ListDataListener)var2.next();
            ListDataEvent var4 = new ListDataEvent(this, 2, 0, var1 - 1);
            var3.intervalAdded(var4);
        }

    }

    public void add(int var1, message_type var2, String var3) {
        this.messagesLine.add(var1);
        this.messagesType.add(var2);
        this.messages.add(var3);
        Iterator var4 = this.listeners.iterator();

        while(var4.hasNext()) {
            ListDataListener var5 = (ListDataListener)var4.next();
            ListDataEvent var6 = new ListDataEvent(this, 1, this.messages.size() - 1, this.messages.size() - 1);
            var5.intervalAdded(var6);
        }

    }

    public void addListDataListener(ListDataListener var1) {
        this.listeners.add(var1);
    }

    public Object getElementAt(int var1) {
        return this.messages.get(var1);
    }

    public message_type getType(int var1) {
        return (message_type)this.messagesType.get(var1);
    }

    public int getLine(int var1) {
        return (Integer)this.messagesLine.get(var1);
    }

    public int getSize() {
        return this.messages.size();
    }

    public void removeListDataListener(ListDataListener var1) {
        this.listeners.remove(var1);
    }
}
