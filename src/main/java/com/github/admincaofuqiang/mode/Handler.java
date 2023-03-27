package com.github.admincaofuqiang.mode;

import javax.print.DocFlavor;

public abstract class Handler {
    private Handler next;

    public Handler getNext() {
        return next;
    }

    public void setNext(Handler next) {
        this.next = next;
    }
    public abstract void  handlerRequest(String request);
}
