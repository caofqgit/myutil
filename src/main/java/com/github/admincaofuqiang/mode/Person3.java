package com.github.admincaofuqiang.mode;

public class Person3 extends Handler{
    @Override
    public void handlerRequest(String request) {
        if(request.equals("3"))
            System.out.println("我处理3的操作");
        else{
            if(getNext()!=null){
                getNext().handlerRequest(request);
            }else{
                System.out.println("不执行操作");
            }
        }
    }
}
