package com.github.admincaofuqiang.mode;

public class Builder {
    private Handler head;
    private Handler tail;
    public Builder addHandler(Handler handler){
        if(head==null){
            this.head=this.tail=handler;
            return this;
        }
        this.tail.setNext(handler);
        this.tail=handler;
        return this;
    }
    public Handler build(){
        return this.head;
    }
}
