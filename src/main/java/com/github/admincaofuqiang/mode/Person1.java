package com.github.admincaofuqiang.mode;

public class Person1 extends Handler{
    @Override
    public void handlerRequest(String request) {
        if(request.equals("0")){
            System.out.println("我是1我处理0的参数");
        }else{
            if(getNext()!=null){
                getNext().handlerRequest(request);
            }else{
                System.out.println("不处理该请求");
            }
        }
    }
}
