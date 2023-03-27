package com.github.admincaofuqiang.mode;

public class Person2 extends Handler{
    @Override
    public void handlerRequest(String request) {
        if(request.equals("2")){
            System.out.println("this is 2 我处理参数2 ");
        }else{
            if(getNext()!=null){
                getNext().handlerRequest(request);
            }else{
                System.out.println("不处理当前参数");
            }
        }
    }
}
