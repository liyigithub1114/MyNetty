package com.liyi.rpc.service;

public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(String msg) {
        return "this is rpc invoke: " + msg;
    }
}
