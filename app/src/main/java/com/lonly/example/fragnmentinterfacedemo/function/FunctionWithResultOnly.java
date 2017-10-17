package com.lonly.example.fragnmentinterfacedemo.function;

/**
 * Created by lonly on 2017/10/16.
 */

public abstract class FunctionWithResultOnly<Result> extends Function{
    public FunctionWithResultOnly(String functionName){
        super(functionName);
    }
    public abstract Result function();
}
